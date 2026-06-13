package com.example.demo.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StepsRequest {
    private Integer steps;
    private LocalDate recordDate;

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}

