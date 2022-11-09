package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.dto.PostRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.model.Category;
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
                                                    @ModelAttribute PostRequestDto postRequestDto,
                                                    Category category,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MultipartFile> multipartFiles = imgs.getFiles("postImg");

        return postService.createPost(postRequestDto,multipartFiles,category,userDetails.getMember());
    }


    @GetMapping("/posts")
    public GlobalResDto<?> allPost(){
        return postService.allPost();
    }

//    @GetMapping("/posts/{postId}")
//    public GlobalResDto<OnePostResponseDto> onePost(@PathVariable Long postId,Long imageId,
//                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return postService.onePost(postId, imageId, userDetails.getMember());
//    }

    @DeleteMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> delPost(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.delPost(postId,userDetails.getMember());
    }

    @PatchMapping("/posts/{postId}")
    public GlobalResDto<PostResponseDto> modifyPost(MultipartHttpServletRequest imgs,
                                                    @PathVariable Long postId,
                                                    @ModelAttribute PostRequestDto postRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){

        List<MultipartFile> multipartFiles = imgs.getFiles("postImg");
        return postService.modifyPost(postId, imgs, postRequestDto ,userDetails.getMember());

    }
}
