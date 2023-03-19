package com.example.blogfinder.application.keyword;

import com.example.blogfinder.infra.KeywordEntity;
import com.example.blogfinder.infra.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeywordServiceTest {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("새로운 키워드 사용 횟수 생성")
    void createNewKeywordUseCountTest() {
        String keyword = "새로운 키워드";

        keywordService.updateUseCount(keyword);

        Optional<KeywordEntity> keywordEntity = keywordRepository.findById(keyword);

        assertThat(keywordEntity).isPresent();

        assertThat(keywordEntity.get().getUseCount()).isEqualTo(1);
    }

    @Test
    @Sql(scripts = "classpath:/sql/insert_keywords.sql")
    @DisplayName("기존 키워드 사용 횟수 업데이트")
    void updateKeywordUseCountTest() {
        String keyword = "카페";

        keywordService.updateUseCount(keyword);

        Optional<KeywordEntity> keywordEntity = keywordRepository.findById(keyword);

        assertThat(keywordEntity).isPresent();

        assertThat(keywordEntity.get().getUseCount()).isEqualTo(11);
    }
}