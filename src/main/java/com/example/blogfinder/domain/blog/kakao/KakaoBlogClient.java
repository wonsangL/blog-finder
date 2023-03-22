package com.example.blogfinder.domain.blog.kakao;

import com.example.blogfinder.domain.blog.BlogClient;
import com.example.blogfinder.domain.blog.BlogClientException;
import com.example.blogfinder.domain.blog.FindBlogResult;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static com.example.blogfinder.presentation.blog.SortType.ACCURACY;

@Component
public class KakaoBlogClient implements BlogClient {
    private final WebClient webClient;

    @Autowired
    public KakaoBlogClient(HttpClient httpClient) {
        webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public FindBlogResult find(FindBlogRequest request) {
        KakaoSearchBlogResponse response = webClient.get()
                .uri("/v2/search/blog", uriBuilder -> uriBuilder.queryParam("query", request.query())
                        .queryParam("sort", request.sort() == ACCURACY ? "accuracy" : "recency")
                        .queryParam("page", request.page())
                        .queryParam("size", request.size())
                        .build()
                ).header("Authorization", "KakaoAK 42ee05dd918adfcfa21b2180603258a5a")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .switchIfEmpty(Mono.just(clientResponse.statusCode().toString()))
                                .map(BlogClientException::new)
                )
                .bodyToMono(KakaoSearchBlogResponse.class)
                .block();

        if (response == null || response.documents() == null) {
            throw new BlogClientException("response is null");
        }

        return FindBlogResult.from(response);
    }
}
