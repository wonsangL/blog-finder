package com.example.blogfinder.presentation.blog;

import com.example.blogfinder.application.blog.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping("/blog")
    public FindBlogResponse findBlogs(@ModelAttribute FindBlogRequest request) {
        return service.findBlogs(request);
    }
}
