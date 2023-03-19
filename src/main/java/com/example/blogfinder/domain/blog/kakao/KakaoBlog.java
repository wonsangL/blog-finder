package com.example.blogfinder.domain.blog.kakao;

import java.time.OffsetDateTime;

public record KakaoBlog(String title, String contents, String url, String blogname, OffsetDateTime dateTime) {
}
