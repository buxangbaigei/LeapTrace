/**
 * health.js - 跨平台健康数据获取工具
 */

// --- iOS 原生桥接封装 ---
const HKHealthStore = uni.getSystemInfoSync().platform === 'ios' ? plus.ios.importClass("HKHealthStore") : null;

/**
 * 请求健康权限 (iOS & Android)
 */
export const requestHealthPermission = () => {
	return new Promise((resolve, reject) => {
		const platform = uni.getSystemInfoSync().platform;

		// --- iOS 权限请求 ---
		if (platform === 'ios') {
			try {
				const healthStore = new HKHealthStore();
				const HKObjectType = plus.ios.importClass("HKObjectType");
				// 获取步数类型
				const stepType = HKObjectType.quantityTypeForIdentifier("HKQuantityTypeIdentifierStepCount");
				const NSSet = plus.ios.importClass("NSSet");
				const readTypes = NSSet.setWithObject(stepType);

				healthStore.requestAuthorizationToShareTypesReadTypes(null, readTypes, (success, error) => {
					if (success) resolve(true);
					else reject("iOS 授权失败: " + error);
				});
			} catch (e) {
				reject("iOS 原生调用异常，请检查是否为自定义基座");
			}
		} 
		// --- Android 权限请求 ---
		else if (platform === 'android') {
			plus.android.requestPermissions(['android.permission.ACTIVITY_RECOGNITION'], (res) => {
				if (res.granted.length > 0) resolve(true);
				else reject("Android 运动权限被拒绝");
			}, (err) => {
				reject("Android 权限请求异常");
			});
		}
	});
};

/**
 * 获取今日步数
 */
export const getTodaySteps = () => {
	return new Promise((resolve, reject) => {
		const platform = uni.getSystemInfoSync().platform;

		// --- iOS 获取逻辑 ---
		if (platform === 'ios') {
			try {
				const healthStore = new HKHealthStore();
				const HKStatisticsQuery = plus.ios.importClass("HKStatisticsQuery");
				const HKUnit = plus.ios.importClass("HKUnit");
				const NSCalendar = plus.ios.importClass("NSCalendar");
				const NSDate = plus.ios.importClass("NSDate");
				const HKObjectType = plus.ios.importClass("HKObjectType");

				const stepType = HKObjectType.quantityTypeForIdentifier("HKQuantityTypeIdentifierStepCount");
				
				// 获取今日凌晨时间
				const calendar = NSCalendar.currentCalendar();
				const now = new NSDate();
				const components = calendar.componentsFromDate(plus.ios.num64(28), now); 
				const startDate = calendar.dateFromComponents(components);

				const query = new HKStatisticsQuery();
				query.initWithQuantityTypeOptionsPredicate(stepType, 1, null, (q, result, error) => {
					if (result) {
						const sum = result.sumQuantity();
						const steps = sum ? sum.doubleValueForUnit(HKUnit.countUnit()) : 0;
						resolve(Math.floor(steps));
					} else {
						reject("iOS 读取步数失败");
					}
				});
				healthStore.executeQuery(query);
			} catch (e) {
				reject("iOS 读取异常");
			}
		} 
		// --- Android 获取逻辑 ---
		else if (platform === 'android') {
			uni.startStepCount({
				success: () => {
					// 监听一次即返回当前值（Android通常返回自开机起的步数，需结合业务处理）
					uni.onStepCountChange((res) => {
						resolve(res.steps);
						uni.stopStepCount(); // 拿到后可以停止监听
					});
				},
				fail: (err) => reject("Android 计步器启动失败")
			});
		}
	});
};