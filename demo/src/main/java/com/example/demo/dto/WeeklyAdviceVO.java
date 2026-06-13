package com.example.demo.dto;
import lombok.Data;

@Data
public class WeeklyAdviceVO {
    private String summary;
    private String dietAdvice;
    private String exerciseAdvice;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDietAdvice() {
        return dietAdvice;
    }

    public void setDietAdvice(String dietAdvice) {
        this.dietAdvice = dietAdvice;
    }

    public String getExerciseAdvice() {
        return exerciseAdvice;
    }

    public void setExerciseAdvice(String exerciseAdvice) {
        this.exerciseAdvice = exerciseAdvice;
    }
}

