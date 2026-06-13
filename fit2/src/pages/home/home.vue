<template>
	<view class="home-container">
		<view class="nav-header">
			<view class="brand-box">
				<text class="brand">LeapTrace</text>
				<text class="tagline">跃动健康 · 智能监测</text>
			</view>
			<view class="header-right" @click="goReport">
				<u-icon name="level" size="20px" color="#10b981"></u-icon>
				<text class="report-link">趋势报表</text>
			</view>
		</view>

		<view class="main-stats">
			<view class="circle-outer" :style="{ '--p': stepRate + '%' }">
				<view class="circle-inner">
					<text class="step-num">{{ dashboard.steps }}</text>
					<text class="step-label">今日步数</text>
					<view class="goal-tag">目标 {{ dashboard.goal_steps }}</view>
				</view>
			</view>
		</view>

		<view class="calorie-card">
			<view class="card-title">热量消耗平衡 (kcal)</view>
			<view class="calorie-grid">
				<view class="cal-item">
					<text class="val">{{ dashboard.consumed_calories }}</text>
					<text class="lab">饮食摄入</text>
					<u-icon name="arrow-down-fill" color="#ef4444" size="12"></u-icon>
				</view>
				<view class="divider"></view>
				<view class="cal-item">
					<text class="val">{{ dashboard.burned_calories }}</text>
					<text class="lab">运动消耗</text>
					<u-icon name="arrow-up-fill" color="#10b981" size="12"></u-icon>
				</view>
				<view class="divider"></view>
				<view class="cal-item">
					<text class="val highlight">{{ dashboard.remaining_calories }}</text>
					<text class="lab">剩余配额</text>
				</view>
			</view>
		</view>

		<view class="action-grid">
			<view class="action-item" @click="tabTo('/pages/diet/index')">
				<view class="icon-circle bg-green"><u-icon name="camera" color="#fff" size="28px"></u-icon></view>
				<text>饮食识别</text>
			</view>
			<view class="action-item" @click="tabTo('/pages/ai-chat/ai-chat')">
				<view class="icon-circle bg-blue"><u-icon name="chat" color="#fff" size="28px"></u-icon></view>
				<text>健康咨询</text>
			</view>
			<view class="action-item" @click="navTo('/pages/me/reminders')">
				<view class="icon-circle bg-orange"><u-icon name="bell" color="#fff" size="28px"></u-icon></view>
				<text>健康提醒</text>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue';
import { onShow, onHide } from '@dcloudio/uni-app';
import { request } from '@/utils/request.js';

// 变量
let syncTimer = null;
const dashboard = ref({
	steps: 0,
	goal_steps: 8000,
	consumed_calories: 0,
	burned_calories: 0,
	remaining_calories: 0
});

const stepRate = computed(() => {
	const current = dashboard.value.steps || 0;
	const goal = dashboard.value.goal_steps || 8000;
	return Math.min(100, Math.floor((current / goal) * 100));
});

/**
 * 1. 上传步数到服务器 (POST)
 */
const syncStepsToServer = async (steps) => {
	if (steps === undefined || steps === null) return;
	const now = new Date();
	const today = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')}`;
	
	try {
		await request({
			url: '/api/health/steps',
			method: 'POST',
			data: { steps: parseInt(steps), record_date: today }
		});
		console.log('☁️ 步数同步云端成功:', steps);
	} catch (e) {
		console.error('❌ 步数同步云端失败:', e);
	}
};

/**
 * 2. 从本地缓存恢复步数 (防止划掉变0)
 */
const recoverStepsFromStorage = () => {
	const now = new Date();
	const today = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')}`;
	const saved = uni.getStorageSync(`steps_${today}`) || uni.getStorageSync('global_last_steps');
	
	if (saved) {
		const stepsNum = parseInt(saved);
		if (stepsNum > dashboard.value.steps) {
			dashboard.value.steps = stepsNum;
		}
	}
};

/**
 * 3. 获取后端看板数据 (GET)
 */
const fetchRemoteDashboard = async () => {
	try {
		const res = await request({ url: '/api/health/dashboard', method: 'GET' });
		if (res.code === 200) {
			dashboard.value.goal_steps = res.data.goal_steps || 8000;
			dashboard.value.consumed_calories = res.data.consumed_calories || 0;
			dashboard.value.burned_calories = res.data.burned_calories || 0;
			dashboard.value.remaining_calories = res.data.remaining_calories || 0;
		}
	} catch (e) {}
};

