package com.example.demo.dto;
import lombok.Data;

@Data
public class ChatRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

