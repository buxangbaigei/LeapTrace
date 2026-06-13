/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : health_ai_app

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 09/04/2026 21:32:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_chat_log
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_log`;
CREATE TABLE `ai_chat_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'user/assistant',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_time`(`user_id` ASC, `created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI聊天记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_log
-- ----------------------------
INSERT INTO `ai_chat_log` VALUES (1, 1, 'user', '我今天吃多了吗？', '2026-03-22 16:35:23');
INSERT INTO `ai_chat_log` VALUES (2, 1, 'assistant', '根据你的记录，你今天摄入略高，建议晚餐减少碳水。', '2026-03-22 16:35:23');
INSERT INTO `ai_chat_log` VALUES (3, 1, 'user', '那我还能吃什么？', '2026-03-22 16:35:23');
INSERT INTO `ai_chat_log` VALUES (4, 1, 'assistant', '建议吃低脂高蛋白食物，比如鸡胸肉或鸡蛋。', '2026-03-22 16:35:23');
INSERT INTO `ai_chat_log` VALUES (5, 1, 'user', '你好', '2026-03-24 23:43:15');
INSERT INTO `ai_chat_log` VALUES (6, 1, 'assistant', '你好，小明！今天你已摄入和消耗热量均为0，剩余热量额度为300 kcal。建议你选择低热量、高蛋白的食物，如鸡胸肉或蔬菜，搭配适量运动提升代谢。保持良好习惯，加油！', '2026-03-24 23:43:15');
INSERT INTO `ai_chat_log` VALUES (7, 1, 'user', '我可以吃麦当劳吗', '2026-03-24 23:43:33');
INSERT INTO `ai_chat_log` VALUES (8, 1, 'assistant', '根据你的热量额度，剩余300大卡，可以适量吃麦当劳，建议选择低热量选项，如麦乐鸡腿堡（约300大卡）或沙拉。避免高糖饮料和薯条。保持运动，祝你健康饮食！', '2026-03-24 23:43:33');
INSERT INTO `ai_chat_log` VALUES (9, 1, 'user', '如何每天健康饮食', '2026-03-25 00:18:24');
INSERT INTO `ai_chat_log` VALUES (10, 1, 'assistant', '小明，今天摄入热量超出目标140大卡，建议明天控制饮食，选择低脂高蛋白食物如鸡胸肉、蔬菜。增加运动量帮助消耗热量，保持每日步数在8000步以上。健康饮食需均衡搭配，规律进餐，避免暴饮暴食。加油，你可以做到的！', '2026-03-25 00:18:24');
INSERT INTO `ai_chat_log` VALUES (11, 1, 'user', '我是说后面', '2026-03-25 00:18:46');
INSERT INTO `ai_chat_log` VALUES (12, 1, 'assistant', '根据你的数据，今日已摄入440 kcal，超过目标140 kcal。建议明天控制饮食，选择低热量高蛋白食物，如鸡胸肉、蔬菜等。同时增加活动量，促进热量消耗，保持健康节奏！加油！', '2026-03-25 00:18:46');
INSERT INTO `ai_chat_log` VALUES (13, 1, 'user', '回答我别的', '2026-03-25 00:18:59');
INSERT INTO `ai_chat_log` VALUES (14, 1, 'assistant', '你的今日热量摄入已超出目标140大卡，建议明天调整饮食，选择低脂高蛋白食物，如鸡胸肉、蔬菜等。同时增加活动量，帮助消耗多余热量。保持积极心态，明天从健康饮食开始！', '2026-03-25 00:18:59');
INSERT INTO `ai_chat_log` VALUES (15, 1, 'user', '我感觉我今天非常不好', '2026-03-25 00:20:27');
INSERT INTO `ai_chat_log` VALUES (16, 1, 'assistant', '小明，今天摄入热量超出目标140大卡，建议明天调整饮食，选择低脂高蛋白食物，如鸡胸肉、蔬菜。适量增加运动，如快走或拉伸，帮助提升消耗。保持积极心态，明天从健康习惯开始！加油！', '2026-03-25 00:20:27');
INSERT INTO `ai_chat_log` VALUES (17, 1, 'user', 'dv', '2026-03-25 00:21:00');
INSERT INTO `ai_chat_log` VALUES (18, 1, 'assistant', '你的今日热量摄入已超目标140大卡，建议明天调整饮食，选择低脂高蛋白食物，如鸡胸肉、蔬菜等。同时增加适量运动，帮助消耗多余热量，保持健康节奏！加油！', '2026-03-25 00:21:00');
INSERT INTO `ai_chat_log` VALUES (19, 1, 'user', 'dgvfbvf', '2026-03-25 00:21:02');
INSERT INTO `ai_chat_log` VALUES (20, 1, 'assistant', '看起来您今天还未进食，已摄入热量为440 kcal，超出每日目标300 kcal，建议适当减少晚餐摄入。同时，增加活动量以提升热量消耗，如快走或拉伸。保持积极心态，明天继续努力！', '2026-03-25 00:21:02');
INSERT INTO `ai_chat_log` VALUES (21, 1, 'user', 'gfdgfbfb', '2026-03-25 00:21:04');
INSERT INTO `ai_chat_log` VALUES (22, 1, 'assistant', '您的今日热量摄入已超过目标，建议适当减少食物摄入，增加轻度运动如快走或拉伸，帮助调节热量平衡。保持良好饮食习惯，明天继续加油！', '2026-03-25 00:21:04');
INSERT INTO `ai_chat_log` VALUES (23, 1, 'user', 'gfgbf', '2026-03-25 00:21:06');
INSERT INTO `ai_chat_log` VALUES (24, 1, 'assistant', '你好小明，当前热量摄入已超过目标，建议适当减少食物分量或选择低热量、高蛋白的健康食品。同时增加活动量，如快走或拉伸，有助于提升消耗。保持积极心态，明天继续努力！', '2026-03-25 00:21:06');
INSERT INTO `ai_chat_log` VALUES (25, 1, 'user', 'dgdfbvdd', '2026-03-25 00:21:09');
INSERT INTO `ai_chat_log` VALUES (26, 1, 'assistant', '您的热量摄入已超出目标，建议今晚选择低脂高蛋白食物，如鸡胸肉或水煮蛋，并适当增加运动，如快走或拉伸，帮助消耗多余热量。保持积极心态，明天继续努力！', '2026-03-25 00:21:09');
INSERT INTO `ai_chat_log` VALUES (27, 1, 'user', '我今天还可以吃什么？', '2026-03-25 00:23:11');
INSERT INTO `ai_chat_log` VALUES (28, 1, 'assistant', '根据你的数据，今日已摄入440 kcal，超出目标140 kcal，建议优先选择低热量、高饱腹感的食物，如黄瓜、西红柿、鸡胸肉（少量）或一份蔬菜沙拉。避免高糖高脂食物，保持水分充足，适量活动有助于消耗多余热量。加油！', '2026-03-25 00:23:11');
INSERT INTO `ai_chat_log` VALUES (29, 1, 'user', '我今天还可以吃什么？', '2026-03-25 00:24:21');
INSERT INTO `ai_chat_log` VALUES (30, 1, 'assistant', '根据你的今日数据，已摄入热量超过目标，建议暂停进食。若非常饥饿，可选择低热量高纤维食物如黄瓜、芹菜或少量坚果（约10g）。保持水分充足，适量活动有助于热量消耗。明天继续努力！', '2026-03-25 00:24:21');
INSERT INTO `ai_chat_log` VALUES (31, 1, 'user', '你好', '2026-03-25 20:13:56');
INSERT INTO `ai_chat_log` VALUES (32, 1, 'assistant', '你好！很高兴见到你，有什么我可以帮你的吗？', '2026-03-25 20:13:56');
INSERT INTO `ai_chat_log` VALUES (33, 1, 'user', '怎么健康饮食', '2026-03-25 20:14:38');
INSERT INTO `ai_chat_log` VALUES (34, 1, 'assistant', '健康饮食是保持身体机能良好、预防疾病和维持良好精神状态的重要基础。以下是一些**健康饮食的基本原则**，帮助你建立科学的饮食习惯：\n\n---\n\n### 🍽️ 一、均衡膳食结构（中国居民膳食指南建议）\n\n1. **食物多样化**  \n   每天摄入至少 **12种以上** 的食物，种类越多，营养越全面。\n\n2. **谷物为主**  \n   粗粮（如糙米、燕麦、玉米）和细粮搭配，增加膳食纤维摄入。\n\n3. **适量蛋白质**  \n   来源包括：  \n   - 动物性：鸡蛋、鱼、瘦肉、奶类  \n   - 植物性：豆类、豆腐、坚果\n\n4. **多吃蔬菜水果**  \n   每天至少 **300克蔬菜 + 200克水果**，颜色丰富更佳（如深绿色、红色、橙色等）。\n\n5. **适量饮水**  \n   每天喝够 **1500-1700毫升水**（约7-8杯），避免含糖饮料。\n\n6. **控制油盐糖**  \n   - 每天盐不超过 **5克**  \n   - 油不超过 **25-30克**  \n   - 添加糖不超过 **25克**\n\n---\n\n### 🧃 二、健康饮食小贴士\n\n| 方面 | 建议 |\n|------|------|\n| **三餐规律** | 定时定量，不暴饮暴食，不吃过晚 |\n| **少油少盐** | 避免油炸、腌制食品，用蒸、煮、炖代替 |\n| **多喝水少喝饮料** | 少喝奶茶、可乐等高糖饮品 |\n| **注意食品安全** | 不吃变质、过期食品，生熟分开 |\n| **适当运动** | 饮食+运动=健康生活方式 |\n\n---\n\n### 🍎 三、推荐的食物组合（一日三餐示例）\n\n| 餐次 | 推荐内容 |\n|------|----------|\n| 早餐 | 全麦面包 + 牛奶/豆浆 + 水煮蛋 + 一份水果 |\n| 午餐 | 糙米饭 + 清蒸鱼 + 绿叶菜 + 豆腐汤 |\n| 晚餐 | 小米粥 + 炒青菜 + 鸡胸肉/豆腐 |\n| 加餐 | 坚果、酸奶、水果等（避免高糖零食） |\n\n---\n\n### ❌ 四、需要避免的不良饮食习惯\n\n- **过度节食**：容易导致营养不良、代谢紊乱\n- **偏食挑食**：营养不均衡，影响身体健康\n- **暴饮暴食**：易引发肥胖、胃病等问题\n- **长期吃外卖**：高油高盐，缺乏营养\n\n---\n\n如果你有特定的健康目标（如减肥、增肌、控制血糖等），也可以告诉我，我可以为你定制更个性化的饮食方案！💪\n\n是否需要我帮你制定一个一周的健康饮食计划？', '2026-03-25 20:14:38');
INSERT INTO `ai_chat_log` VALUES (35, 1, 'user', '如何健康饮食', '2026-03-25 20:53:53');
INSERT INTO `ai_chat_log` VALUES (36, 1, 'assistant', '健康饮食是保持身体活力、预防疾病和维持良好体态的重要基础。以下是一些健康饮食的基本原则和建议：\n\n---\n\n### 一、均衡饮食\n- **食物多样化**：每天摄入多种食物，保证营养全面。\n- **合理搭配**：包括谷物、蔬菜、水果、蛋白质（如鱼、蛋、豆类）、乳制品等。\n\n---\n\n### 二、控制总热量\n- 根据自身年龄、性别、体重和活动量，合理控制每日摄入的热量。\n- 避免过量摄入高糖、高脂肪的食物。\n\n---\n\n### 三、多吃蔬菜和水果\n- 每天至少吃 **500克** 蔬菜和 **200克** 水果。\n- 选择新鲜、颜色多样的蔬果，以获取丰富的维生素和膳食纤维。\n\n---\n\n### 四、适量摄入优质蛋白\n- 来源包括：瘦肉、鱼、蛋、奶、豆制品等。\n- 减少红肉和加工肉类的摄入，避免过多饱和脂肪。\n\n---\n\n### 五、减少高盐、高糖、高脂食物\n- 每日盐摄入量不超过 **5克**。\n- 控制糖分摄入，尤其是含糖饮料和甜点。\n- 少吃油炸食品和高脂肪零食。\n\n---\n\n### 六、多喝水，少喝含糖饮料\n- 每天饮水量建议在 **1500～2000毫升**，以白开水、茶水为主。\n- 避免过多饮用碳酸饮料、果汁饮料等。\n\n---\n\n### 七、规律进餐\n- 一日三餐定时定量，避免暴饮暴食。\n- 不要空腹或过度饥饿后进食。\n\n---\n\n### 八、注意烹饪方式\n- 多采用蒸、煮、炖、拌等方式，少用煎炸、烧烤。\n- 使用健康的食用油，如橄榄油、菜籽油等。\n\n---\n\n### 九、戒烟限酒\n- 吸烟和过量饮酒对健康有极大危害，应尽量避免。\n\n---\n\n### 十、结合运动\n- 均衡饮食与适度运动相结合，才能更好地保持健康。\n\n---\n\n如果你有特定的健康目标（如减肥、增肌、控制血糖等），我可以根据你的需求提供更详细的饮食建议。你愿意告诉我你的具体情况吗？', '2026-03-25 20:53:53');
INSERT INTO `ai_chat_log` VALUES (37, 1, 'user', '分析午餐热量', '2026-03-25 20:58:30');
INSERT INTO `ai_chat_log` VALUES (38, 1, 'assistant', '好的，我可以帮助你分析午餐的热量。为了更准确地进行分析，请提供以下信息：\n\n1. **你吃了哪些食物？**（例如：米饭、鸡胸肉、蔬菜、面条等）\n2. **每种食物的分量是多少？**（例如：100克、一碗、一份等）\n3. **是否有调味料或酱料？**（如酱油、沙拉酱、油等）\n\n如果你能提供这些信息，我就可以帮你计算出这顿午餐的大致热量（以千卡为单位）。', '2026-03-25 20:58:30');
INSERT INTO `ai_chat_log` VALUES (39, 1, 'user', '制定减肥计划', '2026-03-25 20:58:51');
INSERT INTO `ai_chat_log` VALUES (40, 1, 'assistant', '制定一个有效的减肥计划需要结合饮食、运动和生活习惯的调整。以下是一个科学且可持续的减肥计划框架，你可以根据自己的实际情况进行调整：\n\n---\n\n## 一、明确目标（1-2周内）\n\n### 1. 设定具体目标\n- **体重目标**：例如“3个月内减掉5公斤”\n- **体脂率目标**：如“从25%降到20%”\n- **围度目标**：如“腰围减少5厘米”\n\n### 2. 计算基础代谢率（BMR）和每日消耗（TDEE）\n- 可以使用在线计算器或咨询专业人士。\n- 一般建议每日摄入热量比消耗少 **300-500大卡**，每周可减0.5-1公斤。\n\n---\n\n## 二、饮食计划（核心部分）\n\n### 1. 饮食原则\n- **控制总热量**：保持热量赤字\n- **均衡营养**：蛋白质 + 碳水 + 脂肪\n- **多吃高纤维食物**：蔬菜、水果、全谷类\n- **避免高糖、高油、高盐食品**\n- **多喝水**：每天至少1.5-2升\n\n### 2. 三餐搭配示例（供参考）\n\n| 餐次 | 内容 |\n|------|------|\n| 早餐 | 全麦面包+鸡蛋/牛奶+水果 |\n| 午餐 | 粗粮米饭/杂粮饭+鸡胸肉/鱼肉+大量蔬菜 |\n| 晚餐 | 清汤+豆腐/瘦肉+绿叶蔬菜 |\n| 加餐 | 无糖酸奶/坚果/水果 |\n\n### 3. 建议控制的食物\n- 少吃：油炸食品、甜点、含糖饮料、加工食品\n- 控量：主食、坚果、奶酪等\n\n---\n\n## 三、运动计划（每周5-6天）\n\n### 1. 有氧运动（每周3-5次，每次30-60分钟）\n- 快走、慢跑、跳绳、游泳、骑自行车等\n- 目标：提高心率，燃烧脂肪\n\n### 2. 力量训练（每周2-3次）\n- 重点锻炼大肌群（腿、背、胸）\n- 提高基础代谢率，防止肌肉流失\n\n### 3. 每日活动量\n- 避免久坐，每小时起身活动5分钟\n- 增加日常活动量（如步行、爬楼梯）\n\n---\n\n## 四、生活习惯调整\n\n### 1. 睡眠\n- 每晚保证7-8小时高质量睡眠\n- 睡眠不足会影响激素分泌，增加食欲\n\n### 2. 压力管理\n- 减少压力，避免情绪性进食\n- 可尝试冥想、深呼吸、瑜伽等\n\n### 3. 记录与反馈\n- 每天记录饮食、运动、体重、围度\n- 每周称重一次，观察趋势\n\n---\n\n## 五、注意事项\n\n- **不要节食**：长期节食会导致代谢下降、反弹严重\n- **避免极端减肥方法**：如断食、减肥药等\n- **循序渐进**：每周减0.5-1公斤是健康的速度\n- **坚持是关键**：减肥不是短期任务，而是生活方式的改变\n\n---\n\n## 六、可选工具或APP推荐\n\n- **饮食记录**：MyFitnessPal、薄荷健康\n- **运动记录**：Keep、Nike Training Club\n- **睡眠监测**：Sleep Cycle、Fitbit\n- **体重追踪**：MyWeigh\n\n---\n\n如果你愿意告诉我：\n- 你的身高、体重、年龄、性别\n- 当前饮食习惯\n- 运动频率和喜好\n- 是否有健康问题\n\n我可以为你定制一个更个性化的减肥计划！', '2026-03-25 20:58:51');
INSERT INTO `ai_chat_log` VALUES (41, 1, 'user', '推荐5分钟拉伸动作', '2026-03-25 21:00:13');
INSERT INTO `ai_chat_log` VALUES (42, 1, 'assistant', '当然可以！以下是一套适合在5分钟内完成的简单拉伸动作，有助于放松身体、缓解疲劳、提高灵活性，非常适合在工作间隙或运动前后进行。\n\n---\n\n### 🧘‍♀️ **5分钟拉伸动作（每个动作保持20-30秒）**\n\n#### 1. **猫牛式（Cat-Cow Stretch）**\n- **目的**：灵活脊柱，放松背部。\n- **动作**：\n  - 四足跪姿，双手与肩同宽，双膝与髋同宽。\n  - 吸气时，抬头塌腰（牛式）。\n  - 呼气时，低头拱背（猫式）。\n- **重复**：5-8次\n\n#### 2. **下犬式（Downward Dog）**\n- **目的**：拉伸腿部、背部和肩膀。\n- **动作**：\n  - 手掌与肩同宽，双脚与髋同宽，臀部向上推。\n  - 脚跟尽量向下，保持背部平直。\n- **保持**：30秒\n\n#### 3. **坐姿前屈（Seated Forward Fold）**\n- **目的**：拉伸腿后侧和背部。\n- **动作**：\n  - 坐在地上，双腿伸直，脚尖朝上。\n  - 身体向前弯，尽量触碰脚尖。\n- **保持**：30秒\n\n#### 4. **蝴蝶式（Butterfly Stretch）**\n- **目的**：拉伸大腿内侧和髋关节。\n- **动作**：\n  - 坐地，双脚脚底相对，膝盖自然下垂。\n  - 轻轻将膝盖往下压，保持背部挺直。\n- **保持**：30秒\n\n#### 5. **肩部绕环（Shoulder Rolls）**\n- **目的**：放松肩颈，缓解紧张。\n- **动作**：\n  - 站立或坐直，手臂自然下垂。\n  - 缓慢做肩部向后绕环，然后向前绕环。\n- **重复**：10次（每边5次）\n\n---\n\n### ✅ 小贴士：\n- 拉伸时保持呼吸顺畅，不要屏气。\n- 动作要缓慢、柔和，避免突然用力。\n- 可以根据自身情况调整动作幅度。\n\n如果你需要针对特定部位（如肩颈、腰部、腿部）的拉伸，也可以告诉我，我可以为你定制更详细的方案 😊', '2026-03-25 21:00:13');
INSERT INTO `ai_chat_log` VALUES (43, 1, 'user', '我今天该喝多少水？', '2026-03-28 21:23:22');
INSERT INTO `ai_chat_log` VALUES (44, 1, 'assistant', '你今天应该喝的水量取决于多个因素，包括你的体重、活动量、气候、健康状况等。一个常见的建议是：\n\n### 📏 一般建议：\n- **成年人**：每天大约 **2.5升（约8杯水）**。\n- **男性**：约 **3.7升（约13杯水）**。\n- **女性**：约 **2.7升（约9杯水）**。\n\n这些数值包括所有饮料和食物中的水分（比如水果、蔬菜等）。\n\n---\n\n### 🧪 更精确的方法（根据体重计算）：\n- 每公斤体重约需 **30-35毫升水**。\n- 例如：如果你体重60公斤，那么每天大约需要 **1.8~2.1升水**。\n\n---\n\n### ⚠️ 注意事项：\n- 如果你运动、出汗多或在炎热天气中，需要增加饮水量。\n- 喝水要**少量多次**，不要一次大量饮水。\n- 饮料（如咖啡、茶、果汁）也计入总摄入量，但含糖饮料应适量。\n\n---\n\n### 💡 小技巧：\n- 可以用手机设置提醒喝水。\n- 早上起床后先喝一杯水，有助于唤醒身体。\n\n如果你有特定的健康问题（如肾病、心脏病等），建议咨询医生来确定合适的饮水量。\n\n需要我帮你制定一个每日饮水计划吗？😊', '2026-03-28 21:23:22');
INSERT INTO `ai_chat_log` VALUES (45, 1, 'user', '推荐5分钟拉伸动作', '2026-03-28 21:24:53');
INSERT INTO `ai_chat_log` VALUES (46, 1, 'assistant', '当然可以！以下是5分钟的简单拉伸动作，适合在工作、学习或运动后进行，帮助放松肌肉、缓解疲劳。每个动作保持15-30秒，重复2次。\n\n---\n\n### 🧘‍♀️ 1. 颈部拉伸（颈部侧屈）\n- 站立或坐直\n- 缓慢将头向一侧倾斜，耳朵靠近肩膀\n- 保持15-30秒，换另一侧\n\n---\n\n### 🧘‍♂️ 2. 肩部拉伸（肩部绕环）\n- 双手放在肩上\n- 向前做小幅度绕环10次，再向后绕环10次\n- 放松肩部，避免用力过猛\n\n---\n\n### 🧘‍♀️ 3. 上背部拉伸（猫牛式）\n- 四足跪地，双手与肩同宽，双膝与髋同宽\n- 吸气时抬头挺胸（牛式），呼气时拱起背部（猫式）\n- 重复5-8次\n\n---\n\n### 🧘‍♂️ 4. 大腿前侧拉伸（站立前屈）\n- 站直，单腿向前迈出一步\n- 双手扶膝，身体向前倾，感受大腿前侧拉伸\n- 每侧保持15-30秒\n\n---\n\n### 🧘‍♀️ 5. 臀部拉伸（蝴蝶式）\n- 坐下，双脚脚底相对，膝盖自然下垂\n- 轻轻向下压膝盖，感受臀部和大腿内侧的拉伸\n- 保持15-30秒\n\n---\n\n这些动作简单易行，不需要器械，适合日常随时进行。如果你有特定部位需要放松，也可以告诉我，我可以为你定制更详细的拉伸方案哦！', '2026-03-28 21:24:53');
INSERT INTO `ai_chat_log` VALUES (47, 1, 'user', '你好', '2026-04-01 21:53:28');
INSERT INTO `ai_chat_log` VALUES (48, 1, 'assistant', '你好！很高兴见到你。有什么我可以帮你的吗？😊', '2026-04-01 21:53:28');

