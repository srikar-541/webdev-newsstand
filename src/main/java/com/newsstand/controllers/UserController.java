package com.newsstand.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;

import com.newsstand.models.*;
import com.newsstand.repositories.ArticleRepository;
import com.newsstand.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newsstand.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository ArticleRepository;
    @Autowired
    ArticleService articleService;

    @PostMapping("/api/register")
    public User register(@RequestBody User user, HttpSession session) {

        Iterator<User> userIterator = this.userRepository.findAll().iterator();

        while (userIterator.hasNext()) {
            if (userIterator.next().getUsername().equals(user.getUsername())) {
                return new User();
            }
        }

        user.setLikedArticles(new HashSet<>());
        user.setCreatedArticles(new HashSet<>());
        user.setComments(new ArrayList<>());

        user = userRepository.save(user);
        session.setAttribute("currentUser", user);
        return user;

    }

    @PostMapping("/api/login")
    public User login(@RequestBody User user, HttpSession session) {

        Iterator<User> userIterator = userRepository.findAll().iterator();

        while (userIterator.hasNext()) {

            User existingUser = userIterator.next();

            if (existingUser.getUsername().equals(user.getUsername()) &&
                    existingUser.getPassword().equals(user.getPassword())) {

                session.setAttribute("currentUser", existingUser);
                return existingUser;
            }
        }

        return new User();

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


    @GetMapping("/api/users/{userId}")
    public User findUserById(@PathVariable Integer userId, HttpSession session) {

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isPresent()) {

            User user = optional.get();

            User newUser = new User();

            newUser.setId(user.getId());
            newUser.setUsername(user.getUsername());
            newUser.setRole(user.getRole());

            return newUser;

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
    public Iterable<Article> findAllArticlesCommentedByUser(@PathVariable("userId") Integer userId) {


        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            return user.getCreatedArticles();
        }
        return new ArrayList<>();
    }

    @GetMapping("/api/user/{userId}/reviews")
    public String findAllUserReviews(@PathVariable("userId") Integer userId) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Optional<User> optional = userRepository.findById(userId);

        if (optional.isPresent()) {
            User user = optional.get();
            List<Comment> reviews = user.getComments();
            Collections.reverse(reviews);
            return  gson.toJson(reviews);
        }

        return gson.toJson(new ArrayList<>());
    }

    @PutMapping("/api/update")
    public User update(@RequestBody User user, HttpSession session) {

        Optional<User> optionalObject = userRepository.findById(user.getId());
        if (!optionalObject.isPresent()) {
            return new User();
        }
        User existingUser = optionalObject.get();
        existingUser.set(user);
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    @GetMapping("/api/user/{uid}/liked")
    public Set<Article> likedArticles(@PathVariable("uid") Integer userId, HttpSession session) throws AuthenticationException {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = findUserById(userId, session);
        if (currentUser != null) {
            return articleService.getArticlesLikedByUser(user);
        }
        throw new AuthenticationException("User not logged in");

    }

}
