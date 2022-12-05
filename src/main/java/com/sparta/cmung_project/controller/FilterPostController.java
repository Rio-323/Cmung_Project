package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/filter")
public class FilterPostController {
    private final PostService postService;

    @GetMapping
    public GlobalResDto<?> filterPost(@RequestParam(name = "category") String category,
                                      Pageable pageable) {
        log.info("게시글 필터");
        
        return postService.filterPost ( category, pageable );
    }
}
