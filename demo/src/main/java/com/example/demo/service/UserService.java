package com.example.demo.service;

import com.example.demo.dto.GoalRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;


public interface UserService {

    boolean register(RegisterRequest request);

    String login(LoginRequest request);

    User getUserInfo(Long userId);

    void updateProfile(Long userId, ProfileRequest request);

    void setHealthGoal(Long userId, GoalRequest request);

    void updateAvatar(Long userId, String avatarPath);
}



