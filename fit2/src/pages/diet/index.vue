<template>
	<view class="diet-page">
		<view class="hero-card">
			<view class="stats-main">
				<view class="item">
					<text class="num">{{ totalCalories }}</text>
					<text class="label">已摄入 kcal</text>
				</view>
				<view class="divider"></view>
				<view class="item">
					<text class="num">{{ dietLogs.length }}</text>
					<text class="label">记录项</text>
				</view>
			</view>
			<view class="progress-bar">
				<view class="fill" :style="{ width: Math.min((totalCalories / 2000) * 100, 100) + '%' }"></view>
			</view>
		</view>

		<view class="main-content">
			<view class="section-header"><text>今日食谱</text></view>
			<view v-for="item in dietLogs" :key="item.id" class="food-card">
				<view class="card-left">
					<view class="meal-indicator" :class="'m-' + item.meal_type"></view>
					<view>
						<text class="fname">{{ item.food_name }}</text>
						<text class="ftime">{{ getMealName(item.meal_type) }}</text>
					</view>
				</view>
				<view class="card-right">
					<text class="fcal">+{{ item.calories }}</text>
					<u-icon name="close-circle-fill" size="20px" color="#cbd5e1" @click="handleDelete(item.id)"></u-icon>
				</view>
			</view>
			
			<view v-if="dietLogs.length === 0" class="empty-tip">今天还没有记录任何食物哦~</view>
		</view>

		<view class="floating-scan-btn" @tap="goToScan">
			<u-icon name="scan" color="#fff" size="28px"></u-icon>
			<text>避坑</text>
		</view>

		<view class="ai-fab-box">
			<view class="search-suggestions" v-if="searchList.length > 0">
				<view class="sug-item" v-for="food in searchList" :key="food.id" @click="selectFood(food)">
					<view class="s-left">
						<text class="s-name">{{ food.name }}</text>
						<text class="s-info">{{ food.unit_calories }}kcal / {{ food.unit }}</text>
					</view>
					<u-icon name="plus-circle-fill" color="#10b981" size="20px"></u-icon>
				</view>
			</view>

			<view class="input-area">
				<u-icon name="camera-fill" size="26px" color="#64748b" @click="handleUpload"></u-icon>
				<input 
					class="main-input" 
					v-model="keyword" 
					placeholder="搜食物、描述或拍照..." 
					@input="handleInput"
					@confirm="handleSmartInput" 
				/>
				<view class="send-btn" @click="handleSmartInput">
					<u-icon name="arrow-up-fill" color="#fff" size="20px"></u-icon>
				</view>
			</view>
		</view>

		<u-modal :show="showResult" title="确认记录" @confirm="confirmSave" @cancel="showResult=false" showCancelButton confirmColor="#10b981">
			<view class="result-panel">
				<view class="res-item">
					<text class="label">食物</text>
					<u-input v-model="analysisRes.food_name" border="bottom" />
				</view>
				<view class="res-item">
					<text class="label">热量</text>
					<u-input v-model="analysisRes.calories" type="number" border="bottom">
						<template slot="suffix"><text>kcal</text></template>
					</u-input>
				</view>
				<view class="meal-label">选择餐次</view>
				<view class="meal-select">
					<view v-for="(n, i) in ['早', '午', '晚', '加']" :key="i" 
						:class="{active: selectedMeal === i}" @click="selectedMeal = i">
						{{n}}
					</view>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { request, BASE_URL } from '@/utils/request.js';

const dietLogs = ref([]);
const keyword = ref('');
const searchList = ref([]);
const showResult = ref(false);
const analysisRes = ref({ food_name: '', calories: 0 });
const selectedMeal = ref(0);

const totalCalories = computed(() => dietLogs.value.reduce((s, i) => s + Number(i.calories), 0));

const getTodayDiet = async () => {
	const res = await request({ url: '/api/diet/today' });
	if (res.code === 200) dietLogs.value = res.data;
};

