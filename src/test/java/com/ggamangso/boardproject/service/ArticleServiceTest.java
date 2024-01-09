package com.ggamangso.boardproject.service;

import com.ggamangso.boardproject.domain.Article;
import com.ggamangso.boardproject.domain.dto.ArticleDto;
import com.ggamangso.boardproject.domain.dto.ArticleUpdateDto;
import com.ggamangso.boardproject.domain.type.SearchType;
import com.ggamangso.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;

    @Mock private ArticleRepository articleRepository;


    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given

        // When
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "searchKeyword");

        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환함")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticlePage() {
        // Given

        // When
        ArticleDto article = sut.searchArticle(1L);

        // Then
        assertThat(article).isNotNull();

    }
    @DisplayName("게시글을 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // When
        sut.searchArticle(ArticleDto.of(LocalDateTime.now(), "ggamangso", "title", "content", "#java"));

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // When
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#java"));

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        willDoNothing().given(articleRepository).delete(any(Article.class));
        // When
        sut.deleteArticle(1L);

        // Then
        then(articleRepository).should().delete(any(Article.class));
    }








}