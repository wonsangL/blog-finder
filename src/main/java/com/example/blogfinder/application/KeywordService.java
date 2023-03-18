package com.example.blogfinder.application;

import com.example.blogfinder.infra.KeywordEntity;
import com.example.blogfinder.infra.KeywordRepository;
import com.example.blogfinder.presentation.Keyword;
import com.example.blogfinder.presentation.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

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
}
