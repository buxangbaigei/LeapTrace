package com.example.demo.entity;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DietLogRequest {
    private String foodName;
    private Integer calories;
    private Integer mealType;
    private BigDecimal gram;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbs;

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

    public Integer getMealType() {
        return mealType;
    }

    public void setMealType(Integer mealType) {
        this.mealType = mealType;
    }

    public BigDecimal getGram() {
        return gram;
    }

    public void setGram(BigDecimal gram) {
        this.gram = gram;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }
}

