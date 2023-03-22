package com.example.blogfinder.presentation.blog;

import com.example.blogfinder.application.blog.BlogService;
import com.example.blogfinder.domain.blog.FindBlogResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping("/blog")
    public FindBlogResult findBlogs(@Valid @ModelAttribute FindBlogRequest request) {
        return service.findBlogs(request);
    }
}
