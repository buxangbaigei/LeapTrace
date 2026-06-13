package com.example.demo.dto;

import lombok.Data;

@Data
public class GoalRequest {
    private Integer targetSteps;
    private Integer targetCalories;

    public Integer getTargetSteps() {
        return targetSteps;
    }

    public void setTargetSteps(Integer targetSteps) {
        this.targetSteps = targetSteps;
    }

    public Integer getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(Integer targetCalories) {
        this.targetCalories = targetCalories;
    }
}