let timer = null;
const handleInput = () => {
	clearTimeout(timer);
	if (!keyword.value.trim()) {
		searchList.value = [];
		return;
	}
	timer = setTimeout(async () => {
		const res = await request({
			url: '/api/diet/food/search',
			method: 'GET',
			data: { keyword: keyword.value }
		});
		if (res.code === 200) searchList.value = res.data;
	}, 300);
};

const selectFood = (food) => {
	analysisRes.value = { food_name: food.name, calories: food.unit_calories };
	searchList.value = [];
	keyword.value = '';
	showResult.value = true;
};

const handleSmartInput = async () => {
	if(!keyword.value.trim()) return;
	searchList.value = [];
	uni.showLoading({ title: 'AI分析中...', mask: true });
	try {
		const res = await request({
			url: '/api/diet/ai-analyze/text',
			method: 'POST',
			data: { text: keyword.value }
		});
		if(res.code === 200) {
			analysisRes.value = res.data;
			showResult.value = true;
			keyword.value = '';
		}
	} finally { uni.hideLoading(); }
};

const handleUpload = () => {
	uni.chooseImage({
		count: 1,
		success: (res) => {
			uni.showLoading({ title: 'AI识图中...', mask: true });
			uni.uploadFile({
				url: BASE_URL + '/api/diet/ai-analyze/image',
				filePath: res.tempFilePaths[0],
				name: 'file',
				header: { 'Authorization': 'Bearer ' + uni.getStorageSync('token') },
				success: (up) => {
					const result = JSON.parse(up.data);
					if(result.code === 200) {
						analysisRes.value = result.data;
						showResult.value = true;
					}
				},
				complete: () => uni.hideLoading()
			});
		}
	});
};

const confirmSave = async () => {
	const res = await request({
		url: '/api/diet/log',
		method: 'POST',
		data: { ...analysisRes.value, meal_type: selectedMeal.value }
	});
	if (res.code === 200) {
		showResult.value = false;
		getTodayDiet();
	}
};

const goToScan = () => {
	uni.navigateTo({ url: '/pages/diet/scan-analyze' });
}

const handleDelete = async (id) => {
	const res = await request({ url: `/api/diet/log/${id}`, method: 'DELETE' });
	if(res.code === 200) getTodayDiet();
};

const getMealName = (t) => ['早餐','午餐','晚餐','加餐'][t];
onMounted(() => getTodayDiet());
</script>