-- ----------------------------
-- Table structure for ai_weekly_report
-- ----------------------------
DROP TABLE IF EXISTS `ai_weekly_report`;
CREATE TABLE `ai_weekly_report`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `week_start` date NOT NULL,
  `week_end` date NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_week`(`user_id` ASC, `week_start` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI周报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_weekly_report
-- ----------------------------
INSERT INTO `ai_weekly_report` VALUES (1, 1, '2026-03-15', '2026-03-22', '本周你运动表现良好，步数达标率较高，但饮食热量偏高，建议减少碳水摄入。', '2026-03-22 16:35:23');
INSERT INTO `ai_weekly_report` VALUES (2, 1, '2026-03-23', '2026-03-29', '**1. 本周表现总结**  \n小明本周在饮食和运动方面尚未开始执行计划，数据为0，建议尽快开启健康生活第一步，保持积极心态，逐步建立习惯。  \n\n**2. 饮食建议**  \n从明天起，每天可先从少量、易消化的食物开始，如水果或坚果，逐步达到每日300大卡的摄入目标，注意营养均衡。  \n\n**3. 运动建议**  \n可以尝试每天分段走路，比如早中晚各走1000步，逐步提升至8000步目标，让运动变得更轻松、更可持续。', '2026-03-24 23:26:46');

-- ----------------------------
-- Table structure for h_diet_log
-- ----------------------------
DROP TABLE IF EXISTS `h_diet_log`;
CREATE TABLE `h_diet_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `food_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `calories` int NOT NULL,
  `protein` decimal(6, 2) NULL DEFAULT 0.00,
  `fat` decimal(6, 2) NULL DEFAULT 0.00,
  `carbs` decimal(6, 2) NULL DEFAULT 0.00,
  `gram` decimal(6, 2) NULL DEFAULT NULL COMMENT '重量',
  `meal_type` tinyint NOT NULL COMMENT '0早餐 1午餐 2晚餐 3加餐',
  `record_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_time`(`user_id` ASC, `record_time` ASC) USING BTREE,
  INDEX `idx_user_meal`(`user_id` ASC, `meal_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '饮食记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of h_diet_log
