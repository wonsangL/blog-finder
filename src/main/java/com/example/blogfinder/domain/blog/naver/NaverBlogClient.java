package com.example.blogfinder.domain.blog.naver;

import com.example.blogfinder.domain.blog.BlogClient;
import com.example.blogfinder.domain.blog.CannotFoundBlogException;
import com.example.blogfinder.domain.blog.FindBlogResult;
import com.example.blogfinder.presentation.blog.FindBlogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static com.example.blogfinder.presentation.blog.SortType.ACCURACY;

@Component
@DependsOn("kakaoBlogClient")
public class NaverBlogClient implements BlogClient {
    private final WebClient webClient;

    @Autowired
    public NaverBlogClient(HttpClient httpClient) {
        webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("X-Naver-Client-Id", "tEVFc3LWkehfee775Nf2")
                .defaultHeader("X-Naver-Client-Secret", "80WPzYDoLM")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public FindBlogResult find(FindBlogRequest request) {
        NaverSearchBlogResponse response = webClient.get()
                .uri("/v1/search/blog.json", uriBuilder -> uriBuilder
                        .queryParam("query", request.query())
                        .queryParam("display", request.size())
                        .queryParam("start", request.page())
                        .queryParam("sort", request.sort() == ACCURACY? "sim" : "date")
                        .build()
                ).retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .switchIfEmpty(Mono.just(clientResponse.statusCode().toString()))
                        .map(CannotFoundBlogException::new))
                .bodyToMono(NaverSearchBlogResponse.class)
                .block();

        if (response == null || response.items() == null) {
            throw new CannotFoundBlogException("response is null");
        }

        return FindBlogResult.from(response);
    }
}
