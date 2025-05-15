import request from '@/utils/request'
import { getToken } from '@/utils/auth'

/**
 * 获取仪表盘数据
 * @returns {Promise} 返回API请求Promise
 */
export function getDashboardData() {
  console.log('正在请求仪表盘数据...');
  
  // 确保API基础URL正确
  return request({
    url: '/api/admin/dashboard/stats',
    method: 'get',
    params: {
      // 添加时间戳避免缓存
      _t: new Date().getTime()
    },
    timeout: 10000, // 单独设置较长的超时时间
    validateStatus: function (status) {
      return status >= 200 && status < 300; // 仅接受2xx响应状态
    }
  }).then(response => {
    console.log('仪表盘数据请求成功:', response);
    return response;
  }).catch(error => {
    console.error('仪表盘数据请求失败:', error);
    // 继续抛出错误，让上层处理
    return Promise.reject(error);
  });
}