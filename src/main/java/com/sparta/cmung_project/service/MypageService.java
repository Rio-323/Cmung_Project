package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.MemberResponseDto;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final MemberRepository memberRepository;

    public MemberResponseDto getUserInfo() throws RuntimeException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        MemberResponseDto memberDto = member.toDto();

        return memberDto;
    }
}
