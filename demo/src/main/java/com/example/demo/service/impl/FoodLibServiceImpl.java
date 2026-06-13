package com.example.demo.service.impl;
import com.example.demo.dto.FoodSearchVO;
import com.example.demo.entity.FoodLib;
import com.example.demo.mapper.FoodLibMapper;
import com.example.demo.service.FoodLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodLibServiceImpl implements FoodLibService {

    @Autowired
    private FoodLibMapper foodLibMapper;

    @Override
    public FoodLib getFoodByName(String name) {
        return foodLibMapper.selectByName(name);
    }

    @Override
    public List<FoodSearchVO> searchFoods(String keyword) {
        List<FoodLib> foodList = foodLibMapper.searchByName(keyword);

        return foodList.stream()
                .map(food -> {
                    FoodSearchVO vo = new FoodSearchVO();
                    vo.setId(food.getId());
                    vo.setName(food.getName());
                    vo.setUnit(food.getUnit());
                    vo.setUnitCalories(food.getUnitCalories());
                    vo.setProtein(food.getProtein());
                    vo.setFat(food.getFat());
                    vo.setCarbs(food.getCarbs());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}

