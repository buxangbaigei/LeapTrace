<template>
	<view class="chat-container">
		<view class="chat-header">
			<view class="back-btn">
			</view>
			<view class="title-box">
				<text class="title">跃动健康 AI 助手</text>
				<view class="status-tag">
					<view class="dot"></view>
					<text>AI 智能在线</text>
				</view>
			</view>
			<view class="more-btn">
				<u-icon name="info-circle" size="18px" color="#64748b"></u-icon>
			</view>
		</view>

		<scroll-view class="msg-list" scroll-y :scroll-top="scrollTop" scroll-with-animation>
			<view class="welcome-section">
				<image src="/static/aiimage.png" class="big-ai-logo"></image>
				<view class="welcome-text">
					<text class="h1">Hi, 我是你的健康顾问</text>
					<text class="p">我可以为你分析饮食热量、推荐运动计划或解答健康疑问。</text>
				</view>
				<view class="guide-grid">
					<view class="guide-item" @click="quickInput('如何健康饮食')">🥗 如何健康饮食</view>
					<view class="guide-item" @click="quickInput('制定减肥计划')">🏃 减肥计划</view>
				</view>
			</view>

			<view v-for="(msg, index) in messages" :key="index" :class="['msg-row', msg.role]">
				
				<view class="avatar-box" v-if="msg.role === 'ai'">
					<image src="/static/aiimage.png" class="avatar shadow-sm"></image>
				</view>
				
				<view class="content-box">
					<view class="bubble shadow-sm">
						<text class="text">{{ msg.content }}</text>
					</view>
					<text class="time">{{ msg.time }}</text>
				</view>
				
				<view class="avatar-box" v-if="msg.role === 'user'">
					<image :src="userAvatar" class="avatar user-avatar shadow-sm"></image>
				</view>
			</view>

			<view v-if="loading" class="msg-row ai">
				<view class="avatar-box">
					<image src="/static/aiimage.png" class="avatar"></image>
				</view>
				<view class="content-box">
					<view class="bubble loading-bubble">
						<view class="dot-flashing"></view>
					</view>
				</view>
			</view>

			<view style="height: 50rpx;"></view>
		</scroll-view>

		<view class="input-panel shadow-top">
			<view class="quick-bar">
				<text class="tag" @click="quickInput('我今天该喝多少水？')">💧 饮水建议</text>
				<text class="tag" @click="quickInput('推荐5分钟拉伸动作')">🧘 快速拉伸</text>
			</view>
			<view class="input-box">
				<u-input v-model="userInput" placeholder="有问题尽管问我..." border="none" :confirm-type="'send'"
					@confirm="handleSend"
					customStyle="background:#f1f5f9; padding:18rpx 30rpx; border-radius:40rpx; flex:1; font-size:28rpx;"></u-input>
				<view class="send-btn" :class="{ active: userInput.trim() && !loading }" @click="handleSend">
					<u-icon name="arrow-up" color="#fff" size="20" bold></u-icon>
				</view>
			</view>
			<view class="safe-area"></view>
		</view>
	</view>
</template>

<script setup>
	import { ref, onMounted, nextTick, computed } from 'vue';
	import { request ,BASE_URL} from '@/utils/request.js';

	const userInput = ref('');
	const messages = ref([{
		role: 'ai',
		content: '你好！今天有什么我可以帮你的吗？',
		time: getNowTime()
	}]);
	const loading = ref(false);
	const scrollTop = ref(0);
	
	// 用户信息存储
	const userInfo = ref(null);

	// 获取当前时间
	function getNowTime() {
		const d = new Date();
		return `${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`;
	}

	// 动态计算用户头像：有接口头像用接口的，没有则用默认兜底图
	const userAvatar = computed(() => {
		if (userInfo.value && userInfo.value.avatar) {
			console.log(userInfo.value.avatar);
			const path = userInfo.value.avatar;
			console.log("头像"+ BASE_URL + path);
			// 自动兼容绝对路径和相对路径
			return BASE_URL + path; 
		}
		return '/static/avatar-default.png';
	});

	// 获取用户信息
	const getUserInfo = async () => {
		try {
			const res = await request({ url: '/api/user/info', method: 'GET' });
			if (res.code === 200) {
				userInfo.value = res.data;
			}
		} catch (err) {
			console.error('获取用户信息失败', err);
		}
	};

	// 快捷输入
	const quickInput = (txt) => {
		userInput.value = txt;
	};

	// 发送逻辑
	const handleSend = async () => {
		if (!userInput.value.trim() || loading.value) return;

		const userText = userInput.value;
		messages.value.push({
			role: 'user',
			content: userText,
			time: getNowTime()
		});
		userInput.value = '';
		loading.value = true;
		scrollToBottom();

		try {
			const res = await request({
				url: '/api/ai/chat',
				method: 'POST',
				data: { content: userText }
			});

			loading.value = false;
			if (res.code === 200) {
				const fullReply = res.data.reply;
				typeWriterEffect(fullReply);
			} else {
				throw new Error();
			}
		} catch (e) {
			loading.value = false;
			messages.value.push({
				role: 'ai',
				content: '信号开小差了，请稍后再试。',
				time: getNowTime()
			});
			scrollToBottom();
		}
	};

	// 模拟打字机吐字
	const typeWriterEffect = (text) => {
		const newMessage = {
			role: 'ai',
			content: '',
			time: getNowTime()
		};
		messages.value.push(newMessage);

		let i = 0;
		const timer = setInterval(() => {
			if (i < text.length) {
				newMessage.content += text.charAt(i);
				i++;
				if (i % 3 === 0) scrollToBottom(); 
			} else {
				clearInterval(timer);
				scrollToBottom();
			}
		}, 40); 
	};

	const scrollToBottom = () => {
		nextTick(() => {
			setTimeout(() => {
				scrollTop.value += 1000;
			}, 100);
		});
	};

	const goBack = () => uni.navigateBack();

	// 页面挂载时拉取用户信息
	onMounted(() => {
		getUserInfo();
	});
