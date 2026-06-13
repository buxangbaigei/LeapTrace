package com.example.demo.service;
import com.example.demo.dto.ReminderRequest;
import com.example.demo.dto.ReminderVO;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysService {
    String uploadFile(MultipartFile file, Long userId, String fileType);
    List<ReminderVO> getUserReminders(Long userId);
    void updateReminder(Long userId, ReminderRequest request);


    // 获取用户信息
    User getUserInfo(Long userId);

    void deleteReminder(Long id, Long userId);
}

