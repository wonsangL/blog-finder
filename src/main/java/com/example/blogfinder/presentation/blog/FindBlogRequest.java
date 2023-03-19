package com.example.blogfinder.presentation.blog;

public record FindBlogRequest(String query, String sort, Integer page, Integer size) {
    public FindBlogRequest {
        page = page == null ? 1 : page;
        size = size == null ? 10 : size;
    }
}
