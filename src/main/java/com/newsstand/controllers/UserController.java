package com.newsstand.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newsstand.services.CommentService;
import java.util.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;

import com.newsstand.models.*;
import com.newsstand.repositories.CategoryRepository;
import com.newsstand.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.newsstand.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;

    @PostMapping("/api/register")
    public User register(@RequestBody User user, HttpSession session) throws AuthenticationException {

        Iterator<User> userIterator = this.userRepository.findAll().iterator();

        while (userIterator.hasNext()) {
            if (userIterator.next().getUsername().equals(user.getUsername())) {
                throw new AuthenticationException("User already exists");
            }
        }
        user.setLikedArticles(new HashSet<>());
        user.setCreatedArticles(new HashSet<>());
        user.setComments(new ArrayList<>());
        Set<Category> userCategories = user.getCategories();
        user = userRepository.save(user);
        for (Category c: userCategories) {
            c.setUser(user);
            categoryRepository.save(c);
        }
        session.setAttribute("currentUser", user);
        return user;

    }

    @CrossOrigin
    @PostMapping("/api/login")
    public User login(@RequestBody User user, HttpSession session) throws AuthenticationException {

        Iterator<User> userIterator = userRepository.findAll().iterator();

        while (userIterator.hasNext()) {

            User existingUser = userIterator.next();

            if (existingUser.getUsername().equals(user.getUsername()) &&
                    existingUser.getPassword().equals(user.getPassword())) {

                session.setAttribute("currentUser", existingUser);
                return existingUser;
            }
        }

        throw new AuthenticationException("Please Sign Up first!");

    }


    @GetMapping("/api/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }


    @GetMapping("/api/profile")
    public User profile(HttpSession session) {

        if (session.getAttribute("currentUser") != null) {

            User user = (User) session.getAttribute("currentUser");

            Optional<User> optional = userRepository.findById(user.getId());

            if (optional.isPresent()) {
                return optional.get();
            }

        }
        return new User();
    }


    @GetMapping("/api/user/{userId}")
    public User findUserById(@PathVariable Integer userId, HttpSession session) {

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isPresent()) {

            User user = optional.get();
            user.setPassword("*****");

            return user;

        }

        return new User();
    }

    @GetMapping("/api/users")
    public List<User> findAllUsers() {

        List<User> allUsers = new ArrayList<>();

        Iterator<User> userIterator = userRepository.findAll().iterator();

        while (userIterator.hasNext()) {
            allUsers.add(userIterator.next());
        }

        return allUsers;
    }


    @GetMapping("/api/user/{userId}/likedArticles")
    public Iterable<Article> findAllArticlesLikedByUser(@PathVariable("userId") Integer userId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            return user.getLikedArticles();
        }

        return new ArrayList<>();
    }

    @GetMapping("/api/user/{userId}/commentedArticles")
    public Set<Article> findAllArticlesCommentedByUser(@PathVariable("userId") Integer userId) {


        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            Set<Article> articles = new HashSet<>();
            for (Comment c: user.getComments()){
                articles.add(c.getArticle());
            }
            return articles;
        }
        return new HashSet<>();
    }

    @PutMapping("/api/user/{uid}")
    public User update(@PathVariable("uid") Integer userId, @RequestBody User user, HttpSession session) {

        System.out.println("In update profile");

        Optional<User> optionalObject = userRepository.findById(userId);
        System.out.println("optional obj : "+optionalObject.get().getUsername());
        if (!optionalObject.isPresent()) {
            return new User();
        }
        User existingUser = optionalObject.get();

        Set<Category> oldCategories = existingUser.getCategories();
        for (Category c: oldCategories) {
            System.out.println("old cats : "+c.getCategory());
            categoryRepository.delete(c);
        }
        existingUser.set(user);
        existingUser.setCategories(new HashSet<>());
        Set<Category> newCategories = user.getCategories();
        for (Category c: newCategories) {
            existingUser.getCategories().add(c);
            c.setUser(existingUser);
            categoryRepository.save(c);
            System.out.println("new cats : "+c.getCategory());
        }

        User updatedUser = userRepository.save(existingUser);
        System.out.println("updated user : "+updatedUser.getUsername());
        System.out.println("updated user cats : "+updatedUser.getCategories());
        return updatedUser;
    }

    @GetMapping("/api/user/{uid}/comments")
    public List<Comment> getCommentsByUser(@PathVariable("uid") Integer userId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = findUserById(userId, session);
        if (currentUser != null) {
            List<Comment> comments = user.getComments();
            return comments;
        }
        throw new AuthenticationException("User not logged in");
    }

    @GetMapping("/api/user/{uid}/articles")
    public Set<Article> getArticlesWrittenByUser(@PathVariable("uid") Integer userId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = findUserById(userId, session);
        if (currentUser != null) {
            return articleService.getArticlesWrittenByUser(user);
        }
        throw new AuthenticationException("User not logged in");
    }
    @DeleteMapping("/api/user/{uid}")
    public void deleteUser(@PathVariable("uid") Integer userId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = findUserById(userId, session);
        if (currentUser != null) {
            if (currentUser.getRole() == Role.ADMIN) {
                System.out.println("Inside deleettee!!!!!!!!!!!");

                articleService.deleteLikesByAUser(userId);
                commentService.deleteCommentsByAUser(userId);
                articleService.deleteArticlesByAUser(user);
                categoryRepository.deleteCategoriesofAUser(userId);
            }
            else{
                throw new AuthenticationException("Only Admins can delete users");
            }

        }
        else {
            throw new AuthenticationException("User not logged in");
        }
    }
}
