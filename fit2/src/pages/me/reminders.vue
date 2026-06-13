<template>
	<view class="reminder-page">
		<view class="nav-bar">
			<view class="back-area" @click="goBack">
				<u-icon name="arrow-left" size="22px" color="#1e293b"></u-icon>
			</view>
			<text class="nav-title">健康提醒</text>
			<view class="placeholder"></view>
		</view>

		<scroll-view scroll-y class="content-scroll">
			<view class="header-section">
				<view class="title-row">
					<view>
						<text class="main-title">专注健康</text>
						<view class="sub-title">离开页面闹钟依然有效</view>
					</view>
					<view class="add-btn" @tap="openAddModal">
						<u-icon name="plus" size="20px" color="#ffffff"></u-icon>
					</view>
				</view>
			</view>

			<view class="reminder-container">
				<view v-for="(item, index) in reminderList" :key="item.id" 
					class="reminder-card"
					:class="{ 'card-off': !item.is_enabled }"
					@tap="openEditModal(index)"
					@longpress="confirmDelete(index)">
					
					<view class="card-body">
						<view class="info-left">
							<view class="type-tag">
								<view class="tag-dot"></view>
								<text class="reminder-type">{{ getTypeName(item.type) }}</text>
							</view>
							<view class="time-wrapper">
								<text class="time-text">{{ item.time }}</text>
								<text class="time-unit" v-if="item.is_enabled">ON</text>
							</view>
						</view>
						
						<view class="action-right" @tap.stop>
							<u-switch 
								v-model="item.is_enabled" 
								activeColor="#10b981" 
								inactiveColor="#cbd5e1"
								size="24"
								@change="handleToggle(item)"
							></u-switch>
						</view>
					</view>
					<view class="card-footer" v-if="item.is_enabled">
						<text class="next-tip">下次提醒：今天 {{ item.time }}</text>
					</view>
				</view>
			</view>
			<view class="bottom-placeholder"></view>
		</scroll-view>

		<u-modal
			:show="showModal"
			:title="isEdit ? '编辑提醒' : '新增提醒'"
			@confirm="submitForm"
			@cancel="showModal = false"
			showCancelButton
			confirmColor="#10b981"
		>
			<view class="modal-content">
				<view class="form-item">
					<text class="label">提醒名称</text>
					<u-input 
						v-model="tempItem.type" 
						placeholder="例如: 喝水、吃药" 
						border="bottom"
						clearable
					></u-input>
				</view>
				<view class="form-item" @tap="showPicker = true">
					<text class="label">提醒时间</text>
					<view class="time-select-row">
						<text class="selected-time">{{ tempItem.time }}</text>
						<u-icon name="arrow-right" size="14" color="#94a3b8"></u-icon>
					</view>
				</view>
			</view>
		</u-modal>

		<u-picker
			:show="showPicker"
			:columns="timeColumns"
			@confirm="onTimeConfirm"
			@cancel="showPicker = false"
			confirmColor="#10b981"
		></u-picker>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { request } from '@/utils/request.js';

const reminderList = ref([]);
const showModal = ref(false);
const showPicker = ref(false);
const isEdit = ref(false);
const isLongPress = ref(false); // 拦截长按冲突

const tempItem = ref({ id: null, type: '', time: '09:00:00', is_enabled: true });

const timeColumns = ref([
	Array.from({length: 24}, (_, i) => i < 10 ? '0' + i : '' + i),
	Array.from({length: 60}, (_, i) => i < 10 ? '0' + i : '' + i),
	Array.from({length: 60}, (_, i) => i < 10 ? '0' + i : '' + i)
]);

const timerHandlers = getApp().globalData?.timerHandlers || {};

const fetchReminders = async () => {
	const res = await request({ url: '/api/sys/reminders', method: 'GET' });
	if (res.code === 200) {
		reminderList.value = res.data.map(item => {
			let t = item.time || "09:00:00";
			if (t.split(':').length === 2) t += ':00';
			item.time = t;
			return item;
		});
		reminderList.value.forEach(item => { if(item.is_enabled) setSingleTask(item); });
	}
};

const setSingleTask = (item) => {
	if (timerHandlers[item.id]) clearTimeout(timerHandlers[item.id]);
	const [h, m, s] = item.time.split(':').map(Number);
	const now = new Date();
	let target = new Date();
	target.setHours(h, m, s, 0);
	if (target <= now) target.setDate(target.getDate() + 1);
	const delay = target.getTime() - now.getTime();
	
	timerHandlers[item.id] = setTimeout(() => {
		// #ifdef APP-PLUS
		plus.push.createMessage(`[跃动健康] ${getTypeName(item.type)}时间到了！`, "Health", { title: "健康提醒" });
		plus.device.vibrate(1000);
		// #endif
		handleToggle({ ...item, is_enabled: false });
	}, delay);
};

const openAddModal = () => {
	isEdit.value = false;
	tempItem.value = { id: null, type: '', time: '09:00:00', is_enabled: true };
	showModal.value = true;
};

