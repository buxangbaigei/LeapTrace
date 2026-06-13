package com.example.demo.service.impl;
import com.example.demo.dto.DashboardVO;
import com.example.demo.dto.HealthRecordVO;
import com.example.demo.dto.HealthReportVO;
import com.example.demo.entity.DietLog;
import com.example.demo.entity.HealthRecord;
import com.example.demo.entity.User;
import com.example.demo.mapper.DietLogMapper;
import com.example.demo.mapper.HealthMapper;
import com.example.demo.mapper.HealthRecordMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Autowired
    private DietLogMapper dietLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HealthMapper healthMapper;

    @Override
    public int saveSteps(Long userId, Integer steps, LocalDate recordDate) {
        HealthRecord existingRecord = healthRecordMapper.selectByUserIdAndDate(userId, recordDate);

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        BigDecimal weight = user.getWeight();
        if (weight == null) {
            weight = BigDecimal.valueOf(60); // 默认体重 60kg
        }

        int caloriesBurned = (int) (steps * weight.doubleValue() * 0.0007);

        if (existingRecord != null) {
            existingRecord.setSteps(steps);
            existingRecord.setCaloriesBurned(caloriesBurned);
            return healthRecordMapper.update(existingRecord);
        } else {
            HealthRecord newRecord = new HealthRecord();
            newRecord.setUserId(userId);
            newRecord.setSteps(steps);
            newRecord.setCaloriesBurned(caloriesBurned);
            newRecord.setRecordDate(recordDate);
            newRecord.setCreatedAt(LocalDateTime.now());
            return healthRecordMapper.insert(newRecord);
        }
    }

    @Override
    public int saveWeight(Long userId, BigDecimal weight, LocalDate recordDate) {
        HealthRecord existingRecord = healthRecordMapper.selectByUserIdAndDate(userId, recordDate);

        if (existingRecord != null) {
            existingRecord.setWeight(weight);
            return healthRecordMapper.update(existingRecord);
        } else {
            HealthRecord newRecord = new HealthRecord();
            newRecord.setUserId(userId);
            newRecord.setWeight(weight);
            newRecord.setRecordDate(recordDate);
            newRecord.setCreatedAt(LocalDateTime.now());
            return healthRecordMapper.insert(newRecord);
        }
    }

    @Override
    public HealthRecord getHealthRecord(Long userId, LocalDate recordDate) {
        return healthRecordMapper.selectByUserIdAndDate(userId, recordDate);
    }

    @Override
    public List<HealthRecordVO> getUserHealthRecords(Long userId) {
        List<HealthRecord> records = healthRecordMapper.selectByUserId(userId);
        return records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthRecordVO> getHealthRecordsByDateRange(
            Long userId, LocalDate startDate, LocalDate endDate) {
        List<HealthRecord> records = healthRecordMapper.selectByDateRange(userId, startDate, endDate);
        return records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public HealthReportVO getHealthReport(Long userId, String type) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (type) {
            case "week":
                startDate = endDate.minusDays(6);
                break;
            case "month":
                startDate = endDate.minusDays(29);
                break;
            case "year":
                startDate = endDate.minusDays(364);
                break;
            default:
                startDate = endDate.minusDays(6);
        }

        List<HealthRecord> healthRecords = healthRecordMapper.selectByDateRange(userId, startDate, endDate);
        List<DietLog> dietLogs = dietLogMapper.selectByDateRange(userId, startDate, endDate);

        Map<LocalDate, HealthRecord> healthRecordMap = healthRecords.stream()
                .collect(Collectors.toMap(
                        HealthRecord::getRecordDate,
                        record -> record
                ));

        Map<LocalDate, Integer> dailyCaloriesMap = dietLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getRecordTime().toLocalDate(),
                        Collectors.summingInt(log -> log.getCalories().intValue())
                ));

        List<String> dates = new ArrayList<>();
        List<Integer> stepsList = new ArrayList<>();
        List<Integer> caloriesList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.format(formatter));

            HealthRecord record = healthRecordMap.get(date);
            stepsList.add(record != null && record.getSteps() != null ? record.getSteps() : 0);

            Integer calories = dailyCaloriesMap.get(date);
            caloriesList.add(calories != null ? calories : 0);
        }

        HealthReportVO report = new HealthReportVO();
        report.setSteps(stepsList);
        report.setCalories(caloriesList);
        report.setDates(dates);

        return report;
    }

    @Override
    public DashboardVO getDashboardData(Long userId) {
        LocalDate today = LocalDate.now();

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        HealthRecord todayRecord = healthRecordMapper.selectByUserIdAndDate(userId, today);

        List<DietLog> todayDietLogs = dietLogMapper.selectByDateRange(userId, today, today);
        int consumedCalories = todayDietLogs.stream()
                .mapToInt(log -> log.getCalories().intValue())
                .sum();

        int steps = todayRecord != null && todayRecord.getSteps() != null ? todayRecord.getSteps() : 0;
        int burnedCalories = todayRecord != null && todayRecord.getCaloriesBurned() != null ? todayRecord.getCaloriesBurned() : 0;
        int goalSteps = user.getDailyGoalSteps() != null ? user.getDailyGoalSteps() : 8000;
        int goalCalories = user.getDailyGoalCalories() != null ? user.getDailyGoalCalories() : 2500;

        int remainingCalories = goalCalories - consumedCalories + burnedCalories;

        DashboardVO dashboard = new DashboardVO();
        dashboard.setConsumedCalories(consumedCalories);
        dashboard.setBurnedCalories(burnedCalories);
        dashboard.setRemainingCalories(remainingCalories);
        dashboard.setSteps(steps);
        dashboard.setGoalSteps(goalSteps);

        return dashboard;
    }

    private HealthRecordVO convertToVO(HealthRecord record) {
        HealthRecordVO vo = new HealthRecordVO();
        vo.setId(record.getId());
        vo.setSteps(record.getSteps());
        vo.setSleepHours(record.getSleepHours());
        vo.setWeight(record.getWeight());
        vo.setCaloriesBurned(record.getCaloriesBurned());
        vo.setRecordDate(record.getRecordDate());
        return vo;
    }

    @Override
    public Integer getTodaySteps(Long userId) {
        LocalDate today = LocalDate.now();
        HealthRecord record = healthMapper.selectStepsByUserIdAndDate(userId, today);
        return (record != null) ? record.getSteps() : 0;
    }
}





