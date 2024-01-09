package com.ggamangso.boardproject.service;

import com.ggamangso.boardproject.domain.Article;
import com.ggamangso.boardproject.domain.ArticleComment;
import com.ggamangso.boardproject.domain.dto.ArticleCommentDto;
import com.ggamangso.boardproject.domain.dto.ArticleDto;
import com.ggamangso.boardproject.repository.ArticleCommentRepository;
import com.ggamangso.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;

    @InjectMocks
    private ArticleCommentService sut;


    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments(){
        //Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of("title", "content", "#java"))
        );
        //When
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        //Then
        assertThat(articleComments).isNotNull();
        then(articleCommentRepository).should().findById(articleId);

    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(), "Uno", LocalDateTime.now(), "Uno", "comment"));

        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

}