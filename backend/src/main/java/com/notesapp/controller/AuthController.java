package com.notesapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        // Invalidate session
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
    }
}