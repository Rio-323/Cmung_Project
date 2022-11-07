package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.MemberResponseDto;
import com.sparta.cmung_project.dto.PetRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Pet;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.PetRepository;
import com.sparta.cmung_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PetRepository petRepository;

    // API 마이페이지 회원 정보
    public MemberResponseDto getUserInfo() throws RuntimeException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        MemberResponseDto memberDto = member.toDto();

        return memberDto;
    }

    // API 마이페이지 게시글
    // 이미지 반환 미구현
    public List<PostResponseDto> getUserPosts(int typeId) throws RuntimeException {
        List<PostResponseDto> postsDto = postRepository.findAllByType(typeId)
                .stream().map((post) -> {
                    PostResponseDto postDto = post.toDto();

                    // DTO 반환
                    return postDto;
                })
                .collect(Collectors.toList());

        return postsDto;
    }

    // API 마이페이지 이미지 업로드

    // API 마이페이지 반려동물 등록
    // 장재영 매니저 코드 미활용
    @Transactional
    public Pet createPet(PetRequestDto petRequestDto) throws RuntimeException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        Pet pet = new Pet(petRequestDto.getName(), petRequestDto.getAge(),
                petRequestDto.getCategory(), member);

        petRepository.save(pet);

        return pet;
    }

    // API 마이페이지 반려동물 수정
    @Transactional
    public Pet updatePet(Long petId, PetRequestDto petRequestDto) throws RuntimeException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("해당 반려동물이 존재하지 않습니다."));

        // 객체 수정
        pet.update(petRequestDto.getName(), petRequestDto.getAge(), petRequestDto.getCategory());

        petRepository.save(pet);

        return pet;
    }

    // API 마이페이지 반려동물 삭제
    public Long deletePet(Long petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("해당 반려동물이 존재하지 않습니다."));

        petRepository.deleteById(petId);
        return petId;
    }

    // API 마이페이지 반려동물 조회
}
