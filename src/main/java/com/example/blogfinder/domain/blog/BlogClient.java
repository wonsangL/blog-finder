package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.FindBlogRequest;

public interface BlogClient {
    FindBlogResult find(FindBlogRequest request);
}
