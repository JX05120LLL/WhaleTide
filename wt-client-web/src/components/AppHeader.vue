/* eslint-disable */
<template>
  <header class="main-header">
    <div class="header-container">
      <!-- Logo和搜索区域 -->
      <div class="header-left">
        <router-link to="/" class="logo">
          <img src="../assets/logo.png" alt="鲸浪商城" class="logo-img" />
          <span class="logo-text">鲸浪商城</span>
        </router-link>
        
        <div class="search-bar">
          <el-input
            placeholder="搜索海洋商品..."
            v-model="searchKeyword"
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #suffix>
              <el-icon class="search-icon" @click="handleSearch"><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
      
      <!-- 导航区域 -->
      <div class="header-right">
        <nav class="main-nav">
          <ul class="nav-list">
            <li class="nav-item">
              <router-link to="/" class="nav-link">首页</router-link>
            </li>
            <li class="nav-item dropdown-wrapper">
              <a class="nav-link dropdown-trigger">商品分类 <el-icon class="dropdown-icon"><ArrowDown /></el-icon></a>
              <div class="dropdown-menu category-dropdown">
                <div v-if="safeCategories.length === 0" class="dropdown-item">暂无分类数据</div>
                
                <div v-for="category in safeCategories" :key="category.id" class="parent-category">
                  <router-link 
                    :to="`/category/${category.id}`" 
                    class="parent-category-link"
                  >
                    {{ category.name }}
                  </router-link>
                  
                  <!-- 子分类容器 -->
                  <div class="children-categories" v-if="category.children && category.children.length > 0">
                    <router-link 
                      v-for="child in category.children" 
                      :key="child.id"
                      :to="`/category/${child.id}`" 
                      class="child-category-link"
                    >
                      {{ child.name }}
                    </router-link>
                  </div>
                </div>
              </div>
            </li>
            <li class="nav-item">
              <router-link to="/category/new" class="nav-link">新品上市</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/promotion" class="nav-link">优惠专区</router-link>
            </li>
          </ul>
        </nav>
        
        <!-- 用户工具栏 -->
        <div class="user-tools">
          <!-- 购物车 -->
          <div class="tool-item cart-tool">
            <router-link to="/cart" class="tool-link">
              <el-badge :value="cartCount" :hidden="cartCount === 0" class="cart-badge">
                <el-icon><ShoppingCart /></el-icon>
              </el-badge>
              <span class="tool-text">购物车</span>
            </router-link>
          </div>
          
          <!-- 收藏 -->
          <div class="tool-item">
            <router-link to="/user/favorites" class="tool-link">
              <el-icon><Star /></el-icon>
              <span class="tool-text">收藏</span>
            </router-link>
          </div>
          
          <!-- 用户中心 -->
          <div class="tool-item dropdown-wrapper user-tool">
            <a class="tool-link dropdown-trigger">
              <el-icon><User /></el-icon>
              <span class="tool-text">{{ isLoggedIn ? userInfo.username || '我的账户' : '登录/注册' }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </a>
            
            <div class="dropdown-menu user-dropdown">
              <template v-if="isLoggedIn">
                <router-link to="/user/profile" class="dropdown-item">个人信息</router-link>
                <router-link to="/user/orders" class="dropdown-item">我的订单</router-link>
                <router-link to="/user/favorites" class="dropdown-item">我的收藏</router-link>
                <router-link to="/user/history" class="dropdown-item">浏览历史</router-link>
                <router-link to="/user/address" class="dropdown-item">收货地址</router-link>
                <router-link to="/user/security" class="dropdown-item">账号安全</router-link>
                <router-link to="/user/reviews" class="dropdown-item">评价晒单</router-link>
                <a @click="handleLogout" class="dropdown-item">退出登录</a>
              </template>
              <template v-else>
                <router-link to="/login" class="dropdown-item">登录</router-link>
                <router-link to="/register" class="dropdown-item">注册</router-link>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ArrowDown, ShoppingCart, Star, User } from '@element-plus/icons-vue'

export default {
  name: 'AppHeader',
  components: {
    Search,
    ArrowDown,
    ShoppingCart,
    Star,
    User
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const searchKeyword = ref('')
    
    // 从store获取数据
    const cartCount = computed(() => store.getters.cartCount)
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    const userInfo = computed(() => store.getters.currentUser || {})
    
    // 安全获取分类数据，过滤掉无效项
    const categories = computed(() => store.getters.allCategories)
    const safeCategories = computed(() => {
      // 确保返回的是数组类型
      const cats = categories.value || []
      return Array.isArray(cats) ? cats.filter(cat => cat && cat.id != null) : []
    })
    
    // 搜索处理
    const handleSearch = () => {
      if (searchKeyword.value.trim()) {
        router.push(`/search?keyword=${encodeURIComponent(searchKeyword.value.trim())}`)
        searchKeyword.value = ''
      }
    }
    
    // 退出登录
    const handleLogout = () => {
      store.dispatch('logout')
      ElMessage({
        message: '已退出登录',
        type: 'success'
      })
      router.push('/')
    }
    
    // 获取分类数据
    onMounted(() => {
      if (categories.value.length === 0) {
        store.dispatch('fetchCategories').catch(error => {
          console.error('获取分类失败:', error)
          ElMessage.error('获取分类数据失败')
        })
      }
    })
    
    return {
      searchKeyword,
      cartCount,
      isLoggedIn,
      userInfo,
      categories,
      safeCategories,
      handleSearch,
      handleLogout
    }
  }
}
</script>

