package com.example.blogfinder.domain.blog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogClientException extends RuntimeException {
    private final String response;
}
