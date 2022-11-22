package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.dto.PostRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public GlobalResDto<PostResponseDto> createPost(@RequestPart(value="postImg", required=false) List<MultipartFile> multipartFiles,
                                                    @RequestPart PostRequestDto postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 작성");
        
        // 게시글 생성 서비스
        return postService.createPost(postRequestDto, multipartFiles, userDetails.getMember());
    }

    @GetMapping("/posts")
    public GlobalResDto<?> allPost(){
        log.info("게시글 목록 조회");
        
        return postService.allPost();
    }

    @DeleteMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> delPost(@PathVariable Long postId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("게시글 삭제");

        return postService.delPost(postId, userDetails.getMember());
    }

    @PutMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> modifyPost(@RequestPart(value="postImg", required=false) List<MultipartFile> multipartFiles,
                                                    @PathVariable Long postId,
                                                    @RequestPart PostRequestDto postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("게시글 수정");
        
        // 게시글 수정 서비스
        return postService.modifyPost(postId, multipartFiles, postRequestDto ,userDetails.getMember());
    }

    @GetMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> getOne(@PathVariable Long postId){
        log.info("게시글 상세 조회");
        
        return postService.getOne(postId);
    }
}
