package com.example.demo.service;

import com.example.demo.dto.FoodNutritionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AiService {

    FoodNutritionDTO analyzeFoodImage(MultipartFile file);

    FoodNutritionDTO analyzeFoodText(String text);
}

