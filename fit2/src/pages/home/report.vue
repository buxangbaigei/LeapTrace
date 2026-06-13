<template>
	<view class="report-container">
		<view class="status-bar"></view>
		
		<view class="header">
			<u-icon name="arrow-left" size="22px" @click="goBack" color="#1e293b"></u-icon>
			<text class="title">轨迹报表</text>
			<view style="width: 44rpx;"></view>
		</view>

		<view class="stat-overview" v-if="reportData.steps.length">
			<view class="overview-item">
				<text class="label">本周平均步数</text>
				<text class="value">{{ avgSteps }}</text>
			</view>
			<view class="v-line"></view>
			<view class="overview-item">
				<text class="label">目标达成率</text>
				<text class="value">{{ goalDays }}<text class="unit">/7天</text></text>
			</view>
		</view>

		<view class="chart-card">
			<view class="card-head">
				<view class="title-row">
					<text class="main-t">步数趋势</text>
					<text class="sub-t">最近7天 (步)</text>
				</view>
				<u-tag text="周视图" plain size="mini" type="success"></u-tag>
			</view>
			
			<view class="chart-wrapper">
				<view class="y-axis-guides">
					<view class="guide-line" v-for="n in 5" :key="n"></view>
				</view>
				
				<view class="goal-line" :style="{ bottom: goalLineHeight }">
					<view class="line-tag">目标 8000</view>
				</view>

				<view class="chart-main">
					<view v-for="(v, i) in reportData.steps" :key="i" class="bar-item">
						<view class="val-pop" v-if="v > 0">{{ v }}</view>
						
						<view class="bar-group">
							<view 
								class="bar" 
								:style="{ 
									height: ((v / chartMax) * 100) + '%',
									transitionDelay: (i * 0.05) + 's' 
								}"
								:class="{ 'goal-reached': v >= 8000 }"
							></view>
						</view>
						<text class="date">{{ formatLabel(reportData.dates[i]) }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="ai-advice-card">
			<view class="ai-header">
				<view class="ai-icon">
					<u-icon name="zhuanfa" color="#fff" size="14"></u-icon>
				</view>
				<text class="ai-title">AI 智能周评报告</text>
			</view>
			<view class="advice-content">
				<u-loading-icon v-if="loadingAdvice" mode="circle" text="AI 正在分析本周轨迹..."></u-loading-icon>
				<view v-else class="text-body">
					<u-parse :content="weeklyAdvice"></u-parse>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { request } from '@/utils/request.js';

const reportData = ref({ steps: [], dates: [] });
const weeklyAdvice = ref('');
const loadingAdvice = ref(true);

// 1. 获取周报数据 (4.3)
const fetchReport = async () => {
	try {
		const res = await request({ url: '/api/health/report?type=week', method: 'GET' });
		if (res.code === 200 && res.data) {
			reportData.value = res.data;
		}
	} catch (e) {
		uni.showToast({ title: '数据加载失败', icon: 'none' });
	}
};

// 2. 获取 AI 建议 (5.3)
const fetchAIAdvice = async () => {
	loadingAdvice.value = true;
	try {
		const res = await request({ url: '/api/ai/weekly-advice', method: 'GET' });
		if (res.code === 200) {
			weeklyAdvice.value = res.data.summary;
		}
	} catch (e) {
		weeklyAdvice.value = '暂时无法生成 AI 建议，请保持运动后再试。';
	} finally {
		loadingAdvice.value = false;
	}
};

// 计算属性：动态图表最大刻度 (确保柱状图比例协调)
const chartMax = computed(() => {
	const maxInData = reportData.value.steps.length > 0 ? Math.max(...reportData.value.steps) : 0;
	// 刻度至少为 10000，或者比数据最大值高 20%
	return Math.max(10000, maxInData * 1.2);
});

// 计算属性：平均步数
const avgSteps = computed(() => {
	if (!reportData.value.steps.length) return 0;
	const sum = reportData.value.steps.reduce((a, b) => a + b, 0);
	return Math.floor(sum / reportData.value.steps.length);
});

// 计算属性：达标天数
const goalDays = computed(() => reportData.value.steps.filter(v => v >= 8000).length);

// 计算属性：目标线高度
const goalLineHeight = computed(() => {
	return (8000 / chartMax.value * 100) + '%';
});

// 格式化日期显示 (2024-03-24 -> 03/24)
const formatLabel = (dateStr) => {
	if (!dateStr) return '';
	const parts = dateStr.split('-');
	return parts.length >= 3 ? `${parts[1]}/${parts[2]}` : dateStr;
};

const goBack = () => uni.navigateBack();

onMounted(() => {
	fetchReport();
	fetchAIAdvice();
});
</script>

<style lang="scss" scoped>
.report-container {
	padding: 0 40rpx 60rpx; background: #f8fafc; min-height: 100vh;
	.status-bar { height: var(--status-bar-height); }
	
	.header {
		display: flex; justify-content: space-between; align-items: center; 
		padding: 40rpx 0;
		.title { font-size: 34rpx; font-weight: 800; color: #1e293b; }
	}

	.stat-overview {
		background: linear-gradient(135deg, #1e293b, #334155); border-radius: 40rpx; padding: 40rpx;
		display: flex; align-items: center; margin-bottom: 40rpx;
		box-shadow: 0 20rpx 40rpx rgba(30, 41, 59, 0.1);
		.overview-item {
			flex: 1; text-align: center;
			.label { font-size: 22rpx; color: #94a3b8; display: block; margin-bottom: 10rpx; }
			.value { font-size: 40rpx; font-weight: bold; color: #fff; .unit { font-size: 20rpx; margin-left: 4rpx; opacity: 0.6; } }
		}
		.v-line { width: 2rpx; height: 60rpx; background: rgba(255,255,255,0.1); }
	}

	.chart-card {
		background: #fff; border-radius: 40rpx; padding: 40rpx;
		box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.02);
		.card-head {
			display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 60rpx;
			.main-t { font-size: 32rpx; font-weight: bold; color: #1e293b; display: block; }
			.sub-t { font-size: 22rpx; color: #94a3b8; margin-top: 6rpx; }
		}

		.chart-wrapper {
			height: 400rpx; position: relative; margin-top: 40rpx;
			
			.y-axis-guides {
				position: absolute; width: 100%; height: 100%;
				display: flex; flex-direction: column; justify-content: space-between;
				.guide-line { width: 100%; height: 2rpx; background: #f1f5f9; }
			}

			.goal-line {
				position: absolute; width: 100%; border-top: 2rpx dashed #10b981;
				z-index: 1; pointer-events: none; transition: bottom 0.5s;
				.line-tag { 
					position: absolute; right: 0; top: -30rpx; font-size: 18rpx; 
					color: #10b981; font-weight: bold; background: #fff; padding: 0 10rpx;
				}
			}

			.chart-main {
				position: relative; z-index: 2; height: 100%;
				display: flex; align-items: flex-end; justify-content: space-around;
				
				.bar-item {
					display: flex; flex-direction: column; align-items: center; flex: 1;
					.val-pop {
						font-size: 18rpx; color: #64748b; background: #f8fafc;
						padding: 4rpx 8rpx; border-radius: 8rpx; margin-bottom: 10rpx;
						transform: scale(0.9);
					}
					.bar-group {
						width: 28rpx; height: 320rpx; display: flex; align-items: flex-end;
						.bar {
							width: 100%; border-radius: 12rpx 12rpx 4rpx 4rpx;
							background: linear-gradient(to top, #cbd5e1, #e2e8f0);
							height: 0; transition: height 0.8s cubic-bezier(0.18, 0.89, 0.32, 1);
							&.goal-reached { background: linear-gradient(to top, #10b981, #34d399); }
						}
					}
					.date { font-size: 20rpx; color: #94a3b8; margin-top: 20rpx; font-weight: 500; }
				}
			}
		}
	}

	.ai-advice-card {
		margin-top: 40rpx; background: #fff; border-radius: 40rpx; padding: 40rpx;
		.ai-header {
			display: flex; align-items: center; gap: 20rpx; margin-bottom: 30rpx;
			.ai-icon {
				width: 48rpx; height: 48rpx; background: #10b981; border-radius: 12rpx;
				display: flex; align-items: center; justify-content: center;
			}
			.ai-title { font-size: 28rpx; font-weight: bold; color: #1e293b; }
		}
		.advice-content {
			background: #f8fafc; padding: 30rpx; border-radius: 24rpx; min-height: 100rpx;
			.text-body { font-size: 26rpx; color: #475569; line-height: 1.8; }
		}
	}
}
</style>