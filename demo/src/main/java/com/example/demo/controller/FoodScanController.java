package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.FoodScanResultVO;
import com.example.demo.entity.User;
import com.example.demo.service.FoodScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/diet")
public class FoodScanController {

    @Autowired
    private FoodScanService foodScanService;

    @PostMapping("/scan-analyze")
    public Result<FoodScanResultVO> scanAndAnalyze(@RequestParam("file") MultipartFile file) {
        try {
            User currentUser = new User();
            currentUser.setDailyGoalCalories(2500);

            FoodScanResultVO result = foodScanService.analyzeNutritionalLabel(file, currentUser);
            return Result.success("扫描成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("AI 识别失败：" + e.getMessage());
        }
    }
}