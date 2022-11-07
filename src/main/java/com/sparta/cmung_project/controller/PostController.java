package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.PostResponseDto;
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

    @PostMapping("/post")
    public GlobalResDto<PostResponseDto> createPost(MultipartHttpServletRequest imgs,
                                                    @RequestParam(required = false, value = "content")String postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MultipartFile> multipartFiles = imgs.getFiles("img");

        return postService.createPost(postRequestDto,multipartFiles,userDetails.getMember());
    }


    @GetMapping("/post")
    public GlobalResDto<?> allPost(){
        return postService.allPost();
    }

    @GetMapping("/post/{postId}")
    public GlobalResDto<OnePostResponseDto> onePost(@PathVariable Long postId,Long imageId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.onePost(postId, imageId, userDetails.getMember());
    }

    @DeleteMapping("/post/{postId}")
    public GlobalResDto<PostResponseDto> delPost(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.delPost(postId,userDetails.getMember());
    }

    @PatchMapping("/post/{postId}")
    public GlobalResDto<PostResponseDto> modifyPost(@PathVariable Long postId,
                                                    @RequestPart(required = false) MultipartFile file,
                                                    @RequestParam (required = false, value = "content")String content,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.modifyPost(postId, file, content ,userDetails.getMember());

    }
}
