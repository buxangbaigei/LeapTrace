package com.example.demo.controller;
import com.example.demo.common.Result;
import com.example.demo.dto.DashboardVO;
import com.example.demo.dto.HealthRecordVO;
import com.example.demo.dto.HealthReportVO;
import com.example.demo.dto.StepsRequest;
import com.example.demo.dto.WeightRequest;
import com.example.demo.entity.HealthRecord;
import com.example.demo.service.HealthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @PostMapping("/steps")
    public Result<Void> syncSteps(
            @RequestBody StepsRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (request.getSteps() == null || request.getSteps() < 0) {
            return Result.error("步数必须大于 0");
        }

        LocalDate recordDate = request.getRecordDate();
        if (recordDate == null) {
            recordDate = LocalDate.now();
        }

        healthService.saveSteps(userId, request.getSteps(), recordDate);
        return Result.success("同步成功");
    }

    @PostMapping("/weight")
    public Result<Void> recordWeight(
            @RequestBody WeightRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (request.getWeight() == null || request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("体重必须大于 0");
        }

        LocalDate recordDate = request.getRecordDate();
        if (recordDate == null) {
            recordDate = LocalDate.now();
        }

        healthService.saveWeight(userId, request.getWeight(), recordDate);
        return Result.success("记录成功");
    }

    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboard(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        DashboardVO dashboard = healthService.getDashboardData(userId);
        return Result.success(dashboard);
    }

    @GetMapping("/report")
    public Result<HealthReportVO> getHealthReport(
            @RequestParam(defaultValue = "week") String type,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (!"week".equals(type) && !"month".equals(type) && !"year".equals(type)) {
            return Result.error("type 参数必须是 week、month 或 year");
        }

        HealthReportVO report = healthService.getHealthReport(userId, type);
        return Result.success(report);
    }

    @GetMapping("/record")
    public Result<HealthRecordVO> getHealthRecord(
            @RequestParam(required = false) LocalDate recordDate,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (recordDate == null) {
            recordDate = LocalDate.now();
        }

        HealthRecord record = healthService.getHealthRecord(userId, recordDate);

        if (record == null) {
            return Result.error("暂无记录");
        }

        HealthRecordVO vo = new HealthRecordVO();
        vo.setId(record.getId());
        vo.setSteps(record.getSteps());
        vo.setSleepHours(record.getSleepHours());
        vo.setWeight(record.getWeight());
        vo.setCaloriesBurned(record.getCaloriesBurned());
        vo.setRecordDate(record.getRecordDate());

        return Result.success(vo);
    }

    @GetMapping("/records")
    public Result<List<HealthRecordVO>> getUserRecords(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<HealthRecordVO> records = healthService.getHealthRecordsByDateRange(userId, startDate, endDate);
        return Result.success(records);
    }


    @GetMapping("/steps/today")
    public Result<Integer> getTodaySteps(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        Integer steps = healthService.getTodaySteps(userId);
        return Result.success(steps != null ? steps : 0);
    }

}



