package com.example.demo.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FoodSearchVO {
    private Long id;
    private String name;
    private String unit;
    private Integer unitCalories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getUnitCalories() {
        return unitCalories;
    }

    public void setUnitCalories(Integer unitCalories) {
        this.unitCalories = unitCalories;
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

