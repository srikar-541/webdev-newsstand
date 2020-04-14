package com.newsstand.repositories;

import com.newsstand.models.Article;

import com.newsstand.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE category=:category")
    List<Article> getArticlesByCategory(
            @Param("category") String category);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE author=:author")
    List<Article> getArticlesByAuthor(
            @Param("author") String author);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE id=(SELECT article_id FROM likes WHERE user_id=:id)")
    Set<Article> getArticlesLikedBuUser(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE user_id=:id")
    Set<Article> getWrittenByUser(@Param("user_id") int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM likes WHERE article_id=:articleId")
    void deleteLikes( @Param("articleId") int articleId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM comment WHERE article_id=:articleId")
    void deleteComments( @Param("articleId") int articleId);


}
