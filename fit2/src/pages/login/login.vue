<template>
	<view class="login-container">
		<view class="bg-decoration"></view>
		<view class="header">
			<text class="title">跃动轨迹</text>
			<text class="subtitle">LeapTrace · 智绘你的每一次跃动</text>
		</view>

		<view class="form-area">
			<u--input placeholder="请输入用户名" prefixIcon="account" shape="circle"
				:prefixIconStyle="{ fontSize: '44rpx', color: '#10b981' }" v-model="loginForm.username"
				customStyle="background: #fff; margin-bottom: 40rpx; padding: 24rpx; border: none; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.03)">
			</u--input>

			<u--input placeholder="请输入密码" prefixIcon="lock" type="password" shape="circle"
				:prefixIconStyle="{ fontSize: '44rpx', color: '#10b981' }" v-model="loginForm.password"
				customStyle="background: #fff; margin-bottom: 80rpx; padding: 24rpx; border: none; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.03)">
			</u--input>

			<u-button text="开启轨迹" color="#10b981" shape="circle" @click="handleLogin"
				:loading="loading" customStyle="height: 100rpx; font-weight: bold; font-size: 32rpx;"></u-button>

			<view class="footer-links">
				<text @click="goToRegister">新成员？<text class="link">加入跃动之旅</text></text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, reactive } from 'vue';
	import { request } from '@/utils/request.js';

	const loginForm = reactive({ username: 'test123', password: '123456' });
	const loading = ref(false);

	const handleLogin = async () => {
		if (!loginForm.username || !loginForm.password) {
			uni.showToast({ title: '请完善信息', icon: 'none' });
			return;
		}
		loading.value = true;
		try {
			const res = await request({ url: '/api/auth/login', method: 'POST', data: loginForm });
			if (res.code === 200) {
				uni.setStorageSync('token', res.data.token);
				uni.showToast({ title: '数据同步中...' });
				setTimeout(() => uni.switchTab({ url: '/pages/home/home' }), 1000);
			} else {
				uni.showToast({ title: res.message || '识别失败', icon: 'none' });
			}
		} catch (err) { console.error(err) } finally { loading.value = false; }
	};

	const goToRegister = () => uni.navigateTo({ url: '/pages/register/register' });
</script>

<style lang="scss" scoped>
	.login-container {
		min-height: 100vh; background-color: #f8fafc; padding: 120rpx 60rpx; position: relative;
		.bg-decoration {
			position: absolute; top: -100rpx; right: -100rpx; width: 400rpx; height: 400rpx;
			background: radial-gradient(circle, rgba(16, 185, 129, 0.08) 0%, transparent 70%);
		}
		.header {
			margin-bottom: 100rpx;
			.title { font-size: 64rpx; font-weight: 800; color: #1e293b; letter-spacing: 4rpx; display: block; }
			.subtitle { font-size: 28rpx; color: #94a3b8; margin-top: 20rpx; display: block; }
		}
		.footer-links {
			margin-top: 50rpx; text-align: center; font-size: 26rpx; color: #64748b;
			.link { color: #10b981; font-weight: bold; margin-left: 10rpx; }
		}
	}
</style>