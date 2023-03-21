package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.FindBlogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogFinder {
    private final List<BlogClient> blogClients;

    @Cacheable(cacheNames = "kakaoBlog", key = "#request", unless = "#result.meta().total() == 0")
    public FindBlogResult find(FindBlogRequest request) {
        for (BlogClient blogClient : blogClients) {
            FindBlogResult blogs = blogClient.find(request);

            if (blogs != null) {
                return blogs;
            }
        }

        return FindBlogResult.createEmpty();
    }
}
