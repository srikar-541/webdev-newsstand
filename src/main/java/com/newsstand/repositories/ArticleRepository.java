package com.newsstand.repositories;

import com.newsstand.models.Article;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.*;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE category=:category")
    List<Article> getArticlesByCategory(
            @Param("category") String category);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE author=:author")
    List<Article> getArticlesByAuthor(
            @Param("author") String author);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE id=:id")
    Article getById(
            @Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM article WHERE id=(SELECT article_id FROM likes WHERE user_id=:id)")
    Set<Article> getArticlesLikedBuUser(@Param("id") int id);
}
