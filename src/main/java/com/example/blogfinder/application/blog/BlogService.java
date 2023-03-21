package com.example.blogfinder.application.blog;

import com.example.blogfinder.application.keyword.KeywordService;
import com.example.blogfinder.domain.blog.BlogFinder;
import com.example.blogfinder.domain.blog.FindBlogResult;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {
    private final BlogFinder blogFinders;

    private final KeywordService keywordService;

    public FindBlogResult findBlogs(FindBlogRequest request) {
        keywordService.updateUseCount(request.query());
        return blogFinders.find(request);
    }
}
