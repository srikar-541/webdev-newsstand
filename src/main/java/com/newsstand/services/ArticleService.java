package com.newsstand.services;
import com.newsstand.models.Article;
import com.newsstand.models.User;
import com.newsstand.repositories.ArticleRepository;
import com.newsstand.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserRepository userRepository;

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }



    public int updateArticle(int articleId, Article updateArticle) {
        updateArticle.setId(articleId);
        articleRepository.save(updateArticle);
        return 1;
    }

    public Article findArticleById(int aid) {
        return articleRepository.getById(aid);
    }

    public List<Article> getArticlesByCategory(String category) {
        List<Article> articles = articleRepository.getArticlesByCategory(category);
        return articles;
    }

    public List<Article> getArticlesByAuthor(String author) {
        List<Article> articles = articleRepository.getArticlesByAuthor(author);
        return articles;
    }

   public Article likeArticle(Article article, User user){
        article.setLikedUsers(new HashSet<>());
        user.setLikedArticles(new HashSet<>());
        article.getLikedUsers().add(user);
        user.getLikedArticles().add(article);
        articleRepository.save(article);
        userRepository.save(user);
        return article;
   }

   public Set<Article> getArticlesLikedByUser(User user){
        return articleRepository.getArticlesLikedBuUser(user.getId());
   }



}
