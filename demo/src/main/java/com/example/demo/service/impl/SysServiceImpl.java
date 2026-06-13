package com.example.demo.service.impl;
import com.example.demo.dto.ReminderRequest;
import com.example.demo.dto.ReminderVO;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.SysFile;
import com.example.demo.entity.SysReminder;
import com.example.demo.entity.User;
import com.example.demo.mapper.SysFileMapper;
import com.example.demo.mapper.SysReminderMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private SysReminderMapper sysReminderMapper;

    @Autowired
    private UserMapper userMapper;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public String uploadFile(MultipartFile file, Long userId, String fileType) {
        try {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null ?
                    originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
            String newFileName = UUID.randomUUID().toString() + extension;


            String projectRoot = System.getProperty("user.dir");
            Path uploadPath = Paths.get(projectRoot, UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath);

            SysFile sysFile = new SysFile();
            sysFile.setUserId(userId);

            sysFile.setFileUrl("/uploads/" + newFileName);
            sysFile.setFileType(fileType);
            sysFile.setCreatedAt(LocalDateTime.now());

            sysFileMapper.insert(sysFile);

            return "/uploads/" + newFileName;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public List<ReminderVO> getUserReminders(Long userId) {
        List<SysReminder> reminders = sysReminderMapper.selectByUserId(userId);

        return reminders.stream()
                .map(reminder -> {
                    ReminderVO vo = new ReminderVO();
                    vo.setId(reminder.getId());
                    vo.setType(reminder.getType());
                    vo.setTime(reminder.getTime());
                    vo.setIsEnabled(reminder.getIsEnabled());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateReminder(Long userId, ReminderRequest request) {
        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new RuntimeException("提醒类型不能为空");
        }

        if (request.getTime() == null) {
            throw new RuntimeException("提醒时间不能为空");
        }

        SysReminder existingReminder = sysReminderMapper.selectByUserIdAndType(userId, request.getType());

        if (existingReminder != null) {
            existingReminder.setTime(request.getTime());
            existingReminder.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);
            sysReminderMapper.update(existingReminder);
        } else {
            SysReminder newReminder = new SysReminder();
            newReminder.setUserId(userId);
            newReminder.setType(request.getType());
            newReminder.setTime(request.getTime());
            newReminder.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);
            newReminder.setCreatedAt(LocalDateTime.now());
            sysReminderMapper.insert(newReminder);
        }
    }


    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 隐藏敏感信息
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReminder(Long id, Long userId) {
        int rows = sysReminderMapper.deleteByIdAndUserId(id, userId);
        if (rows == 0) {
            throw new RuntimeException("删除失败，记录不存在或无权限");
        }
    }
}

