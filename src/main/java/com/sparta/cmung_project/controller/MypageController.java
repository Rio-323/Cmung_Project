package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.*;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Slf4j
@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage")
    public GlobalResDto<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        log.info("마이페이지 유저 정보 조회");

        return mypageService.getUserInfo(userDetails.getMember());
    }

    @GetMapping("/mypage/posts")
    public GlobalResDto<?> getUserPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        log.info("마이페이지 유저 게시글 조회");

        return mypageService.getUserPosts(page, userDetails.getMember());
    }

    @PostMapping("/mypage/image")
    public GlobalResDto<?> createUserImage(@RequestPart(value="userImg", required=false) MultipartFile multipartFile,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("마이페이지 유저 프로필 이미지 업로드");

        // 마이페이지 유저 이미지 업로드
        return mypageService.createUserImage(multipartFile, userDetails.getMember());
    }

    @PostMapping(value = "/mypage/pet")
    public GlobalResDto<?> createPet(@RequestBody PetRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        log.info("마이페이지 유저 애완동물 등록");

        return mypageService.createPet(requestDto, userDetails.getMember());
    }

    @PutMapping(value = "/mypage/pet/{petId}")
    public GlobalResDto<?> updatePet(@PathVariable Long petId, @RequestBody PetRequestDto requestDto) throws CustomException {
        log.info("마이페이지 유저 애완동물 수정");

        return mypageService.updatePet(petId, requestDto);
    }

    @DeleteMapping("/mypage/pet/{petId}")
    public GlobalResDto<?> deletePet(@PathVariable Long petId) {
        log.info("마이페이지 유저 애완동물 삭제");

        return mypageService.deletePet(petId);
    }

    @GetMapping("/mypage/pet")
    public GlobalResDto<?> getPet(@AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        log.info("마이페이지 유저 애완동물 조회");

        return mypageService.getPet(userDetails.getMember());
    }
}
