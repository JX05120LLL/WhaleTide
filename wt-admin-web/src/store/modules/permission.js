import { constantRoutes } from '@/router'

const state = {
  routes: constantRoutes,
  refreshKey: 0 // 添加刷新键
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  REFRESH_ROUTES: (state) => {
    state.refreshKey += 1
  }
}

const actions = {
  generateRoutes({ commit }) {
    return new Promise(resolve => {
      console.log('生成路由数据，总路由数量:', constantRoutes.length)
      
      // 调试: 检查商品管理路由是否存在
      const productRoute = constantRoutes.find(route => route.path === '/product')
      if (productRoute) {
        console.log('商品管理路由配置存在:', productRoute)
      } else {
        console.warn('商品管理路由配置不存在!')
      }
      
      // 调试: 检查动漫角色管理路由是否存在
      const characterRoute = constantRoutes.find(route => route.path === '/character')
      if (characterRoute) {
        console.log('动漫角色管理路由配置存在:', characterRoute)
        
        // 确保角色管理路由不会被隐藏
        if (characterRoute.hidden) {
          characterRoute.hidden = false
          console.log('启用了角色管理路由显示')
        }
      } else {
        console.warn('动漫角色管理路由配置不存在!')
      }
      
      // 过滤掉hidden属性为true的路由
      const accessedRoutes = constantRoutes.filter(route => {
        if (route.hidden) {
          console.log('过滤隐藏路由:', route.path)
          return false // 真正过滤掉隐藏路由
        }
        return true
      })
      console.log('处理后的路由数量:', accessedRoutes.length)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },
  
  refreshRoutes({ commit }) {
    commit('REFRESH_ROUTES')
    return Promise.resolve()
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 