<style scoped>
.main-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

/* Logo区域 */
.header-left {
  display: flex;
  align-items: center;
  flex: 0 0 auto;
  width: 40%;
}

.logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  margin-right: 20px;
}

.logo-img {
  height: 38px;
  width: 38px;
  object-fit: contain;
  border-radius: 4px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #0078d7;
  margin-left: 8px;
  white-space: nowrap;
}

/* 搜索栏 */
.search-bar {
  width: 300px;
  flex-shrink: 1;
}

.search-input {
  width: 100%;
}

.search-icon {
  cursor: pointer;
}

/* 导航区域 */
.header-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex: 1;
}

.main-nav {
  margin-right: 10px;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  flex-wrap: nowrap;
}

.nav-item {
  margin: 0 10px;
  position: relative;
  white-space: nowrap;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  display: flex;
  align-items: center;
  padding: 5px 0;
}

.nav-link:hover {
  color: #0078d7;
}

.dropdown-icon {
  margin-left: 3px;
  font-size: 12px;
}

/* 下拉菜单 */
.dropdown-wrapper {
  position: relative;
}

.dropdown-trigger {
  cursor: pointer;
}

.dropdown-wrapper:hover .dropdown-menu {
  display: block;
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.dropdown-menu {
  display: block;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  border-radius: 4px;
  padding: 10px 0;
  min-width: 150px;
  z-index: 10;
  opacity: 0;
  transform: translateY(10px);
  transition: opacity 0.3s, transform 0.3s;
  pointer-events: none;
}

.dropdown-item {
  display: block;
  padding: 8px 15px;
  color: #333;
  text-decoration: none;
  font-size: 14px;
  white-space: nowrap;
}

.dropdown-item:hover {
  background-color: #f5f7fa;
  color: #0078d7;
}

/* 分类下拉菜单样式 */
.category-dropdown {
  width: 400px;
  padding: 15px;
}

.parent-category {
  margin-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 10px;
}

.parent-category:last-child {
  margin-bottom: 0;
  border-bottom: none;
  padding-bottom: 0;
}

.parent-category-link {
  display: block;
  font-weight: 500;
  color: #333;
  text-decoration: none;
  font-size: 14px;
  padding: 5px 0;
}

.parent-category-link:hover {
  color: #0078d7;
}

.children-categories {
  display: flex;
  flex-wrap: wrap;
  margin-top: 5px;
}

.child-category-link {
  display: block;
  color: #666;
  text-decoration: none;
  font-size: 13px;
  margin-right: 15px;
  margin-bottom: 5px;
  white-space: nowrap;
}

.child-category-link:hover {
  color: #0078d7;
  text-decoration: underline;
}

/* 用户工具栏 */
.user-tools {
  display: flex;
  align-items: center;
}

.tool-item {
  margin-left: 15px;
  position: relative;
}

.tool-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  font-size: 14px;
  cursor: pointer;
}

.tool-link i {
  font-size: 18px;
}

.tool-text {
  margin: 0 5px;
  white-space: nowrap;
}

.user-tool .tool-link {
  flex-direction: row;
  align-items: center;
}

.cart-badge {
  display: flex;
  align-items: center;
}

.user-dropdown {
  right: 0;
  left: auto;
}

@media (max-width: 1200px) {
  .search-bar {
    width: 250px;
  }
  
  .header-container {
    padding: 10px;
  }
  
  .nav-item {
    margin: 0 8px;
  }
  
  .tool-item {
    margin-left: 10px;
  }
  
  .category-dropdown {
    width: 350px;
  }
}

@media (max-width: 992px) {
  .header-container {
    flex-wrap: wrap;
    justify-content: space-between;
  }
  
  .header-left {
    width: 100%;
    margin-bottom: 10px;
    justify-content: space-between;
  }
  
  .header-right {
    width: 100%;
    justify-content: space-between;
  }
  
  .search-bar {
    width: 60%;
  }
  
  .category-dropdown {
    width: 300px;
  }
}

@media (max-width: 768px) {
  .nav-item {
    margin: 0 5px;
  }
  
  .tool-item {
    margin-left: 8px;
  }
  
  .tool-text {
    font-size: 12px;
  }
  
  .category-dropdown {
    width: 250px;
  }
}
</style> 