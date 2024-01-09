package com.ggamangso.boardproject.domain.dto;

import java.time.LocalDateTime;

public record ArticleDto(
        LocalDateTime createAt,
        String createBy,
        String title,
        String content,
        String hashtag
)  {
    public static ArticleDto of(LocalDateTime createAt, String createBy, String title, String content, String hashtag) {
        return new ArticleDto(createAt, createBy, title, content, hashtag);
    }
}