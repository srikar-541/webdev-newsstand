package com.newsstand.controllers;

import com.newsstand.models.Article;
import com.newsstand.models.Role;
import com.newsstand.models.User;
import com.newsstand.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class ArticleController {

    @Autowired
    ArticleService service;

    @PostMapping("/api/article")
    public Article createArticle(@RequestBody Article newArticle,  HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            if (currentUser.getRole() != Role.USER) {
                newArticle.setCreatedUser(currentUser);
                return service.createArticle(newArticle);
            }
            else {
                throw new AuthenticationException("Only Editors have permission to create an article");
            }
        }
        throw new AuthenticationException("User not logged in");
    }

    @PutMapping("/api/article/{aid}")
    public Article updateArticle(@PathVariable("aid") int articleId,
                                @RequestBody Article article, HttpSession session) throws AuthenticationException{
        User currentUser = (User) session.getAttribute("currentUser");
        Article oldArticle = service.findArticleById(articleId);
        if (currentUser != null) {
            if ((currentUser.getRole() == Role.EDITOR && oldArticle.getCreatedUser().equals(currentUser)) || (currentUser.getRole() == Role.ADMIN)) {
                return service.updateArticle(articleId, article);
            }
            else {
                throw new AuthenticationException("Only the editor who created an article can delete it");
            }
        }
        throw new AuthenticationException("User not logged in");
    }

    @DeleteMapping("api/article/{aid}")
    public void deleteArticle(@PathVariable("aid") Integer articleId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = service.findArticleById(articleId);
        article.populate();
        System.out.println(article.getCreatedUser().getUsername());
        if (currentUser != null) {
            if ((currentUser.getRole() == Role.EDITOR && article.getCreatedUser().equals(currentUser)) || (currentUser.getRole() == Role.ADMIN)) {
                service.deleteArticle(article);
            }
            else {
                throw new AuthenticationException("Only the editor who created an article can delete it");
            }
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/article/{aid}")
    public Article findArticleById(@PathVariable("aid") Integer aid, HttpSession session) throws AuthenticationException {
            return service.findArticleById(aid);
    }

    @GetMapping("/api/articles/category/{category}")
    public List<Article> findArticleByCategory(@PathVariable("category") String category, HttpSession session) throws AuthenticationException {
        return service.getArticlesByCategory(category);
    }


    @GetMapping("/api/articles/author/{author}")
    public List<Article> findArticleByAuthor(@PathVariable("author") String author, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return service.getArticlesByAuthor(author);
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/article/{aid}/likedUsers")
    public Set<User> getLikedUsers(@PathVariable("aid") Integer articleId, HttpSession session) throws AuthenticationException {
        Article article = findArticleById(articleId, session);
        Set<User> set = service.getLikedUsers(article);
        System.out.println(set.size());
        return set;
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

    @DeleteMapping("/api/article/{aid}/unlike")
    public Article unlikeArticle(@PathVariable("aid") Integer articleId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        Article article = service.findArticleById(articleId);
        if (currentUser != null) {
            return service.unlikeArticle(article, currentUser);

        }
        throw new AuthenticationException("User not logged in");
    }
}
