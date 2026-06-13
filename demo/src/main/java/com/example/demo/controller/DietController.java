package com.example.demo.controller;
import com.example.demo.common.Result;
import com.example.demo.dto.DietAnalyzeRequest;
import com.example.demo.entity.DietLogRequest;
import com.example.demo.dto.FoodNutritionDTO;
import com.example.demo.dto.FoodSearchVO;
import com.example.demo.dto.TodayDietVO;
import com.example.demo.entity.DietLog;
import com.example.demo.service.AiService;
import com.example.demo.service.DietService;
import com.example.demo.service.FoodLibService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/diet")
public class DietController {
    @Autowired
    private AiService aiService;

    @Autowired
    private DietService dietService;

    @Autowired
    private FoodLibService foodLibService;

    @PostMapping("/ai-analyze/image")
    public Result<FoodNutritionDTO> analyzeFoodImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        if (file == null || file.isEmpty()) {
            return Result.error("请上传图片");
        }

        FoodNutritionDTO result = aiService.analyzeFoodImage(file);
        return Result.success(result);
    }

    @PostMapping("/ai-analyze/text")
    public Result<FoodNutritionDTO> analyzeFoodText(
            @RequestBody DietAnalyzeRequest jsonRequest,
            HttpServletRequest request) {

        if (jsonRequest == null || jsonRequest.getText() == null || jsonRequest.getText().isEmpty()) {
            return Result.error("请输入食物描述");
        }

        FoodNutritionDTO result = aiService.analyzeFoodText(jsonRequest.getText());
        return Result.success(result);
    }

    @PostMapping("/log")
    public Result<Long> saveDietLog(
            @RequestBody DietLogRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        DietLog dietLog = new DietLog();
        dietLog.setUserId(userId);
        dietLog.setFoodName(request.getFoodName());
        dietLog.setCalories(request.getCalories());
        dietLog.setMealType(request.getMealType());
        dietLog.setGram(request.getGram());
        dietLog.setProtein(request.getProtein());
        dietLog.setFat(request.getFat());
        dietLog.setCarbs(request.getCarbs());

        int rows = dietService.saveDietLog(dietLog);

        if (rows > 0) {
            return Result.success(dietLog.getId());
        } else {
            return Result.error("保存失败");
        }
    }

    @GetMapping("/today")
    public Result<List<TodayDietVO>> getTodayDiet(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<TodayDietVO> todayDiet = dietService.getTodayDiet(userId);
        return Result.success(todayDiet);
    }

    @DeleteMapping("/log/{id}")
    public Result<Void> deleteDietLog(@PathVariable Long id, HttpServletRequest httpRequest) {
        DietLog dietLog = dietService.getDietLogById(id);

        if (dietLog == null) {
            return Result.error("记录不存在");
        }

//        Long userId = (Long) httpRequest.getAttribute("userId");
//        if (!dietLog.getUserId().equals(userId)) {
//            return Result.error("无权限删除该记录");
//        }
//
        int rows = dietService.deleteDietLog(id);

        if (rows > 0) {
            return Result.success(null);
        } else {
            return Result.error("删除失败");
        }
    }

    @GetMapping("/food/search")
    public Result<List<FoodSearchVO>> searchFoods(@RequestParam String keyword) {
        List<FoodSearchVO> foods = foodLibService.searchFoods(keyword);
        return Result.success(foods);
    }
}





