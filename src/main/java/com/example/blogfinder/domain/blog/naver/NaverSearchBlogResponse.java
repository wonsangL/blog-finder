package com.example.blogfinder.domain.blog.naver;

import java.util.List;

public record NaverSearchBlogResponse(Integer total, Integer start, Integer display, List<NaverBlog> items) {
}
