package com.example.blogfinder.presentation.blog;

import com.example.blogfinder.domain.blog.CannotFoundBlogException;
import com.example.blogfinder.domain.blog.FindBlogResult;
import com.example.blogfinder.domain.blog.kakao.KakaoBlog;
import com.example.blogfinder.domain.blog.kakao.KakaoBlogClient;
import com.example.blogfinder.domain.blog.kakao.KakaoSearchBlogMeta;
import com.example.blogfinder.domain.blog.kakao.KakaoSearchBlogResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private KakaoBlogClient blogFinder;

    @BeforeEach
    void setup() {
        RestAssured.port = port;

        Cache blog = cacheManager.getCache("blog");
        if(blog != null) blog.clear();
    }

    @Test
    @DisplayName("키워드를 통해 블로그 검색")
    void findBlogTest() {
        KakaoBlog blog = new KakaoBlog("<b>카카오뱅크</b> 비상금대출 조건 연장 이자 어떻게 낮출까?", "", "", "", OffsetDateTime.now());
        List<KakaoBlog> blogs = Collections.singletonList(blog);

        KakaoSearchBlogMeta meta = new KakaoSearchBlogMeta(1, 1, true);

        FindBlogResult result = FindBlogResult.from(new KakaoSearchBlogResponse(meta, blogs));

        when(blogFinder.find(any()))
                .thenReturn(result);

        RestAssured
                .given()
                .queryParam("query", "카카오뱅크")

                .when()
                .get("/blog")

                .then()
                .statusCode(200)
                .assertThat()
                .body("data[0].title", is("<b>카카오뱅크</b> 비상금대출 조건 연장 이자 어떻게 낮출까?"));
    }

    @Test
    @DisplayName("모든 블로그 검색 소스 에러 발생")
    void findBlogFailTest() {
        when(blogFinder.find(any()))
                .thenThrow(CannotFoundBlogException.class);

        RestAssured
                .given()
                .queryParam("query", "카카오뱅크")

                .when()
                .get("/blog")

                .then()
                .statusCode(500)
                .assertThat()
                .body("message", is("모든 블로그 검색 소스에서 블로그 검색에 실패하였습니다."));
    }
}