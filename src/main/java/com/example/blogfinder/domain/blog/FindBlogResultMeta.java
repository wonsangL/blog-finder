package com.example.blogfinder.domain.blog;

import com.example.blogfinder.domain.blog.kakao.KakaoSearchBlogMeta;

public record FindBlogResultMeta(Integer total, Boolean isEnd) {
    public FindBlogResultMeta(KakaoSearchBlogMeta meta) {
        this(meta.totalCount(), meta.isEnd());
    }
}
