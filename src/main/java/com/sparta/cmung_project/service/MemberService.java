package com.sparta.cmung_project.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${kakao.rest.api}")
    private String kakaoRestApi;
    @Value("${naver.client.id}")
    private String naverClientId;
    @Value("${naver.client.secret}")
    private String naverClientSecret;
    List<String> badWords = Arrays.asList ( "18년", "18놈", "18새끼", "ㄱㅐㅅㅐㄲl", "ㄱㅐㅈㅏ", "가슴만져", "가슴빨아", "가슴빨어", "가슴조물락", "가슴주물럭", "가슴쪼물딱", "가슴쪼물락", "가슴핧아", "가슴핧어", "강간", "개가튼년", "개가튼뇬", "개같은년", "개걸레", "개고치",
            "개너미", "개넘", "개년", "개놈", "개늠", "개똥", "개떵", "개떡", "개라슥", "개보지", "개부달", "개부랄", "개불랄", "개붕알", "개새", "개세", "개쓰래기", "개쓰레기", "개씁년", "개씁블", "개씁자지", "개씨발", "개씨블", "개자식", "개자지", "개잡년", "개젓가튼넘", "개좆", "개지랄",
            "개후라년", "개후라들놈", "개후라새끼", "걔잡년", "거시기", "걸래년", "걸레같은년", "걸레년", "걸레핀년", "게부럴", "게세끼", "게이", "게새끼", "게늠", "게자식", "게지랄놈", "고환", "공지", "공지사항", "귀두", "깨쌔끼", "난자마셔", "난자먹어", "난자핧아", "내꺼빨아", "내꺼핧아",
            "내버지", "내자지", "내잠지", "내조지", "너거애비", "노옴", "누나강간", "니기미", "니뿡", "니뽕", "니씨브랄", "니아범", "니아비", "니애미", "니애뷔", "니애비", "니할애비", "닝기미", "닌기미", "니미", "닳은년", "덜은새끼", "돈새끼", "돌으년", "돌은넘", "돌은새끼", "동생강간", "동성애자",
            "딸딸이", "똥구녁", "똥꾸뇽", "똥구뇽", "똥", "띠발뇬", "띠팔", "띠펄", "띠풀", "띠벌", "띠벨", "띠빌", "마스터", "막간년", "막대쑤셔줘", "막대핧아줘", "맛간년", "맛없는년", "맛이간년", "멜리스", "미친구녕", "미친구멍", "미친넘", "미친년", "미친놈", "미친눔", "미친새끼", "미친쇄리",
            "미친쇠리", "미친쉐이", "미친씨부랄", "미튄", "미티넘", "미틴", "미틴넘", "미틴년", "미틴놈", "미틴것", "백보지", "버따리자지", "버지구녕", "버지구멍", "버지냄새", "버지따먹기", "버지뚫어", "버지뜨더", "버지물마셔", "버지벌려", "버지벌료", "버지빨아", "버지빨어", "버지썰어", "버지쑤셔",
            "버지털", "버지핧아", "버짓물", "버짓물마셔", "벌창같은년", "벵신", "병닥", "병딱", "병신", "보쥐", "보지", "보지핧어", "보짓물", "보짓물마셔", "봉알", "부랄", "불알", "붕알", "붜지", "뷩딱", "븅쉰", "븅신", "빙띤", "빙신", "빠가십새", "빠가씹새", "빠구리", "빠굴이", "뻑큐", "뽕알", "뽀지",
            "뼝신", "사까시", "상년", "새꺄", "새뀌", "새끼", "색갸", "색끼", "색스", "색키", "샤발", "서버", "써글", "써글년", "성교", "성폭행", "세꺄", "세끼", "섹스", "섹스하자", "섹스해", "섹쓰", "섹히", "수셔", "쑤셔", "쉐끼", "쉑갸", "쉑쓰", "쉬발", "쉬방", "쉬밸년", "쉬벌", "쉬불", "쉬붕", "쉬빨",
            "쉬이발", "쉬이방", "쉬이벌", "쉬이불", "쉬이붕", "쉬이빨", "쉬이팔", "쉬이펄", "쉬이풀", "쉬팔", "쉬펄", "쉬풀", "쉽쌔", "시댕이", "시발", "시발년", "시발놈", "시발새끼", "시방새", "시밸", "시벌", "시불", "시붕", "시이발", "시이벌", "시이불", "시이붕", "시이팔", "시이펄", "시이풀", "시팍새끼",
            "시팔", "시팔넘", "시팔년", "시팔놈", "시팔새끼", "시펄", "실프", "십8", "십때끼", "십떼끼", "십버지", "십부랄", "십부럴", "십새", "십세이", "십셰리", "십쉐", "십자석", "십자슥", "십지랄", "십창녀", "십창", "십탱", "십탱구리", "십탱굴이", "십팔새끼", "ㅆㅂ", "ㅆㅂㄹㅁ", "ㅆㅂㄻ", "ㅆㅣ", "쌍넘",
            "쌍년", "쌍놈", "쌍눔", "쌍보지", "쌔끼", "쌔리", "쌕스", "쌕쓰", "썅년", "썅놈", "썅뇬", "썅늠", "쓉새", "쓰바새끼", "쓰브랄쉽세", "씌발", "씌팔", "씨가랭넘", "씨가랭년", "씨가랭놈", "씨발", "씨발년", "씨발롬", "씨발병신", "씨방새", "씨방세", "씨밸", "씨뱅가리", "씨벌", "씨벌년", "씨벌쉐이",
            "씨부랄", "씨부럴", "씨불", "씨불알", "씨붕", "씨브럴", "씨블", "씨블년", "씨븡새끼", "씨빨", "씨이발", "씨이벌", "씨이불", "씨이붕", "씨이팔", "씨파넘", "씨팍새끼", "씨팍세끼", "씨팔", "씨펄", "씨퐁넘", "씨퐁뇬", "씨퐁보지", "씨퐁자지", "씹년", "씹물", "씹미랄", "씹버지", "씹보지", "씹부랄",
            "씹브랄", "씹빵구", "씹뽀지", "씹새", "씹새끼", "씹세", "씹쌔끼", "씹자석", "씹자슥", "씹자지", "씹지랄", "씹창", "씹창녀", "씹탱", "씹탱굴이", "씹탱이", "씹팔", "아가리", "애무", "애미", "애미랄", "애미보지", "애미씨뱅", "애미자지", "애미잡년", "애미좃물", "애비", "애자", "양아치", "어미강간",
            "어미따먹자", "어미쑤시자", "영자", "엄창", "에미", "에비", "엔플레버", "엠플레버", "염병", "염병할", "염뵹", "엿먹어라", "오랄", "오르가즘", "왕버지", "왕자지", "왕잠지", "왕털버지", "왕털보지", "왕털자지", "왕털잠지", "우미쑤셔", "운디네", "운영자", "유두", "유두빨어", "유두핧어", "유방",
            "유방만져", "유방빨아", "유방주물럭", "유방쪼물딱", "유방쪼물럭", "유방핧아", "유방핧어", "육갑", "이그니스", "이년", "이프리트", "자기핧아", "자지", "자지구녕", "자지구멍", "자지꽂아", "자지넣자", "자지뜨더", "자지뜯어", "자지박어", "자지빨아", "자지빨아줘", "자지빨어", "자지쑤셔", "자지쓰레기",
            "자지정개", "자지짤라", "자지털", "자지핧아", "자지핧아줘", "자지핧어", "작은보지", "잠지", "잠지뚫어", "잠지물마셔", "잠지털", "잠짓물마셔", "잡년", "잡놈", "저년", "점물", "젓가튼", "젓가튼쉐이", "젓같내", "젓같은", "젓까", "젓나", "젓냄새", "젓대가리", "젓떠", "젓마무리", "젓만이", "젓물",
            "젓물냄새", "젓밥", "정액마셔", "정액먹어", "정액발사", "정액짜", "정액핧아", "정자마셔", "정자먹어", "정자핧아", "젖같은", "젖까", "젖밥", "젖탱이", "조개넓은년", "조개따조", "조개마셔줘", "조개벌려조", "조개속물", "조개쑤셔줘", "조개핧아줘", "조까", "조또", "족같내", "족까", "족까내", "존나",
            "존나게", "존니", "졸라", "좀마니", "좀물", "좀쓰레기", "좁빠라라", "좃가튼뇬", "좃간년", "좃까", "좃까리", "좃깟네", "좃냄새", "좃넘", "좃대가리", "좃도", "좃또", "좃만아", "좃만이", "좃만한것", "좃만한쉐이", "좃물", "좃물냄새", "좃보지", "좃부랄", "좃빠구리", "좃빠네", "좃빠라라", "좃털",
            "좆같은놈", "좆같은새끼", "좆까", "좆까라", "좆나", "좆년", "좆도", "좆만아", "좆만한년", "좆만한놈", "좆만한새끼", "좆먹어", "좆물", "좆밥", "좆빨아", "좆새끼", "좆털", "좋만한것", "주글년", "주길년", "쥐랄", "지랄", "지랼", "지럴", "지뢀", "쪼까튼", "쪼다", "쪼다새끼", "찌랄", "찌질이",
            "창남", "창녀", "창녀버지", "창년", "처먹고", "처먹을", "쳐먹고", "쳐쑤셔박어", "촌씨브라리", "촌씨브랑이", "촌씨브랭이", "크리토리스", "큰보지", "클리토리스", "트랜스젠더", "페니스", "항문수셔", "항문쑤셔", "허덥", "허버리년", "허벌년", "허벌보지", "허벌자식", "허벌자지", "허접", "허젚",
            "허졉", "허좁", "헐렁보지", "혀로보지핧기", "호냥년", "호로", "호로새끼", "호로자슥", "호로자식", "호로짜식", "호루자슥", "호모", "호졉", "호좁", "후라덜넘", "후장", "후장꽂아", "후장뚫어", "흐접", "흐젚", "흐졉", "bitch", "fuck", "fuckyou", "nflavor", "penis", "pennis", "pussy",
            "sex" );

    public GlobalResDto<Object> idCheck(IdCheckDto idCheckDto) {
        if (null != isPresentMember ( idCheckDto.getEmail () )) {
            throw new CustomException ( ErrorCode.DuplicatedEmail );
        }

        return GlobalResDto.success ( null, "사용 가능한 아이디 입니다." );
    }
    public GlobalResDto<Object> nicknameCheck(NicknameCheckDto nicknameCheckDto) {

        if (null != isPresentNickname ( nicknameCheckDto.getNickname () )) {
            throw new CustomException ( ErrorCode.DuplicatedNickname );
        } else if (nicknameCheckDto.getNickname ().contains ( " " )) {
            throw new CustomException ( ErrorCode.NoContainsBlank );
        } else {
            for (String badWord : badWords) {
                if (nicknameCheckDto.getNickname ().contains ( badWord )) {
                    throw new CustomException ( ErrorCode.BadWordsException );
                }
            }
        }

        return GlobalResDto.success ( null, "사용가능한 Nickname 입니다." );
    }
    @Transactional
    public GlobalResDto<Object> signup(MemberReqDto memberReqDto) {
        // email 중복검사
        if (null != isPresentMember ( memberReqDto.getEmail () )) {
            throw new CustomException ( ErrorCode.DuplicatedEmail );
        }

        if (!memberReqDto.getPassword ().equals ( memberReqDto.getPasswordCheck () )) {
            throw new CustomException ( ErrorCode.WrongPassword );
        }

        for (String badWord : badWords) {
            if (memberReqDto.getNickname ().contains ( badWord )) {
                throw new CustomException ( ErrorCode.BadWordsException );
            }
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

        if (member.validatePassword ( passwordEncoder, loginReqDto.getPassword () )) {
            throw new CustomException ( ErrorCode.WrongPassword );
        }

        TokenDto tokenDto = jwtUtil.createAllToken ( loginReqDto.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( loginReqDto.getEmail () );

        if (refreshToken.isPresent ()) {
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
        response.addHeader ( JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken () );
        response.addHeader ( JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken () );
    }
    @Autowired
    public MemberService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public GlobalResDto<Object> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        log.info ( "1" );
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken ( code );

        log.info ( "2" );
        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo ( accessToken );

        log.info ( "3" );
        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member kakaoMember = registerKakaoMemberIfNeeded ( kakaoMemberInfo );

        log.info ( "4" );
        // 4. 강제 로그인 처리
        forceLogin ( kakaoMember );

        log.info ( "토큰 발급" );
        // 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken ( kakaoMemberInfo.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( kakaoMemberInfo.getEmail () );

        log.info ( "로그아웃한 후 로그인을 다시 하는가?" );
        // 로그아웃한 후 로그인을 다시 하는가?
        RefreshToken refreshToken1;
        if (refreshToken.isPresent ()) {
            refreshToken1 = refreshToken.get ().updateToken ( tokenDto.getRefreshToken () );
        } else {
            refreshToken1 = new RefreshToken ( tokenDto.getRefreshToken (), kakaoMemberInfo.getEmail () );
        }
        refreshTokenRepository.save ( refreshToken1 );

        log.info ( "토큰을 header에 넣어서 클라이언트에게 전달하기" );
        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader ( response, tokenDto );

        LoginResDto loginResDto = new LoginResDto ( kakaoMember, kakaoMember.getUserImage () );

        log.info ( "return문" );
        return GlobalResDto.success ( loginResDto, kakaoMember.getNickname () + "님 반갑습니다." );
    }
    private String getAccessToken(String code) throws JsonProcessingException {
        log.info ( "header 생성" );
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders ();
        headers.add ( "Content-type", "application/x-www-form-urlencoded;charset=utf-8" );

        log.info ( "body 생성" );
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<> ();
        body.add ( "grant_type", "authorization_code" );
        body.add ( "client_id", kakaoRestApi ); // REST API키
        body.add ( "redirect_uri", "https://크멍.com/auth/member/kakao/callback" );
        body.add ( "code", code );

        log.info ( "http 요청 보내기" );
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<> ( body, headers );
        RestTemplate rt = new RestTemplate ();

        log.info ( "ResponseEntity" );
        ResponseEntity<String> response = rt.exchange (
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        log.info ( "http 응답" );
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody ();
        ObjectMapper objectMapper = new ObjectMapper ();
        JsonNode jsonNode = objectMapper.readTree ( responseBody );

        log.info ( "return문" );
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
        String email = jsonNode.get ( "kakao_account" ).get ( "email" ).asText ();
        String userImage = jsonNode.get ( "kakao_account" ).get ( "profile" ).get ( "profile_image_url" ).asText ();

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

            if (sameEmailMember != null) {
                kakaoMember = sameEmailMember;

                // 기존 회원정보에 카카오 Id 추가
                kakaoMember.setKakaoId ( kakaoId );
            } else {
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
    public GlobalResDto<Object> naverLogin(String code, String state, HttpServletResponse response) throws IOException {
        NaverMemberInfoDto naverMemberInfoDto = getNaverMemberInfo ( code, state );

        String naverId = naverMemberInfoDto.getNaverId ();
        Member naverMember = memberRepository.findByNaverId ( naverId ).orElse ( null );

        if (naverMember == null) {
            // 네이버 사용자 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            String naverEmail = naverMemberInfoDto.getEmail ();
            Member sameEmailMember = memberRepository.findByEmail ( naverEmail ).orElse ( null );


            if (sameEmailMember != null) {
                naverMember = sameEmailMember;

                // 기존 회원정보에 네이버 Id 추가
                naverMember.setNaverId ( naverId );
            } else {
                // 신규 회원가입

                // username: naver nickname
                String nickname = naverMemberInfoDto.getNickname ();


                // password: random UUID
                String password = UUID.randomUUID ().toString ();
                String encodePassword = passwordEncoder.encode ( password );

                // email: naver email
                String email = naverMemberInfoDto.getEmail ();

                // 프로필 사진 가져오기
                String userImage = naverMemberInfoDto.getUserImage ();

                naverMember = new Member ( email, nickname, password, userImage, naverId );
            }

            memberRepository.save ( naverMember );
        }
        naverForceLogin ( naverMember );

        TokenDto tokenDto = jwtUtil.createAllToken ( naverMember.getEmail () );

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail ( naverMember.getEmail () );

        log.info ( "로그아웃한 후 로그인을 다시 하는가?" );
        // 로그아웃한 후 로그인을 다시 하는가?
        RefreshToken refreshToken1;
        if (refreshToken.isPresent ()) {
            refreshToken1 = refreshToken.get ().updateToken ( tokenDto.getRefreshToken () );
        } else {
            refreshToken1 = new RefreshToken ( tokenDto.getRefreshToken (), naverMember.getEmail () );
        }
        refreshTokenRepository.save ( refreshToken1 );


        log.info ( "토큰을 header에 넣어서 클라이언트에게 전달하기" );
        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader ( response, tokenDto );

        LoginResDto loginResDto = new LoginResDto ( naverMember, naverMember.getUserImage () );

        log.info ( "return문" );

        return GlobalResDto.success ( loginResDto, naverMember.getNickname () + "님 반갑습니다." );
    }
    // 네이버에 요청해서 회원정보 받는 메소드
    public NaverMemberInfoDto getNaverMemberInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";

        // 코드를 네이버에 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);

        String access_Token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰을 네이버에 전달하여 유저정보 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_Token, null, null);

        String naverId = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("id"));
        String email = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email"));
        String nickname = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("nickname"));
        String userImage = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("profile_image"));


        naverId = naverId.substring(1, naverId.length()-1);
        email = email.substring(1, email.length()-1);
        nickname = nickname.substring(1, nickname.length()-1);
        userImage = userImage.substring(1, userImage.length()-1);

        return new NaverMemberInfoDto ( naverId, nickname, email, userImage, access_Token, refresh_token );
    }
    // 네이버에 요청해서 데이터 전달 받는 메소드
    public JsonElement jsonElement(String reqUrl, String token, String code, String state) throws IOException {

        URL url = new URL ( reqUrl );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection ();

        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter (connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&redirect_uri= https://xn--922bn81b.com/auth/member/naver/callback" +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader (connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }
    private void naverForceLogin(Member naverMember) {
        UserDetails userDetails = new UserDetailsImpl(naverMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
