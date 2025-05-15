/* eslint-disable */
import axios from 'axios'

// 设置基本URL，生产环境可以在环境变量中配置
axios.defaults.baseURL = 'http://localhost:8080/api'

// 图片URL处理函数
export const getImageUrl = (path) => {
  if (!path) return '';
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path;
  }
  return `http://localhost:8080${path.startsWith('/') ? path : '/' + path}`;
};

// 请求拦截器，添加认证信息
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// API请求方法
export default {
  // 用户认证相关
  auth: {
    login: (credentials) => axios.post('/auth/login', credentials),
    register: (userData) => axios.post('/auth/register', userData),
    getProfile: () => axios.get('/auth/profile')
  },
  
  // 分类相关
  categories: {
    getAll: () => axios.get('/categories'),
    getCategoryDetails: (id) => axios.get(`/categories/${id}`),
    getCategoryDropdown: (id) => axios.get(`/categories/${id}/dropdown`)
  },
  
  // 产品相关
  products: {
    getAll: (params) => axios.get('/products', { params }),
    getById: (id) => axios.get(`/products/${id}`),
    search: (keyword) => axios.get('/products/search', { params: { keyword } })
  },
  
  // 购物车相关
  cart: {
    getCart: () => axios.get('/cart'),
    addToCart: (item) => axios.post('/cart', item),
    updateCartItem: (id, quantity) => axios.put(`/cart/${id}`, { quantity }),
    removeFromCart: (id) => axios.delete(`/cart/${id}`),
    clearCart: () => axios.delete('/cart')
  },
  
  // 订单相关
  orders: {
    getOrders: () => axios.get('/orders'),
    getOrderDetails: (id) => axios.get(`/orders/${id}`),
    createOrder: (orderData) => axios.post('/orders', orderData)
  }
} 