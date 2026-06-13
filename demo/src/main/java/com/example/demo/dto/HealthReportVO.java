package com.example.demo.dto;
import lombok.Data;
import java.util.List;

@Data
public class HealthReportVO {
    private List<Integer> steps;
    private List<Integer> calories;
    private List<String> dates;

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }

    public List<Integer> getCalories() {
        return calories;
    }

    public void setCalories(List<Integer> calories) {
        this.calories = calories;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}

