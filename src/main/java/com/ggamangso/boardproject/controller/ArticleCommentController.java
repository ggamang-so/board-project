package com.ggamangso.boardproject.controller;

import com.ggamangso.boardproject.domain.dto.UserAccountDto;
import com.ggamangso.boardproject.domain.dto.request.ArticleCommentRequest;
import com.ggamangso.boardproject.domain.dto.security.BoardPrincipal;
import com.ggamangso.boardproject.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewComment(
            ArticleCommentRequest articleCommentRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
//        TODO : 인증 정보를 넣어줘야함
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/"+ articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment
            (@PathVariable Long commentId,
             @AuthenticationPrincipal BoardPrincipal boardPrincipal,
             Long articleId
    ) {

        articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());

        return "redirect:/articles/" + articleId;
    }
}
