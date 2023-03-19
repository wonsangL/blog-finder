package com.example.blogfinder.domain.blog;

import com.example.blogfinder.presentation.blog.Blog;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogFinder {
    private final List<BlogClient> blogClients;

    public List<Blog> find(FindBlogRequest request) {
        for (BlogClient blogClient : blogClients) {
            List<Blog> blogs = blogClient.find(request);

            if (blogs != null) {
                return blogs;
            }
        }

        return Collections.emptyList();
    }
}
