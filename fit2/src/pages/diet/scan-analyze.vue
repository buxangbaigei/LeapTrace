<template>
	<view class="container">
		<view class="bg-decoration"></view>

		<view class="header-section">
			<view class="title-wrapper">
				<text class="title">智 慧 避 坑</text>
				<view class="title-line"></view>
			</view>
			<text class="subtitle">AI 视觉扫描 · 营养深度解析</text>
		</view>

		<view class="main-card">
			<view class="scan-area" @tap="handleChooseImage">
				<view class="corner top-left"></view>
				<view class="corner top-right"></view>
				<view class="corner bottom-left"></view>
				<view class="corner bottom-right"></view>
				
				<view class="scan-line"></view>

				<view class="content-overlay">
					<view class="icon-circle">
						<u-icon name="camera-fill" size="44" color="#3c9cff"></u-icon>
					</view>
					<text class="action-text">对准营养成分表拍照</text>
					<view class="tip-tag">支持本地图片上传</view>
				</view>
			</view>
		</view>

		<view class="footer-tips">
			<u-icon name="info-circle" color="#94a3b8" size="16"></u-icon>
			<text>提示：避坑指南根据您的每日热量目标动态计算</text>
		</view>

		<u-popup :show="showResult" mode="center" round="30" @close="showResult = false">
			<view class="result-panel" :class="scanResult.status">
				<view class="status-icon-wrap">
					<u-icon :name="scanResult.status === 'danger' ? 'error-circle-fill' : 'checkmark-circle-fill'" 
						size="80" color="#fff"></u-icon>
				</view>
				
				<text class="alert-title">{{ scanResult.status === 'danger' ? '建议放下！' : '可以食用' }}</text>
				
				<view class="food-info-card">
					<text class="food-name">{{ scanResult.food_name || '未知食品' }}</text>
					<view class="divider"></view>
					<text class="advice-text">{{ scanResult.advice }}</text>
				</view>

				<view class="nutrition-card">
					<view class="progress-info">
						<text>单份热量占全天额度</text>
						<text class="percent">{{ scanResult.calories_ratio }}%</text>
					</view>
					<u-line-progress :percentage="scanResult.calories_ratio" activeColor="#fff" height="12" :showText="false"></u-line-progress>
					<view class="calories-val">{{ scanResult.calories }} <text>kcal</text></view>
				</view>

				<view class="btn-group">
					<button class="confirm-btn" @tap="showResult = false">我知道了</button>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script setup>
import { ref } from 'vue';
import { BASE_URL } from '@/utils/request.js';

// 响应式变量
const showResult = ref(false);
const scanResult = ref({
	food_name: '',
	calories: 0,
	calories_ratio: 0,
	status: 'safe', // safe, warning, danger
	advice: ''
});

// 选择图片
const handleChooseImage = () => {
	uni.chooseImage({
		count: 1,
		sizeType: ['compressed'],
		success: (res) => {
			uploadFile(res.tempFilePaths[0]);
		}
	});
};

// 上传并分析
const uploadFile = (filePath) => {
	uni.showLoading({ title: 'AI 实验室分析中...', mask: true });
	
	const token = uni.getStorageSync('token'); 

	uni.uploadFile({
		url: BASE_URL + '/api/diet/scan-analyze', // 使用你定义的 BASE_URL
		filePath: filePath,
		name: 'file',
		header: {
			'Authorization': 'Bearer ' + token
		},
		success: (uploadRes) => {
			try {
				const res = JSON.parse(uploadRes.data);
				if (res.code === 200) {
					scanResult.value = res.data;
					showResult.value = true;
					// 触感反馈
					if (scanResult.value.status === 'danger') {
						uni.vibrateLong();
					} else {
						uni.vibrateShort();
					}
				} else {
					uni.showToast({ title: res.message || '识别失败', icon: 'none' });
				}
			} catch (e) {
				uni.showToast({ title: '数据解析错误', icon: 'none' });
			}
		},
		fail: () => {
			uni.showToast({ title: '网络请求失败', icon: 'none' });
		},
		complete: () => {
			uni.hideLoading();
		}
	});
};
</script>

<style lang="scss" scoped>
.container {
	padding: 40rpx;
	background-color: #f8fbff;
	min-height: 100vh;
	position: relative;
	overflow: hidden;
}