<style lang="scss" scoped>
.diet-page {
	background: #f8fafc; min-height: 100vh; 
	padding: 30rpx 30rpx calc(240rpx + env(safe-area-inset-bottom) + var(--window-bottom));

	/* 优化后的悬浮避坑按钮 - 位于输入框之上 */
	.floating-scan-btn {
		position: fixed;
		right: 40rpx;
		// 动态计算位置：距离底部（输入框高度110 + 间距50 + 安全区）
		bottom: calc(180rpx + env(safe-area-inset-bottom) + var(--window-bottom)); 
		width: 106rpx;
		height: 106rpx;
		background: #1e293b; // 深色背景
		border-radius: 50%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		box-shadow: 0 12rpx 30rpx rgba(0, 0, 0, 0.2);
		z-index: 110;
		border: 4rpx solid #fff; // 白色外边框，增强层次感
		transition: all 0.2s;
		
		&:active { transform: scale(0.9); opacity: 0.9; }
		text { color: #fff; font-size: 20rpx; margin-top: 2rpx; font-weight: bold; }
	}

	.hero-card {
		margin-top: 40px;
		background: #1e293b; border-radius: 40rpx; padding: 50rpx 40rpx; color: #fff;
		box-shadow: 0 20rpx 40rpx rgba(30, 41, 59, 0.2);
		.stats-main {
			display: flex; justify-content: space-around;
			.item { text-align: center; .num { font-size: 56rpx; font-weight: 800; display: block; } .label { font-size: 22rpx; color: #94a3b8; margin-top: 8rpx; } }
			.divider { width: 1rpx; height: 60rpx; background: rgba(255,255,255,0.1); align-self: center; }
		}
		.progress-bar {
			height: 12rpx; background: rgba(255,255,255,0.1); border-radius: 20rpx; margin-top: 40rpx; overflow: hidden;
			.fill { height: 100%; background: #10b981; transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1); }
		}
	}

	.main-content {
		margin-top: 60rpx;
		.section-header { font-size: 32rpx; font-weight: 800; color: #1e293b; margin-bottom: 30rpx; }
		.empty-tip { text-align: center; color: #94a3b8; padding: 100rpx 0; font-size: 26rpx; }
		.food-card {
			background: #fff; padding: 30rpx; border-radius: 32rpx; margin-bottom: 24rpx;
			display: flex; justify-content: space-between; align-items: center;
			box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.02);
			.card-left {
				display: flex; align-items: center; gap: 24rpx;
				.meal-indicator { width: 8rpx; height: 50rpx; border-radius: 10rpx; }
				.m-0 { background: #f59e0b; } .m-1 { background: #10b981; } .m-2 { background: #3b82f6; } .m-3 { background: #8b5cf6; }
				.fname { font-size: 30rpx; font-weight: 700; color: #1e293b; display: block; } .ftime { font-size: 22rpx; color: #94a3b8; margin-top: 4rpx; }
			}
			.card-right { display: flex; align-items: center; gap: 24rpx; .fcal { font-weight: 800; color: #1e293b; font-size: 30rpx; } }
		}
	}

	.search-suggestions {
		background: #fff; border-radius: 32rpx; margin-bottom: 20rpx;
		max-height: 400rpx; overflow-y: auto; padding: 10rpx;
		box-shadow: 0 -10rpx 40rpx rgba(0,0,0,0.08);
		border: 1rpx solid #f1f5f9;
		.sug-item {
			display: flex; justify-content: space-between; align-items: center;
			padding: 24rpx 30rpx; border-bottom: 1rpx solid #f8fafc;
			&:last-child { border-bottom: none; }
			.s-left {
				.s-name { font-size: 28rpx; font-weight: 600; color: #1e293b; display: block; }
				.s-info { font-size: 22rpx; color: #94a3b8; }
			}
		}
	}

	.ai-fab-box {
		position: fixed; bottom: calc(30rpx + env(safe-area-inset-bottom) + var(--window-bottom)); 
		left: 30rpx; right: 30rpx; z-index: 100;
		.input-area {
			background: #fff; height: 110rpx; border-radius: 60rpx; display: flex; align-items: center;
			padding: 0 12rpx 0 36rpx; box-shadow: 0 20rpx 50rpx rgba(0,0,0,0.12);
			.main-input { flex: 1; margin: 0 20rpx; font-size: 28rpx; color: #1e293b; }
			.send-btn { width: 88rpx; height: 88rpx; background: #1e293b; border-radius: 50%; display: flex; align-items: center; justify-content: center; transition: all 0.2s; &:active { transform: scale(0.9); } }
		}
	}

	.result-panel {
		padding: 10rpx;
		.res-item { display: flex; align-items: center; margin-bottom: 40rpx; .label { width: 120rpx; color: #64748b; font-size: 26rpx; } flex: 1; }
		.meal-label { font-size: 26rpx; color: #64748b; margin-bottom: 24rpx; font-weight: 600; }
		.meal-select {
			display: flex; justify-content: space-between;
			view { 
				width: 22%; height: 74rpx; background: #f1f5f9; display: flex; align-items: center; justify-content: center; 
				border-radius: 16rpx; font-size: 26rpx; transition: all 0.2s;
				&.active { background: #10b981; color: #fff; font-weight: bold; box-shadow: 0 8rpx 20rpx rgba(16, 185, 129, 0.3); }
			}
		}
	}
}
</style>