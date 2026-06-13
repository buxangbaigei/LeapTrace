package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.GoalRequest;
import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.service.SysService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final String uploadPath = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private UserService userService;


    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody ProfileRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        userService.updateProfile(userId, request);
        return Result.success("更新成功");
    }

    @PostMapping("/goal")
    public Result<Void> setHealthGoal(@RequestBody GoalRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        userService.setHealthGoal(userId, request);
        return Result.success("设置成功");
    }

    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) return Result.error("文件不能为空");

        Long userId = (Long) request.getAttribute("userId");

        try {
            File folder = new File(uploadPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + fileName));
            String dbPath = "/uploads/" + fileName;
            userService.updateAvatar(userId, dbPath);
            return Result.success(dbPath);
        } catch (IOException e) {
            return Result.error("上传失败");
        }
    }
}