/* 背景装饰球 */
.bg-decoration {
	position: absolute;
	top: -100rpx;
	right: -100rpx;
	width: 400rpx;
	height: 400rpx;
	background: radial-gradient(circle, rgba(60, 156, 255, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
	border-radius: 50%;
}

.header-section {
	margin-top: 80rpx;
	text-align: center;
	.title-wrapper {
		display: inline-block;
		position: relative;
		.title { font-size: 52rpx; font-weight: 800; color: #1e293b; letter-spacing: 4rpx; }
		.title-line { width: 100%; height: 12rpx; background: rgba(60, 156, 255, 0.2); position: absolute; bottom: 8rpx; left: 0; z-index: -1; }
	}
	.subtitle { font-size: 26rpx; color: #94a3b8; margin-top: 16rpx; display: block; }
}

/* 主扫描卡片 */
.main-card {
	margin-top: 80rpx;
	background: #ffffff;
	padding: 30rpx;
	border-radius: 40rpx;
	box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.05);
}

.scan-area {
	height: 540rpx;
	background-color: #f0f7ff;
	border-radius: 30rpx;
	position: relative;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow: hidden;

	.corner {
		position: absolute; width: 40rpx; height: 40rpx; border: 6rpx solid #3c9cff;
		&.top-left { top: 30rpx; left: 30rpx; border-right: none; border-bottom: none; }
		&.top-right { top: 30rpx; right: 30rpx; border-left: none; border-bottom: none; }
		&.bottom-left { bottom: 30rpx; left: 30rpx; border-right: none; border-top: none; }
		&.bottom-right { bottom: 30rpx; right: 30rpx; border-left: none; border-top: none; }
	}

	.scan-line {
		position: absolute; z-index: 5; top: 0; left: 0; width: 100%; height: 6rpx;
		background: linear-gradient(to right, transparent, #3c9cff, transparent);
		box-shadow: 0 0 15rpx rgba(60, 156, 255, 0.5);
		animation: scanning 3s infinite linear;
	}

	.content-overlay {
		position: relative; z-index: 10; display: flex; flex-direction: column; align-items: center;
		.icon-circle {
			width: 120rpx; height: 120rpx; background: #fff; border-radius: 50%;
			display: flex; align-items: center; justify-content: center;
			box-shadow: 0 10rpx 20rpx rgba(60, 156, 255, 0.1);
		}
		.action-text { margin-top: 30rpx; font-size: 34rpx; color: #3c9cff; font-weight: bold; }
		.tip-tag { margin-top: 20rpx; padding: 8rpx 30rpx; background: rgba(60, 156, 255, 0.1); color: #3c9cff; font-size: 24rpx; border-radius: 50rpx; }
	}
}

@keyframes scanning {
	0% { top: 0%; }
	100% { top: 100%; }
}

.footer-tips {
	margin-top: 60rpx; display: flex; align-items: center; justify-content: center;
	text { font-size: 24rpx; color: #94a3b8; margin-left: 10rpx; }
}

/* 结果弹窗样式 */
.result-panel {
	width: 650rpx; padding: 60rpx 40rpx; border-radius: 40rpx;
	display: flex; flex-direction: column; align-items: center;

	&.danger { background: linear-gradient(135deg, #ef4444, #b91c1c); }
	&.safe { background: linear-gradient(135deg, #10b981, #047857); }
	&.warning { background: linear-gradient(135deg, #f59e0b, #b45309); }

	.status-icon-wrap {
		width: 140rpx; height: 140rpx; background: rgba(255,255,255,0.2);
		border-radius: 50%; display: flex; align-items: center; justify-content: center;
		animation: pulse 2s infinite;
	}

	.alert-title { font-size: 52rpx; font-weight: 900; color: #fff; margin-top: 30rpx; }

	.food-info-card {
		margin-top: 40rpx; background: rgba(255,255,255,0.95); width: 100%;
		padding: 30rpx; border-radius: 24rpx; text-align: center;
		.food-name { font-size: 36rpx; font-weight: bold; color: #1e293b; }
		.divider { height: 2rpx; background: #e2e8f0; margin: 20rpx 0; }
		.advice-text { font-size: 28rpx; color: #475569; line-height: 1.6; }
	}

	.nutrition-card {
		width: 100%; margin-top: 40rpx;
		.progress-info {
			display: flex; justify-content: space-between; color: #fff; font-size: 26rpx; margin-bottom: 12rpx;
			.percent { font-weight: bold; font-size: 32rpx; }
		}
		.calories-val { color: #fff; text-align: right; margin-top: 10rpx; font-weight: bold; font-size: 36rpx; text { font-size: 24rpx; font-weight: normal; } }
	}

	.btn-group {
		width: 100%; margin-top: 50rpx;
		.confirm-btn {
			width: 100%; height: 90rpx; line-height: 90rpx; background: #fff;
			color: #1e293b; font-weight: bold; border-radius: 50rpx; border: none;
		}
	}
}

@keyframes pulse {
	0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(255,255,255,0.4); }
	70% { transform: scale(1.05); box-shadow: 0 0 0 20rpx rgba(255,255,255,0); }
	100% { transform: scale(1); }
}
</style>