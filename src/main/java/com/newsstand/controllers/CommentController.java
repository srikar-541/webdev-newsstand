package com.newsstand.controllers;

import com.newsstand.models.Article;
import com.newsstand.models.Comment;
import com.newsstand.models.User;
import com.newsstand.services.ArticleService;
import com.newsstand.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class CommentController {
    @Autowired
    CommentService service;
    @Autowired
    ArticleService articleService;
    @PostMapping("/api/article/{aid}/comment")
    public Comment createArticle(@PathVariable ("aid") Integer articleId, @RequestBody Comment newComment, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = articleService.findArticleById(articleId);
        if (currentUser != null) {
            newComment.setUser(currentUser);
            newComment.setArticle(article);
            return service.createComment(newComment);
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/article/{aid}/comments")
    public Set<Comment> getComments(@PathVariable ("aid") Integer articleId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = articleService.findArticleById(articleId);
        if (currentUser != null){
            return service.getComments(article);

        }
        throw new AuthenticationException("User not logged in");
    }

}
