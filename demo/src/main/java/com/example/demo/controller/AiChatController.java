package com.example.demo.controller;
import com.example.demo.common.Result;
import com.example.demo.dto.ChatHistoryDTO;
import com.example.demo.dto.ChatRequest;
import com.example.demo.dto.WeeklyAdviceVO;
import com.example.demo.service.AiChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/chat")
    public Result<Map<String, String>> chat(
            @RequestBody ChatRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String userNickname = (String) httpRequest.getAttribute("userNickname");

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return Result.error("消息内容不能为空");
        }

        try {
            String reply = aiChatService.chat(userId, userNickname, request.getContent());

            Map<String, String> response = new HashMap<>();
            response.put("reply", reply);

            return Result.success(response);
        } catch (Exception e) {
            return Result.error("请求失败：" + e.getMessage());
        }
    }

    @GetMapping("/chat/history")
    public Result<List<ChatHistoryDTO>> getChatHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        List<ChatHistoryDTO> history = aiChatService.getChatHistory(userId, page, pageSize);
        return Result.success(history);
    }

    @GetMapping("/weekly-advice")
    public Result<Map<String, String>> getWeeklyAdvice(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        WeeklyAdviceVO advice = aiChatService.generateWeeklyAdvice(userId);

        Map<String, String> response = new HashMap<>();
        response.put("summary", advice.getSummary());

        return Result.success(response);
    }
}