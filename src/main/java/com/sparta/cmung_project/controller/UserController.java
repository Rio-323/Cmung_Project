package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/users/{userId}")
    public GlobalResDto<?> getUserInfo(@PathVariable Long userId) throws CustomException {
        return userService.getUserInfo(userId);
    }
    
    // 회원 반려동물 조회
    @GetMapping("/users/{userId}/pet")
    public GlobalResDto<?> getUserPetInfo(@PathVariable Long userId) throws CustomException {
        return userService.getUserPetInfo(userId);
    }

    // 회원 게시글 조회
    @GetMapping("/users/{userId}/posts")
    public GlobalResDto<?> getUserPosts(@PathVariable Long userId) throws RuntimeException {
        return userService.getUserPosts(userId);
    }
}
