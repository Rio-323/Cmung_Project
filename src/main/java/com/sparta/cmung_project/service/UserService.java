package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.dto.MemberResponseDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final MemberRepository memberRepository;

    public GlobalResDto<?> getUserInfo(Long userId) throws CustomException {
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
}
