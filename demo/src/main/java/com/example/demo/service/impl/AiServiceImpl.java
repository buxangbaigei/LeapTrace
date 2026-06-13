package com.example.demo.service.impl;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.example.demo.dto.FoodNutritionDTO;
import com.example.demo.entity.FoodLib;
import com.example.demo.mapper.FoodLibMapper;
import com.example.demo.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AiServiceImpl implements AiService {
    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);
    @Autowired
    private FoodLibMapper foodLibMapper;

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.model}")
    private String model;

    @Override
    public FoodNutritionDTO analyzeFoodImage(MultipartFile image) {
        File tempFile = null;
        try {
            validateImageFile(image);
            tempFile = convertMultipartFileToFile(image);

            // 1. 调用 AI 识别名称
            String recognizedFood = recognizeFoodFromImage(tempFile);

            if (recognizedFood != null && !recognizedFood.isEmpty()) {
                // 2. 优先查库
                FoodLib foodLib = foodLibMapper.selectByName(recognizedFood.trim());
                if (foodLib != null) {
                    return buildFoodNutritionDTO(foodLib, 100);
                }
            }
            // 3. 库里没有，AI 分析营养
            return analyzeFoodNutritionWithAI(recognizedFood);
        } catch (Exception e) {
            log.error("AI识别失败", e);
            throw new RuntimeException("识别失败: " + e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }

    private String recognizeFoodFromImage(File imageFile) throws Exception {
        MultiModalConversation conv = new MultiModalConversation();

        // 确保 encodeFileToBase64 内部已经加上了 "data:image/jpeg;base64," 前缀
        String base64Image = encodeFileToBase64(imageFile);

        // 构建 Content 列表
        List<Map<String, Object>> content = new ArrayList<>();

        // 图片部分
        Map<String, Object> imageItem = new HashMap<>();
        imageItem.put("image", base64Image);
        content.add(imageItem);

        // 文本部分
        Map<String, Object> textItem = new HashMap<>();
        textItem.put("text", "请识别图中主要的食物名称，只输出名称，不要多余解释。");
        content.add(textItem);

        MultiModalMessage userMessage = MultiModalMessage.builder()
                .role("user")
                .content(content)
                .build();

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey)
                .model(model) // 重要：请确保配置文件中 model 是 qwen-vl-plus 或 qwen-vl-max
                .messages(Collections.singletonList(userMessage))
                .build();

        log.info("正在发送请求到通义千问 VL 模型...");
        MultiModalConversationResult result = conv.call(param);

        if (result.getOutput() != null && !result.getOutput().getChoices().isEmpty()) {
            String resultText = result.getOutput().getChoices().get(0).getMessage().getContent().toString();
            log.info("AI 原始返回: {}", resultText);
            return resultText.replaceAll("[\\p{Punct}]+", "").trim();
        }
        return null;
    }

    @Override
    public FoodNutritionDTO analyzeFoodText(String text) {
        FoodLib foodLib = foodLibMapper.selectByName(text.trim());
        if (foodLib != null) return buildFoodNutritionDTO(foodLib, 100);
        try {
            return analyzeFoodNutritionWithAI(text);
        } catch (Exception e) {
            return createDefaultNutritionDTO(text);
        }
    }

    private FoodNutritionDTO analyzeFoodNutritionWithAI(String foodName) throws Exception {
        MultiModalConversation conv = new MultiModalConversation();
        String prompt = String.format("分析 %s 每100g的营养，严格返回JSON: {\"food_name\":\"%s\",\"calories\":数值,\"protein\":数值,\"fat\":数值,\"carbs\":数值}", foodName, foodName);

        List<Map<String, Object>> content = Collections.singletonList(Collections.singletonMap("text", prompt));
        MultiModalMessage msg = MultiModalMessage.builder().role("user").content(content).build();

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey).model(model).messages(Collections.singletonList(msg)).build();

        MultiModalConversationResult result = conv.call(param);
        String rawJson = result.getOutput().getChoices().get(0).getMessage().getContent().toString();
        return parseJsonResponse(rawJson, foodName);
    }

    private String encodeFileToBase64(File file) throws IOException {
        byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    private FoodNutritionDTO parseJsonResponse(String raw, String defaultName) {
        try {
            // 提取 {} 之间的内容，防止 AI 返回 markdown
            int start = raw.indexOf("{");
            int end = raw.lastIndexOf("}");
            String json = raw.substring(start, end + 1);

            FoodNutritionDTO dto = new FoodNutritionDTO();
            dto.setFoodName(extractVal(json, "food_name", defaultName));
            dto.setGram(100);
            dto.setCalories(new BigDecimal(extractVal(json, "calories", "200")));
            dto.setProtein(new BigDecimal(extractVal(json, "protein", "10")));
            dto.setFat(new BigDecimal(extractVal(json, "fat", "5")));
            dto.setCarbs(new BigDecimal(extractVal(json, "carbs", "30")));
            return dto;
        } catch (Exception e) {
            return createDefaultNutritionDTO(defaultName);
        }
    }

    private String extractVal(String json, String key, String def) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^\",}]+)\"?").matcher(json);
        return m.find() ? m.group(1).trim() : def;
    }

    private FoodNutritionDTO buildFoodNutritionDTO(FoodLib food, int g) {
        FoodNutritionDTO d = new FoodNutritionDTO();
        d.setFoodName(food.getName()); d.setGram(g);
        d.setCalories(BigDecimal.valueOf(food.getUnitCalories()));
        d.setProtein(food.getProtein()); d.setFat(food.getFat()); d.setCarbs(food.getCarbs());
        return d;
    }

    private FoodNutritionDTO createDefaultNutritionDTO(String name) {
        FoodNutritionDTO d = new FoodNutritionDTO();
        d.setFoodName(name); d.setGram(100);
        d.setCalories(new BigDecimal("150")); d.setProtein(new BigDecimal("5"));
        d.setFat(new BigDecimal("3")); d.setCarbs(new BigDecimal("20"));
        return d;
    }

    private void validateImageFile(MultipartFile f) {
        if (f == null || f.isEmpty()) throw new IllegalArgumentException("文件为空");
    }

    private File convertMultipartFileToFile(MultipartFile mf) throws IOException {
        File f = File.createTempFile("ai_v_", ".jpg");
        try (FileOutputStream os = new FileOutputStream(f)) { os.write(mf.getBytes()); }
        return f;
    }
}