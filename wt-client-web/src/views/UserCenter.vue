<template>
<!-- eslint-disable -->
  <div class="user-center-page">
    <app-header></app-header>
    
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>个人中心</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      
      <!-- 用户中心主体 -->
      <div class="user-center">
        <!-- 侧边栏 -->
        <div class="sidebar">
          <div class="user-info">
            <div class="avatar">
              <template v-if="user?.avatar">
                <img :src="user.avatar" alt="用户头像">
              </template>
              <template v-else>
                <el-icon><User /></el-icon>
              </template>
            </div>
            <div class="username">{{ user?.username || '用户名' }}</div>
            <div class="user-level">{{ user?.level || '普通会员' }}</div>
          </div>
          
          <div class="menu-items">
            <div class="menu-group">
              <div class="menu-title">订单中心</div>
              <el-menu
                :default-active="activeMenu"
                class="user-menu"
                @select="handleMenuSelect">
                <el-menu-item index="orders">
                  <el-icon><Tickets /></el-icon>
                  <span>我的订单</span>
                </el-menu-item>
                <el-menu-item index="reviews">
                  <el-icon><ChatLineRound /></el-icon>
                  <span>评价晒单</span>
                </el-menu-item>
              </el-menu>
            </div>
            
            <div class="menu-group">
              <div class="menu-title">资产中心</div>
              <el-menu
                :default-active="activeMenu"
                class="user-menu"
                @select="handleMenuSelect">
                <el-menu-item index="wallet">
                  <el-icon><Wallet /></el-icon>
                  <span>我的钱包</span>
                </el-menu-item>
                <el-menu-item index="coupon">
                  <el-icon><Discount /></el-icon>
                  <span>优惠券</span>
                </el-menu-item>
                <el-menu-item index="points">
                  <el-icon><GoldMedal /></el-icon>
                  <span>积分中心</span>
                </el-menu-item>
              </el-menu>
            </div>
            
            <div class="menu-group">
              <div class="menu-title">个人信息</div>
              <el-menu
                :default-active="activeMenu"
                class="user-menu"
                @select="handleMenuSelect">
                <el-menu-item index="profile">
                  <el-icon><Avatar /></el-icon>
                  <span>基本资料</span>
                </el-menu-item>
                <el-menu-item index="address">
                  <el-icon><Location /></el-icon>
                  <span>收货地址</span>
                </el-menu-item>
                <el-menu-item index="security">
                  <el-icon><Lock /></el-icon>
                  <span>账号安全</span>
                </el-menu-item>
              </el-menu>
            </div>
            
            <div class="menu-group">
              <div class="menu-title">收藏关注</div>
              <el-menu
                :default-active="activeMenu"
                class="user-menu"
                @select="handleMenuSelect">
                <el-menu-item index="favorites">
                  <el-icon><Star /></el-icon>
                  <span>我的收藏</span>
                </el-menu-item>
                <el-menu-item index="history">
                  <el-icon><Timer /></el-icon>
                  <span>浏览历史</span>
                </el-menu-item>
              </el-menu>
            </div>
          </div>
        </div>
        
        <!-- 内容区域 -->
        <div class="content-area">
          <!-- 如果有子路由，则显示子路由内容 -->
          <router-view v-if="$route.matched.length > 1"></router-view>
          
          <!-- 默认展示内容 -->
          <template v-else>
            <div class="welcome-panel">
              <div class="welcome-header">
                <div class="welcome-info">
                  <h2>欢迎回来，{{ user?.username || '亲爱的用户' }}</h2>
                  <p>今天是 {{ currentDate }}，祝您购物愉快！</p>
                </div>
                <el-button type="primary" @click="goShopping">去购物</el-button>
              </div>
              
              <div class="data-overview">
                <div class="data-card">
                  <div class="data-icon">
                    <el-icon><Wallet /></el-icon>
                  </div>
                  <div class="data-info">
                    <div class="data-title">账户余额</div>
                    <div class="data-value">¥{{ userInfo.balance || '0.00' }}</div>
                  </div>
                </div>
                
                <div class="data-card">
                  <div class="data-icon">
                    <el-icon><Discount /></el-icon>
                  </div>
                  <div class="data-info">
                    <div class="data-title">优惠券</div>
                    <div class="data-value">{{ userInfo.coupons || 0 }}张</div>
                  </div>
                </div>
                
                <div class="data-card">
                  <div class="data-icon">
                    <el-icon><GoldMedal /></el-icon>
                  </div>
                  <div class="data-info">
                    <div class="data-title">积分</div>
                    <div class="data-value">{{ userInfo.points || 0 }}</div>
                  </div>
                </div>
                
                <div class="data-card">
                  <div class="data-icon">
                    <el-icon><Star /></el-icon>
                  </div>
                  <div class="data-info">
                    <div class="data-title">收藏商品</div>
                    <div class="data-value">{{ userInfo.favorites || 0 }}件</div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 订单概览 -->
            <div class="order-overview">
              <div class="section-header">
                <h3>订单概览</h3>
                <el-button type="text" @click="goToOrders">查看全部订单</el-button>
              </div>
              
              <div class="order-status-cards">
                <div class="status-card" @click="goToOrders('unpaid')">
                  <el-badge :value="orderCount.unpaid || 0" :hidden="!orderCount.unpaid">
                    <el-icon><Money /></el-icon>
                  </el-badge>
                  <div class="status-text">待付款</div>
                </div>
                
                <div class="status-card" @click="goToOrders('unshipped')">
                  <el-badge :value="orderCount.unshipped || 0" :hidden="!orderCount.unshipped">
                    <el-icon><Box /></el-icon>
                  </el-badge>
                  <div class="status-text">待发货</div>
                </div>
                
                <div class="status-card" @click="goToOrders('shipped')">
                  <el-badge :value="orderCount.shipped || 0" :hidden="!orderCount.shipped">
                    <el-icon><Van /></el-icon>
                  </el-badge>
                  <div class="status-text">待收货</div>
                </div>
                
                <div class="status-card" @click="goToOrders('completed')">
                  <el-badge :value="orderCount.completed || 0" :hidden="!orderCount.completed">
                    <el-icon><Finished /></el-icon>
                  </el-badge>
                  <div class="status-text">已完成</div>
                </div>
                
                <div class="status-card" @click="goToOrders('afterSale')">
                  <el-badge :value="orderCount.afterSale || 0" :hidden="!orderCount.afterSale">
                    <el-icon><Service /></el-icon>
                  </el-badge>
                  <div class="status-text">售后</div>
                </div>
              </div>
            </div>
            
            <!-- 近期订单 -->
            <div class="recent-orders">
              <div class="section-header">
                <h3>近期订单</h3>
              </div>
              
              <div class="orders-list" v-if="recentOrders.length > 0">
                <el-table :data="recentOrders" style="width: 100%">
                  <el-table-column prop="orderNumber" label="订单编号" width="180"></el-table-column>
                  <el-table-column prop="createTime" label="下单时间" width="180"></el-table-column>
                  <el-table-column prop="amount" label="金额" width="100">
                    <template #default="scope">
                      <span class="price">¥{{ scope.row.amount.toFixed(2) }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态">
                    <template #default="scope">
                      <el-tag :type="getStatusType(scope.row.status)">
                        {{ scope.row.statusText }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作">
                    <template #default="scope">
                      <el-button 
                        type="primary" 
                        size="small" 
                        plain
                        @click="viewOrderDetail(scope.row.id)">
                        查看详情
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              
              <div class="empty-data" v-else>
                <el-empty description="暂无订单数据"></el-empty>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { 
  User, Tickets, ChatLineRound, Wallet, Discount, GoldMedal,
  Avatar, Location, Lock, Star, Timer, Money, Box, Van,
  Finished, Service
} from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import request from '@/utils/request'

export default {
  name: 'UserCenterPage',
  components: {
    AppHeader,
    AppFooter,
    User,
    Tickets,
    ChatLineRound,
    Wallet,
    Discount,
    GoldMedal,
    Avatar,
    Location,
    Lock,
    Star,
    Timer,
    Money,
    Box,
    Van,
    Finished,
    Service
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const route = useRoute()
    
    // 获取当前用户信息
    const user = computed(() => store.getters.currentUser)
    
    // 获取当前日期
    const currentDate = computed(() => {
      const date = new Date()
      return `${date.getFullYear()}年${date.getMonth()+1}月${date.getDate()}日`
    })
    
    // 激活的菜单项
    const activeMenu = computed(() => {
      if (route.name === 'UserCenter') {
        return 'profile'
      }
      return route.path.split('/').pop()
    })
    
    // 用户相关信息
    const userInfo = reactive({
      balance: '0.00',
      coupons: 0,
      points: 0,
      favorites: 0
    })
    
    // 各状态订单数量
    const orderCount = reactive({
      unpaid: 0,
      unshipped: 0,
      shipped: 0,
      completed: 0,
      afterSale: 0
    })
    
    // 最近订单
    const recentOrders = ref([
      {
        id: 10001,
        orderNumber: 'WT20230511001',
        createTime: '2023-05-11 10:30:22',
        amount: 299.00,
        status: 'unpaid',
        statusText: '待付款'
      },
      {
        id: 10002,
        orderNumber: 'WT20230510002',
        createTime: '2023-05-10 15:20:18',
        amount: 4578.00,
        status: 'shipped',
        statusText: '待收货'
      },
      {
        id: 10003,
        orderNumber: 'WT20230508003',
        createTime: '2023-05-08 09:15:32',
        amount: 128.50,
        status: 'completed',
        statusText: '已完成'
      }
    ])
    
    // 获取状态对应的类型
    const getStatusType = (status) => {
      switch (status) {
        case 'unpaid':
          return 'danger'
        case 'unshipped':
          return 'warning'
        case 'shipped':
          return ''
        case 'completed':
          return 'success'
        case 'afterSale':
          return 'info'
        default:
          return ''
      }
    }
    
    // 加载用户中心数据
    const loadUserCenterData = async () => {
      try {
        // 在实际应用中，此处应该调用API获取数据
        // const response = await request.get('/user/center/data')
        // userInfo.balance = response.data.balance
        // userInfo.coupons = response.data.coupons
        // userInfo.points = response.data.points
        // userInfo.favorites = response.data.favorites
        
        // 模拟数据
        userInfo.balance = '1,258.50'
        userInfo.coupons = 3
        userInfo.points = 520
        userInfo.favorites = 8
        
        orderCount.unpaid = 1
        orderCount.unshipped = 0
        orderCount.shipped = 1
        orderCount.completed = 5
        orderCount.afterSale = 0
        
      } catch (error) {
        console.error('获取用户中心数据失败:', error)
      }
    }
    
    // 菜单选择处理
    const handleMenuSelect = (index) => {
      if (route.path !== `/user/${index}`) {
        router.push(`/user/${index}`)
      }
    }
    
    // 去购物
    const goShopping = () => {
      router.push('/')
    }
    
    // 查看所有订单
    const goToOrders = (status) => {
      router.push({
        path: '/user/orders',
        query: status ? { status } : {}
      })
    }
    
    // 查看订单详情
    const viewOrderDetail = (orderId) => {
      router.push(`/order/${orderId}`)
    }
    
    onMounted(() => {
      loadUserCenterData()
    })
    
    return {
      user,
      currentDate,
      activeMenu,
      userInfo,
      orderCount,
      recentOrders,
      handleMenuSelect,
      getStatusType,
      goShopping,
      goToOrders,
      viewOrderDetail
    }
  }
}
</script>

<style scoped>
.user-center-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-color);
}

