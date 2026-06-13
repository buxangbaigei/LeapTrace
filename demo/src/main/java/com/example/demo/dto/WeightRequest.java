package com.example.demo.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WeightRequest {
    private BigDecimal weight;
    private LocalDate recordDate;

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}

