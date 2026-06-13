package com.example.demo.mapper;
import com.example.demo.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HealthRecordMapper {
    int insert(HealthRecord healthRecord);
    int update(HealthRecord healthRecord);
    HealthRecord selectById(@Param("id") Long id);
    HealthRecord selectByUserIdAndDate(
        @Param("userId") Long userId,
        @Param("recordDate") LocalDate recordDate
    );
    List<HealthRecord> selectByUserId(@Param("userId") Long userId);
    List<HealthRecord> selectByDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

