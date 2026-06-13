package com.example.demo.mapper;
import com.example.demo.entity.FoodLib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodLibMapper {
    FoodLib selectByName(@Param("name") String name);
    List<FoodLib> searchByName(@Param("keyword") String keyword);
    FoodLib selectById(@Param("id") Long id);
}


