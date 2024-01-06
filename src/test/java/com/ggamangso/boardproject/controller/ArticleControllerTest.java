package com.ggamangso.boardproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
    @DisplayName("[view][GET] 게시글 리스트 페이지(게시판) - 정상호출 ")
    @Test
    public void ArticleListTest() throws Exception {
      //Given

      //When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentType(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles")); // model에 articles라는 이름의 정보가 넘어오는지
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출 ")
    @Test
    public void OneArticleTest() throws Exception {
        //Given

        //When & Then
        mvc.perform(get("/article/1"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentType(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("article")); // model에 article라는 이름의 정보가 넘어오는지
    }
    @DisplayName("[view][GET] 게시글 검색전용 페이지 - 정상호출 ")
    @Test
    public void ArticleSearchTest() throws Exception {
        //Given

        //When & Then
        mvc.perform(get("/article/search"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentType(MediaType.TEXT_HTML)); // contentType 이 HTML view 파일인지

    }
    @DisplayName("[view][GET] 게시글 해시태그 검색전용 페이지 - 정상호출 ")
    @Test
    public void ArticleHashTagSearchTest() throws Exception {
        //Given

        //When & Then
        mvc.perform(get("/article/search-hashtag"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentType(MediaType.TEXT_HTML)); // contentType 이 HTML view 파일인지

    }
}