import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { checkFirstVisit: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/Cart.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: () => import('../views/Checkout.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/payment',
    name: 'Payment',
    component: () => import('../views/Payment.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/payment/success',
    name: 'PaymentSuccess',
    component: () => import('../views/PaymentSuccess.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/detail',
    name: 'OrderDetail',
    component: () => import('../views/OrderDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/category/:id',
    name: 'Category',
    component: () => import('../views/Category.vue')
  },
  {
    path: '/new-products',
    name: 'NewProducts',
    redirect: '/category/new'
  },
  {
    path: '/promotion',
    name: 'Promotion',
    component: () => import('../views/Promotion.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue')
  },
  {
    path: '/brands',
    name: 'Brands',
    component: () => import('../views/Brands.vue')
  },
  {
    path: '/brand/:id',
    name: 'BrandDetail',
    component: () => import('../views/BrandDetail.vue')
  },
  {
    path: '/user',
    name: 'UserCenter',
    component: () => import('../views/UserCenter.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('../views/user/Profile.vue')
      },
      {
        path: 'orders',
        name: 'UserOrders',
        component: () => import('../views/user/Orders.vue')
      },
      {
        path: 'address',
        name: 'UserAddress',
        component: () => import('../views/user/Address.vue')
      },
      {
        path: 'favorites',
        name: 'UserFavorites',
        component: () => import('../views/user/Favorites.vue')
      },
      {
        path: 'history',
        name: 'UserHistory',
        component: () => import('../views/user/History.vue')
      },
      {
        path: 'security',
        name: 'UserSecurity',
        component: () => import('../views/user/Security.vue')
      },
      {
        path: 'reviews',
        name: 'UserReviews',
        component: () => import('../views/user/Reviews.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫，检查登录状态
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('token')
  
  // 检查是否首次访问
  const hasVisited = localStorage.getItem('hasVisited')
  
  // 需要授权的页面且未登录，跳转到登录页面
  if (to.matched.some(record => record.meta.requiresAuth) && !isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }
  
  // 如果是首页且是首次访问（没有登录也没有访问标记），跳转到登录页
  if (to.matched.some(record => record.meta.checkFirstVisit) && !isLoggedIn && !hasVisited) {
    // 设置访问标记，避免循环重定向
    localStorage.setItem('hasVisited', 'true')
    next({ name: 'Login' })
    return
  }
  
  // 如果不是首次访问或已登录，或者前往的页面不需要检查，则正常导航
  next()
})

export default router 