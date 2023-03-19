package com.example.blogfinder.domain.blog.naver;

import com.example.blogfinder.domain.blog.BlogClient;
import com.example.blogfinder.presentation.blog.Blog;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@DependsOn("kakaoBlogClient")
public class NaverBlogClient implements BlogClient {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://openapi.naver.com")
            .defaultHeader("X-Naver-Client-Id", "tEVFc3LWkehfee775Nf2")
            .defaultHeader("X-Naver-Client-Secret", "80WPzYDoLM")
            .build();

    @Override
    public List<Blog> find(FindBlogRequest request) {
        NaverSearchBlogResponse response = webClient.get()
                .uri("/v1/search/blog.json", uriBuilder -> uriBuilder
                        .queryParam("query", request.query())
                        .queryParam("display", request.size())
                        .queryParam("start", request.page())
                        .queryParam("sort", request.sort().equals("accuracy") ? "sim" : "date")
                        .build()
                ).retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.empty())
                .bodyToMono(NaverSearchBlogResponse.class)
                .block();

        if (response == null || response.items() == null) {
            return null;
        }

        return response.items()
                .stream()
                .map(Blog::new)
                .toList();
    }
}
