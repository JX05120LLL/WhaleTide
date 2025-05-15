import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: '', // 使用相对路径，让Vue代理来处理
  timeout: 8000 // 增加超时时间，防止请求超时
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token) {
      // 简化token处理逻辑，统一使用一种方式
      const token = getToken()
      
      // 采用标准的Bearer token格式
      config.headers['Authorization'] = `Bearer ${token}`
      
      // 如果请求是GET请求，同时在参数中带上token
      if (config.method === 'get' && config.params) {
        config.params = {
          ...config.params,
          token: token
        }
      }
      
      console.log('请求URL:', config.url)
      console.log('请求方法:', config.method)
      console.log('请求头:', config.headers)
      if (config.params) {
        console.log('请求参数:', config.params)
      }
      if (config.data) {
        console.log('请求数据:', config.data)
      }
    }
    
    // 确保POST请求默认发送JSON格式
    if (config.method === 'post' || config.method === 'put') {
      config.headers['Content-Type'] = 'application/json'
    }
    
    return config
  },
  error => {
    // do something with request error
    console.log('请求发送错误:', error) // for debug
    return Promise.reject(error)
  }
)

// 用于控制只弹出一次登录过期提示
let isShowingLoginMessage = false

// response interceptor
service.interceptors.response.use(
  response => {
    console.log('收到响应:', response.config.url, response.data)
    const res = response.data

    // 后端返回的状态码是200，代表成功
    if (res.code === 200) {
      return res
    } else {
      // 其他状态码都视为错误
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 401: 未授权; 403: 权限不足
      if (res.code === 401) {
        // 避免重复弹出
        if (!isShowingLoginMessage) {
          isShowingLoginMessage = true
          MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            store.dispatch('user/resetToken').then(() => {
              location.reload()
            })
          }).finally(() => {
            isShowingLoginMessage = false
          })
        }
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
  },
  error => {
    console.log('响应错误:', error) // for debug
    
    // 处理HTTP错误状态码
    if (error.response) {
      const { status, config } = error.response
      console.log('HTTP状态码:', status)
      console.log('请求URL:', config.url)
      
      // 处理401未授权错误
      if (status === 401 && !isShowingLoginMessage) {
        isShowingLoginMessage = true
        
        MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        }).finally(() => {
          isShowingLoginMessage = false
        })
      } else if (status === 404) {
        // 处理404错误
        Message({
          message: `接口不存在: ${config.url}`,
          type: 'error',
          duration: 5 * 1000
        })
      } else if (status === 500) {
        // 处理500错误
        Message({
          message: '服务器内部错误，请联系管理员',
          type: 'error',
          duration: 5 * 1000
        })
      } else if (status !== 401) {
        // 非401错误才显示消息提示
        Message({
          message: error.message || `请求失败(${status})`,
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else if (error.message.includes('timeout')) {
      // 处理超时错误
      Message({
        message: '请求超时，请检查网络连接',
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      Message({
        message: error.message || '未知错误',
        type: 'error',
        duration: 5 * 1000
      })
    }
    
    return Promise.reject(error)
  }
)

export default service 