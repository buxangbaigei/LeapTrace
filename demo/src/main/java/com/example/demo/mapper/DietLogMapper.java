package com.example.demo.mapper;
import com.example.demo.entity.DietLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietLogMapper {
    int insert(DietLog dietLog);
    DietLog selectById(@Param("id") Long id);
    int deleteById(@Param("id") Long id);
    List<DietLog> selectByUserId(@Param("userId") Long userId);
    List<DietLog> selectByDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}


