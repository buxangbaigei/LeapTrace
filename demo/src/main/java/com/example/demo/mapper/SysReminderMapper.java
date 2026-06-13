package com.example.demo.mapper;
import com.example.demo.entity.SysReminder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysReminderMapper {
    int insert(SysReminder reminder);
    int update(SysReminder reminder);
    int deleteById(@Param("id") Long id);
    SysReminder selectById(@Param("id") Long id);
    List<SysReminder> selectByUserId(@Param("userId") Long userId);
    SysReminder selectByUserIdAndType(
        @Param("userId") Long userId,
        @Param("type") String type
    );

    @Delete("DELETE FROM sys_reminder WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}

