import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  roles: [],
  username: '',
  email: '',
  phone: '',
  createTime: '',
  lastLoginTime: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_PHONE: (state, phone) => {
    state.phone = phone
  },
  SET_CREATE_TIME: (state, createTime) => {
    state.createTime = createTime
  },
  SET_LAST_LOGIN_TIME: (state, lastLoginTime) => {
    state.lastLoginTime = lastLoginTime
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          // 打印完整响应以便调试
          console.log('完整登录响应:', response)
          
          // 后端登录成功后会返回的数据结构
          // { code: 200, message: "登录成功", data: { token: "xxx", ...其他用户信息 } }
          if (response.code === 200) {
            const { data } = response
            if (!data || !data.token) {
              reject(new Error('登录成功但未收到token，请联系管理员'))
              return
            }
            
            // 存储token
            commit('SET_TOKEN', data.token)
            setToken(data.token)
            resolve(data)
          } else {
            reject(new Error(response.message || '登录失败'))
          }
        })
        .catch(error => {
          console.error('登录请求失败:', error)
          reject(error)
        })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // 打印token用于调试
      console.log('获取用户信息使用的token:', state.token)
      
      getInfo(state.token)
        .then(response => {
          // 记录响应用于调试
          console.log('获取用户信息响应:', response)
          
          const { data } = response
          
          if (!data) {
            reject(new Error('验证失败，请重新登录'))
            return
          }
          
          // 提取用户信息，兼容不同的API返回格式
          const { 
            name, 
            username, 
            avatar, 
            roles = [], 
            email, 
            phone, 
            createTime, 
            lastLoginTime 
          } = data
          
          // 用户名处理：优先使用name，如果没有则使用username
          const displayName = name || username || 'Admin User'
          
          // 头像处理：如果没有则使用默认头像
          const avatarUrl = avatar || 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
          
          // 角色处理：如果没有则提供默认角色 - 在开发环境允许跳过角色验证
          let userRoles = roles
          if ((!userRoles || userRoles.length <= 0) && process.env.NODE_ENV === 'development') {
            console.warn('开发环境 - 使用默认管理员角色')
            userRoles = ['admin']
          } else if (!userRoles || userRoles.length <= 0) {
            reject(new Error('用户角色必须是非空数组!'))
            return
          }
          
          // 提交基本信息
          commit('SET_NAME', displayName)
          commit('SET_AVATAR', avatarUrl)
          commit('SET_ROLES', userRoles)
          
          // 提交额外信息
          commit('SET_USERNAME', username || '')
          commit('SET_EMAIL', email || '')
          commit('SET_PHONE', phone || '')
          commit('SET_CREATE_TIME', createTime || '')
          commit('SET_LAST_LOGIN_TIME', lastLoginTime || '')
          
          resolve(data)
        })
        .catch(error => {
          console.error('获取用户信息失败:', error)
          
          // 开发环境下使用模拟数据
          if (process.env.NODE_ENV === 'development') {
            console.warn('开发环境 - 使用模拟用户数据')
            const mockUserData = {
              name: 'Admin',
              username: 'admin',
              avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
              roles: ['admin'],
              email: 'admin@example.com',
              phone: '138****1234',
              createTime: '2023-01-01',
              lastLoginTime: '2023-06-15'
            }
            commit('SET_NAME', mockUserData.name)
            commit('SET_AVATAR', mockUserData.avatar)
            commit('SET_ROLES', mockUserData.roles)
            commit('SET_USERNAME', mockUserData.username)
            commit('SET_EMAIL', mockUserData.email)
            commit('SET_PHONE', mockUserData.phone)
            commit('SET_CREATE_TIME', mockUserData.createTime)
            commit('SET_LAST_LOGIN_TIME', mockUserData.lastLoginTime)
            resolve(mockUserData)
            return
          }
          
          reject(error)
        })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve) => {
      // 尝试调用后端登出接口，但不等待结果
      logout().catch(error => {
        console.warn('后端登出接口调用失败，但本地登出仍将继续:', error)
      }).finally(() => {
        // 无论后端请求成功或失败，都执行本地清理
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_NAME', '')
        commit('SET_AVATAR', '')
        commit('SET_USERNAME', '')
        commit('SET_EMAIL', '')
        commit('SET_PHONE', '')
        commit('SET_CREATE_TIME', '')
        commit('SET_LAST_LOGIN_TIME', '')
        removeToken()
        
        // 记录日志
        console.log('用户已登出系统')
        
        // 移除其他可能存在的用户信息
        localStorage.removeItem('sidebarStatus')
        sessionStorage.clear()
        
        // 始终被视为成功，从不reject
        resolve()
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      commit('SET_USERNAME', '')
      commit('SET_EMAIL', '')
      commit('SET_PHONE', '')
      commit('SET_CREATE_TIME', '')
      commit('SET_LAST_LOGIN_TIME', '')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 