import uviewPlus from 'uview-plus'
import {
	createSSRApp
} from "vue";


import App from "./App.vue";
uni.$u.config.unit = 'rpx'
export function createApp() {
	const app = createSSRApp(App);
	app.use(uviewPlus)
	return {
		app,
	};
}

// #ifndef MP
// 处理 wx.connectSocket promisify 兼容问题，强制返回 SocketTask
uni.connectSocket = (function (connectSocket) {
	return function (options) {
		options.success = options.success || function () {}
		return connectSocket.call(this, options)
	}
})(uni.connectSocket)
// #endif
