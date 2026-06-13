package com.example.demo.mapper;

import com.example.demo.entity.UserToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TokenMapper {

    @Select("SELECT * FROM u_user_token WHERE token = #{token}")
    UserToken findByToken(@Param("token") String token);

    @Select("SELECT * FROM u_user_token WHERE user_id = #{userId}")
    UserToken findByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO u_user_token(user_id, token, expire_time) VALUES(#{userId}, #{token}, #{expireTime})")
    int insert(UserToken userToken);

    @Update("UPDATE u_user_token SET token = #{token}, expire_time = #{expireTime} WHERE user_id = #{userId}")
    int update(UserToken userToken);

    @Delete("DELETE FROM u_user_token WHERE token = #{token}")
    int deleteByToken(@Param("token") String token);
}
