package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        LoginResponse response = new LoginResponse(token);
        return Result.success(response);
    }
}

