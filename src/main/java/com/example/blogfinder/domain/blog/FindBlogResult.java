package com.example.blogfinder.domain.blog;

import com.example.blogfinder.domain.blog.kakao.KakaoSearchBlogResponse;
import com.example.blogfinder.domain.blog.naver.NaverSearchBlogResponse;

import java.util.Collections;
import java.util.List;

public record FindBlogResult(FindBlogResultMeta meta, List<Blog> data) {
    public static FindBlogResult from(KakaoSearchBlogResponse response) {
        List<Blog> data = response.documents()
                .stream()
                .map(Blog::new)
                .toList();

        return new FindBlogResult(new FindBlogResultMeta(response.meta()), data);
    }

    public static FindBlogResult from(NaverSearchBlogResponse response) {
        List<Blog> data = response.items()
                .stream()
                .map(Blog::new)
                .toList();

        boolean isEnd = response.total() < (response.start() * response.display());

        return new FindBlogResult(new FindBlogResultMeta(response.total(), isEnd), data);
    }

    public static FindBlogResult createEmpty() {
        return new FindBlogResult(new FindBlogResultMeta(0, true), Collections.emptyList());
    }
}
