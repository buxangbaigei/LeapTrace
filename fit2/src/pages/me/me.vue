<template>
	<view class="me-container">
		<view class="user-card" @click="gouserEdit">
			<u-avatar :src="fullAvatarUrl" size="60px" border="2px solid #10b981"></u-avatar>
			<view class="user-info">
				<text class="nickname">{{ userInfo.nickname || '跃动行者' }}</text>
				<text class="tag">ID: {{ userInfo.username }} · 轨迹探索中</text>
			</view>
			<u-icon name="arrow-right" color="#cbd5e1"></u-icon>
		</view>

		<view class="stats-grid">
			<view class="stat-card">
				<text class="val">{{ userInfo.height || '--' }}<text class="unit">cm</text></text>
				<text class="lab">身高基准</text>
			</view>
			<view class="stat-card active">
				<text class="val-white">{{ userInfo.weight || '--' }}<text class="unit">kg</text></text>
				<text class="lab-white">实时体重</text>
			</view>
			<view class="stat-card">
				<text class="val">{{ userInfo.target_weight || '--' }}<text class="unit">kg</text></text>
				<text class="lab">目标体重</text>
			</view>
		</view>

		<view class="menu-list">
			<u-cell-group :border="false">
				<u-cell icon="edit-pen" title="身体指标追踪" isLink @click="openProfileModal"
					:iconStyle="{ fontSize: '44rpx', color: '#10b981', marginRight: '20rpx' }"></u-cell>
				<u-cell icon="map" title="设定轨迹目标" isLink @click="showGoalModal = true"
					:iconStyle="{ fontSize: '44rpx', color: '#3b82f6', marginRight: '20rpx' }"></u-cell>
				<u-cell icon="question-circle" title="关于 LeapTrace" isLink
					:iconStyle="{ fontSize: '44rpx', color: '#94a3b8', marginRight: '20rpx' }"></u-cell>
			</u-cell-group>
		</view>

		<view class="logout-btn">
			<u-button text="暂离轨迹" :plain="true" color="#cbd5e1" shape="circle" @click="handleLogout"></u-button>
		</view>

		<u-modal :show="showProfileModal" title="更新身体指标" @confirm="updateProfile" @cancel="showProfileModal=false" showCancelButton confirmColor="#10b981">
			<view class="modal-content">
				<view class="input-item">
					<view class="input-label">
						<text class="label-text">身高</text>
						<text class="label-desc">单位：cm</text>
					</view>
					<u--input placeholder="请输入身高" type="number" v-model="profileForm.height" border="bottom" :clearable="true">
						<template #suffix><text class="suffix-text">cm</text></template>
					</u--input>
				</view>
				
				<view class="input-item">
					<view class="input-label">
						<text class="label-text">体重</text>
						<text class="label-desc">单位：kg</text>
					</view>
					<u--input placeholder="请输入体重" type="number" v-model="profileForm.weight" border="bottom" :clearable="true">
						<template #suffix><text class="suffix-text">kg</text></template>
					</u--input>
				</view>
				
				<view class="bmi-preview" v-if="profileForm.height && profileForm.weight">
					<view class="bmi-info">
						<text class="bmi-label">BMI指数：</text>
						<text class="bmi-value">{{ calculateBMI() }}</text>
					</view>
					<view class="bmi-status" :class="getBMIStatusClass()">{{ getBMIStatusText() }}</view>
				</view>
			</view>
		</u-modal>
		
		<u-modal :show="showGoalModal" title="设定健康目标" @confirm="updateGoal" @cancel="showGoalModal=false" showCancelButton confirmColor="#3b82f6">
			<view class="modal-content">
				<view class="input-item">
					<view class="input-label">
						<text class="label-text">每日目标步数</text>
						<text class="label-desc">建议：6000-10000步</text>
					</view>
					<u--input placeholder="请输入步数" type="number" v-model="goalForm.target_steps" border="bottom">
						<template #suffix><text class="suffix-text">步</text></template>
					</u--input>
				</view>
				
				<view class="input-item">
					<view class="input-label">
						<text class="label-text">消耗热量目标</text>
						<text class="label-desc">建议：1800-2500 kcal</text>
					</view>
					<u--input placeholder="请输入热量" type="number" v-model="goalForm.target_calories" border="bottom">
						<template #suffix><text class="suffix-text">kcal</text></template>
					</u--input>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script setup>
	import { ref, reactive, onMounted, computed } from 'vue';
	import { request, BASE_URL } from '@/utils/request.js';
