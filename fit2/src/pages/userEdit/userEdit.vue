<template>
	<view class="edit-container">
		<view class="avatar-section">
			<view class="avatar-title">个人头像</view>
			<view class="avatar-wrapper" @click="uploadAvatarAction">
				<u-avatar :src="avatarPreview" size="80px" border="2px solid #10b981"></u-avatar>
				<view class="avatar-edit-icon">
					<u-icon name="camera" size="32" color="#fff"></u-icon>
				</view>
			</view>
			<text class="avatar-tip">点击更换头像</text>
		</view>

		<view class="form-section">
			<u-form :model="formData" labelWidth="120" labelAlign="left">
				<u-form-item label="昵称" borderBottom>
					<u--input 
						v-model="formData.nickname" 
						placeholder="请输入昵称"
						:clearable="true"
						border="none">
					</u--input>
				</u-form-item>
				
				<u-form-item label="性别" borderBottom>
					<view class="gender-group">
						<view class="gender-option" @click="formData.gender = 1">
							<u-icon :name="formData.gender === 1 ? 'checkbox-mark' : ''" 
									size="32" 
									:color="formData.gender === 1 ? '#10b981' : '#cbd5e1'"></u-icon>
							<text :class="{active: formData.gender === 1}">男</text>
						</view>
						<view class="gender-option" @click="formData.gender = 2">
							<u-icon :name="formData.gender === 2 ? 'checkbox-mark' : ''" 
									size="32" 
									:color="formData.gender === 2 ? '#10b981' : '#cbd5e1'"></u-icon>
							<text :class="{active: formData.gender === 2}">女</text>
						</view>
						<view class="gender-option" @click="formData.gender = 0">
							<u-icon :name="formData.gender === 0 ? 'checkbox-mark' : ''" 
									size="32" 
									:color="formData.gender === 0 ? '#10b981' : '#cbd5e1'"></u-icon>
							<text :class="{active: formData.gender === 0}">保密</text>
						</view>
					</view>
				</u-form-item>
				
				<u-form-item label="年龄" borderBottom>
					<u--input 
						v-model="formData.age" 
						placeholder="请输入年龄"
						type="number"
						border="none">
						<template #suffix>
							<text class="suffix-text">岁</text>
						</template>
					</u--input>
				</u-form-item>
				
				<view v-if="showMore">
					<u-form-item label="身高" borderBottom>
						<u--input 
							v-model="formData.height" 
							placeholder="身高(cm)"
							type="digit"
							border="none">
							<template #suffix>
								<text class="suffix-text">cm</text>
							</template>
						</u--input>
					</u-form-item>
					
					<u-form-item label="体重" borderBottom>
						<u--input 
							v-model="formData.weight" 
							placeholder="体重(kg)"
							type="digit"
							border="none">
							<template #suffix>
								<text class="suffix-text">kg</text>
							</template>
						</u--input>
					</u-form-item>
				</view>
			</u-form>
			
			<view class="more-toggle" @click="showMore = !showMore">
				<text>{{ showMore ? '收起更多信息' : '展开更多信息' }}</text>
				<u-icon :name="showMore ? 'arrow-up' : 'arrow-down'" size="28" color="#10b981"></u-icon>
			</view>
		</view>

		<view class="button-section">
			<u-button 
				type="primary" 
				text="保存修改" 
				color="#10b981"
				:loading="saving" 
				@click="handleSave">
			</u-button>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { request, BASE_URL } from '@/utils/request.js'; // 统一从工具类导入

// 接口地址配置
const API_PROFILE = '/api/user/profile';
const API_AVATAR = '/api/user/avatar';   
const API_INFO = '/api/user/info';       

const saving = ref(false);
const showMore = ref(false);
const avatarPreview = ref('/static/avatar-default.png'); 

const formData = reactive({
	nickname: '',
	gender: 0,
	age: '',
	height: '',
	weight: ''
});

// 1. 使用封装好的 request 获取用户信息
const getUserInfo = async () => {
	try {
		const res = await request({
			url: API_INFO,
			method: 'GET'
		});
		
		if (res.code === 200) {
			const d = res.data;
			formData.nickname = d.nickname || '';
			formData.gender = d.gender ?? 0;
			formData.age = d.age || '';
			formData.height = d.height || '';
			formData.weight = d.weight || '';
			if (d.avatar) {
				// 拼接完整路径并加时间戳破除缓存
				const fullPath = d.avatar.startsWith('http') ? d.avatar : BASE_URL + d.avatar;
				avatarPreview.value = fullPath + '?t=' + Date.now();
			}
		}
	} catch (err) {
		console.error('获取个人信息失败', err);
	}
};

