<script>
// App.vue
import { startStepCountingUpdates } from '@/utils/sensor-step.js';

export default {
    onLaunch: function() {
        console.log('App Launch - 正在恢复步数服务');
        // 启动计步服务
        startStepCountingUpdates({
            handler: (steps) => {
                // 可以在这里把步数存入全局状态（如 Vuex 或 Pinia）
                console.log('计步服务已恢复，当前步数:', steps);
            }
        });
		// 1. 获取本地存储的 Token
		        const token = uni.getStorageSync('token');
		        
		        // 2. 判断是否存在 Token
		        if (token) {
		            // 如果有 Token，说明登录过，直接跳转到首页（避开登录页）
		            // 注意：如果你在 pages.json 里把登录页设为了第一个页面，这里需要跳转
		            uni.switchTab({
		                url: '/pages/home/home' 
		            });
		        } else {
		            // 如果没有 Token，才跳转到登录页
		            uni.reLaunch({
		                url: '/pages/login/login'
		            });
		        }
    },
    onShow: function() {
        console.log('App Show');
    },
    onHide: function() {
        console.log('App Hide');
    }
}
</script>

<style lang="scss">
@import "uview-plus/index.scss";
/*每个页面公共css */
</style>