import { onShow, onUnload } from '@dcloudio/uni-app';
	const userInfo = ref({});
	const showProfileModal = ref(false);
	const showGoalModal = ref(false);

	// 表单数据
	const profileForm = reactive({ height: '', weight: '' });
	const goalForm = reactive({ target_steps: '', target_calories: '' });

	// 计算头像完整地址
	const fullAvatarUrl = computed(() => {
		if (!userInfo.value.avatar) return '/static/avatar-default.png';
		return userInfo.value.avatar.startsWith('http') ? userInfo.value.avatar : BASE_URL + userInfo.value.avatar;
	});

	// 获取用户信息
	const getUserInfo = async () => {
		try {
			const res = await request({ url: '/api/user/info', method: 'GET' });
			if (res.code === 200) {
				userInfo.value = res.data;
				// 回显数据
				profileForm.height = res.data.height || '';
				profileForm.weight = res.data.weight || '';
				goalForm.target_steps = res.data.daily_goal_steps || '';
				goalForm.target_calories = res.data.daily_goal_calories || '';
			}
		} catch (err) {
			console.error('获取用户信息失败', err);
		}
	};

	const gouserEdit = () => uni.navigateTo({ url: '../userEdit/userEdit' });

	// 打开指标弹窗
	const openProfileModal = () => {
		profileForm.height = userInfo.value.height || '';
		profileForm.weight = userInfo.value.weight || '';
		showProfileModal.value = true;
	};

	// --- 核心逻辑：更新身体指标 (已移除冗余弹出框) ---
	const updateProfile = async () => {
		const h = parseFloat(profileForm.height);
		const w = parseFloat(profileForm.weight);

		if (!h || !w) {
			uni.showToast({ title: '请完整填写身高体重', icon: 'none' });
			return;
		}

		try {
			// 直接提交数据，无需二次确认
			const res = await request({
				url: '/api/user/profile',
				method: 'PUT', // 对齐你要求的 POST 接口
				data: {
					height: h,
					weight: w,
					nickname: userInfo.value.nickname,
					gender: userInfo.value.gender
				}
			});

			if (res.code === 200) {
				uni.showToast({ title: '更新成功', icon: 'success' });
				showProfileModal.value = false;
				getUserInfo();
			}
		} catch (error) {
			uni.showToast({ title: '保存失败', icon: 'none' });
		}
	};

	// 更新目标
	const updateGoal = async () => {
		if (!goalForm.target_steps || !goalForm.target_calories) {
			uni.showToast({ title: '请填写完整目标', icon: 'none' });
			return;
		}
		try {
			const res = await request({
				url: '/api/user/goal',
				method: 'POST',
				data: {
					target_steps: parseInt(goalForm.target_steps),
					target_calories: parseInt(goalForm.target_calories)
				}
			});
			if (res.code === 200) {
				uni.showToast({ title: '目标已更新', icon: 'success' });
				showGoalModal.value = false;
				getUserInfo();
			}
		} catch (err) {
			uni.showToast({ title: '设置失败', icon: 'none' });
		}
	};

	// 计算 BMI 相关函数
	const calculateBMI = () => {
		const h = parseFloat(profileForm.height) / 100;
		const w = parseFloat(profileForm.weight);
		return (h && w) ? (w / (h * h)).toFixed(1) : '--';
	};

	const getBMIStatusText = () => {
		const bmi = parseFloat(calculateBMI());
		if (isNaN(bmi)) return '--';
		if (bmi < 18.5) return '偏瘦';
		if (bmi < 24) return '标准';
		if (bmi < 28) return '偏胖';
		return '肥胖';
	};

	const getBMIStatusClass = () => {
		const bmi = parseFloat(calculateBMI());
		if (bmi < 18.5) return 'underweight';
		if (bmi < 24) return 'normal';
		if (bmi < 28) return 'overweight';
		return 'obese';
	};

	const handleLogout = () => {
		uni.showModal({
			title: '提示',
			content: '确定要退出登录吗？',
			success: (res) => {
				if (res.confirm) {
					uni.clearStorageSync();
					uni.reLaunch({ url: '/pages/login/login' });
				}
			}
		});
	};

onMounted(() => {
    getUserInfo(); // 初始加载数据
    
    // 开启监听
    uni.$on('refreshUserInfo', () => {
        console.log('检测到资料更新，正在重新获取...');
        getUserInfo(); 
    });
});

// 页面销毁前必须关闭监听
onUnload(() => {
    uni.$off('refreshUserInfo');
});
</script>