-- ----------------------------
INSERT INTO `h_diet_log` VALUES (1, 1, '鸡胸肉', 200, 30.00, 5.00, 0.00, 150.00, 1, '2026-03-22 16:35:23');
INSERT INTO `h_diet_log` VALUES (2, 1, '米饭', 300, 6.00, 1.00, 65.00, 200.00, 1, '2026-03-22 16:35:23');
INSERT INTO `h_diet_log` VALUES (3, 1, '牛肉面', 650, 30.00, 20.00, 80.00, 500.00, 2, '2026-03-22 16:35:23');
INSERT INTO `h_diet_log` VALUES (4, 1, '鸡蛋', 150, 12.00, 10.00, 1.00, 100.00, 0, '2026-03-21 16:35:23');
INSERT INTO `h_diet_log` VALUES (5, 1, '沙拉', 180, 5.00, 8.00, 20.00, 200.00, 2, '2026-03-21 16:35:23');
INSERT INTO `h_diet_log` VALUES (39, 1, '一个包子', 133, 5.52, 2.48, 22.78, 100.00, 0, '2026-03-25 21:07:11');
INSERT INTO `h_diet_log` VALUES (40, 1, '一杯豆浆', 30, 3.30, 1.80, 4.20, 100.00, 0, '2026-03-25 21:07:32');
INSERT INTO `h_diet_log` VALUES (41, 1, '米饭', 130, 2.70, 0.30, 28.00, 100.00, 1, '2026-03-25 21:07:45');
INSERT INTO `h_diet_log` VALUES (42, 1, '辣椒炒肉', 250, 15.00, 15.00, 10.00, 100.00, 1, '2026-03-25 21:10:35');
INSERT INTO `h_diet_log` VALUES (43, 1, '一碗牛肉面', 600, 25.00, 15.00, 80.00, 100.00, 1, '2026-03-26 19:50:29');
INSERT INTO `h_diet_log` VALUES (44, 1, '鸡胸肉', 165, NULL, NULL, NULL, NULL, 0, '2026-03-26 19:56:27');
INSERT INTO `h_diet_log` VALUES (45, 1, '鸡蛋', 155, NULL, NULL, NULL, NULL, 0, '2026-03-26 19:56:40');
INSERT INTO `h_diet_log` VALUES (46, 1, '西瓜', 30, 0.60, 0.20, 7.60, 100.00, 1, '2026-03-26 19:59:55');
INSERT INTO `h_diet_log` VALUES (47, 1, '披萨', 266, 10.30, 9.80, 35.40, 100.00, 2, '2026-03-26 21:00:35');
INSERT INTO `h_diet_log` VALUES (48, 1, '汉堡', 295, 17.00, 14.00, 24.00, 100.00, 2, '2026-03-26 21:00:42');
INSERT INTO `h_diet_log` VALUES (49, 1, '沙县小吃饺子', 250, 10.00, 8.00, 35.00, 100.00, 2, '2026-03-26 21:01:29');
INSERT INTO `h_diet_log` VALUES (50, 1, '鸡蛋', 155, NULL, NULL, NULL, NULL, 0, '2026-03-28 20:11:51');
INSERT INTO `h_diet_log` VALUES (51, 1, '牛奶(全脂)', 54, NULL, NULL, NULL, NULL, 0, '2026-03-28 20:11:56');
INSERT INTO `h_diet_log` VALUES (52, 1, '牛肉面', 130, NULL, NULL, NULL, NULL, 1, '2026-03-28 20:12:05');
INSERT INTO `h_diet_log` VALUES (53, 1, '希腊酸奶(无糖)', 59, NULL, NULL, NULL, NULL, 1, '2026-03-28 20:12:11');
INSERT INTO `h_diet_log` VALUES (54, 1, '麻辣牛肉板面', 350, 12.00, 8.00, 55.00, 100.00, 0, '2026-03-28 21:18:28');
INSERT INTO `h_diet_log` VALUES (55, 1, '一个包子', 250, 8.00, 6.00, 40.00, 100.00, 0, '2026-03-28 21:19:47');
INSERT INTO `h_diet_log` VALUES (56, 1, '全麦面包', 246, NULL, NULL, NULL, NULL, 0, '2026-03-29 00:05:10');
INSERT INTO `h_diet_log` VALUES (57, 1, '牛肉', 250, NULL, NULL, NULL, NULL, 0, '2026-03-29 00:05:22');

