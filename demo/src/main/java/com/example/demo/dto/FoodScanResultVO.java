package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// DTO: 用于向前端返回分析结果
@Data
public class FoodScanResultVO {
    @JsonProperty("name")
    private String foodName;
    private Integer calories;
    private Double protein;
    private Double fat;
    private Double carbs;
    private Double sodium; // 钠

    // 占比计算结果
    private Double caloriesRatio; // 热量占每日目标的百分比
    private String status;        // safe, warning, danger
    private String advice;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public Double getCaloriesRatio() {
        return caloriesRatio;
    }

    public void setCaloriesRatio(Double caloriesRatio) {
        this.caloriesRatio = caloriesRatio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
    // 避坑建议
}