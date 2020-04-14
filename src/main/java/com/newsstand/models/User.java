package com.newsstand.models;

import com.google.gson.annotations.Expose;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "User")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String username;
  private String firstName;
  private String lastName;
  private String password;
  private String phoneNumber;
  private String email;
  private Role role;
  private String dateOfBirth;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "LIKES", joinColumns = @JoinColumn(name = "USER_ID"),
          inverseJoinColumns =  @JoinColumn(name =
                  "ARTICLE_ID"))
  @JsonIgnore
  private Set<Article> likedArticles;

  @OneToMany(mappedBy = "createdUser")
  @JsonIgnore
  private Set<Article> articles;


  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Comment> comments;

  @OneToMany(mappedBy = "user")
  private Set<Category> categories;


  public User() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }


  public void set(User newUser) {

    this.username = newUser.username;
    this.password = newUser.password;
    this.email = newUser.email;
    this.firstName = newUser.firstName;
    this.lastName = newUser.lastName;
    this.phoneNumber = newUser.phoneNumber;
    this.role = newUser.role;
    this.dateOfBirth = newUser.dateOfBirth;


  }

  @Override
  public boolean equals(Object o) {
    System.out.println(id);

    if (!( o instanceof User )) {
      return false;
    }

    User user = (User) o;
    System.out.println(user.getId());
    return this.id == user.getId();

  }
  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public Set<Article> getLikedArticles() {
    return likedArticles;
  }

  public void setLikedArticles(Set<Article> likedArticles) {
    this.likedArticles = likedArticles;
  }

  public Set<Article> getCreatedArticles() {
    return articles;
  }

  public void setCreatedArticles(Set<Article> articles) {
    this.articles = articles;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }
}
