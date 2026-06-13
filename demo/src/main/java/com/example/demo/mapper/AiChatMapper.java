package com.example.demo.mapper;
import com.example.demo.entity.AiChat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiChatMapper {
    int insert(AiChat aiChat);
    AiChat selectById(@Param("id") Long id);
    List<AiChat> selectByUserIdWithPagination(
        @Param("userId") Long userId,
        @Param("offset") Integer offset,
        @Param("limit") Integer limit
    );
}


