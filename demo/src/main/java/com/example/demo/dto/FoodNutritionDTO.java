package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodNutritionDTO {
    private String foodName;
    private Integer gram;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbs;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getGram() {
        return gram;
    }

    public void setGram(Integer gram) {
        this.gram = gram;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = calories;
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

