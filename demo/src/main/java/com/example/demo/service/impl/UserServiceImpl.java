package com.example.demo.service.impl;

import com.example.demo.dto.GoalRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserToken;
import com.example.demo.mapper.TokenMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenMapper tokenMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean register(RegisterRequest request) {
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
            user.setNickname(request.getUsername());
        } else {
            user.setNickname(request.getNickname());
        }

        return userMapper.insert(user) > 0;
    }

    @Override
    public String login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        String token = java.util.UUID.randomUUID().toString().replace("-", "");
        java.time.LocalDateTime expireTime = java.time.LocalDateTime.now().plusHours(24);

        UserToken userToken = tokenMapper.findByUserId(user.getId());
        if (userToken != null) {
            userToken.setToken(token);
            userToken.setExpireTime(expireTime);
            tokenMapper.update(userToken);
        } else {
            UserToken newToken = new UserToken();
            newToken.setUserId(user.getId());
            newToken.setToken(token);
            newToken.setExpireTime(expireTime);
            tokenMapper.insert(newToken);
        }

        return token;
    }

    @Override
    public User getUserInfo(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public void updateProfile(Long userId, ProfileRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            user.setWeight(request.getWeight());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }

        userMapper.update(user);
    }

    @Override
    public void setHealthGoal(Long userId, GoalRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (request.getTargetSteps() != null) {
            user.setDailyGoalSteps(request.getTargetSteps());
        }
        if (request.getTargetCalories() != null) {
            user.setDailyGoalCalories(request.getTargetCalories());
        }

        userMapper.update(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatarPath) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setAvatar(avatarPath);
        userMapper.update(user);
    }
}