.breadcrumb {
  padding: 15px 0;
  font-size: 12px;
  color: var(--light-text);
}

.user-center {
  display: flex;
  gap: 25px;
  margin: 20px 0 40px;
}

/* 侧边栏 */
.sidebar {
  width: 240px;
  background-color: var(--card-bg);
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  position: relative;
  border: 1px solid var(--border-color);
}

.sidebar::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: var(--border-radius-lg);
  padding: 1px;
  background: linear-gradient(135deg, transparent 70%, rgba(0, 120, 215, 0.2));
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  pointer-events: none;
}

.user-info {
  padding: 30px 20px;
  text-align: center;
  border-bottom: 1px solid var(--border-color);
  position: relative;
  background: linear-gradient(135deg, rgba(0, 120, 215, 0.05), rgba(91, 235, 235, 0.05));
}

.user-info::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 10%;
  width: 80%;
  height: 1px;
  background: linear-gradient(to right, transparent, var(--border-color), transparent);
}

.avatar {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  margin: 0 auto 15px;
  overflow: hidden;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: var(--light-text);
  border: 2px solid white;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-normal);
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: var(--shadow-lg);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all var(--transition-normal);
}

.avatar:hover img {
  transform: scale(1.1);
}

.username {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-color);
}

.user-level {
  display: inline-block;
  background: var(--primary-gradient);
  color: white;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 12px;
  box-shadow: 0 3px 10px rgba(0, 120, 215, 0.2);
}

