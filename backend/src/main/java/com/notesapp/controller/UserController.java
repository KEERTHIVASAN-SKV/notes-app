package com.notesapp.controller;

import com.notesapp.entity.User;
import com.notesapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:3000"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // Backend validation
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "Username cannot be empty";
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Password cannot be empty";
        }

        if (user.getUsername().length() < 3) {
            return "Username must be at least 3 characters";
        }

        if (user.getPassword().length() < 6) {
            return "Password must be at least 6 characters";
        }

        // Check for existing username
        User existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "Username already exists";
        }

        repository.save(user);
        return "Registration Successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession httpSession) {

        // Backend validation for login
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "Username cannot be empty";
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Password cannot be empty";
        }

        User existingUser = repository.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            httpSession.setAttribute("username", existingUser.getUsername());
            return "Login Success";
        }

        return "Invalid Credentials";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged Out";
    }

    @GetMapping("/check-session")
    public String checkSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            return "Logged in as: " + username;
        }
        return "Not logged in";
    }
}