package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.Blog;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import jakarta.annotation.Nullable;

import java.util.List;

public interface BlogClient {
    @Nullable
    List<Blog> find(FindBlogRequest request);
}
