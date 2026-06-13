package com.example.demo.service;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.example.demo.dto.FoodScanResultVO;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;

@Service
public class FoodScanService {

    @Value("${dashscope.api-key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FoodScanResultVO analyzeNutritionalLabel(MultipartFile file, User user) throws Exception {
        // 1. 将文件转为 Base64（或者上传到 OSS 传 URL）
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        String imageUrl = "data:image/png;base64," + base64Image;

        // 2. 构建 AI 提示词
        MultiModalMessage systemMsg = MultiModalMessage.builder()
                .role(Role.SYSTEM.getValue())
                .content(Arrays.asList(java.util.Map.of("text", "你是一个营养学专家，请从图片中提取营养成分表数据。")))
                .build();

        MultiModalMessage userMsg = MultiModalMessage.builder()
                .role(Role.USER.getValue())
                .content(Arrays.asList(
                        java.util.Map.of("image", imageUrl),
                        java.util.Map.of("text", "请提取：食物名称、能量(kcal)、蛋白质(g)、脂肪(g)、碳水化合物(g)、钠(mg)。" +
                                "请严格以JSON格式输出，不要包含任何说明文字。键名分别为：name, calories, protein, fat, carbs, sodium。")))
                .build();

        // 3. 调用 Qwen-VL
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey)
                .model("qwen-vl-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .build();

        MultiModalConversation conv = new MultiModalConversation();
        MultiModalConversationResult result = conv.call(param);

        // 提取并清理 JSON 文本
        String jsonText = result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text").toString();
        jsonText = jsonText.replace("```json", "").replace("```", "").trim();

        // 4. 解析 AI 返回的数据并计算占比
        FoodScanResultVO vo = objectMapper.readValue(jsonText, FoodScanResultVO.class);

        // 5. 核心逻辑：计算与用户目标的匹配度
        calculateAdvice(vo, user);

        return vo;
    }

    private void calculateAdvice(FoodScanResultVO vo, User user) {
        // 假设用户每日目标是 2000kcal (从数据库 user 对象取)
        Integer dailyGoal = user.getDailyGoalCalories() != null ? user.getDailyGoalCalories() : 2000;

        // 计算当前食物热量占每日目标的百分比
        double ratio = (vo.getCalories() / (double) dailyGoal) * 100;
        vo.setCaloriesRatio(ratio);

        if (ratio > 30) {
            vo.setStatus("danger");
            vo.setAdvice("⚠️ 高能预警！该食物热量占你今日总额的 " + String.format("%.1f", ratio) + "%，建议放下或极少量食用。");
        } else if (ratio > 15) {
            vo.setStatus("warning");
            vo.setAdvice("💡 提示：热量适中，吃的时候记得扣除后续的正餐配额哦。");
        } else {
            vo.setStatus("safe");
            vo.setAdvice("✅ 安全：这款食物很健康，可以放心记录。");
        }
    }
}