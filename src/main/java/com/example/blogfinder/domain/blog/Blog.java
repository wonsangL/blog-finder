package com.example.blogfinder.domain.blog;

import com.example.blogfinder.domain.blog.kakao.KakaoBlog;
import com.example.blogfinder.domain.blog.naver.NaverBlog;

public record Blog(String title, String content, String name) {
    public Blog(KakaoBlog kakaoBlog) {
        this(kakaoBlog.title(), kakaoBlog.contents(), kakaoBlog.blogname());
    }

    public Blog(NaverBlog naverBlog) {
        this(naverBlog.title(), naverBlog.description(), naverBlog.bloggername());
    }
}
