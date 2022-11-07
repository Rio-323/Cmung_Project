package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getUserInfo() throws RuntimeException {
        return ResponseEntity.ok(mypageService.getUserInfo());
    }
}
