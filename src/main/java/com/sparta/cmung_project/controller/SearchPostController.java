package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchPostController {
    private final PostService postService;

    @GetMapping
    public GlobalResDto<?> searchPost(@RequestParam(name = "content") String content) {
        log.info("게시글 검색");
        
        return postService.searchPost ( content );
    }

}
