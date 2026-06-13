package com.example.demo.service;
import com.example.demo.dto.ChatHistoryDTO;
import com.example.demo.dto.WeeklyAdviceVO;

import java.util.List;

public interface AiChatService {
    String chat(Long userId, String userNickname, String content);
    List<ChatHistoryDTO> getChatHistory(Long userId, Integer page, Integer pageSize);
    WeeklyAdviceVO generateWeeklyAdvice(Long userId);
}
