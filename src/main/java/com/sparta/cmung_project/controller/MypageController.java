package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.*;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage")
    public GlobalResDto<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        return mypageService.getUserInfo(userDetails.getMember());
    }

    @GetMapping("/mypage/posts")
    public GlobalResDto<?> getUserPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        return mypageService.getUserPosts(page, userDetails.getMember());
    }

    @PostMapping("/mypage/image")
    public GlobalResDto<?> createUserImage(MultipartHttpServletRequest img,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 이미지 파일 리스트
        MultipartFile multipartFile = img.getFile("postImg");

        // 마이페이지 유저 이미지 업로드
        return mypageService.createUserImage(multipartFile, userDetails.getMember());
    }

    @PostMapping(value = "/mypage/pet")
    public GlobalResDto<?> createPet(@RequestBody PetRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        return mypageService.createPet(requestDto, userDetails.getMember());
    }

    @PutMapping(value = "/mypage/pet/{petId}")
    public GlobalResDto<?> updatePet(@PathVariable Long petId, @RequestBody PetRequestDto requestDto) throws CustomException {
        return mypageService.updatePet(petId, requestDto);
    }

    @DeleteMapping("/mypage/pet/{petId}")
    public GlobalResDto<?> deletePet(@PathVariable Long petId) {
        return mypageService.deletePet(petId);
    }

    @GetMapping("/mypage/pet")
    public GlobalResDto<?> getPet(@AuthenticationPrincipal UserDetailsImpl userDetails) throws RuntimeException {
        return mypageService.getPet(userDetails.getMember());
    }
}
