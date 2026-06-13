package com.example.demo.entity;
import lombok.Data; import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data public class DietRecord {
    private Long id;
    private Long userId;
    private String foodName;
    private Integer gram;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbs;
    private LocalDateTime eatTime;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
