/* eslint-disable */
import { createStore } from 'vuex'
import api from '../api'
import { deepClone, safeJSONParse, safeJSONStringify } from '@/utils/deep-clone'

// 安全处理localStorage存储
const safeLocalStorage = {
  get(key) {
    try {
      const value = localStorage.getItem(key);
      if (value === 'undefined' || value === 'null') return null;
      return safeJSONParse(value);
    } catch (e) {
      console.error('localStorage读取错误:', e);
      return null;
    }
  },
  set(key, value) {
    try {
      // 使用深拷贝处理循环引用
      localStorage.setItem(key, safeJSONStringify(value));
    } catch (e) {
      console.error('localStorage存储错误:', e);
    }
  },
  remove(key) {
    try {
      localStorage.removeItem(key);
    } catch (e) {
      console.error('localStorage删除错误:', e);
    }
  }
};

export default createStore({
  state: {
    user: safeLocalStorage.get('user'),
    token: localStorage.getItem('token') || null,
    cart: safeLocalStorage.get('cart') || [],
    cartCount: 0,
    categories: [],
    categoryData: {}
  },
  getters: {
    isLoggedIn: state => !!state.token,
    currentUser: state => state.user,
    userInfo: state => state.user,
    cartItems: state => state.cart,
    cartTotal: state => {
      return state.cart.reduce((total, item) => {
        return total + (item.price * item.quantity)
      }, 0)
    },
    cartCount: state => {
      if (!state.cart || !Array.isArray(state.cart)) {
        return 0;
      }
      return state.cart.reduce((count, item) => {
        return count + (item.quantity || 0)
      }, 0)
    },
    allCategories: state => state.categories,
    getCategoryData: state => categoryId => state.categoryData[categoryId]
  },
  mutations: {
    setUser(state, user) {
      state.user = user
      safeLocalStorage.set('user', user)
    },
    setToken(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    logout(state) {
      state.user = null
      state.token = null
      safeLocalStorage.remove('user')
      localStorage.removeItem('token')
    },
    addToCart(state, item) {
      const existingItem = state.cart.find(cartItem => cartItem.id === item.id)
      
      if (existingItem) {
        existingItem.quantity += item.quantity
      } else {
        state.cart.push(item)
      }
      
      // 更新本地存储
      safeLocalStorage.set('cart', state.cart)
    },
    updateCartItem(state, { id, quantity }) {
      const item = state.cart.find(item => item.id === id)
      if (item) {
        item.quantity = quantity
      }
      safeLocalStorage.set('cart', state.cart)
    },
    removeFromCart(state, id) {
      state.cart = state.cart.filter(item => item.id !== id)
      safeLocalStorage.set('cart', state.cart)
    },
    clearCart(state) {
      state.cart = []
      safeLocalStorage.set('cart', state.cart)
    },
    setCategories(state, categories) {
      // 确保categories始终是数组
      state.categories = Array.isArray(categories) ? categories : 
                        (categories && categories.length ? categories : [])
    },
    setCategoryData(state, { categoryId, data }) {
      state.categoryData = {
        ...state.categoryData,
        [categoryId]: data
      }
    }
  },
  actions: {
    // 调试用户登录状态
    async checkLoginStatus({ commit, state }) {
      const token = localStorage.getItem('token');
      const storedUser = safeLocalStorage.get('user');
      
      console.log('===== 检查登录状态 =====');
      console.log('当前store中的token:', state.token);
      console.log('localStorage中的token:', token);
      console.log('当前store中的user:', state.user);
      console.log('localStorage中的user:', storedUser);
      
      if (token && (!state.token || !state.user)) {
        console.log('发现token但是store中没有用户信息，尝试重新获取用户信息');
        try {
          // 更新store中的token
          commit('setToken', token);
          
          // 获取用户信息
          const response = await api.user.getUserInfo();
          if (response && response.code === 200) {
            console.log('成功获取到用户信息:', response.data);
            commit('setUser', response.data);
            return {
              isLoggedIn: true,
              token: token,
              user: response.data
            };
          } else {
            console.error('获取用户信息失败:', response);
            if (response.code === 401) {
              console.warn('token可能无效，执行登出操作');
              commit('logout');
            }
            return {
              isLoggedIn: false,
              error: response.message || '获取用户信息失败'
            };
          }
        } catch (error) {
          console.error('获取用户信息异常:', error);
          // 如果是401错误，清除用户登录状态
          if (error.response && error.response.status === 401) {
            console.warn('获取用户信息时收到401错误，清除登录状态');
            commit('logout');
          }
          return {
            isLoggedIn: false,
            error: error.message || '获取用户信息异常'
          };
        }
      }
      
      return {
        isLoggedIn: !!token && !!state.user,
        token: token,
        user: state.user,
        tokenMissing: !token,
        userMissing: !state.user
      };
    },
    
    // 登录
    async login({ commit, dispatch }, credentials) {
      try {
        const response = await api.user.login(credentials)
        if (response.code === 200) {
          // 保存token
          commit('setToken', response.data)
          
          // 获取用户信息
          await dispatch('getUserInfo')
          
          return Promise.resolve(response.data)
        } else {
          return Promise.reject(new Error(response.message || '登录失败'))
        }
      } catch (error) {
        console.error('登录失败:', error)
        return Promise.reject(error)
      }
    },
    
    // 注册
    async register({ commit }, userData) {
      try {
        const response = await api.user.register(userData)
        if (response.code === 200) {
          return Promise.resolve(response.data)
        } else {
          return Promise.reject(new Error(response.message || '注册失败'))
        }
      } catch (error) {
        console.error('注册失败:', error)
        return Promise.reject(error)
      }
    },
    
    // 获取用户信息
    async getUserInfo({ commit }) {
      try {
        const response = await api.user.getUserInfo()
        if (response.code === 200) {
          commit('setUser', response.data)
          return Promise.resolve(response.data)
        } else {
          // 如果获取用户信息失败，清空用户信息和token
          if (response.code === 401) {
            commit('logout')
          }
          return Promise.reject(new Error(response.message || '获取用户信息失败'))
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 如果是401错误，清空用户信息和token
        if (error.response && error.response.status === 401) {
          commit('logout')
        }
        return Promise.reject(error)
      }
    },
    
    // 登出
    logout({ commit }) {
      commit('logout')
    },
    
    // 购物车相关
    async addToCart({ commit }, product) {
      try {
        // 调用后端API添加商品到购物车
        const response = await api.cart.addToCart({
          productId: product.id,
          quantity: product.quantity || 1
        });
        
        if (response && response.code === 200) {
          // API调用成功后再更新前端状态
          commit('addToCart', product);
          return true;
        } else {
          console.error('添加购物车失败:', response?.message || '未知错误');
          return false;
        }
      } catch (error) {
        console.error('添加购物车请求异常:', error);
        return false;
      }
    },
    updateCartItem({ commit }, payload) {
      commit('updateCartItem', payload)
    },
    removeFromCart({ commit }, id) {
      commit('removeFromCart', id)
    },
    clearCart({ commit }) {
      commit('clearCart')
    },
    
    // 分类相关
    async fetchCategories({ commit }) {
      try {
        const response = await api.product.getProductCategories()
        if (response && response.code === 200) {
          // 确保数据是数组格式
          const categoriesData = response.data || []
          const categories = Array.isArray(categoriesData) ? categoriesData : 
                            (categoriesData && typeof categoriesData === 'object' ? [categoriesData] : [])
          
          commit('setCategories', categories)
          return categories
        } else {
          console.error('Failed to fetch categories:', response?.message || 'Unknown error')
          
          // 使用默认数据
          const defaultCategories = [
            { id: 1, name: '海洋设备', image: '/images/categories/marine-equipment.png' },
            { id: 2, name: '潜水装备', image: '/images/categories/diving-gear.png' },
            { id: 3, name: '海滩用品', image: '/images/categories/beach-accessories.png' },
          ]
          commit('setCategories', defaultCategories)
          return defaultCategories
        }
      } catch (error) {
        console.error('Failed to fetch categories:', error)
        
        // 使用默认数据
        const defaultCategories = [
          { id: 1, name: '海洋设备', image: '/images/categories/marine-equipment.png' },
          { id: 2, name: '潜水装备', image: '/images/categories/diving-gear.png' },
          { id: 3, name: '海滩用品', image: '/images/categories/beach-accessories.png' },
        ]
        commit('setCategories', defaultCategories)
        return defaultCategories
      }
    },
    async fetchCategoryData({ commit, state }, categoryId) {
      // 如果已经有数据，不重复获取
      if (state.categoryData[categoryId]) {
        return state.categoryData[categoryId]
      }

      try {
        let data = {}
        
        // 处理特殊分类
        if (categoryId === 'new') {
          // 处理新品发布分类
          try {
            const res = await api.home.getNewProducts({ limit: 20 });
            data = {
              subcategories: [],
              featuredProducts: res.data.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price,
                image: product.pic || product.image,
                description: product.description
              }))
            };
          } catch (error) {
            console.error('获取新品发布商品失败:', error);
          }
        } else if (categoryId === 'hot') {
          // 处理热卖商品分类
          try {
            const res = await api.home.getHotProducts({ limit: 20 });
            data = {
              subcategories: [],
              featuredProducts: res.data.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price,
                image: product.pic || product.image,
                description: product.description
              }))
            };
          } catch (error) {
            console.error('获取热卖商品失败:', error);
          }
        } else if (categoryId === 'discount') {
          // 处理限时特惠分类
          try {
            const res = await api.home.getDiscountProducts({ limit: 20 });
            data = {
              subcategories: [],
              featuredProducts: res.data.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price,
                image: product.pic || product.image,
                description: product.description
              }))
            };
          } catch (error) {
            console.error('获取限时特惠商品失败:', error);
          }
        } else if (categoryId === 'selected') {
          // 处理优选好物分类
          try {
            const res = await api.home.getSelectedProducts({ limit: 20 });
            data = {
              subcategories: [],
              featuredProducts: res.data.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price,
                image: product.pic || product.image,
                description: product.description
              }))
            };
          } catch (error) {
            console.error('获取优选好物商品失败:', error);
          }
        } else {
          // 普通分类，调用获取分类商品API
          try {
            // 使用API获取分类数据
            const res = await api.product.getCategoryProducts(categoryId, 1, 20);
            data = {
              subcategories: [],  // 先不获取子分类
              featuredProducts: res.data.map(product => ({
                id: product.id,
                name: product.name, 
                price: product.price,
                image: product.pic || product.image,
                description: product.description
              }))
            };
          } catch (error) {
            console.error(`获取分类 ${categoryId} 的商品失败:`, error);
            // 使用静态数据作为后备
            await new Promise(resolve => setTimeout(resolve, 300)); // 模拟加载延迟
            data = {
              subcategories: [],
              featuredProducts: [
                {
                  id: 101,
                  name: '分类商品示例',
                  price: 199,
                  image: '/images/placeholder.png',
                  description: '这是一个示例商品'
                }
              ]
            };
          }
        }
        
        commit('setCategoryData', { categoryId, data });
        return data;
      } catch (error) {
        console.error('获取分类数据失败:', error);
        return {
          subcategories: [],
          featuredProducts: []
        };
      }
    }
  }
}) 