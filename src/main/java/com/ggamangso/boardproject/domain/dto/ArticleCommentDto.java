package com.ggamangso.boardproject.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.ggamangso.boardproject.domain.ArticleComment}
 */
public record ArticleCommentDto(
        LocalDateTime createAt,
        String createBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        String content
){
    public static ArticleCommentDto of(LocalDateTime createAt, String createBy, LocalDateTime modifiedAt, String modifiedBy, String content) {
        return new ArticleCommentDto(createAt, createBy, modifiedAt, modifiedBy, content);
    }
}