// 2. 上传头像（uploadFile 无法直接用 request 封装，但要保持逻辑一致）
const uploadAvatarAction = () => {
	uni.chooseImage({
		count: 1,
		sizeType: ['compressed'],
		success: (res) => {
			const tempFilePath = res.tempFilePaths[0];
			const token = uni.getStorageSync('token');
			
			// 立即预览本地图片，提升用户感知速度
			avatarPreview.value = tempFilePath;
			
			uni.showLoading({ title: '正在同步头像...', mask: true });
			
			uni.uploadFile({
				url: BASE_URL + API_AVATAR,
				filePath: tempFilePath,
				name: 'avatar', 
				header: { 'Authorization': token ? `Bearer ${token}` : '' },
				success: (uploadRes) => {
					const result = JSON.parse(uploadRes.data);
					if (result.code === 200) {
						// 更新为服务器正式路径
						const serverPath = result.data.startsWith('http') ? result.data : BASE_URL + result.data;
						avatarPreview.value = serverPath + '?t=' + Date.now();
						uni.showToast({ title: '头像上传成功', icon: 'success' });
					} else {
						uni.showToast({ title: result.message || '上传失败', icon: 'none' });
						getUserInfo(); // 回滚到旧头像
					}
				},
				fail: () => {
					uni.showToast({ title: '服务器连接异常', icon: 'none' });
					getUserInfo();
				},
				complete: () => uni.hideLoading()
			});
		}
	});
};

// 3. 提交资料修改
const handleSave = async () => {
	if (!formData.nickname.trim()) {
		return uni.showToast({ title: '给自己起个好听的昵称吧', icon: 'none' });
	}
	
	saving.value = true;
	try {
		const res = await request({
			url: API_PROFILE,
			method: 'PUT',
			data: {
				nickname: formData.nickname,
				gender: Number(formData.gender),
				age: formData.age ? Number(formData.age) : null,
				height: formData.height ? Number(formData.height) : null,
				weight: formData.weight ? Number(formData.weight) : null
			}
		});
		
		if (res.code === 200) {
			uni.showToast({ title: '资料更新成功', icon: 'success' });
			
			// 发出全局通知，通知“我的”页面更新数据
			uni.$emit('refreshUserInfo'); 
			
			setTimeout(() => {
				uni.navigateBack();
			}, 1000);
		} else {
			uni.showToast({ title: res.message || '保存失败', icon: 'none' });
		}
	} catch (err) {
		console.error('保存请求异常', err);
	} finally {
		saving.value = false;
	}
};

onMounted(() => {
	getUserInfo();
});
</script>

<style lang="scss" scoped>
/* 样式部分保持不变，已支持 scss 变量和 flex 布局 */
.edit-container {
	min-height: 100vh;
	background-color: #F8FAFC;
	padding: 30rpx;

	.avatar-section {
		background: #fff;
		border-radius: 24rpx;
		padding: 50rpx 0;
		margin-bottom: 24rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.avatar-wrapper {
			position: relative;
			margin: 20rpx 0;
			
			.avatar-edit-icon {
				position: absolute;
				bottom: 0;
				right: 0;
				background: #10b981;
				border-radius: 50%;
				padding: 8rpx;
				border: 4rpx solid #fff;
				line-height: 1;
			}
		}
		.avatar-tip { font-size: 24rpx; color: #94a3b8; }
	}

	.form-section {
		background: #fff;
		border-radius: 24rpx;
		padding: 0 30rpx;
		
		.gender-group {
			display: flex;
			gap: 30rpx;
			.gender-option {
				display: flex;
				align-items: center;
				gap: 8rpx;
				text { font-size: 28rpx; color: #64748b; }
				.active { color: #10b981; font-weight: bold; }
			}
		}
		
		.suffix-text { color: #94a3b8; font-size: 26rpx; }
		
		.more-toggle {
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 30rpx;
			color: #10b981;
			font-size: 26rpx;
		}
	}

	.button-section {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}
}
</style>