package com.newsstand.controllers;

import com.newsstand.models.Article;
import com.newsstand.models.User;
import com.newsstand.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.*;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class ArticleController {

    @Autowired
    ArticleService service;


    @PutMapping("/api/articles/{aid}")
    public int updateWidget(@PathVariable("aid") int articleId,
                            @RequestBody Article article) {
        return service.updateArticle(articleId, article);
    }

    @PostMapping("/api/article")
    public Article createArticle(@RequestBody Article newArticle,  HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            return service.createArticle(newArticle);
        }
        throw new AuthenticationException("User not logged in");
    }



    @GetMapping("/api/articles/{aid}")
    public Article findArticleById(@PathVariable("aid") int aid, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return service.findArticleById(aid);
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/articles/category/{category}")
    public List<Article> findArticleByCategory(@PathVariable("category") String category, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return service.getArticlesByCategory(category);
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/articles/author/{author}")
    public List<Article> findArticleByAuthor(@PathVariable("author") String author, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return service.getArticlesByAuthor(author);
        }
        throw new AuthenticationException("User not logged in");
    }

    @PostMapping("/api/article/{aid}/like")
    public Article likeArticle(@PathVariable("aid") Integer articleId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = service.findArticleById(articleId);
        if (currentUser != null) {
           return service.likeArticle(article, currentUser);
        }
        throw new AuthenticationException("User not logged in");

    }



}
