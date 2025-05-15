/* eslint-disable */
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 获取API基础URL
const apiBaseURL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api';

// 提取基础域名部分用于静态资源 - 重要：确保使用与后端相同的端口
const baseURL = 'http://localhost:8080';

// 创建axios实例
const service = axios.create({
  baseURL: apiBaseURL, // 后端API基础URL
  timeout: 15000, // 请求超时时间
  withCredentials: true, // 允许跨域携带cookie
  headers: {
    'Content-Type': 'application/json'
  },
  // 添加参数序列化器，避免嵌套参数使用方括号
  paramsSerializer: {
    serialize: params => {
      // 平铺参数对象为简单的键值对
      let result = '';
      Object.keys(params).forEach(key => {
        const value = params[key];
        if (value !== undefined && value !== null) {
          if (result) result += '&';
          result += `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
        }
      });
      console.log('序列化后的参数:', result);
      return result;
    }
  }
})

// 导出基础域名供图片等静态资源使用
export const staticBaseURL = baseURL;

// 请求拦截器
service.interceptors.request.use(
  config => {
    console.log('===== 请求拦截 =====');
    console.log('请求URL:', config.url);
    
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    console.log('Token存在?', !!token);
    
    // 如果token存在，添加到请求头
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
      
      // 添加调试日志，查看请求头中的Authorization
      console.log('添加Authorization头:', `Bearer ${token.substring(0, 15)}...`);
      console.log('Authorization头完整值:', `Bearer ${token}`);
    } else {
      console.warn('No token found in localStorage');
    }
    
    // 在开发环境下打印请求信息
    if (process.env.NODE_ENV === 'development') {
      console.log('API请求详情:', {
        url: config.url,
        method: config.method,
        data: config.data,
        params: config.params,
        headers: config.headers
      })
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    console.log('===== 响应拦截 =====');
    console.log('响应URL:', response.config.url);
    console.log('响应状态码:', response.status);
    console.log('响应数据:', response.data);
    
    const res = response.data
    
    // 根据后端返回的状态码判断请求是否成功
    if (res.code && res.code !== 200) {
      // 日志记录
      console.warn(`接口返回错误码: ${res.code}`, response.config.url);
      
      ElMessage({
        message: res.message || '请求失败',
        type: 'error',
        duration: 3000
      })
      
      // 处理特定错误码
      if (res.code === 401) {
        // 未登录或者token过期
        console.warn('接口返回401未授权:', response.config.url);
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        
        // 页面跳转到登录
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误: ', error)
    console.log('错误响应详情:', {
      url: error.config?.url,
      status: error.response?.status,
      data: error.response?.data
    });
    
    let message = '网络错误，请稍后重试'
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          console.warn('API请求401未授权:', error.config.url);
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          router.push('/login')
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器错误'
          break
        default:
          message = error.response.data?.message || `请求失败: ${error.response.status}`
      }
    }
    
    ElMessage({
      message: message,
      type: 'error',
      duration: 3000
    })
    
    return Promise.reject(error)
  }
)

// 封装请求方法
export const http = {
  get(url, params, config = {}) {
    return service.get(url, { params, ...config })
  },
  post(url, data, config = {}) {
    return service.post(url, data, config)
  },
  put(url, data, config = {}) {
    return service.put(url, data, config)
  },
  delete(url, params, config = {}) {
    return service.delete(url, { params, ...config })
  }
}

export default service 