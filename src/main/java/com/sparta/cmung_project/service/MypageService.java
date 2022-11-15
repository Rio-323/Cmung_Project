package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.*;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Category;
import com.sparta.cmung_project.model.Image;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Pet;
import com.sparta.cmung_project.repository.CategoryRepository;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.PetRepository;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MypageService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    // API 마이페이지 회원 정보
    public GlobalResDto<?> getUserInfo(Member member) throws RuntimeException {
        // 멤버 객체를 DTO로 만든다
        log.info(member.getEmail());
        log.info(member.getNickname());
        MemberResponseDto memberDto = member.toDto();

        // DTO 반환
        return GlobalResDto.success(memberDto,"마이페이지 회원 정보 조회");
    }

    // API 마이페이지 게시글
    public GlobalResDto<?> getUserPosts(int page, Member member) throws RuntimeException {
        // 게시글 조회
        List<PostResponseDto> postsDto = postRepository.findAllByMemberOrderByCreatedAtDesc(member)
                .stream().map((post) -> {
                    // 포스트 객체를 DTO로 만든다.
                    PostResponseDto postDto = post.toDto();

                    // DTO 반환
                    return postDto;
                })
                .collect(Collectors.toList());

        // DTO 반환
        return GlobalResDto.success(postsDto,"마이페이지 게시글 조회 완료.");
    }

    // API 마이페이지 이미지 업로드
    @Transactional
    public GlobalResDto<?> createUserImage(MultipartFile file, Member member) throws RuntimeException {
        // 이미지 업로드
        String imageUrl = s3Service.uploadFile(file);
        
        // 멤버 객체 이미지 필드 수정
        member.setUserImage(imageUrl);
        
        // 멤버 객체 DB 저장
        memberRepository.save(member);

        // 멤버 객체 DTO로 변환
        MemberResponseDto memberResponseDto = member.toDto();
        
        // DTO 반환
        return GlobalResDto.success(memberResponseDto,"마이페이지 유저 이미지 업로드 완료.");
    }

    // API 마이페이지 반려동물 등록
    @Transactional
    public GlobalResDto<?> createPet(PetRequestDto petRequestDto, Member member) throws RuntimeException {
        Optional<Category> category = categoryRepository.findByName(petRequestDto.getCategory());

        Pet pet = null;
        if(category.isPresent()) {
            // 카테고리가 존재할 때
            log.info("카테고리 존재");
            pet = new Pet(petRequestDto.getName(), petRequestDto.getAge(), category.get(), member);
        } else {
            // 카테고리가 존재하지 않을 때
            log.info("카테고리 생성");
            Category categoryObj = new Category(petRequestDto.getCategory());
            categoryRepository.save(categoryObj);
            pet = new Pet(petRequestDto.getName(), petRequestDto.getAge(), categoryObj, member);
        }
        
        // 애완동물을 DB에 저장
        petRepository.save(pet);

        // 애완동물 객체를 DTO로 변환
        PetResponseDto petDto = pet.toDto();

        // DTO 반환
        return GlobalResDto.success(petDto,"마이페이지 애완동물 등록 완료.");
    }

    // API 마이페이지 반려동물 수정
    @Transactional
    public GlobalResDto<?> updatePet(Long petId, PetRequestDto petRequestDto) throws CustomException {
        // 애완동물 존재 검사
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException( ErrorCode.NotFoundMember ));

        // 카테고리 존재 검사
        Optional<Category> category = categoryRepository.findByName(petRequestDto.getCategory());
        
        Category categoryObj = null;
        if(category.isPresent()) {
            // 카테고리가 존재할 때
            log.info("카테고리 존재");
        } else {
            // 카테고리가 존재하지 않을 때
            log.info("카테고리 생성");
            categoryObj = new Category(petRequestDto.getCategory());
            categoryRepository.save(categoryObj);
        }

        // 객체 수정
        pet.update(petRequestDto.getName(), petRequestDto.getAge(), categoryObj);
        
        // 수정된 애완동물 객체 DB에 저장
        petRepository.save(pet);

        // 애완동물 객체를 DTO로 변환
        PetResponseDto petDto = pet.toDto();

        // DTO 반환
        return GlobalResDto.success(petDto,"마이페이지 애완동물 수정 완료.");
    }

    // API 마이페이지 반려동물 삭제
    public GlobalResDto<?> deletePet(Long petId) {
        // 애완동물 존재 검사
        petRepository.findById(petId)
                .orElseThrow(() -> new CustomException( ErrorCode.NotFoundPet ));
        
        // 애완동물 삭제
        petRepository.deleteById(petId);

        // DTO 반환
        // 애완동물 번호 반환
        return GlobalResDto.success(petId,"마이페이지 애완동물 삭제 완료.");
    }

    // API 마이페이지 반려동물 조회
    public GlobalResDto<?> getPet(Member member) throws RuntimeException {
        // 애완동물 존재 검사
        Pet pet = petRepository.findByMember(member)
                .orElseThrow(() -> new CustomException( ErrorCode.NotFoundPet ));
        
        // 애완동물 객체 DTO로 변환
        PetResponseDto petDto = pet.toDto();

        // DTO에 애완동물 주인 설정
        petDto.setUserId(member.getId());

        // DTO 반환
        return GlobalResDto.success(petDto,"마이페이지 애완동물 조회 완료.");
    }
}
