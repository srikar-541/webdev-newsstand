package com.newsstand.controllers;

import com.newsstand.models.Article;
import com.newsstand.models.Comment;
import com.newsstand.models.Role;
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
    public Comment createComment(@PathVariable ("aid") Integer articleId, @RequestBody Comment newComment, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = articleService.findArticleById(articleId);
        if (currentUser != null) {
                Integer userId = currentUser.getId();
                newComment.setUserId(userId);
                newComment.setArticle(article);
                return service.createComment(newComment);
        }
        throw new AuthenticationException("User not logged in");
    }

    @PutMapping("api/article/{aid}/comment/{cid}")
    public Comment updateComment(@PathVariable("aid") Integer articleId, @PathVariable("cid") Integer commentId, @RequestBody Comment newComment, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Comment comment = service.getCommentById(commentId);
        if (currentUser != null) {
            if (comment.getUserId().equals(currentUser.getId()) || currentUser.getRole() == Role.ADMIN) {
                return service.updateComment(comment, newComment);
            } else {
                throw new AuthenticationException("Only User who created the comment can edit it");
            }

        }
        throw new AuthenticationException("User not logged in");
    }

    @DeleteMapping("api/article/{aid}/comment/{cid}")
    public void deleteComment(@PathVariable("aid") Integer articleId, @PathVariable("cid") Integer commentId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Comment comment = service.getCommentById(commentId);
        if (currentUser != null) {
            if (comment.getUserId().equals(currentUser.getId()) || currentUser.getRole() == Role.ADMIN) {
                service.deleteComment(comment);
            } else {
                throw new AuthenticationException("Only User who created the comment can delete it");
            }

        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/article/{aid}/comment/{cid}")
    public Comment getCommentById(@PathVariable ("aid") Integer articleId, @PathVariable ("cid") Integer commentId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null){
            return service.getCommentById(commentId);

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
