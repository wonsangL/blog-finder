package com.example.blogfinder.domain.blog.kakao;

import java.util.List;

public record KakaoSearchBlogResponse(KakaoSearchBlogMeta meta, List<KakaoBlog> documents) {
}
