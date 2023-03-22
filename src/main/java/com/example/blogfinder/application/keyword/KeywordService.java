package com.example.blogfinder.application.keyword;

import com.example.blogfinder.domain.keyword.KeywordCacheManager;
import com.example.blogfinder.infra.KeywordEntity;
import com.example.blogfinder.infra.KeywordRepository;
import com.example.blogfinder.presentation.keyword.Keyword;
import com.example.blogfinder.presentation.keyword.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    private final KeywordCacheManager keywordCacheManager;

    @Cacheable(cacheNames = "keywords", key = "'top10'")
    public PopularKeywordResponse getPopularKeywords() {
        List<KeywordEntity> keywords = keywordRepository.findTop10Keywords();
        return mapToResponse(keywords);
    }

    private PopularKeywordResponse mapToResponse(List<KeywordEntity> keywordEntities) {
        List<Keyword> keywords = keywordEntities.stream()
                .map(Keyword::new)
                .toList();

        return new PopularKeywordResponse(keywords);
    }

    public void updateUseCount(String keyword) {
        KeywordEntity keywordEntity = keywordRepository.findById(keyword)
                .orElse(new KeywordEntity(keyword));

        keywordEntity.setUseCount(keywordEntity.getUseCount() + 1);

        keywordCacheManager.clearIfPopularKeywordsChange(keywordEntity);

        keywordRepository.save(keywordEntity);
    }
}
