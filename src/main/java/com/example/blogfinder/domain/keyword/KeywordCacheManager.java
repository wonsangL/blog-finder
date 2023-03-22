package com.example.blogfinder.domain.keyword;

import com.example.blogfinder.infra.KeywordEntity;
import com.example.blogfinder.presentation.keyword.Keyword;
import com.example.blogfinder.presentation.keyword.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordCacheManager {
    private final CacheManager cacheManager;

    public void clearIfPopularKeywordsChange(KeywordEntity keywordEntity) {
        Cache keywordsCache = cacheManager.getCache("keywords");

        if (keywordsCache == null) return;

        PopularKeywordResponse popularKeywords = keywordsCache.get("top10", PopularKeywordResponse.class);

        if (popularKeywords == null) return;

        for (Keyword keyword : popularKeywords.keywords()) {
            //업데이트 대상이 Top 10 키워드이거나, 기존 Top 10보다 많이 사용된 키워드가 등장하면 캐시를 지운다.
            if (keyword.title().equals(keywordEntity.getTitle()) ||
                    keyword.useCount() <= keywordEntity.getUseCount()) {
                keywordsCache.clear();
                return;
            }
        }
    }
}
