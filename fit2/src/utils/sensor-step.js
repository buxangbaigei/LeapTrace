// utils/sensor-step.js

let stepCount = 0;
let isStarted = false;
let lastAcceleration = { x: 0, y: 0, z: 0 };
let lastStepTime = 0;

// 获取标准日期 Key (YYYY-MM-DD)
function getTodayKey() {
    const d = new Date();
    return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
}

// 核心：保存步数到硬盘
function saveToDisk(steps) {
    const key = `steps_${getTodayKey()}`;
    uni.setStorageSync(key, steps.toString());
    // 额外存一个全局Key作为双保险
    uni.setStorageSync('global_last_steps', steps.toString());
}

// 核心：从硬盘读取步数
function loadFromDisk() {
    const key = `steps_${getTodayKey()}`;
    const localData = uni.getStorageSync(key) || uni.getStorageSync('global_last_steps');
    return localData ? parseInt(localData) : 0;
}

export function startStepCountingUpdates({ handler }) {
    if (isStarted) return;
    isStarted = true;

    // 【最关键一步】启动时立刻读取旧数据，接力计数
    stepCount = loadFromDisk();
    console.log('接力成功，当前起始步数:', stepCount);

    // 立即通知UI，防止显示0
    handler(stepCount, Date.now(), null);

    uni.onAccelerometerChange((res) => {
        const now = Date.now();
        // 1. 震动幅度过滤
        const delta = Math.sqrt(
            Math.pow(res.x - lastAcceleration.x, 2) +
            Math.pow(res.y - lastAcceleration.y, 2) +
            Math.pow(res.z - lastAcceleration.z, 2)
        );
        lastAcceleration = { x: res.x, y: res.y, z: res.z };

        // 2. 计步算法判定
        if (delta > 15 && (now - lastStepTime) > 280) {
            stepCount++;
            lastStepTime = now;
            
            // 3. 实时存入持久化存储（防止下次划掉丢失）
            saveToDisk(stepCount);
            
            // 4. 通知页面更新
            uni.$emit('step-change', stepCount);
            handler(stepCount, now, null);
        }
    });

    uni.startAccelerometer({ interval: 'ui' });
}