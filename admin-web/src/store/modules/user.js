import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: []
  },

  mutations: {
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
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          // 打印响应内容以便调试
          console.log('登录响应:', response)
          
          // 检查完整的响应结构
          if (response.code === 200 && response.data) {
            // 处理嵌套的响应结构
            const data = response.data
            console.log('登录数据:', data)
            
            // 使用正确的token格式 - 根据后端代码，应该直接使用token值，不需要拼接tokenHead
            let tokenStr = ''
            if (data.token) {
              // 直接使用token，不要添加tokenHead
              tokenStr = data.token
              console.log('设置token:', tokenStr)
              setToken(tokenStr)
              commit('SET_TOKEN', tokenStr)
              resolve()
            } else {
              console.error('无法从响应中获取token')
              reject('无法从响应中获取token')
            }
          } else {
            console.error('登录响应格式不正确:', response)
            reject('登录响应格式不正确')
          }
        }).catch(error => {
          console.error('登录失败:', error)
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        // 打印当前token
        console.log('获取用户信息前的token:', getToken())
        
        getInfo().then(response => {
          // 打印响应内容以便调试
          console.log('获取用户信息响应:', response)
          
          // 检查完整的响应结构
          if (response.code === 200 && response.data) {
            const data = response.data
            console.log('用户信息数据:', data)
            
            // 详细打印菜单信息，帮助排查问题
            console.log('原始菜单列表:', data.menus);
            console.log('菜单详情结构:', JSON.stringify(data.menuDetails, null, 2));
            
            // 特殊处理：如果后端没有返回roles，创建一个默认角色
            if (!data.roles || data.roles.length === 0) {
              console.warn('后端未返回角色信息，使用默认角色')
              data.roles = ['DEFAULT']
            }
            
            // 设置角色
            commit('SET_ROLES', data.roles)
            
            // 设置用户名，如果不存在使用默认值
            commit('SET_NAME', data.username || 'Default User')
            
            // 设置头像，如果不存在使用默认值
            commit('SET_AVATAR', data.icon || '')
            
            // 处理菜单详情数据
            if (!data.menuDetails) {
              console.warn('后端未返回菜单详情信息')
              data.menuDetails = []
            } else {
              console.log('接收到菜单详情数据:', data.menuDetails.length, '项')
              
              // 检查菜单路径和组件
              data.menuDetails.forEach((menu, index) => {
                console.log(`顶级菜单${index+1}: ${menu.title}, 路径: ${menu.path}, 组件: ${menu.component}`);
                
                if (menu.children && menu.children.length > 0) {
                  menu.children.forEach((child, childIndex) => {
                    console.log(`  子菜单${index+1}-${childIndex+1}: ${child.title}, 路径: ${child.path}, 组件: ${child.component}`);
                  });
                }
              });
            }
            
            resolve(response)
          } else {
            console.error('获取用户信息格式不正确:', response)
            reject('获取用户信息格式不正确')
          }
        }).catch(error => {
          // 详细记录错误信息
          console.error('获取用户信息失败:', error)
          console.error('错误详情:', JSON.stringify(error, null, 2))
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