-- ----------------------------
-- Table structure for h_food_lib
-- ----------------------------
DROP TABLE IF EXISTS `h_food_lib`;
CREATE TABLE `h_food_lib`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '100g',
  `unit_calories` int NOT NULL,
  `protein` decimal(6, 2) NULL DEFAULT NULL,
  `fat` decimal(6, 2) NULL DEFAULT NULL,
  `carbs` decimal(6, 2) NULL DEFAULT NULL,
  `is_common` tinyint NULL DEFAULT 1 COMMENT '是否常用食物',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '食物库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of h_food_lib
-- ----------------------------
INSERT INTO `h_food_lib` VALUES (1, '鸡胸肉', '100g', 165, 31.00, 3.60, 0.00, 1);
INSERT INTO `h_food_lib` VALUES (2, '牛肉', '100g', 250, 26.00, 15.00, 0.00, 1);
INSERT INTO `h_food_lib` VALUES (3, '米饭', '100g', 130, 2.70, 0.30, 28.00, 1);
INSERT INTO `h_food_lib` VALUES (4, '鸡蛋', '100g', 155, 13.00, 11.00, 1.10, 1);
INSERT INTO `h_food_lib` VALUES (5, '苹果', '100g', 52, 0.30, 0.20, 14.00, 1);
INSERT INTO `h_food_lib` VALUES (6, '牛肉面', '100g', 130, 6.00, 4.00, 20.00, 1);
INSERT INTO `h_food_lib` VALUES (7, '燕麦片', '100g', 367, 15.00, 6.70, 61.00, 1);
INSERT INTO `h_food_lib` VALUES (8, '全麦面包', '100g', 246, 9.10, 3.30, 45.00, 1);
INSERT INTO `h_food_lib` VALUES (9, '红薯', '100g', 86, 1.60, 0.10, 20.00, 1);
INSERT INTO `h_food_lib` VALUES (10, '玉米(鲜)', '100g', 112, 4.00, 1.20, 22.80, 1);
INSERT INTO `h_food_lib` VALUES (11, '馒头', '100g', 223, 7.00, 1.10, 47.00, 1);
INSERT INTO `h_food_lib` VALUES (12, '猪瘦肉', '100g', 143, 20.30, 6.20, 1.50, 1);
INSERT INTO `h_food_lib` VALUES (13, '三文鱼', '100g', 139, 17.20, 7.80, 0.00, 1);
INSERT INTO `h_food_lib` VALUES (14, '虾仁', '100g', 85, 18.00, 1.10, 0.00, 1);
INSERT INTO `h_food_lib` VALUES (15, '鸭肉', '100g', 240, 15.00, 19.70, 0.20, 1);
INSERT INTO `h_food_lib` VALUES (16, '培根', '100g', 181, 14.00, 13.00, 2.00, 0);
INSERT INTO `h_food_lib` VALUES (17, '牛奶(全脂)', '100ml', 54, 3.00, 3.20, 3.40, 1);
INSERT INTO `h_food_lib` VALUES (18, '希腊酸奶(无糖)', '100g', 59, 10.00, 0.40, 3.60, 1);
INSERT INTO `h_food_lib` VALUES (19, '豆腐(北)', '100g', 98, 12.20, 4.80, 1.50, 1);
INSERT INTO `h_food_lib` VALUES (20, '豆浆(无糖)', '100ml', 31, 3.00, 1.60, 1.20, 1);
INSERT INTO `h_food_lib` VALUES (21, '奶酪(切达)', '100g', 403, 25.00, 33.00, 1.30, 0);
INSERT INTO `h_food_lib` VALUES (22, '西兰花', '100g', 34, 4.10, 0.60, 7.00, 1);
INSERT INTO `h_food_lib` VALUES (23, '菠菜', '100g', 23, 2.60, 0.30, 2.80, 1);
INSERT INTO `h_food_lib` VALUES (24, '西红柿', '100g', 18, 0.90, 0.20, 4.00, 1);
INSERT INTO `h_food_lib` VALUES (25, '黄瓜', '100g', 15, 0.80, 0.20, 2.90, 1);
INSERT INTO `h_food_lib` VALUES (26, '胡萝卜', '100g', 41, 1.00, 0.20, 9.50, 1);
INSERT INTO `h_food_lib` VALUES (27, '香蕉', '100g', 89, 1.10, 0.30, 22.80, 1);
INSERT INTO `h_food_lib` VALUES (28, '牛油果', '100g', 160, 2.00, 14.70, 8.50, 1);
INSERT INTO `h_food_lib` VALUES (29, '蓝莓', '100g', 57, 0.70, 0.30, 14.00, 1);
INSERT INTO `h_food_lib` VALUES (30, '葡萄', '100g', 69, 0.50, 0.20, 18.00, 1);
INSERT INTO `h_food_lib` VALUES (31, '草莓', '100g', 32, 0.70, 0.30, 7.70, 1);
INSERT INTO `h_food_lib` VALUES (32, '核桃', '100g', 654, 15.00, 65.00, 13.70, 1);
INSERT INTO `h_food_lib` VALUES (33, '杏仁', '100g', 579, 21.00, 49.00, 21.00, 1);
INSERT INTO `h_food_lib` VALUES (34, '黑巧克力(70%以上)', '100g', 598, 7.80, 42.60, 45.90, 0);
INSERT INTO `h_food_lib` VALUES (35, '薯片', '100g', 536, 7.00, 35.00, 53.00, 0);
INSERT INTO `h_food_lib` VALUES (36, '汉堡', '1个', 295, 17.00, 14.00, 24.00, 0);
INSERT INTO `h_food_lib` VALUES (37, '披萨(至尊)', '100g', 266, 11.00, 10.00, 33.00, 0);
INSERT INTO `h_food_lib` VALUES (38, '麻辣烫', '500g', 450, 25.00, 20.00, 40.00, 0);
INSERT INTO `h_food_lib` VALUES (39, '皮蛋瘦肉粥', '100g', 54, 2.50, 1.20, 8.40, 1);

