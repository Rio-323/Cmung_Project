package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.LoginReqDto;
import com.sparta.cmung_project.dto.MemberReqDto;
import com.sparta.cmung_project.global.dto.GlobalResDto;
import com.sparta.cmung_project.jwt.dto.TokenDto;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.RefreshToken;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResDto signup(MemberReqDto memberReqDto) {
        // userId 중복검사
        if(memberRepository.findByUserId ( memberReqDto.getUserId () ).isPresent ()) {
            throw new RuntimeException ("Overlap Check");
        }

        memberReqDto.setEncodePwd ( passwordEncoder.encode ( memberReqDto.getPassword () ) );
        Member member = new Member ( memberReqDto );

        memberRepository.save ( member );
        return new GlobalResDto ( "Success signup", HttpStatus.OK.value () );
    }

    @Transactional
    public GlobalResDto login(LoginReqDto loginReqDto, HttpServletResponse response) {

        Member member = memberRepository.findByUserId ( loginReqDto.getUserId () ).orElseThrow (
                () -> new RuntimeException ( "Not found Account" )
        );

        if(!passwordEncoder.matches ( loginReqDto.getPassword (), member.getPassword () )) {
            throw new RuntimeException ( "Not matches Password" );
        }

        TokenDto tokenDto = jwtUtil.createAllToken ( loginReqDto.getUserId () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberUserId ( loginReqDto.getUserId () );

        if(refreshToken.isPresent ()) {
            refreshTokenRepository.save ( refreshToken.get ().updateToken ( tokenDto.getRefreshToken () ) );
        } else {
            RefreshToken newToken = new RefreshToken ( tokenDto.getRefreshToken (), loginReqDto.getUserId () );
            refreshTokenRepository.save ( newToken );
        }

        setHeader ( response, tokenDto );

        return new GlobalResDto ( "Success Login", HttpStatus.OK.value () );
    }

    public GlobalResDto<Object> idCheck(IdCheckDto idCheckDto) {
        if(null != isPresentMember(idCheckDto.getUserId())) {
            throw new CustomException(ErrorCode.Dupicated)
        }
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String userId) {
        Optional<Member> member = memberRepository.findByUserId ( userId );
        return member.orElse ( null );
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
