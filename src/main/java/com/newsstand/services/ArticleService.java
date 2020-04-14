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



  public Article updateArticle(int articleId, Article updateArticle) {
    updateArticle.setId(articleId);
    Article oldArticle = findArticleById(articleId);
    updateArticle.setCreatedUser(oldArticle.getCreatedUser());
    updateArticle.setComments(oldArticle.getComments());
    updateArticle.setLikedUsers(oldArticle.getLikedUsers());
    articleRepository.save(updateArticle);
    return updateArticle;
  }

  public Article findArticleById(int aid) {
    return articleRepository.findById(aid).get();
  }

  public List<Article> getArticlesByCategory(String category) {
    List<Article> articles = articleRepository.getArticlesByCategory(category);
    for (Article article: articles){
      article.populate();
    }
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
    //userRepository.save(user);
    return article;
  }

  public Set<Article> getArticlesLikedByUser(User user){
    return user.getLikedArticles();
  }


  public void deleteArticle(Article article) {
    articleRepository.deleteLikes(article.getId());
    articleRepository.deleteComments(article.getId());
    article.populate();
    articleRepository.delete(article);
  }

  public Article unlikeArticle(Article article, User user) {
    Set<User> likedUsers = article.getLikedUsers();
    Set<Article> likedArticles = user.getLikedArticles();
    if (likedUsers.contains(user)) {
      likedUsers.remove(user);
      likedArticles.remove(article);
      articleRepository.save(article);
      userRepository.save(user);
    }
    return article;
  }

  public Set<Article> getArticlesWrittenByUser(User user) {
    return articleRepository.getWrittenByUser(user.getId());
  }


}
