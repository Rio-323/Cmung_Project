package com.sparta.cmung_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.cmung_project.dto.IdCheckDto;
import com.sparta.cmung_project.dto.LoginReqDto;
import com.sparta.cmung_project.dto.MemberReqDto;
import com.sparta.cmung_project.dto.NicknameCheckDto;
import com.sparta.cmung_project.global.dto.GlobalResDto;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import com.sparta.cmung_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;



import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @PostMapping("/signup")
    public GlobalResDto<?> signup(@RequestBody @Valid MemberReqDto memberReqDto) {
        log.info("회원 가입");

        return memberService.signup ( memberReqDto );
    }

    @PostMapping("/login")
    public GlobalResDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        log.info("로그인");

        return memberService.login ( loginReqDto, response );
    }

    @PostMapping("/idCheck")
    public GlobalResDto<?> idCheck(@RequestBody @Valid IdCheckDto idCheckDto) {
        log.info("아이디 체크");

        return memberService.idCheck ( idCheckDto );
    }

    @PostMapping("/nicknameCheck")
    public GlobalResDto<?> nicknameCheck(@RequestBody @Valid NicknameCheckDto nicknameCheckDto) {
        log.info("닉네임 체크");

        return memberService.nicnameCheck ( nicknameCheckDto );
    }

    @GetMapping("/member/kakao/callback")
    public GlobalResDto<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        log.info("카카오 로그인");

        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        return memberService.kakaoLogin(code, response);
    }
}
