import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权

const whiteList = ['/login'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      // 打印当前状态用于调试
      console.log('当前路由状态:', { 
        to: to.path, 
        roles: store.getters.roles, 
        token: getToken() 
      })

      if (store.getters.roles.length === 0) {
        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          console.log('获取用户信息成功:', res)
          
          // 增强错误处理
          let menus = [];
          let username = 'admin';
          let roles = [];
          let menuDetails = [];
          
          // 尝试从响应中获取菜单和用户名
          if (res && res.data) {
            menus = res.data.menus || [];
            username = res.data.username || 'admin';
            roles = res.data.roles || [];
            menuDetails = res.data.menuDetails || [];
            
            console.log('获取到菜单数量:', menus.length);
            console.log('获取到菜单详情数量:', menuDetails.length);
            
            // 检查菜单详情的完整性
            if (menuDetails.length > 0) {
              console.log('第一个顶级菜单:', menuDetails[0].title);
              if (menuDetails[0].children && menuDetails[0].children.length > 0) {
                console.log('第一个菜单的子菜单数量:', menuDetails[0].children.length);
                console.log('第一个子菜单:', menuDetails[0].children[0].title);
              } else {
                console.warn('第一个菜单没有子菜单');
              }
            } else {
              console.warn('没有菜单详情数据');
            }
          }
          
          console.log('生成路由参数:', { menus, username, roles, menuDetails })
          
          // 处理路由生成
          store.dispatch('GenerateRoutes', { menus, username, roles, menuDetails }).then(() => {
            console.log('路由生成成功，添加的路由数量:', store.getters.addRouters.length)
            
            // 打印生成的路由
            store.getters.addRouters.forEach((route, index) => {
              console.log(`路由${index+1}:`, {
                path: route.path,
                name: route.name,
                childrenCount: route.children ? route.children.length : 0
              });
              
              // 打印子路由
              if (route.children && route.children.length > 0) {
                route.children.forEach((child, childIndex) => {
                  console.log(`  子路由${index+1}-${childIndex+1}:`, {
                    path: child.path,
                    name: child.name,
                    component: child.component ? '已定义' : '未定义'
                  });
                });
              }
            });
            
            // 动态添加可访问路由表
            router.addRoutes(store.getters.addRouters);

            // 打印每个路由对应的组件加载情况，帮助诊断404问题
            console.log('检查所有路由组件:');
            const checkRouteComponents = (routes, parentPath = '') => {
              routes.forEach(route => {
                const fullPath = parentPath ? `${parentPath}${route.path.startsWith('/') ? route.path : '/' + route.path}` : route.path;
                
                console.log(`路由 ${fullPath}:`);
                
                // 检查组件
                if (route.component) {
                  if (typeof route.component === 'function') {
                    console.log(`- 动态组件: 已定义`);
                  } else {
                    console.log(`- 静态组件: ${route.component.name || '未命名'}`);
                  }
                } else {
                  console.warn(`- 警告: 未定义组件!`);
                }
                
                // 检查子路由
                if (route.children && route.children.length > 0) {
                  checkRouteComponents(route.children, fullPath);
                }
              });
            };
            checkRouteComponents(store.getters.addRouters);
            
            // 提供一个测试指令，用户可以在控制台输入此命令进行菜单权限测试
            console.log('%c如需测试菜单权限，请在控制台执行:', 'color: #67C23A; font-size: 14px; font-weight: bold;');
            console.log('%cwindow.testMenuPermission = function(path) { return store.getters.addRouters.some(r => r.path === path || (r.children && r.children.some(c => r.path + "/" + c.path === path))); }', 'color: #409EFF;');
            console.log('%c示例: testMenuPermission("/pms/product")', 'color: #909399;');
            
            // 确保路由加载完成，避免陷入白屏
            next({ ...to, replace: true })
          }).catch(routeErr => {
            console.error('路由生成失败:', routeErr)
            // 即使路由生成失败，也尝试导航到首页
            next({ path: '/' })
          })
        }).catch((err) => {
          console.error('获取用户信息失败，准备登出:', err)
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || '验证失败，请重新登录')
            next({ path: '/login' })
          })
        })
      } else {
        // 记录当前访问的路径和匹配情况
        console.log('直接访问路径:', to.path);
        // 检查当前路径是否在生成的路由中
        const matchedInAddRouters = store.getters.addRouters.some(route => {
          // 检查主路由
          if (route.path === to.path) return true;
          // 检查子路由
          if (route.children) {
            return route.children.some(child => {
              const fullPath = route.path + '/' + child.path;
              return fullPath === to.path || route.path + child.path === to.path;
            });
          }
          return false;
        });
        console.log('路径是否在生成的路由中:', matchedInAddRouters);
        
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
