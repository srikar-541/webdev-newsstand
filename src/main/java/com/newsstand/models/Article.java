package com.newsstand.models;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String source_id;
    private String source_name;
    private String author;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedDate;
    private String content;
    private String category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "LIKES", joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns =  @JoinColumn(name =
                    "ARTICLE_ID"))
    @JsonIgnore
    private Set<User> likedUsers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User createdUser;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (!( o instanceof Article )) {
            return false;
        }

        Article article = (Article) o;

        return this.id == article.getId();
    }
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public void populate(){
        this.getCreatedUser();
        this.getLikedUsers();
        this.getComments();
    }
}
