package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.dto.MemberResponseDto;
import com.sparta.cmung_project.dto.PetResponseDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Pet;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.PetRepository;
import com.sparta.cmung_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final PostRepository postRepository;

    public GlobalResDto<?> getUserInfo(Long userId) throws CustomException {
        // 회원 존재 검사
        Optional<Member> member = memberRepository.findById(userId);

        MemberResponseDto memberDto = null;
        if(member.isPresent()) {
            memberDto = member.get().toDto();
        } else {
            throw new CustomException ( ErrorCode.NotFoundMember );
        }

        // DTO 반환
        return GlobalResDto.success(memberDto,"회원 정보 조회");
    }

    public GlobalResDto<?> getUserPetInfo(Long userId) throws CustomException {
        // 회원 존재 검사
        Optional<Member> member = memberRepository.findById(userId);

        if(member.isEmpty()) {
            throw new CustomException ( ErrorCode.NotFoundMember );
        }
        
        // 애완동물 존재 검사
        Optional<Pet> pet = petRepository.findByMember(member.get());

        PetResponseDto petResponseDto = null;
        if(pet.isPresent()) {
            petResponseDto = pet.get().toDto();
        } else {
            throw new CustomException( ErrorCode.NotFoundPet );
        }

        // DTO 반환
        return GlobalResDto.success(petResponseDto,"애완동물 정보 조회");
    }

    public GlobalResDto<?> getUserPosts(Long userId) throws CustomException {
        // 회원 존재 검사
        Optional<Member> member = memberRepository.findById(userId);

        if(member.isEmpty()) {
            throw new CustomException ( ErrorCode.NotFoundMember );
        }

        // 게시글 조회
        List<PostResponseDto> postsDto = postRepository.findAllByMemberOrderByCreatedAtDesc(member.get())
                .stream().map((post) -> {
                    // 포스트 객체를 DTO로 만든다.
                    PostResponseDto postDto = post.toDto();

                    // DTO 반환
                    return postDto;
                })
                .collect(Collectors.toList());

        // DTO 반환
        return GlobalResDto.success(postsDto,"유저 게시글 목록 조회");
    }
}
