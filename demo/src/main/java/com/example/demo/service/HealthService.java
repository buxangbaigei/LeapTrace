package com.example.demo.service;
import com.example.demo.dto.DashboardVO;
import com.example.demo.dto.HealthRecordVO;
import com.example.demo.dto.HealthReportVO;
import com.example.demo.entity.HealthRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HealthService {
    int saveSteps(Long userId, Integer steps, LocalDate recordDate);
    int saveWeight(Long userId, BigDecimal weight, LocalDate recordDate);
    HealthRecord getHealthRecord(Long userId, LocalDate recordDate);
    List<HealthRecordVO> getUserHealthRecords(Long userId);
    List<HealthRecordVO> getHealthRecordsByDateRange(
        Long userId, LocalDate startDate, LocalDate endDate
    );
    HealthReportVO getHealthReport(Long userId, String type);
    DashboardVO getDashboardData(Long userId);

    Integer getTodaySteps(Long userId);
}




