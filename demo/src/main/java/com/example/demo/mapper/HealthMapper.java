package com.example.demo.mapper;

import com.example.demo.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface HealthMapper {

    //获取步数
    @Select("SELECT steps FROM h_health_record WHERE user_id = #{userId} AND record_date = #{date}")
    HealthRecord selectStepsByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}