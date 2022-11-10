package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.IdCheckDto;
import com.sparta.cmung_project.dto.LoginReqDto;
import com.sparta.cmung_project.dto.MemberReqDto;
import com.sparta.cmung_project.dto.NicknameCheckDto;
import com.sparta.cmung_project.global.dto.GlobalResDto;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import com.sparta.cmung_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @PostMapping("/signup")
    public GlobalResDto<?> signup(@RequestBody @Valid MemberReqDto memberReqDto) {
        return memberService.signup ( memberReqDto );
    }

    @PostMapping("/login")
    public GlobalResDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        return memberService.login ( loginReqDto, response );
    }

    @PostMapping("/idCheck")
    public GlobalResDto<?> idCheck(@RequestBody @Valid IdCheckDto idCheckDto) {
        return memberService.idCheck ( idCheckDto );
    }

    @PostMapping("/nicknameCheck")
    public GlobalResDto<?> nicknameCheck(@RequestBody @Valid NicknameCheckDto nicknameCheckDto) {
        return memberService.nicnameCheck ( nicknameCheckDto );
    }
}
