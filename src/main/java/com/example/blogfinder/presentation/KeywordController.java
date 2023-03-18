package com.example.blogfinder.presentation;

import com.example.blogfinder.application.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService service;

    @GetMapping("/keywords")
    public PopularKeywordResponse getPopularKeywords() {
        return service.getPopularKeywords();
    }
}
