package com.example.demo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class UpdateProfileRequest {
    private String nickname;      // 昵称
    private Integer gender;       // 性别：0-未知，1-男，2-女
    private BigDecimal height;       // 身高(cm)
    private BigDecimal weight;       // 体重(kg)
    private Integer age;          // 年龄
    private Integer dailyGoalSteps;     // 每日目标步数
    private Integer dailyGoalCalories;   // 每日目标卡路里
    private MultipartFile avatarFile;    // 头像文件

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDailyGoalSteps() {
        return dailyGoalSteps;
    }

    public void setDailyGoalSteps(Integer dailyGoalSteps) {
        this.dailyGoalSteps = dailyGoalSteps;
    }

    public Integer getDailyGoalCalories() {
        return dailyGoalCalories;
    }

    public void setDailyGoalCalories(Integer dailyGoalCalories) {
        this.dailyGoalCalories = dailyGoalCalories;
    }

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}