.menu-items {
  padding: 15px 0;
}

.menu-group {
  margin-bottom: 15px;
}

.menu-title {
  padding: 10px 25px;
  color: var(--light-text);
  font-size: 14px;
  position: relative;
  font-weight: 500;
}

.menu-title::before {
  content: '';
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: var(--secondary-color);
}

.user-menu {
  border-right: none;
}

.user-menu :deep(.el-menu-item) {
  height: 45px;
  line-height: 45px;
  transition: all var(--transition-normal);
}

.user-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(0, 120, 215, 0.1), transparent);
  color: var(--primary-color);
  font-weight: 500;
}

.user-menu :deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: var(--primary-gradient);
  border-radius: 0 3px 3px 0;
}

.user-menu :deep(.el-menu-item:hover) {
  background-color: rgba(0, 120, 215, 0.05);
}

.user-menu :deep(.el-menu-item .el-icon) {
  color: inherit;
  transition: all var(--transition-normal);
}

.user-menu :deep(.el-menu-item:hover .el-icon) {
  transform: translateX(2px);
}

/* 内容区域 */
.content-area {
  flex: 1;
  background-color: var(--card-bg);
  border-radius: var(--border-radius-lg);
  padding: 25px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-color);
  position: relative;
}

.content-area::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: var(--border-radius-lg);
  padding: 1px;
  background: linear-gradient(135deg, transparent 70%, rgba(0, 120, 215, 0.2));
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  pointer-events: none;
}

