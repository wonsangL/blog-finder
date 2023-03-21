package com.example.blogfinder.domain.blog.kakao;

import com.example.blogfinder.domain.blog.BlogClient;
import com.example.blogfinder.domain.blog.Blog;
import com.example.blogfinder.domain.blog.FindBlogResult;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoBlogClient implements BlogClient {
    private static final Integer PAGE = 50;

    private static final Integer SIZE = 10;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .build();

    @Override
    public FindBlogResult find(FindBlogRequest request) {
        KakaoSearchBlogResponse response = webClient.get()
                .uri("/v2/search/blog", uriBuilder -> uriBuilder.queryParam("query", request.query())
                        .queryParam("sort", request.sort())
                        .queryParam("page", request.page())
                        .queryParam("size", request.size())
                        .build()
                ).header("Authorization", "KakaoAK 42ee05dd918adfcfa21b2180603258a5")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.empty())
                .bodyToMono(KakaoSearchBlogResponse.class)
                .block();

        if (response == null || response.documents() == null) {
            return null;
        }

        return FindBlogResult.from(response);
    }
}
