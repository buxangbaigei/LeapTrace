package com.example.demo.service.impl;
import com.example.demo.dto.TodayDietVO;
import com.example.demo.entity.DietLog;
import com.example.demo.mapper.DietLogMapper;
import com.example.demo.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietServiceImpl implements DietService {

    @Autowired
    private DietLogMapper dietLogMapper;

    @Override
    public int saveDietLog(DietLog dietLog) {
        dietLog.setRecordTime(LocalDateTime.now());
        return dietLogMapper.insert(dietLog);
    }

    @Override
    public DietLog getDietLogById(Long id) {
        return dietLogMapper.selectById(id);
    }

    @Override
    public int deleteDietLog(Long id) {
        return dietLogMapper.deleteById(id);
    }

    @Override
    public List<DietLog> getUserDietLogs(Long userId) {
        return dietLogMapper.selectByUserId(userId);
    }

    @Override
    public List<TodayDietVO> getTodayDiet(Long userId) {
        LocalDate today = LocalDate.now();
        List<DietLog> dietLogs = dietLogMapper.selectByDateRange(userId, today, today);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return dietLogs.stream()
                .map(log -> {
                    TodayDietVO vo = new TodayDietVO();
                    vo.setId(log.getId());
                    vo.setFoodName(log.getFoodName());
                    vo.setCalories(log.getCalories());
                    vo.setMealType(log.getMealType());
                    vo.setTime(log.getRecordTime().format(timeFormatter));
                    vo.setGram(log.getGram());
                    vo.setProtein(log.getProtein());
                    vo.setFat(log.getFat());
                    vo.setCarbs(log.getCarbs());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}




