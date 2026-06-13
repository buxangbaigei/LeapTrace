package com.example.demo.service;
import com.example.demo.dto.TodayDietVO;
import com.example.demo.entity.DietLog;
import java.util.List;

public interface DietService {
    int saveDietLog(DietLog dietLog);
    DietLog getDietLogById(Long id);
    int deleteDietLog(Long id);
    List<DietLog> getUserDietLogs(Long userId);
    List<TodayDietVO> getTodayDiet(Long userId);
}


