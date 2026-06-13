package com.example.demo.service;
import com.example.demo.dto.FoodSearchVO;
import com.example.demo.entity.FoodLib;

import java.util.List;

public interface FoodLibService {
    FoodLib getFoodByName(String name);
    List<FoodSearchVO> searchFoods(String keyword);
}
