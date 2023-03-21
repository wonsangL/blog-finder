package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.FindBlogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogFinder {
    private final List<BlogClient> blogClients;

    @Cacheable(cacheNames = "blog", key = "#request", condition = "!#request.sort().equals('recency')", unless = "#result.meta().total() == 0")
    public FindBlogResult find(FindBlogRequest request) {
        for (BlogClient blogClient : blogClients) {
            try {
                return blogClient.find(request);
            } catch (CannotFoundBlogException e) {
                log.warn("""
                        {} 블로그 검색 API 호출에 실패하였습니다.
                        request: {}
                        response: {}""", blogClient.getClass().getSimpleName(), request, e.getResponse());
            }
        }

        return FindBlogResult.createEmpty();
    }
}
