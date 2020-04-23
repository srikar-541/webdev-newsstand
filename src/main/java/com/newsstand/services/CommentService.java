package com.newsstand.services;

import com.newsstand.models.Article;
import com.newsstand.models.Comment;
import com.newsstand.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Set<Comment> getComments(Article article) {
        return commentRepository.getComments(article.getId());
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).get();
    }

    public Comment updateComment(Comment oldComment, Comment newComment) {
        oldComment.setComment(newComment.getComment());
        return commentRepository.save(oldComment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteCommentsByAUser(int userId){
        commentRepository.deleteCommentsByAUser(userId);
    }
}
