package com.example.demo.dto;
import lombok.Data;

@Data
public class DashboardVO {
    private Integer consumedCalories;
    private Integer burnedCalories;
    private Integer remainingCalories;
    private Integer steps;
    private Integer goalSteps;

    public Integer getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(Integer consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public Integer getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(Integer burnedCalories) {
        this.burnedCalories = burnedCalories;
    }

    public Integer getRemainingCalories() {
        return remainingCalories;
    }

    public void setRemainingCalories(Integer remainingCalories) {
        this.remainingCalories = remainingCalories;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getGoalSteps() {
        return goalSteps;
    }

    public void setGoalSteps(Integer goalSteps) {
        this.goalSteps = goalSteps;
    }
}

