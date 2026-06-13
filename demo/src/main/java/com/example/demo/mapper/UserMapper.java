package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM u_user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM u_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    // 根据ID查询用户
    @Select("SELECT * FROM u_user WHERE id = #{id}")
    @Results(id = "userResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "height", column = "height"),
            @Result(property = "weight", column = "weight"),
            @Result(property = "age", column = "age"),
            @Result(property = "dailyGoalSteps", column = "daily_goal_steps"),
            @Result(property = "dailyGoalCalories", column = "daily_goal_calories"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    User selectById(@Param("id") Long id);

    // 根据用户名查询
    @Select("SELECT * FROM u_user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    @Insert("INSERT INTO u_user(username, password, nickname) VALUES(#{username}, #{password}, #{nickname})")
    int insert(User user);
    @Update("<script>UPDATE u_user SET " +
            "<if test=\"nickname != null\">nickname = #{nickname},</if>" +
            "<if test=\"gender != null\">gender = #{gender},</if>" +
            "<if test=\"age != null\">age = #{age},</if>" +
            "<if test=\"height != null\">height = #{height},</if>" +
            "<if test=\"weight != null\">weight = #{weight},</if>" +
            "<if test=\"targetWeight != null\">target_weight = #{targetWeight},</if>" +
            "<if test=\"dailyGoalSteps != null\">daily_goal_steps = #{dailyGoalSteps},</if>" +
            "<if test=\"dailyGoalCalories != null\">daily_goal_calories = #{dailyGoalCalories},</if>" +
            "<if test=\"avatar != null\">avatar = #{avatar},</if>" + // 新增这一行
            "updated_at = NOW() " +
            "WHERE id = #{id}</script>")
    int update(User user);


}