const openEditModal = (index) => {
	if (isLongPress.value) return; // 拦截长按后的点击
	isEdit.value = true;
	tempItem.value = JSON.parse(JSON.stringify(reminderList.value[index]));
	showModal.value = true;
};

const onTimeConfirm = (e) => {
	tempItem.value.time = e.value.join(':');
	showPicker.value = false;
};

const submitForm = async () => {
	if (!tempItem.value.type.trim()) return uni.showToast({ title: '请输入名称', icon: 'none' });
	
	const res = await request({
		url: '/api/sys/reminders',
		method: 'PUT',
		data: tempItem.value
	});
	
	if (res.code === 200) {
		showModal.value = false;
		fetchReminders();
		uni.showToast({ title: '保存成功', icon: 'success' });
	}
};

const handleToggle = (item) => {
	request({
		url: '/api/sys/reminders',
		method: 'PUT',
		data: { ...item }
	}).then(res => {
		if (res.code === 200) {
			if (item.is_enabled) setSingleTask(item);
			else if (timerHandlers[item.id]) clearTimeout(timerHandlers[item.id]);
		}
	});
};

const confirmDelete = (index) => {
	isLongPress.value = true;
	const item = reminderList.value[index];
	uni.showModal({
		title: '删除提醒',
		content: `确定要删除“${getTypeName(item.type)}”吗？`,
		confirmColor: '#ef4444',
		success: async (res) => {
			if (res.confirm) {
				const delRes = await request({ url: `/api/sys/reminders/${item.id}`, method: 'DELETE' });
				if (delRes.code === 200) {
					reminderList.value.splice(index, 1);
					if (timerHandlers[item.id]) clearTimeout(timerHandlers[item.id]);
					uni.showToast({ title: '已删除' });
				}
			}
			// 延迟重置，防止触发点击
			setTimeout(() => { isLongPress.value = false; }, 300);
		},
		fail: () => { isLongPress.value = false; }
	});
};

const getTypeName = (type) => ({'drink':'准时补水', 'move':'起身活动', 'sleep':'准备就寝'}[type] || type);
const goBack = () => uni.navigateBack();

onMounted(() => fetchReminders());
</script>

<style lang="scss" scoped>
/* 样式保持不变，已在之前版本中优化 */
.reminder-page {
	background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
	min-height: 100vh;
	.nav-bar {
		padding: 100rpx 40rpx 30rpx;
		display: flex; align-items: center; justify-content: space-between;
		.nav-title { font-size: 32rpx; font-weight: 700; color: #1e293b; }
		.placeholder { width: 44rpx; }
	}
	.content-scroll { padding: 0 40rpx; box-sizing: border-box; }
	.header-section {
		padding: 40rpx 0 60rpx;
		.title-row { display: flex; justify-content: space-between; align-items: flex-start; }
		.main-title { font-size: 64rpx; font-weight: 800; color: #1e293b; }
		.sub-title { font-size: 26rpx; color: #64748b; margin-top: 12rpx; }
		.add-btn {
			width: 88rpx; height: 88rpx; background: #10b981; border-radius: 28rpx;
			display: flex; align-items: center; justify-content: center;
			box-shadow: 0 10rpx 20rpx rgba(16, 185, 129, 0.2);
		}
	}
	.reminder-card {
		background: #ffffff; border-radius: 48rpx; padding: 40rpx; margin-bottom: 40rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.03); transition: all 0.3s;
		&.card-off { opacity: 0.6; .tag-dot { background: #94a3b8; } }
		.card-body { display: flex; justify-content: space-between; align-items: center; }
		.type-tag {
			display: flex; align-items: center; margin-bottom: 10rpx;
			.tag-dot { width: 12rpx; height: 12rpx; border-radius: 50%; background: #10b981; margin-right: 12rpx; }
			.reminder-type { font-size: 28rpx; font-weight: 600; color: #64748b; }
		}
		.time-text { font-size: 80rpx; font-weight: 800; color: #1e293b; font-family: monospace; }
		.time-unit { font-size: 20rpx; color: #10b981; margin-left: 10rpx; background: #ecfdf5; padding: 4rpx 10rpx; border-radius: 8rpx; }
		.card-footer { margin-top: 20rpx; padding-top: 20rpx; border-top: 2rpx solid #f1f5f9; .next-tip { font-size: 22rpx; color: #94a3b8; } }
	}
	.modal-content {
		padding: 20rpx 0;
		.form-item {
			margin-bottom: 30rpx;
			.label { font-size: 24rpx; color: #94a3b8; margin-bottom: 10rpx; display: block; }
			.time-select-row {
				padding: 20rpx 0; border-bottom: 1rpx solid #eee;
				display: flex; justify-content: space-between; align-items: center;
				.selected-time { font-size: 44rpx; font-weight: 800; color: #1e293b; }
			}
		}
	}
	.bottom-placeholder { height: 100rpx; }
}
</style>