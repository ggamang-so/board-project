package com.ggamangso.boardproject.controller;

import com.ggamangso.boardproject.config.SecurityConfig;
import com.ggamangso.boardproject.domain.dto.ArticleCommentDto;
import com.ggamangso.boardproject.domain.dto.ArticleWithCommentsDto;
import com.ggamangso.boardproject.domain.dto.UserAccountDto;
import com.ggamangso.boardproject.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[view][GET] 게시글 리스트 페이지(게시판) - 정상호출 ")
    @Test
    public void ArticleListTest() throws Exception {
      //Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
      //When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))// contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles")); // model에 articles라는 이름의 정보가 넘어오는지
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출 ")
    @Test
    public void OneArticleTest() throws Exception {
        //Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        //When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))// contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);// model에 article라는 이름의 정보가 넘어오는지
    }


    @Disabled("구현중")
    @DisplayName("[view][GET] 게시글 검색전용 페이지 - 정상호출 ")
    @Test
    public void ArticleSearchTest() throws Exception {
        //Given

        //When & Then
        mvc.perform(get("/article/search"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles/search"));

    }
    @Disabled("구현중")
    @DisplayName("[view][GET] 게시글 해시태그 검색전용 페이지 - 정상호출 ")
    @Test
    public void ArticleHashTagSearchTest() throws Exception {
        //Given

        //When & Then
        mvc.perform(get("/article/search-hashtag"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }




    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "hashtag",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(1L,
                "Uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}
