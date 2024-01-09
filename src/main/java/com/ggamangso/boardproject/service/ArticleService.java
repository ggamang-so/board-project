package com.ggamangso.boardproject.service;


import com.ggamangso.boardproject.domain.dto.ArticleCommentDto;
import com.ggamangso.boardproject.domain.dto.ArticleUpdateDto;
import com.ggamangso.boardproject.domain.type.SearchType;
import com.ggamangso.boardproject.domain.dto.ArticleDto;
import com.ggamangso.boardproject.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return Page.empty();
    }
    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }

    public void searchArticle(ArticleDto dto) {
    }

    public void updateArticle(long articleId, ArticleUpdateDto dto) {
    }

    public void deleteArticle(long articleId) {
    }


}
