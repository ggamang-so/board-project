package com.ggamangso.boardproject.controller;

import com.ggamangso.boardproject.config.SecurityConfig;
import com.ggamangso.boardproject.config.TestSecurityConfig;
import com.ggamangso.boardproject.domain.constant.FormStatus;
import com.ggamangso.boardproject.domain.dto.ArticleDto;
import com.ggamangso.boardproject.domain.dto.ArticleWithCommentsDto;
import com.ggamangso.boardproject.domain.dto.UserAccountDto;
import com.ggamangso.boardproject.domain.constant.SearchType;
import com.ggamangso.boardproject.domain.dto.request.ArticleRequest;
import com.ggamangso.boardproject.domain.dto.response.ArticleResponse;
import com.ggamangso.boardproject.service.ArticleService;
import com.ggamangso.boardproject.service.PaginationService;
import com.ggamangso.boardproject.util.FormDataEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc, @Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }


    @DisplayName("[view][GET] 게시글 리스트 페이지(게시판) - 정상호출 ")
    @Test
    public void ArticleListTest() throws Exception {
      //Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
      //When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))// contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes")); // model에 articles라는 이름의 정보가 넘어오는지
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 페이지(게시판) - 검색어와 함께 호출 ")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        //When & Then
        mvc.perform(get("/articles")
                        .queryParam("searchType", String.valueOf(searchType))
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))// contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes")); // model에 articles라는 이름의 정보가 넘어오는지
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출 , 인증된 사용자 ")
    @Test
    public void OneArticleTest() throws Exception {
        //Given
        Long articleId = 1L;
        long totalCount = 1L;
        given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);
        //When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))// contentType 이 HTML view 파일인지
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect((model().attribute("totalCount", totalCount)));
        then(articleService).should().getArticleWithComments(articleId);//model에 article라는 이름의 정보가 넘어오는지
        then(articleService).should().getArticleCount();
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 인증 없을 땐, 로그인 페이지로 이동")
    @Test
    public void givenNothing_whenRequestingArticlePage_thenRedirectsToLoginPage() throws Exception {
        //Given
        Long articleId = 1L;

        //When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().is3xxRedirection()) // 상태가 Ok 인지
                .andExpect(redirectedUrlPattern("**/login"));
        then(articleService).shouldHaveNoInteractions();//model에 article라는 이름의 정보가 넘어오는지

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
                .andExpect(view().name("articles/search"));

    }

    @DisplayName("[view][GET] 게시글 해시태그 검색전용 페이지 - 정상호출 ")
    @Test
    public void givenNothing_whenRequestingArticleSearchingHashtagView_thenReturnArticleHashtagSearchView() throws Exception {
        //Given
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtags()).willReturn(hashtags);
        //When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
        then(articleService).should().getHashtags();
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색전용 페이지 - 정상호출, 해시태그 입력 ")
    @Test
    public void givenHashtag_whenRequestingArticleSearchingHashtagView_thenReturnArticleHashtagSearchView() throws Exception {
        //Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtags()).willReturn(hashtags);
        //When & Then
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue", hashtag))
                .andExpect(status().isOk()) // 상태가 Ok 인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // contentType 이 HTML view 파일인지
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
        then(articleService).should().getHashtags();
    }

    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewArticlePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    @WithUserDetails(value="unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequesting_thenSavesNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).saveArticle(any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().saveArticle(any(ArticleDto.class));
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지 - 정상호출, 인증된 사용자")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdatedArticlePage() throws Exception {
        // Given
        long articleId = 1L;
        ArticleDto dto = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(articleService).should().getArticle(articleId);
    }

    @WithUserDetails(value="unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).updateArticle(eq(articleId), any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleService).should().updateArticle(eq(articleId), any(ArticleDto.class));
    }

    @WithUserDetails(value="unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long articleId = 1L;
        String userId = "unoTest";
        willDoNothing().given(articleService).deleteArticle(articleId, userId);

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().deleteArticle(articleId, userId);
    }



//    fixture
    private ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
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
        return UserAccountDto.of(
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
