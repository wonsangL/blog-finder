package com.example.blogfinder.presentation.keyword;

import com.example.blogfinder.infra.KeywordEntity;

public record Keyword(String title, Integer useCount) {
    public Keyword(KeywordEntity entity) {
        this(entity.getTitle(), entity.getUseCount());
    }
}
