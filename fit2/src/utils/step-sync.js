import { startStepCountingUpdates, stopStepCountingUpdates } from './sensor-step.js';
import { request } from '@/utils/request.js';

let syncTimer = null;
let currentSteps = 0;
let lastSyncSteps = 0;
let isRunning = false;

/**
 * 启动步数同步服务
 */
export function startStepSync() {
    if (isRunning) {
        console.log('步数同步已在运行，跳过');
        return;
    }
    
    console.log('🚀 启动步数同步服务...');
    isRunning = true;
    
    // 启动传感器监听
    startStepCountingUpdates({
        handler: (steps, timestamp, error) => {
            if (error) {
                console.error('❌ 计步器错误:', error);
                return;
            }
            
            currentSteps = steps;
            console.log('当前步数:', steps);
            
            // 如果步数有变化，立即同步
            if (steps > lastSyncSteps) {
                syncStepsToBackend(steps);
                lastSyncSteps = steps;
            }
        }
    });
    
    // 每30秒强制同步一次
    syncTimer = setInterval(async () => {
        if (currentSteps > 0) {
            await syncStepsToBackend(currentSteps);
            lastSyncSteps = currentSteps;
        }
    }, 30000);
    
    // 2秒后尝试首次同步
    setTimeout(() => {
        if (currentSteps > 0) {
            syncStepsToBackend(currentSteps);
            lastSyncSteps = currentSteps;
        } else {
            console.warn('⏰ 传感器尚未返回步数数据，请拿着手机走动几步');
        }
    }, 2000);
}

/**
 * 停止步数同步服务
 */
export function stopStepSync() {
    if (!isRunning) {
        return;
    }
    
    console.log('🛑 停止步数同步服务...');
    isRunning = false;
    
    stopStepCountingUpdates();
    
    if (syncTimer) {
        clearInterval(syncTimer);
        syncTimer = null;
    }
}

/**
 * 同步步数到后端
 */
async function syncStepsToBackend(steps) {
    try {
        const res = await request({
            url: '/api/health/steps',
            method: 'POST',
            data: {
                steps: steps,
                recordDate: new Date().toISOString().split('T')[0]
            }
        });
        
        if (res.code === 200) {
            console.log('✅ 步数同步成功:', steps, '步');
        } else {
            console.error('❌ 步数同步失败:', res.message);
        }
    } catch (e) {
        console.error('❌ 步数同步请求异常:', e);
    }
}

/**
 * 获取当前步数
 */
export function getCurrentSteps() {
    return currentSteps;
}
