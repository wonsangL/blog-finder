package com.example.blogfinder.presentation.blog;

import com.example.blogfinder.domain.blog.CannotFoundBlogException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlogControllerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CannotFoundBlogException.class)
    public ErrorResponse cannotFoundBlogExceptionHandler() {
        return new ErrorResponse("모든 블로그 검색 소스에서 블로그 검색에 실패하였습니다.");
    }
}
