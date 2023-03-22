package com.example.blogfinder.presentation.blog;

import jakarta.validation.constraints.NotBlank;

import static com.example.blogfinder.presentation.blog.SortType.ACCURACY;

public record FindBlogRequest(@NotBlank String query, SortType sort, Integer page, Integer size) {
    public FindBlogRequest {
        sort = sort == null ? ACCURACY : sort;
        page = page == null ? 1 : page;
        size = size == null ? 10 : size;
    }
}
