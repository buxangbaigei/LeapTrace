package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.ReminderRequest;
import com.example.demo.dto.ReminderVO;
import com.example.demo.service.SysService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys")
public class SysController {

    @Autowired
    private SysService sysService;

    @PostMapping("/upload")
    public Result<Map<String, String>> uploadFile(
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest) {

        System.out.println("========== 文件上传调试 START ==========");
        System.out.println("1. file 对象：" + (file == null ? "null" : "非 null"));
        System.out.println("2. file.isEmpty(): " + (file != null && file.isEmpty()));
        System.out.println("3. file.getName(): " + (file != null ? file.getName() : "N/A"));
        System.out.println("4. file.getOriginalFilename(): " + (file != null ? file.getOriginalFilename() : "N/A"));
        System.out.println("5. file.getSize(): " + (file != null ? file.getSize() : 0));
        System.out.println("6. file.getContentType(): " + (file != null ? file.getContentType() : "N/A"));
        System.out.println("7. userId: " + ((Long) httpRequest.getAttribute("userId")));
        System.out.println("========== 文件上传调试 END ==========");

        Long userId = (Long) httpRequest.getAttribute("userId");

        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        String fileType = null;
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                fileType = "image";
            } else if (contentType.startsWith("video/")) {
                fileType = "video";
            } else {
                fileType = "file";
            }
        }

        String url = sysService.uploadFile(file, userId, fileType);

        Map<String, String> response = new HashMap<>();
        response.put("url", url);

        return Result.success(response);
    }

    @GetMapping("/reminders")
    public Result<List<ReminderVO>> getReminders(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<ReminderVO> reminders = sysService.getUserReminders(userId);
        return Result.success(reminders);
    }

    @PutMapping("/reminders")
    public Result<Void> updateReminder(
            @RequestBody ReminderRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        sysService.updateReminder(userId, request);
        return Result.success(null);
    }

    @DeleteMapping("/reminders/{id}")
    public Result<Void> deleteReminder(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        sysService.deleteReminder(id, userId);
        return Result.success("删除成功");
    }
}

