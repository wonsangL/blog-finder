package com.example.blogfinder.presentation.blog;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("키워드를 통해 블로그 검색")
    void findBlogTest() {
        RestAssured
                .given()
                .queryParam("query", "카카오뱅크")

                .when()
                .get("/blog")

                .then()
                .statusCode(200)
                .assertThat()
                .body("data[0].title", is("<b>카카오</b><b>뱅크</b> - 나무위키"));
    }
}