-- ----------------------------
-- Table structure for h_health_record
-- ----------------------------
DROP TABLE IF EXISTS `h_health_record`;
CREATE TABLE `h_health_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `steps` int NULL DEFAULT 0 COMMENT '步数',
  `sleep_hours` decimal(3, 1) NULL DEFAULT NULL COMMENT '睡眠时长',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重',
  `calories_burned` int NULL DEFAULT 0 COMMENT '消耗热量',
  `record_date` date NOT NULL COMMENT '记录日期',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id` ASC, `record_date` ASC) USING BTREE,
  INDEX `idx_user_date`(`user_id` ASC, `record_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of h_health_record
-- ----------------------------
INSERT INTO `h_health_record` VALUES (1, 1, 5000, 7.5, 70.00, 200, '2026-03-16', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (2, 1, 6500, 7.0, 69.80, 250, '2026-03-17', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (3, 1, 7200, 6.5, 69.60, 300, '2026-03-18', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (4, 1, 8000, 8.0, 69.50, 350, '2026-03-19', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (5, 1, 9000, 7.2, 69.30, 400, '2026-03-20', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (6, 1, 7500, 6.8, 69.20, 320, '2026-03-21', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (7, 1, 7560, 7.5, 69.00, 402, '2026-03-22', '2026-03-22 16:35:23');
INSERT INTO `h_health_record` VALUES (8, 1, 3977, NULL, NULL, NULL, '2026-03-24', '2026-03-24 23:25:09');
INSERT INTO `h_health_record` VALUES (9, 1, 3776, NULL, NULL, NULL, '2026-03-25', '2026-03-25 00:06:36');
INSERT INTO `h_health_record` VALUES (10, 1, 7560, NULL, NULL, 402, '2026-03-26', '2026-03-26 20:03:49');
INSERT INTO `h_health_record` VALUES (11, 1, 7560, NULL, NULL, 402, '2026-03-28', '2026-03-28 20:12:58');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, 1, 'https://xxx.com/avatar1.png', 'avatar', '2026-03-22 16:35:23');
INSERT INTO `sys_file` VALUES (2, 1, 'https://xxx.com/food1.jpg', 'food', '2026-03-22 16:35:23');
INSERT INTO `sys_file` VALUES (3, 1, '/uploads/1b1b5ded-f5d3-4bb0-981f-3a6cfbcd68f5.JPG', 'image', '2026-03-24 19:16:32');
INSERT INTO `sys_file` VALUES (4, 1, '/static/uploads/c0ee2b76-9fe7-4fef-b96c-aff41fe61db0.JPG', 'image', '2026-03-24 19:20:01');
INSERT INTO `sys_file` VALUES (5, 1, '/static/uploads/7e4ae70e-c6e8-43bf-88aa-1211cd43feeb.png', 'image', '2026-03-24 19:20:49');
INSERT INTO `sys_file` VALUES (6, 1, '/uploads/e087a914-ef96-42d8-93f7-023d85988746.png', 'image', '2026-03-24 19:22:49');
INSERT INTO `sys_file` VALUES (7, 1, '/uploads/33f9c035-5c4d-46ad-bba1-89af8df9763e.png', 'avatar', '2026-03-24 19:43:25');
INSERT INTO `sys_file` VALUES (8, 1, '/uploads/69e20a68-3b09-4eef-ad6d-844c54103f5b.jpg', 'avatar', '2026-03-24 20:22:40');
INSERT INTO `sys_file` VALUES (9, 1, '/uploads/9c14b11f-fa05-4b3c-ae90-4b536592d38b.jpg', 'avatar', '2026-03-24 21:25:49');
INSERT INTO `sys_file` VALUES (10, 1, '/uploads/2daa5d5e-d9c2-4844-b2bf-7dd9456b4fcc.jpg', 'avatar', '2026-03-24 21:29:23');
INSERT INTO `sys_file` VALUES (11, 1, '/uploads/a1f69f51-ec90-4e89-94f2-c4647361ddf7.jpg', 'avatar', '2026-03-24 21:50:03');

-- ----------------------------
-- Table structure for sys_reminder
-- ----------------------------
DROP TABLE IF EXISTS `sys_reminder`;
CREATE TABLE `sys_reminder`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'drink/exercise',
  `time` time NOT NULL,
  `is_enabled` tinyint NULL DEFAULT 1,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '提醒配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_reminder
-- ----------------------------
INSERT INTO `sys_reminder` VALUES (6, 1, '跑步', '10:00:00', 1, '2026-04-05 21:13:42');

-- ----------------------------
-- Table structure for u_user
-- ----------------------------
DROP TABLE IF EXISTS `u_user`;
CREATE TABLE `u_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别 0未知 1男 2女',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `height` decimal(5, 2) NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重(kg)',
  `target_weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '目标体重',
  `daily_goal_steps` int NULL DEFAULT 8000 COMMENT '每日步数目标',
  `daily_goal_calories` int NULL DEFAULT 2000 COMMENT '每日热量目标',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 1正常 0禁用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user
-- ----------------------------
INSERT INTO `u_user` VALUES (1, 'test123', '$2a$10$.ceUv1YN8NwulK00oBrxz.bmJvSxYDeUeE6DiKnukFNfEtrhyY9lG', '小明', 2, 21, 180.00, 75.00, 65.00, 8000, 2000, 1, '2026-03-22 16:35:23', '2026-04-09 21:30:26', '/uploads/f6fa7fcc-0347-41bd-8cd7-a3354e3837e6_user.jpg');
INSERT INTO `u_user` VALUES (2, 'user456', '$2a$10$.ceUv1YN8NwulK00oBrxz.bmJvSxYDeUeE6DiKnukFNfEtrhyY9lG', '小红', 2, 25, 165.00, 55.00, 50.00, 6000, 1800, 1, '2026-03-22 16:35:23', '2026-03-22 18:20:02', NULL);
INSERT INTO `u_user` VALUES (4, 'useradmin', '$2a$10$.ceUv1YN8NwulK00oBrxz.bmJvSxYDeUeE6DiKnukFNfEtrhyY9lG', '小a', 0, NULL, NULL, NULL, NULL, 8000, 2000, 1, '2026-03-22 18:19:48', '2026-03-22 18:20:05', NULL);
INSERT INTO `u_user` VALUES (5, 'test354', '$2a$10$aGKcz9uUAgYJQ1OSt8v7Iemd/E46Ug3BtRzMcFyk14YPjH4p1.CA.', '小明', 0, NULL, NULL, NULL, NULL, 8000, 2000, 1, '2026-03-23 15:17:38', '2026-03-23 15:17:38', NULL);
INSERT INTO `u_user` VALUES (6, 'test12jh3', '$2a$10$m2/OwVY.wfpxI.Js8pJ8i.rdx5e3H0lPqh7RZlBj51Hs1SUn/PCnG', '小明', 0, NULL, NULL, NULL, NULL, 8000, 2000, 1, '2026-03-24 19:11:47', '2026-03-24 19:11:47', NULL);

-- ----------------------------
-- Table structure for u_user_token
-- ----------------------------
DROP TABLE IF EXISTS `u_user_token`;
CREATE TABLE `u_user_token`  (
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录 token',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录 Token 表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of u_user_token
-- ----------------------------
INSERT INTO `u_user_token` VALUES (1, '04134b1567b345dfa751495069406e18', '2026-04-10 21:30:06', '2026-04-09 21:30:05');

SET FOREIGN_KEY_CHECKS = 1;
