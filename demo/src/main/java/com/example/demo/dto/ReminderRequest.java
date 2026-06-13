package com.example.demo.dto;
import lombok.Data;
import java.time.LocalTime;

@Data
public class ReminderRequest {
    private String type;
    private LocalTime time;
    private Boolean isEnabled;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}

