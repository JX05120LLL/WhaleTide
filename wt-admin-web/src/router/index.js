import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

// 预加载商品组件
const ProductList = () => import('@/views/product/List')
const ProductDetail = () => import('@/views/product/Detail')

// 预加载订单组件
const OrderList = () => import('@/views/order/List')
// 直接导入OrderDetail组件
import OrderDetail from '@/views/OrderDetail'
const OrderStatistics = () => import('@/views/order/Statistics')

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/Login'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/Dashboard'),
        name: 'Dashboard',
        meta: { title: '仪表盘', icon: 'el-icon-s-data' }
      }
    ]
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/list',
    name: 'Product',
    meta: { title: '商品管理', icon: 'el-icon-goods' },
    children: [
      {
        path: 'list',
        component: ProductList,
        name: 'ProductList',
        meta: { title: '商品列表', icon: 'el-icon-shopping-bag-1' }
      },
      {
        path: 'category',
        component: () => import('@/views/product/category'),
        name: 'ProductCategory',
        meta: { title: '商品分类', icon: 'el-icon-folder' }
      },
      {
        path: 'add',
        component: () => import('@/views/product/Add'),
        name: 'ProductAdd',
        meta: { title: '添加商品', icon: 'el-icon-plus' }
      },
      {
        path: 'detail/:id',
        component: ProductDetail,
        name: 'ProductDetail',
        meta: { title: '商品详情', noCache: true },
        hidden: true
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/list',
    name: 'Order',
    meta: { title: '订单管理', icon: 'el-icon-s-order' },
    children: [
      {
        path: 'list',
        component: OrderList,
        name: 'OrderList',
        meta: { title: '订单列表', icon: 'el-icon-document' }
      },
      {
        path: 'detail/:id',
        component: OrderDetail,
        name: 'OrderDetail',
        meta: { title: '订单详情', noCache: true },
        hidden: true
      },
      {
        path: 'statistics',
        component: OrderStatistics,
        name: 'OrderStatistics',
        meta: { title: '订单统计', icon: 'el-icon-data-analysis' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/role',
    name: 'System',
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      {
        path: 'role',
        component: () => import('@/views/system/role'),
        name: 'Role',
        meta: { title: '角色管理', icon: 'el-icon-s-check' }
      }
    ]
  },
  // 通配符路由，必须放在最后
  {
    path: '*',
    redirect: '/dashboard',
    hidden: true
  }
]

const createRouter = () => new Router({
  mode: 'hash', // 使用hash模式确保在各环境下都能正常工作
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 增强调试信息，帮助排查路由问题
router.beforeEach((to, from, next) => {
  console.log('路由导航开始:', {
    from: from.path,
    to: to.path,
    matched: to.matched.map(m => m.path || m.name),
    timestamp: new Date().toISOString()
  });
  
  // 如果是产品或订单相关路由，打印更多信息
  if (to.path.includes('/product') || to.path.includes('/order')) {
    console.log('路由详情:', {
      name: to.name,
      params: to.params,
      query: to.query,
      hash: to.hash,
      fullPath: to.fullPath
    });
  }
  
  next();
});

router.afterEach((to, from) => {
  console.log('路由导航完成:', {
    from: from.path,
    to: to.path,
    timestamp: new Date().toISOString()
  });
});

// 重置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router 