</script>

<style lang="scss" scoped>
	.chat-container {
		display: flex;
		flex-direction: column;
		height: calc(100vh - var(--window-bottom));
		background-color: #f8fafc;
		overflow: hidden;

		/* 头部导航 */
		.chat-header {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 20rpx 30rpx;
			background: #fff;
			border-bottom: 1px solid #f1f5f9;

			.title-box {
				text-align: center;
				.title { font-size: 30rpx; font-weight: 700; color: #1e293b; }
				.status-tag {
					display: flex; align-items: center; justify-content: center;
					.dot { width: 10rpx; height: 10rpx; background: #10b981; border-radius: 50%; margin-right: 8rpx; }
					text { font-size: 20rpx; color: #64748b; }
				}
			}
		}

		/* 消息列表 */
		.msg-list {
			flex: 1; height: 0; width:95% ; margin: auto;

			.welcome-section {
				padding: 60rpx 0; display: flex; flex-direction: column; align-items: center;
				.big-ai-logo { width: 140rpx; height: 140rpx; margin-bottom: 30rpx; }
				.welcome-text {
					text-align: center; margin-bottom: 40rpx;
					.h1 { display: block; font-size: 36rpx; font-weight: 700; color: #1e293b; margin-bottom: 12rpx; }
					.p { font-size: 26rpx; color: #94a3b8; line-height: 1.5; padding: 0 60rpx; }
				}
				.guide-grid {
					display: flex; gap: 20rpx;
					.guide-item {
						background: #fff; border: 1px solid #e2e8f0; padding: 16rpx 30rpx;
						border-radius: 40rpx; font-size: 24rpx; color: #64748b;
						&:active { background: #f1f5f9; }
					}
				}
			}

			.msg-row {
				display: flex; margin-bottom: 40rpx; animation: slideUp 0.3s ease;

				.avatar { width: 76rpx; height: 76rpx; border-radius: 50%; background: #fff; }
				.content-box {
					max-width: 70%; display: flex; flex-direction: column;
					.time { font-size: 20rpx; color: #cbd5e1; margin-top: 10rpx; }
				}

				&.ai {
					.content-box { margin-left: 20rpx; align-items: flex-start; }
					.bubble { background: #fff; color: #334155; border-radius: 4rpx 24rpx 24rpx 24rpx; }
				}

				&.user {
					justify-content: flex-end;
					.avatar-box { margin-left: 20rpx; flex-shrink: 0; }
					.content-box { margin-right: 0rpx; align-items: flex-end; }
					.user-avatar { border: 2px solid #fff; display: block; }
				}

				.bubble { padding: 22rpx 30rpx; font-size: 28rpx; line-height: 1.6; word-break: break-all; }
			}
		}

		/* 思考动画样式 */
		.loading-bubble {
			padding: 35rpx 45rpx !important;
			.dot-flashing {
				width: 10rpx; height: 10rpx; border-radius: 50%; background-color: #10b981;
				animation: dot-flashing 1s infinite alternate; position: relative;
				&::before, &::after {
					content: ''; display: inline-block; position: absolute; top: 0;
					width: 10rpx; height: 10rpx; border-radius: 50%; background-color: #10b981;
					animation: dot-flashing 1s infinite alternate;
				}
				&::before { left: -20rpx; animation-delay: 0s; }
				&::after { left: 20rpx; animation-delay: 0.5s; }
			}
		}

		/* 输入面板 */
		.input-panel {
			background: #fff; border-radius: 40rpx 40rpx 0 0; padding: 20rpx 30rpx;
			.quick-bar {
				display: flex; gap: 16rpx; margin-bottom: 24rpx; overflow-x: auto;
				.tag {
					white-space: nowrap; font-size: 22rpx; color: #10b981;
					background: #f0fdf4; border: 1px solid #dcfce7; padding: 8rpx 20rpx; border-radius: 20rpx;
				}
			}
			.input-box {
				display: flex; align-items: center; gap: 20rpx;
				.send-btn {
					width: 76rpx; height: 76rpx; background: #cbd5e1; border-radius: 50%;
					display: flex; align-items: center; justify-content: center;
					transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
					&.active { background: #10b981; transform: scale(1.05); }
					&:active { transform: scale(0.9); }
				}
			}
			.safe-area { height: env(safe-area-inset-bottom); }
		}
	}

	@keyframes slideUp { from { opacity: 0; transform: translateY(20rpx); } to { opacity: 1; transform: translateY(0); } }
	@keyframes dot-flashing { 0% { opacity: 0.2; } 100% { opacity: 1; } }

	.shadow-sm { box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.05); }
	.shadow-top { box-shadow: 0 -5rpx 20rpx rgba(0, 0, 0, 0.04); }
</style>