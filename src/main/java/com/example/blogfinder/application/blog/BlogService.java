package com.example.blogfinder.application.blog;

import com.example.blogfinder.application.keyword.KeywordService;
import com.example.blogfinder.domain.blog.BlogFinder;
import com.example.blogfinder.presentation.blog.Blog;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import com.example.blogfinder.presentation.blog.FindBlogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogFinder blogFinders;

    private final KeywordService keywordService;

    public FindBlogResponse findBlogs(FindBlogRequest request) {
        List<Blog> blogs = blogFinders.find(request);

        keywordService.updateUseCount(request.query());

        return paging(blogs);
    }

    private FindBlogResponse paging(List<Blog> blogs) {
        return new FindBlogResponse(blogs);
    }
}
