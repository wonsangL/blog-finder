package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.FindBlogRequest;
import jakarta.annotation.Nullable;

public interface BlogClient {
    @Nullable
    FindBlogResult find(FindBlogRequest request);
}
