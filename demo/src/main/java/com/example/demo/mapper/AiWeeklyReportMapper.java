package com.example.demo.mapper;
import com.example.demo.entity.AiWeeklyReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AiWeeklyReportMapper {
    int insert(AiWeeklyReport report);
    AiWeeklyReport selectByUserIdAndWeek(
        @Param("userId") Long userId,
        @Param("weekStart") LocalDate weekStart
    );
    List<AiWeeklyReport> selectByUserId(@Param("userId") Long userId);
}

