package com.example.blogfinder.presentation;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KeywordControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @Sql(scripts = {"classpath:/sql/insert_keywords.sql"})
    @DisplayName("사용자들이 많이 검색한 키워드 10개 조회")
    void getPopularKeywordTest() {
        RestAssured
                .given()

                .when()
                .get("/keywords")

                .then()
                .statusCode(200)
                .assertThat()
                .body("keywords[0].title", is("카페"))
                .body("keywords[0].useCount", is(10))
                .body("keywords[9].title", is("독서실"))
                .body("keywords[9].useCount", is(1));
    }
}