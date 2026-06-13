<template>
	<view class="register-container">
		<view class="header">
			<u-icon name="arrow-left" size="24px" @click="goBack"></u-icon>
			<view class="title-group">
				<text class="title">加入跃动轨迹</text>
				<text class="subtitle">创建你的数字健康档案</text>
			</view>
		</view>

		<view class="form-card">
			<view class="input-box">
				<text class="label">用户名</text>
				<u--input placeholder="设置你的唯一标识" v-model="regForm.username" border="bottom" clearable></u--input>
			</view>
			<view class="input-box">
				<text class="label">昵称</text>
				<u--input placeholder="我们该如何称呼你" v-model="regForm.nickname" border="bottom" clearable></u--input>
			</view>
			<view class="input-box">
				<text class="label">设置密码</text>
				<u--input placeholder="请输入密码" type="password" v-model="regForm.password" border="bottom"></u--input>
			</view>
			<view class="input-box">
				<text class="label">确认密码</text>
				<u--input placeholder="请再次核对密码" type="password" v-model="confirmPwd" border="bottom"></u--input>
			</view>

			<u-button text="建立档案" color="linear-gradient(to right, #10b981, #059669)" shape="circle"
				:loading="loading" @click="handleRegister" customStyle="margin-top: 60rpx; height: 96rpx;"></u-button>

			<view class="footer" @click="goBack">
				<text>已有轨迹账号？<text class="active">立即同步</text></text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { reactive, ref } from 'vue';
	import { request } from '@/utils/request.js';

	const regForm = reactive({ username: '', password: '', nickname: '' });
	const confirmPwd = ref('');
	const loading = ref(false);

	const handleRegister = async () => {
		if (!regForm.username || !regForm.password) { uni.showToast({ title: '必填项不能为空', icon: 'none' }); return; }
		if (regForm.password !== confirmPwd.value) { uni.showToast({ title: '密码核对不一致', icon: 'none' }); return; }
		loading.value = true;
		try {
			const res = await request({ url: '/api/auth/register', method: 'POST', data: regForm });
			if (res.code === 200) {
				uni.showToast({ title: '档案建立成功' });
				setTimeout(() => uni.navigateTo({ url: '/pages/login/login' }), 1500);
			} else {
				uni.showToast({ title: res.message || '创建失败', icon: 'none' });
			}
		} catch (err) { console.error(err) } finally { loading.value = false; }
	};
	const goBack = () => uni.navigateBack();
</script>

<style lang="scss" scoped>
	.register-container {
		min-height: 100vh; background-color: #f8fafc; padding: 40rpx;
		.header {
			margin-top: 40rpx;
			.title-group { margin-top: 40rpx; .title { font-size: 52rpx; font-weight: 800; color: #1e293b; } .subtitle { font-size: 28rpx; color: #94a3b8; margin-top: 10rpx; display: block; } }
		}
		.form-card {
			margin-top: 60rpx; background: #fff; padding: 60rpx 40rpx; border-radius: 48rpx; box-shadow: 0 10rpx 40rpx rgba(0,0,0,0.03);
			.input-box { margin-bottom: 40rpx; .label { font-size: 24rpx; color: #94a3b8; margin-bottom: 10rpx; display: block; } }
			.footer { margin-top: 40rpx; text-align: center; font-size: 26rpx; color: #64748b; .active { color: #10b981; font-weight: bold; margin-left: 10rpx; } }
		}
	}
</style>