import { asyncRouterMap, constantRouterMap } from '@/router/index';

// 判断菜单项是否有权限访问
function hasPermission(menus, route, roles, menuDetails) {
  // 1. 没有name的路由默认有权限（如布局组件）
  if (!route.name) {
    return true;
  }
  
  // 打印调试信息
  console.log('检查路由权限:', { path: route.path, name: route.name, meta: route.meta });
  
  // 2. 根据角色判断权限
  if (roles.includes('ADMIN') || roles.includes('超级管理员')) {
    // 超级管理员拥有所有菜单权限
    console.log('超级管理员权限通过:', route.path);
    return true;
  } else if (roles.includes('MERCHANT') || roles.includes('商家')) {
    // 商家只能访问商品和订单相关菜单
    if (route.path.includes('/pms') || route.path.includes('/oms')) {
      console.log('商家权限通过:', route.path);
      return true;
    }
    
    // 确保商家能看到子菜单
    if (route.path && 
       (route.path.startsWith('product') || 
        route.path.startsWith('order') || 
        route.path.includes('brand') ||
        route.path.includes('productCate'))) {
      console.log('商家子菜单权限通过:', route.path);
      return true;
    }
  } else if (roles.includes('USER') || roles.includes('普通用户')) {
    // 普通用户只能访问仪表盘和部分只读菜单
    if (route.path.includes('/home')) {
      return true;
    }
    // 允许查看商品列表
    if (route.path === 'product' && route.meta && route.meta.title === '商品列表') {
      return true;
    }
  }
  
  // 3. 通过菜单标题匹配
  if (route.meta && route.meta.title && menus.includes(route.meta.title)) {
    console.log('菜单标题匹配成功:', route.meta.title);
    return true;
  }
  
  // 4. 通过menuDetails进行精确匹配
  if (menuDetails && menuDetails.length > 0) {
    // 记录详情匹配结果
    let matchResult = matchRouteInMenuDetails(route, menuDetails);
    if (matchResult.matched) {
      console.log('菜单详情匹配成功:', matchResult.reason);
      return true;
    }
  }
  
  // 5. 隐藏路由默认有权限
  if (route.hidden === true) {
    return true;
  }
  
  console.log('路由无权限:', route.path);
  return false;
}

// 在菜单详情中匹配路由
function matchRouteInMenuDetails(route, menuDetails) {
  // 遍历顶级菜单
  for (const menu of menuDetails) {
    // 直接匹配当前菜单
    if (matchMenuItem(menu, route)) {
      return { matched: true, reason: `直接匹配菜单 ${menu.title}` };
    }
    
    // 匹配子菜单
    if (menu.children && menu.children.length > 0) {
      for (const child of menu.children) {
        if (matchMenuItem(child, route)) {
          return { matched: true, reason: `匹配子菜单 ${child.title}` };
        }
      }
    }
  }
  
  return { matched: false };
}

// 单个菜单项匹配函数
function matchMenuItem(menu, route) {
  // 路径精确匹配
  if (menu.path && route.path) {
    // 处理带/和不带/的路径
    const menuPath = menu.path.startsWith('/') ? menu.path : `/${menu.path}`;
    const routePath = route.path.startsWith('/') ? route.path : `/${route.path}`;
    
    if (menuPath === routePath || menu.path === route.path) {
      return true;
    }
    
    // 处理父子路由路径
    if (route.path.includes(menu.path) && menu.path !== '') {
      return true;
    }
  }
  
  // 名称匹配
  if (menu.name && route.name && (menu.name === route.name)) {
    return true;
  }
  
  // 标题匹配
  if (route.meta && route.meta.title && menu.title === route.meta.title) {
    return true;
  }
  
  // 组件路径匹配
  if (menu.component && route.component && typeof route.component === 'function') {
    // 组件路径通常是异步加载的函数，无法直接比较
    // 这里通过组件字符串表示进行部分匹配
    const routeComponentStr = route.component.toString();
    if (routeComponentStr.includes(menu.component)) {
      return true;
    }
  }
  
  return false;
}

//根据路由名称获取菜单
function getMenu(name, menus) {
  for (let i = 0; i < menus.length; i++) {
    let menu = menus[i];
    if (name===menu.name) {
      return menu;
    }
  }
  return null;
}

//对菜单进行排序
function sortRouters(accessedRouters) {
  for (let i = 0; i < accessedRouters.length; i++) {
    let router = accessedRouters[i];
    if(router.children && router.children.length > 0){
      router.children.sort(compare("sort"));
    }
  }
  accessedRouters.sort(compare("sort"));
}

//降序比较函数
function compare(p){
  return function(m,n){
    let a = m[p];
    let b = n[p];
    return b - a;
  }
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers;
      state.routers = constantRouterMap.concat(routers);
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { menus } = data;
        const { username } = data;
        const roles = data.roles || [];
        const menuDetails = data.menuDetails || [];
        
        console.log('生成路由，用户名:', username, '角色:', roles, '菜单权限:', menus);
        console.log('菜单详情:', menuDetails);
        
        // 过滤路由
        const accessedRouters = asyncRouterMap.filter(v => {
          // 使用menuDetails进行匹配
          if (hasPermission(menus, v, roles, menuDetails)) {
            if (v.children && v.children.length > 0) {
              // 过滤子菜单
              v.children = v.children.filter(child => {
                if (hasPermission(menus, child, roles, menuDetails)) {
                  return child
                }
                return false;
              });
              // 如果过滤后子菜单为空，但该菜单本身有权限，则保留
              return v.children.length > 0 || !v.alwaysShow;
            } else {
              return v
            }
          }
          return false;
        });
        
        // 对菜单进行排序
        sortRouters(accessedRouters);
        commit('SET_ROUTERS', accessedRouters);
        resolve();
      })
    }
  }
};

export default permission;

