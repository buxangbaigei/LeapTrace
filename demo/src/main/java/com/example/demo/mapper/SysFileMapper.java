package com.example.demo.mapper;
import com.example.demo.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysFileMapper {
    int insert(SysFile sysFile);
    SysFile selectById(@Param("id") Long id);
}

