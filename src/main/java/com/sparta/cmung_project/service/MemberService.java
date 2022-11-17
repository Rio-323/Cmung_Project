package com.sparta.cmung_project.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cmung_project.dto.*;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.global.dto.GlobalResDto;
import com.sparta.cmung_project.jwt.dto.TokenDto;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.RefreshToken;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.RefreshTokenRepository;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public GlobalResDto<Object> idCheck(IdCheckDto idCheckDto) {
        if(null != isPresentMember ( idCheckDto.getEmail () )) {
            throw new CustomException ( ErrorCode.DuplicatedEmail );
        }

        return GlobalResDto.success ( null, "사용 가능한 아이디 입니다." );
    }

    public GlobalResDto<Object> nicnameCheck(NicknameCheckDto nicknameCheckDto) {
        if(null != isPresentNickname ( nicknameCheckDto.getNickname () )) {
            throw new CustomException ( ErrorCode.DuplicatedNickname );
        } else if (nicknameCheckDto.getNickname ().contains ( " " )) {
            throw new CustomException ( ErrorCode.NoContainsBlank );
        }

        return GlobalResDto.success ( null, "사용가능한 Nickname 입니다." );
    }

    @Transactional
    public GlobalResDto<Object> signup(MemberReqDto memberReqDto) {
        // email 중복검사
        if(null != isPresentMember ( memberReqDto.getEmail () )) {
            throw new CustomException ( ErrorCode.DuplicatedEmail );
        }

        if(!memberReqDto.getPassword ().equals ( memberReqDto.getPasswordCheck () )) {
            throw new CustomException ( ErrorCode.WrongPassword );
        }

        memberReqDto.setEncodePwd ( passwordEncoder.encode ( memberReqDto.getPassword () ) );

        Member member = new Member ( memberReqDto );

        memberRepository.save ( member );
        return GlobalResDto.success ( null, "회원가입이 완료되었습니다." );
    }

    @Transactional
    public GlobalResDto<?> login(LoginReqDto loginReqDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail ( loginReqDto.getEmail () ).orElseThrow (
                () -> new CustomException ( ErrorCode.NotFoundMember )
        );

        if(member.validatePassword ( passwordEncoder, loginReqDto.getPassword () )) {
            throw new CustomException ( ErrorCode.WrongPassword );
        }

        TokenDto tokenDto = jwtUtil.createAllToken ( loginReqDto.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( loginReqDto.getEmail () );

        if(refreshToken.isPresent ()) {
            refreshTokenRepository.save ( refreshToken.get ().updateToken ( tokenDto.getRefreshToken () ) );
        } else {
            RefreshToken newToken = new RefreshToken ( tokenDto.getRefreshToken (), loginReqDto.getEmail () );
            refreshTokenRepository.save ( newToken );
        }

        setHeader ( response, tokenDto );

        LoginResDto loginResDto = new LoginResDto ( member, member.getUserImage () );
        return GlobalResDto.success ( loginResDto, member.getNickname () + "님 반갑습니다." );
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> member = memberRepository.findByEmail ( email );
        return member.orElse ( null );
    }

    @Transactional(readOnly = true)
    public Member isPresentNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname ( nickname );
        return member.orElse ( null );
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }


    @Autowired
    public MemberService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public GlobalResDto<Object> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken ( code );

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo ( accessToken );

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member kakaoMember = registerKakaoMemberIfNeeded ( kakaoMemberInfo );

        // 4. 강제 로그인 처리
        forceLogin(kakaoMember);


        // 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken ( kakaoMemberInfo.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( kakaoMemberInfo.getEmail () );

        // 로그아웃한 후 로그인을 다시 하는가?
        RefreshToken refreshToken1;
        if(refreshToken.isPresent ()) {
            refreshToken1 = refreshToken.get ().updateToken ( tokenDto.getRefreshToken () );
        } else {
            refreshToken1 = new RefreshToken ( tokenDto.getRefreshToken (), kakaoMemberInfo.getEmail () );
        }
        refreshTokenRepository.save ( refreshToken1 );

        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader ( response, tokenDto );

        LoginResDto loginResDto = new LoginResDto ( kakaoMember, kakaoMember.getUserImage () );

        return GlobalResDto.success ( loginResDto, kakaoMember.getNickname () + "님 반갑습니다." );
    }


    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders ();
        headers.add ( "Content-type", "application/x-www-form-urlencoded;charset=utf-8" );

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<> ();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "${kakao.rest.api}"); // REST API키
        body.add("redirect_uri", "http://localhost:3000/auth/member/kakao/callback"); // 추후에 프론트 서버로 바껴야 함.
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<> ( body, headers );
        RestTemplate rt = new RestTemplate ();

        ResponseEntity<String> response = rt.exchange (
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody ();
        ObjectMapper objectMapper = new ObjectMapper ();
        JsonNode jsonNode = objectMapper.readTree ( responseBody );
        return jsonNode.get ( "access_token" ).asText ();
    }
    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders ();
        headers.add ( "Authorization", "Bearer " + accessToken );
        headers.add ( "Content-type", "application/x-www-form-urlencoded;charset=utf-8" );

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<> ( headers );
        RestTemplate rt = new RestTemplate ();
        ResponseEntity<String> response = rt.exchange (
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoMemberInfoRequest,
                String.class
        );

        String responseBody = response.getBody ();
        ObjectMapper objectMapper = new ObjectMapper ();
        JsonNode jsonNode = objectMapper.readTree ( responseBody );


        Long id = jsonNode.get ( "id" ).asLong ();
        String nickname = jsonNode.get ( "properties" ).get ( "nickname" ).asText ();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String userImage = jsonNode.get("kakao_account").get ( "profile" ).get("profile_image_url").asText();

        return new KakaoMemberInfoDto ( id, nickname, email, userImage );
    }
    private Member registerKakaoMemberIfNeeded(KakaoMemberInfoDto kakaoMemberInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoMemberInfo.getId ();

        Member kakaoMember = memberRepository.findByKakaoId ( kakaoId ).orElse ( null );

        if (kakaoMember == null) {
            // 카카오 사용자 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            String kakaoEmail = kakaoMemberInfo.getEmail ();
            Member sameEmailMember = memberRepository.findByEmail ( kakaoEmail ).orElse ( null );

            if(sameEmailMember != null) {
                kakaoMember = sameEmailMember;

                // 기존 회원정보에 카카오 Id 추가
                kakaoMember.setKakaoId ( kakaoId );
            }else {
                // 신규 회원가입

                // username: kakao nickname
                String nickname = kakaoMemberInfo.getNickname ();

                // password: random UUID
                String password = UUID.randomUUID ().toString ();
                String encodePassword = passwordEncoder.encode ( password );

                // email: kakao email
                String email = kakaoMemberInfo.getEmail ();

                // 프로필 사진 가져오기
                String userImage = kakaoMemberInfo.getUserImage ();

                kakaoMember = new Member ( nickname, encodePassword, email, userImage, kakaoId );
            }

            memberRepository.save ( kakaoMember );
        }

        return kakaoMember;
    }
    private void forceLogin(Member kakaoMember) {
        UserDetails userDetails = new UserDetailsImpl ( kakaoMember );
        Authentication authentication = new UsernamePasswordAuthenticationToken ( userDetails, null, userDetails.getAuthorities () );
        SecurityContextHolder.getContext ().setAuthentication ( authentication );
    }

    public GlobalResDto<Object> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getNaverAccessToken ( code, state );

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        NaverMemberInfoDto naverMemberInfo = getNaverMemberInfo ( accessToken );

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member naverMember = registerNaverMemberIfNeeded ( naverMemberInfo );

        // 4. 강제 로그인 처리
        naverForceLogin ( naverMember );


        // 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken ( naverMemberInfo.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( naverMemberInfo.getEmail () );

        // 로그아웃한 후 로그인을 다시 하는가?
        RefreshToken refreshToken1;
        if(refreshToken.isPresent ()) {
            refreshToken1 = refreshToken.get ().updateToken ( tokenDto.getRefreshToken () );
        } else {
            refreshToken1 = new RefreshToken ( tokenDto.getRefreshToken (), naverMemberInfo.getEmail () );
        }
        refreshTokenRepository.save ( refreshToken1 );

        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader ( response, tokenDto );

        LoginResDto loginResDto = new LoginResDto ( naverMember, naverMember.getUserImage () );

        return GlobalResDto.success ( loginResDto, naverMember.getNickname () + "님 반갑습니다." );
    }

    private String getNaverAccessToken(String code, String state) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders ();
        headers.add ( "Content-type", "application/x-www-form-urlencoded;charset=utf-8" );

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<> ();
        body.add ( "grant_type", "authorization_code" );
        body.add ( "client_id", "${ naver.client.id }");
        body.add ( "client_secret", "${ naver.client.secret }" );
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<> ( body, headers );

        RestTemplate rt = new RestTemplate ();
        ResponseEntity<String> response = rt.exchange (
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody ();
        ObjectMapper objectMapper = new ObjectMapper ();
        JsonNode jsonNode = objectMapper.readTree ( responseBody );

        return jsonNode.get ( "access_token" ).asText ();
    }
    private NaverMemberInfoDto getNaverMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders ();
        headers.add ( "Authorization", "Bearer " + accessToken );
        headers.add ( "Content-type", "application/x-www-form-urlencoded;charset=utf-8" );

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverMemberInfoRequest = new HttpEntity<> ( headers );
        RestTemplate rt = new RestTemplate ();
        ResponseEntity<String> response = rt.exchange (
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverMemberInfoRequest,
                String.class
        );

        // HTTP 응답 받아오기
        String responseBody = response.getBody ();
        ObjectMapper objectMapper = new ObjectMapper ();
        JsonNode jsonNode = objectMapper.readTree ( responseBody );

        Long id = jsonNode.get ( "response" ).get ( "id" ).asLong ();
        String nickname = jsonNode.get ( "response" ).get ( "nickname" ).asText ();
        String email = jsonNode.get ( "response" ).get ( "email" ).asText ();
        String userImage = jsonNode.get ( "response" ).get ( "profile_image" ).asText ();

        return new NaverMemberInfoDto ( id, nickname, email, userImage );
    }
    private Member registerNaverMemberIfNeeded(NaverMemberInfoDto naverMemberInfo) {

        // DB 에 중복된 Naver Id 가 있는지 확인
        Long naverId = naverMemberInfo.getId ();

        Member naverMember = memberRepository.findByNaverId ( naverId ).orElse ( null );

        if (naverMember == null) {
            // 카카오 사용자 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            String naverEmail = naverMemberInfo.getEmail ();
            Member sameEmailMember = memberRepository.findByEmail ( naverEmail ).orElse ( null );

            if(sameEmailMember != null) {
                naverMember = sameEmailMember;

                // 기존 회원정보에 카카오 Id 추가
                naverMember.setNaverId ( naverId );
            }else {
                // 신규 회원가입

                // username: naver nickname
                String nickname = naverMemberInfo.getNickname ();

                // password: random UUID
                String password = UUID.randomUUID ().toString ();
                String encodePassword = passwordEncoder.encode ( password );

                // email: naver email
                String email = naverMemberInfo.getEmail ();

                // 프로필 사진 가져오기
                String userImage = naverMemberInfo.getUserImage ();

                naverMember = new Member ( nickname, encodePassword, email, userImage, naverId );
            }

            memberRepository.save ( naverMember );
        }

        return naverMember;

    }
    private void naverForceLogin(Member naverMember) {
        UserDetails userDetails = new UserDetailsImpl ( naverMember );
        Authentication authentication = new UsernamePasswordAuthenticationToken ( userDetails, null, userDetails.getAuthorities () );
        SecurityContextHolder.getContext ().setAuthentication ( authentication );
    }

}
