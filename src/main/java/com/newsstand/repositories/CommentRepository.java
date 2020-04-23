package com.newsstand.repositories;

import com.newsstand.models.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;

import com.newsstand.models.Comment;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE article_id=:id")
    Set<Comment> getComments(
            @Param("id") int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM comment WHERE user_id=:id")
    void deleteCommentsByAUser(
            @Param("id") int id);

}