/* 欢迎面板 */
.welcome-panel {
  margin-bottom: 30px;
}

.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border-color);
  position: relative;
}

.welcome-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 80px;
  height: 3px;
  background: var(--primary-gradient);
  border-radius: 3px;
}

.welcome-info h2 {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-color);
  position: relative;
  display: inline-block;
}

.welcome-info p {
  color: var(--light-text);
}

.data-overview {
  display: flex;
  gap: 25px;
  margin-top: 20px;
}

.data-card {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 18px;
  background: linear-gradient(135deg, white, #f9f9fa);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-color);
  transition: all var(--transition-normal);
  position: relative;
  overflow: hidden;
}

.data-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--primary-gradient);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.data-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.data-card:hover::before {
  opacity: 1;
}

.data-icon {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(0, 120, 215, 0.1), rgba(91, 235, 235, 0.1));
  color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 15px;
  transition: all var(--transition-normal);
  box-shadow: 0 3px 10px rgba(0, 120, 215, 0.1);
}

.data-card:hover .data-icon {
  transform: scale(1.1);
  background: var(--primary-gradient);
  color: white;
}

.data-title {
  font-size: 14px;
  color: var(--light-text);
  margin-bottom: 5px;
}

.data-value {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
}

/* 订单概览 */
.order-overview, .recent-orders {
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border-color);
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 500;
}

.order-status-cards {
  display: flex;
  justify-content: space-between;
}

.status-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 15px;
  transition: background-color 0.3s;
  border-radius: 5px;
}

.status-card:hover {
  background-color: #f5f5f5;
}

.status-card .el-icon {
  font-size: 30px;
  color: #0078d7;
  margin-bottom: 10px;
}

.status-text {
  font-size: 14px;
}

/* 价格样式 */
.price {
  color: #f56c6c;
  font-weight: bold;
}

/* 空数据 */
.empty-data {
  padding: 30px 0;
  text-align: center;
}
</style> 