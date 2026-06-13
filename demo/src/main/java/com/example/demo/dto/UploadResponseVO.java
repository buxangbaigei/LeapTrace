package com.example.demo.dto;
import lombok.Data;

@Data
public class UploadResponseVO {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

