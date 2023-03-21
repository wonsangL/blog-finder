package com.example.blogfinder.domain.blog.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoSearchBlogMeta(@JsonProperty("total_count") Integer totalCount,
                                  @JsonProperty("pageable_count") Integer pageableCount,
                                  @JsonProperty("is_end") Boolean isEnd) {
}