onShow(() => {
	// 立即找回本地数据
	recoverStepsFromStorage();
	// 获取其他健康指标
	fetchRemoteDashboard();

	// 监听传感器步数变化
	uni.$on('step-change', (steps) => {
		dashboard.value.steps = steps;
		// 策略：每走 30 步自动上报一次，避免请求过勤
		if (steps % 30 === 0) {
			syncStepsToServer(steps);
		}
	});

	// 每秒检查一次本地存储（双保险）
	syncTimer = setInterval(recoverStepsFromStorage, 1000);
});

onHide(() => {
	if (syncTimer) clearInterval(syncTimer);
	// 【关键】切后台或划掉前，强制同步一次最终步数到服务器
	syncStepsToServer(dashboard.value.steps);
	uni.$off('step-change');
});

onUnmounted(() => {
	if (syncTimer) clearInterval(syncTimer);
	uni.$off('step-change');
});

const goReport = () => uni.navigateTo({ url: '/pages/home/report' });
const navTo = (url) => uni.navigateTo({ url });
const tabTo = (url) => uni.switchTab({ url });
</script>

<style lang="scss" scoped>
/* 保持原有样式不变 */
.home-container {
	padding: 40rpx; background: #f8fafc; min-height: 100vh;
	padding-bottom: calc(var(--window-bottom) + 40rpx);
	.nav-header {
		display: flex; justify-content: space-between; padding-top: 60rpx; margin-bottom: 60rpx;
		.brand { font-size: 48rpx; font-weight: 900; color: #10b981; font-style: italic; }
		.tagline { font-size: 22rpx; color: #94a3b8; }
		.header-right { background: #fff; padding: 12rpx 24rpx; border-radius: 30rpx; display: flex; align-items: center; gap: 8rpx; box-shadow: 0 4rpx 10rpx rgba(0,0,0,0.03); .report-link { font-size: 24rpx; font-weight: bold; } }
	}
	.main-stats {
		display: flex; justify-content: center; margin-bottom: 60rpx;
		.circle-outer {
			width: 420rpx; height: 420rpx; border-radius: 50%;
			background: conic-gradient(#10b981 0%, #10b981 var(--p), #f1f5f9 var(--p), #f1f5f9 100%);
			display: flex; align-items: center; justify-content: center;
			box-shadow: 0 20rpx 40rpx rgba(16, 185, 129, 0.1); transition: all 0.4s ease-out;
			.circle-inner {
				width: 360rpx; height: 360rpx; background: #fff; border-radius: 50%;
				display: flex; flex-direction: column; align-items: center; justify-content: center;
				.step-num { font-size: 80rpx; font-weight: 800; color: #1e293b; }
				.step-label { font-size: 24rpx; color: #94a3b8; }
				.goal-tag { font-size: 22rpx; background: #f0fdf4; color: #10b981; padding: 4rpx 20rpx; border-radius: 20rpx; }
			}
		}
	}
	.calorie-card {
		background: #fff; border-radius: 40rpx; padding: 40rpx; margin-bottom: 40rpx;
		.card-title { font-size: 26rpx; color: #94a3b8; margin-bottom: 30rpx; text-align: center; }
		.calorie-grid {
			display: flex; justify-content: space-between; align-items: center;
			.cal-item { flex: 1; text-align: center; display: flex; flex-direction: column; .val { font-size: 34rpx; font-weight: bold; } .lab { font-size: 22rpx; color: #64748b; } .highlight { color: #10b981; font-size: 40rpx; } }
			.divider { width: 2rpx; height: 60rpx; background: #f1f5f9; }
		}
	}
	.action-grid {
		display: grid; grid-template-columns: repeat(3, 1fr); gap: 20rpx;
		.action-item {
			background: #fff; padding: 30rpx; border-radius: 36rpx; display: flex; flex-direction: column; align-items: center;
			.icon-circle { width: 100rpx; height: 100rpx; border-radius: 30rpx; display: flex; align-items: center; justify-content: center; margin-bottom: 15rpx; }
			.bg-green { background: #10b981; } .bg-blue { background: #3b82f6; } .bg-orange { background: #f59e0b; }
			text { font-size: 26rpx; font-weight: 500; }
		}
	}
}
</style>