package com.prodigy.server.auth.controller;

import com.prodigy.server.auth.model.TokenResponse;
import com.prodigy.server.auth.model.User;
import com.prodigy.server.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author badreddine
 **/

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody User user){

        return userService.verify(user);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody Map<String, String> requestBody){
        String refreshToken = requestBody.get("refreshToken");
        return userService.refresh(refreshToken);
    }
}