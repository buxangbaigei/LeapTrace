package com.example.demo.service.impl;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.example.demo.dto.ChatHistoryDTO;
import com.example.demo.dto.WeeklyAdviceVO;
import com.example.demo.entity.AiChat;
import com.example.demo.entity.AiWeeklyReport;
import com.example.demo.entity.DietLog;
import com.example.demo.entity.HealthRecord;
import com.example.demo.entity.User;
import com.example.demo.mapper.AiChatMapper;
import com.example.demo.mapper.AiWeeklyReportMapper;
import com.example.demo.mapper.DietLogMapper;
import com.example.demo.mapper.HealthRecordMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiChatServiceImpl implements AiChatService {
    @Autowired
    private AiChatMapper aiChatMapper;

    @Autowired
    private AiWeeklyReportMapper aiWeeklyReportMapper;

    @Autowired
    private DietLogMapper dietLogMapper;

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${dashscope.api-key:}")
    private String apiKey;

    @Override
    public String chat(Long userId, String userNickname, String content) {
        try {
            StringBuilder contextBuilder = new StringBuilder();

            if (userNickname != null && !userNickname.isEmpty()) {
                contextBuilder.append("我的昵称是：").append(userNickname).append("\n");
            }

            contextBuilder.append("\n用户消息：").append(content);

            String prompt = contextBuilder.toString();

            Generation gen = new Generation();
            Message userMessage = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();

            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-turbo")
                    .messages(java.util.Collections.singletonList(userMessage))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            String reply = result.getOutput().getChoices().get(0).getMessage().getContent();

            // 保存聊天记录
            AiChat userChat = new AiChat();
            userChat.setUserId(userId);
            userChat.setRole("user");
            userChat.setContent(content);
            userChat.setCreatedAt(LocalDateTime.now());
            aiChatMapper.insert(userChat);

            AiChat assistantChat = new AiChat();
            assistantChat.setUserId(userId);
            assistantChat.setRole("assistant");
            assistantChat.setContent(reply);
            assistantChat.setCreatedAt(LocalDateTime.now());
            aiChatMapper.insert(assistantChat);

            return reply;
        } catch (Exception e) {
            throw new RuntimeException("AI 调用失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<ChatHistoryDTO> getChatHistory(Long userId, Integer page, Integer pageSize) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 20;
        }

        int offset = (page - 1) * pageSize;
        List<AiChat> chats = aiChatMapper.selectByUserIdWithPagination(userId, offset, pageSize);

        return chats.stream()
                .map(chat -> {
                    ChatHistoryDTO dto = new ChatHistoryDTO();
                    dto.setId(chat.getId());
                    dto.setRole(chat.getRole());
                    dto.setContent(chat.getContent());
                    dto.setCreatedAt(chat.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyAdviceVO generateWeeklyAdvice(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        AiWeeklyReport existingReport = aiWeeklyReportMapper.selectByUserIdAndWeek(userId, weekStart);
        if (existingReport != null) {
            WeeklyAdviceVO adviceVO = new WeeklyAdviceVO();
            adviceVO.setSummary(existingReport.getContent());
            return adviceVO;
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<DietLog> dietLogs = dietLogMapper.selectByDateRange(userId, weekStart, weekEnd);
        List<HealthRecord> healthRecords = healthRecordMapper.selectByDateRange(userId, weekStart, weekEnd);

        int totalCalories = dietLogs.stream()
                .mapToInt(log -> log.getCalories().intValue())
                .sum();

        int totalSteps = healthRecords.stream()
                .mapToInt(record -> record.getSteps() != null ? record.getSteps() : 0)
                .sum();

        int totalBurnedCalories = healthRecords.stream()
                .mapToInt(record -> record.getCaloriesBurned() != null ? record.getCaloriesBurned() : 0)
                .sum();

        double avgDailyCalories = totalCalories / 7.0;
        double avgDailySteps = totalSteps / 7.0;

        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("用户信息：").append(user.getNickname()).append("\n");
        if (user.getAge() != null) {
            contextBuilder.append("年龄：").append(user.getAge()).append("岁\n");
        }
        if (user.getWeight() != null) {
            contextBuilder.append("当前体重：").append(user.getWeight()).append("kg\n");
        }
        if (user.getDailyGoalCalories() != null) {
            contextBuilder.append("每日热量目标：").append(user.getDailyGoalCalories()).append("kcal\n");
        }
        if (user.getDailyGoalSteps() != null) {
            contextBuilder.append("每日步数目标：").append(user.getDailyGoalSteps()).append("步\n");
        }
        contextBuilder.append("\n本周数据（").append(weekStart).append(" 至 ").append(weekEnd).append("）：\n");
        contextBuilder.append("总摄入热量：").append(totalCalories).append(" kcal\n");
        contextBuilder.append("平均每日摄入：").append(String.format("%.1f", avgDailyCalories)).append(" kcal\n");
        contextBuilder.append("总运动步数：").append(totalSteps).append(" 步\n");
        contextBuilder.append("平均每日步数：").append(String.format("%.0f", avgDailySteps)).append(" 步\n");
        contextBuilder.append("总消耗热量：").append(totalBurnedCalories).append(" kcal\n");

        String prompt = "你是一位专业的健康管理顾问。请根据以下用户数据，生成简洁、积极、有建设性的周报建议：\n\n" +
                contextBuilder.toString() + "\n\n" +
                "请从以下三个方面总结（每方面 1-2 句话）：\n" +
                "1. 本周表现总结\n" +
                "2. 饮食建议\n" +
                "3. 运动建议\n\n" +
                "要求：语气鼓励性，建议具体可执行，总字数不超过 300 字。";

        try {
            Generation gen = new Generation();
            Message userMessage = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();

            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-turbo")
                    .messages(java.util.Collections.singletonList(userMessage))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            String advice = result.getOutput().getChoices().get(0).getMessage().getContent();

            AiWeeklyReport report = new AiWeeklyReport();
            report.setUserId(userId);
            report.setWeekStart(weekStart);
            report.setWeekEnd(weekEnd);
            report.setContent(advice);
            report.setCreatedAt(LocalDateTime.now());
            aiWeeklyReportMapper.insert(report);

            WeeklyAdviceVO adviceVO = new WeeklyAdviceVO();
            adviceVO.setSummary(advice);

            return adviceVO;
        } catch (Exception e) {
            throw new RuntimeException("AI 调用失败：" + e.getMessage(), e);
        }
    }

    // ... existing code ...

    private String buildPrompt(Long userId, User user, String userContent) {
        LocalDate today = LocalDate.now();

        List<DietLog> todayDietLogs = dietLogMapper.selectByDateRange(userId, today, today);
        int consumedCalories = todayDietLogs.stream()
                .mapToInt(log -> log.getCalories().intValue())
                .sum();

        HealthRecord todayRecord = healthRecordMapper.selectByUserIdAndDate(userId, today);
        int burnedCalories = todayRecord != null && todayRecord.getCaloriesBurned() != null ? todayRecord.getCaloriesBurned() : 0;
        int steps = todayRecord != null && todayRecord.getSteps() != null ? todayRecord.getSteps() : 0;

        int goalCalories = user.getDailyGoalCalories() != null ? user.getDailyGoalCalories() : 2500;
        int remainingCalories = goalCalories - consumedCalories + burnedCalories;

        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("用户信息：\n");
        if (user.getNickname() != null) {
            contextBuilder.append("昵称：").append(user.getNickname()).append("\n");
        }
        if (user.getAge() != null) {
            contextBuilder.append("年龄：").append(user.getAge()).append("岁\n");
        }
        if (user.getGender() != null) {
            contextBuilder.append("性别：").append(user.getGender() == 1 ? "男" : user.getGender() == 2 ? "女" : "未知").append("\n");
        }
        if (user.getWeight() != null) {
            contextBuilder.append("体重：").append(user.getWeight()).append("kg\n");
        }
        if (user.getHeight() != null) {
            contextBuilder.append("身高：").append(user.getHeight()).append("cm\n");
        }

        contextBuilder.append("\n今日数据：\n");
        contextBuilder.append("已摄入热量：").append(consumedCalories).append(" kcal\n");
        contextBuilder.append("已消耗热量：").append(burnedCalories).append(" kcal\n");
        contextBuilder.append("剩余热量额度：").append(remainingCalories).append(" kcal\n");
        contextBuilder.append("步数：").append(steps).append(" 步\n");
        contextBuilder.append("每日热量目标：").append(goalCalories).append(" kcal\n");

        contextBuilder.append("\n用户问题：").append(userContent).append("\n\n");
        contextBuilder.append("请作为健康管理顾问，根据用户的身体数据和今日饮食、运动情况，给出专业、具体、可执行的建议。");
        contextBuilder.append("回答要简洁明了（不超过 200 字），语气友好鼓励性。");

        return contextBuilder.toString();
    }
}



