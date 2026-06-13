// export const BASE_URL = 'http://106.52.8.244:9900'; 
// export const BASE_URL = 'http://172.20.10.3:8080'; 
export const BASE_URL = 'http://localhost:8080'; 
export const request = (options) => {
    const token = uni.getStorageSync('token');
    return new Promise((resolve, reject) => {
        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Authorization': token ? `Bearer ${token}` : '',
                ...options.header
            },
            success: (res) => {
                // 只要状态码是 2xx 都算成功（后端 200, 201, 204 等）
                if (res.statusCode >= 200 && res.statusCode < 300) {
                    resolve(res.data);
                } else if (res.statusCode === 401) {
                    // Token 失效逻辑
                    uni.removeStorageSync('token');
                    uni.showToast({ title: '登录过期', icon: 'none' });
                    uni.reLaunch({ url: '/pages/login/login' });
                    reject('unauthorized');
                } else {
                    reject(res);
                }
            },
            fail: (err) => {
                // 如果进到这里，说明请求根本没发出去（跨域、断网、IP错）
                console.error('Request Fail Detail:', err);
                uni.showToast({ title: '服务器连接失败', icon: 'none' });
                reject(err);
            }
        });
    });
};