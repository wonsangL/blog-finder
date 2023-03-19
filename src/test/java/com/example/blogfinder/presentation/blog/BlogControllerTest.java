package com.example.blogfinder.presentation.blog;

import com.example.blogfinder.domain.blog.BlogFinder;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogControllerTest {
    @LocalServerPort
    int port;

    @MockBean
    private BlogFinder blogFinder;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("키워드를 통해 블로그 검색")
    void findBlogTest() {
        Blog blog = new Blog("<b>카카오뱅크</b> 비상금대출 조건 연장 이자 어떻게 낮출까?", "", "");

        List<Blog> blogs = Collections.singletonList(blog);

        when(blogFinder.find(any()))
                .thenReturn(blogs);

        RestAssured
                .given()
                .queryParam("query", "카카오뱅크")
                .queryParam("sort", "accuracy")

                .when()
                .get("/blog")

                .then()
                .statusCode(200)
                .assertThat()
                .body("data[0].title", is("<b>카카오뱅크</b> 비상금대출 조건 연장 이자 어떻게 낮출까?"));
    }
}