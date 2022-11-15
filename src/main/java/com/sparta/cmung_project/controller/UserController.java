package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/users/{userId}")
    public GlobalResDto<?> getUserInfo(@PathVariable Long userId) throws CustomException {
        log.info("다른 회원 정보 조회");
        
        return userService.getUserInfo(userId);
    }
    
    // 회원 반려동물 조회
    @GetMapping("/users/{userId}/pet")
    public GlobalResDto<?> getUserPetInfo(@PathVariable Long userId) throws CustomException {
        log.info("다른 회원 애완동물 조회");

        return userService.getUserPetInfo(userId);
    }

    // 회원 게시글 조회
    @GetMapping("/users/{userId}/posts")
    public GlobalResDto<?> getUserPosts(@PathVariable Long userId) throws RuntimeException {
        log.info("다른 회원 게시글 조회");

        return userService.getUserPosts(userId);
    }
}