<style lang="scss" scoped>
	.me-container {
		min-height: 100vh; background-color: #F8FAFC; padding: 40rpx;
		.user-card {
			background: #fff; padding: 40rpx; border-radius: 40rpx; display: flex; align-items: center; margin-top: 20rpx;
			.user-info { flex: 1; margin-left: 24rpx; .nickname { font-size: 38rpx; font-weight: bold; } .tag { font-size: 24rpx; color: #94a3b8; margin-top: 8rpx; } }
		}
		.stats-grid {
			display: flex; align-items: flex-end; justify-content: space-between; margin: 40rpx 0;
			.stat-card {
				flex: 1; background: #fff; margin: 0 10rpx; padding: 30rpx 20rpx; border-radius: 32rpx; text-align: center;
				&.active { background: linear-gradient(135deg, #10b981, #059669); padding: 40rpx 20rpx; }
				.val { font-size: 34rpx; font-weight: bold; color: #10b981; } .val-white { font-size: 34rpx; font-weight: bold; color: #fff; }
				.lab { font-size: 22rpx; color: #94a3b8; } .lab-white { font-size: 22rpx; color: #fff; }
			}
		}
		.menu-list { background: #fff; border-radius: 30rpx; overflow: hidden; }
		.logout-btn { margin-top: 60rpx; }
	}
	.modal-content { padding: 30rpx; .input-item { margin-bottom: 30rpx; .input-label { display: flex; justify-content: space-between; margin-bottom: 10rpx; .label-text { font-weight: bold; } .label-desc { font-size: 22rpx; color: #999; } } } }
	.bmi-preview { display: flex; justify-content: space-between; background: #f8f9fa; padding: 20rpx; border-radius: 12rpx; margin-top: 20rpx; .bmi-value { color: #10b981; font-weight: bold; } .bmi-status { font-size: 24rpx; &.normal { color: #52c41a; } } }
	.modal-content {
		padding: 20rpx 30rpx;
	}
	
	.input-item {
		margin-bottom: 40rpx;
	}
	
	.input-label {
		margin-bottom: 16rpx;
		display: flex;
		justify-content: space-between;
		align-items: baseline;
	}
	
	.label-left {
		display: flex;
		align-items: center;
		gap: 12rpx;
	}
	
	.label-text {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
	}
	
	.label-desc {
		font-size: 24rpx;
		color: #999;
	}
	
	.suffix-text {
		color: #999;
		font-size: 28rpx;
	}
	
	.bmi-preview {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		padding: 16rpx 24rpx;
		background-color: #f8f9fa;
		border-radius: 16rpx;
	}
	
	.bmi-info {
		display: flex;
		align-items: baseline;
		gap: 8rpx;
	}
	
	.bmi-label {
		font-size: 26rpx;
		color: #666;
	}
	
	.bmi-value {
		font-size: 32rpx;
		font-weight: bold;
		color: #10b981;
	}
	
	.bmi-status {
		font-size: 26rpx;
		padding: 4rpx 16rpx;
		border-radius: 20rpx;
		font-weight: 500;
	}
	
	.bmi-status.underweight {
		color: #ffa940;
		background-color: #fff7e6;
	}
	
	.bmi-status.normal {
		color: #52c41a;
		background-color: #f6ffed;
	}
	
	.bmi-status.overweight {
		color: #ff7a45;
		background-color: #fff2e8;
	}
	
	.bmi-status.obese {
		color: #f5222d;
		background-color: #fff1f0;
	}
	
	.health-advice {
		display: flex;
		align-items: center;
		padding: 20rpx;
		background: linear-gradient(135deg, #e8f5e9, #fff);
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		border: 1rpx solid #c8e6c9;
	}
	
	.advice-text {
		font-size: 24rpx;
		color: #10b981;
		margin-left: 12rpx;
		flex: 1;
		line-height: 1.4;
	}
	
	.tips {
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 20rpx;
		background-color: #f5f5f5;
		border-radius: 12rpx;
		margin-top: 20rpx;
	}
	
	.tips-text {
		font-size: 24rpx;
		color: #666;
		margin-left: 10rpx;
	}
	
	.current-values {
		margin-top: 20rpx;
		padding-top: 20rpx;
		border-top: 1rpx solid #eee;
		text-align: center;
	}
	
	.current-title {
		font-size: 26rpx;
		color: #666;
	}
	
	.current-value {
		font-size: 28rpx;
		color: #10b981;
		font-weight: 500;
		margin-left: 10rpx;
	}
	
	.ideal-weight {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 20rpx;
		padding: 16rpx;
		background-color: #fffbe6;
		border-radius: 12rpx;
	}
	
	.ideal-text {
		font-size: 24rpx;
		color: #faad14;
		margin-left: 10rpx;
	}
	
	.me-container {
		min-height: 100vh; background-color: #F8FAFC; padding: 40rpx;
		.user-card {
			background: #fff; padding: 40rpx; border-radius: 40rpx; display: flex; align-items: center; margin-top: 60rpx; box-shadow: 0 10rpx 40rpx rgba(0,0,0,0.02);
			.user-info { flex: 1; margin-left: 24rpx; .nickname { font-size: 38rpx; font-weight: bold; color: #1e293b; display: block; } .tag { font-size: 24rpx; color: #94a3b8; margin-top: 8rpx; display: block; } }
		}
		.stats-grid {
			display: flex; align-items: flex-end; justify-content: space-between; margin-top: 40rpx;
			.stat-card {
				flex: 1; background: #fff; margin: 0 10rpx; padding: 30rpx 20rpx; border-radius: 32rpx; text-align: center; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.02);
				&.active { background: linear-gradient(135deg, #10b981, #059669); padding: 45rpx 20rpx; transform: translateY(-10rpx); }
				.val { font-size: 34rpx; font-weight: 800; color: #10b981; } .val-white { font-size: 38rpx; font-weight: 800; color: #fff; }
				.unit { font-size: 20rpx; margin-left: 4rpx; } .lab { font-size: 22rpx; color: #94a3b8; margin-top: 8rpx; display: block; } .lab-white { font-size: 22rpx; color: rgba(255,255,255,0.8); margin-top: 8rpx; display: block; }
			}
		}
		.menu-list { background: #fff; margin-top: 40rpx; border-radius: 40rpx; overflow: hidden; }
		.logout-btn { margin-top: 60rpx; padding: 0 40rpx; }
	}
</style>