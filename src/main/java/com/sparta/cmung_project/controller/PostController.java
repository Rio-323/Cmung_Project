package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.dto.PostRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public GlobalResDto<PostResponseDto> createPost(MultipartHttpServletRequest imgs,
                                                    @RequestPart PostRequestDto postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 이미지 파일 리스트
        List<MultipartFile> multipartFiles = imgs.getFiles("postImg");

        // 게시글 생성 서비스
        return postService.createPost(postRequestDto, multipartFiles, userDetails.getMember());
    }

    @GetMapping("/posts")
        public GlobalResDto<?> allPost(){
            return postService.allPost();
    }

    @DeleteMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> delPost(@PathVariable Long postId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.delPost(postId, userDetails.getMember());
    }

    @PutMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> modifyPost(MultipartHttpServletRequest imgs,
                                                    @PathVariable Long postId,
                                                    @RequestPart PostRequestDto postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        // 이미지 파일 리스트
        List<MultipartFile> multipartFiles = imgs.getFiles("postImg");

        // 게시글 수정 서비스
        return postService.modifyPost(postId, multipartFiles, postRequestDto ,userDetails.getMember());
    }

    @GetMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> getOne(@PathVariable Long postId){
        return postService.getOne(postId);
    }
}
