package com.ggamangso.boardproject.domain.dto.request;

import com.ggamangso.boardproject.domain.dto.ArticleCommentDto;
import com.ggamangso.boardproject.domain.dto.UserAccountDto;

public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }

}