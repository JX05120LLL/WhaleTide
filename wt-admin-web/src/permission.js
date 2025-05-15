import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'
import { getToken } from '@/utils/auth' // 引入getToken方法
import { Message } from 'element-ui' // 引入消息提示组件

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// 白名单路径，不需要登录就可以访问
const whiteList = ['/login', '/404', '/401'] 

// 公共页面，不需要严格提示
const publicPages = ['/home', '/about', '/help']

// 是否已经显示过令牌失效提示，避免重复提示
let hasShownTokenInvalidTip = false

// 重置提示状态的函数（登录成功后调用）
export function resetTokenTipStatus() {
  hasShownTokenInvalidTip = false
}

// 权限控制逻辑
router.beforeEach(async(to, from, next) => {
  // 开始进度条
  NProgress.start()

  // 设置页面标题
  document.title = getPageTitle(to.meta.title)
  
  // 记录导航信息
  console.log('[Permission] 路由导航:', {
    from: from.path,
    to: to.path,
    matched: to.matched.map(m => m.path)
  });

  // 获取令牌
  const hasToken = getToken()
  
  // 判断是否有令牌
  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登录且目标是登录页，则重定向到首页
      next({ path: '/' })
      NProgress.done()
    } else {
      try {
        // 检查是否已获取用户信息
        const hasUserInfo = store.getters.name
        
        if (hasUserInfo) {
          // 已有用户信息，直接放行
          next()
        } else {
          // 尝试获取用户信息
          await store.dispatch('user/getInfo')
          
          // 确保路由配置已生成
          await store.dispatch('permission/generateRoutes')
          
          // 如果有提示过令牌失效，那么重置提示状态
          hasShownTokenInvalidTip = false
          
          // 继续导航
          next({ ...to, replace: true })
        }
      } catch (error) {
        // 如果获取用户信息失败，说明令牌已失效
        console.error('获取用户信息失败，令牌可能已失效:', error)
        
        // 清除令牌
        await store.dispatch('user/resetToken')
        
        // 根据当前页面类型决定提示内容
        const isPublicPage = publicPages.some(path => to.path.startsWith(path))
        
        // 仅在非公共页面且未显示过提示时显示提示
        if (!isPublicPage && !hasShownTokenInvalidTip) {
          Message({
            message: '您的登录状态已失效，请重新登录',
            type: 'error',
            duration: 3000,
            showClose: true,
            onClose: () => {
              hasShownTokenInvalidTip = true
            }
          })
          hasShownTokenInvalidTip = true
        }
        
        // 跳转到登录页，并携带原目标路径
        next(`/login?redirect=${to.path}`)
        NProgress.done()
      }
    }
  } else {
    // 没有令牌
    
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单中，直接进入
      next()
    } else {
      // 判断是否是公共页面
      const isPublicPage = publicPages.some(path => to.path.startsWith(path))
      
      // 如果不是公共页面或白名单页面，且未显示过提示，则显示提示
      if (!isPublicPage && !hasShownTokenInvalidTip) {
        Message({
          message: '您尚未登录或登录状态已失效，请登录后访问',
          type: 'warning',
          duration: 3000,
          showClose: true
        })
        hasShownTokenInvalidTip = true
      }
      
      // 没有访问权限的页面重定向到登录页
      console.log('[Permission] 未登录，重定向到登录页')
      
      // 跳转到登录页，并携带原目标路径
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // 结束进度条
  NProgress.done()
  console.log('[Permission] 路由导航完成')
}) 