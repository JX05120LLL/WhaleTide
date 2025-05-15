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
        
        <div class="search-bar" @mouseenter="handleSearchBarMouseEnter" @mouseleave="handleSearchBarMouseLeave">
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
          
          <!-- 搜索历史悬浮框 -->
          <div class="search-history-dropdown" v-if="showSearchHistory && searchHistoryList.length > 0">
            <div class="search-history-header">
              <h4>搜索历史</h4>
              <a @click="clearSearchHistory" class="clear-history">清空</a>
            </div>
            <div class="search-history-list">
              <div 
                v-for="(item, index) in searchHistoryList" 
                :key="index" 
                class="search-history-item"
                @click="useSearchHistory(item)"
              >
                <span class="history-keyword">{{ item }}</span>
                <el-icon class="search-icon-small"><Search /></el-icon>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 导航区域 -->
      <div class="header-right">
        <nav class="main-nav">
          <ul class="nav-list">
            <li class="nav-item">
              <router-link to="/" class="nav-link">首页</router-link>
            </li>
            <li class="nav-item nav-dropdown">
              <a class="nav-link dropdown-trigger">商品分类 <el-icon class="dropdown-icon"><ArrowDown /></el-icon></a>
              <div class="nav-dropdown-menu category-dropdown">
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
          <div class="tool-item user-dropdown">
            <a class="tool-link dropdown-trigger">
              <el-icon><User /></el-icon>
              <span class="tool-text">{{ isLoggedIn ? userInfo.username || '我的账户' : '登录/注册' }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </a>
            
            <div class="user-dropdown-menu">
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
    <div class="header-wave"></div>
  </header>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ArrowDown, ShoppingCart, Star, User } from '@element-plus/icons-vue'
import api from '@/api'

export default {
  name: 'AppHeader',
  components: {
    Search,
    ArrowDown,
    ShoppingCart,
    Star,
    User
  },
  directives: {
    // 点击外部指令，用于隐藏搜索历史
    clickOutside: {
      mounted(el, binding) {
        el._clickOutside = (event) => {
          if (!(el === event.target || el.contains(event.target))) {
            binding.value(event)
          }
        }
        document.body.addEventListener('click', el._clickOutside)
      },
      unmounted(el) {
        document.body.removeEventListener('click', el._clickOutside)
      }
    }
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const searchKeyword = ref('')
    const showSearchHistory = ref(false)
    const searchHistoryList = ref([])
    
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
    
    // 鼠标悬浮搜索栏时显示搜索历史
    const handleSearchBarMouseEnter = () => {
      if (isLoggedIn.value) {
        loadSearchHistory();
        showSearchHistory.value = true;
      }
    }
    
    // 鼠标离开搜索栏时隐藏搜索历史
    const handleSearchBarMouseLeave = () => {
      showSearchHistory.value = false;
    }
    
    // 搜索处理
    const handleSearch = async () => {
      if (searchKeyword.value.trim()) {
        // 添加搜索历史
        await addToSearchHistory(searchKeyword.value.trim())
        
        // 跳转到搜索结果页
        router.push(`/search?keyword=${encodeURIComponent(searchKeyword.value.trim())}`)
        
        // 清空搜索框
        searchKeyword.value = ''
        // 隐藏搜索历史
        showSearchHistory.value = false
      }
    }
    
    // 获取搜索历史
    const loadSearchHistory = async () => {
      try {
        if (isLoggedIn.value && searchHistoryList.value.length === 0) {
          const res = await api.history.getSearchHistory(10)
          if (res.code === 200 && res.data) {
            searchHistoryList.value = res.data
          }
        }
      } catch (error) {
        console.error('获取搜索历史失败:', error)
      }
    }
    
    // 添加搜索历史
    const addToSearchHistory = async (keyword) => {
      try {
        if (isLoggedIn.value && keyword.trim()) {
          await api.history.addSearchHistory(keyword.trim())
          // 重新加载搜索历史
          await loadSearchHistory()
        }
      } catch (error) {
        console.error('添加搜索历史失败:', error)
      }
    }
    
    // 清空搜索历史
    const clearSearchHistory = async () => {
      try {
        if (isLoggedIn.value) {
          await api.history.clearSearchHistory()
          searchHistoryList.value = []
          ElMessage.success('已清空搜索历史')
        }
      } catch (error) {
        console.error('清空搜索历史失败:', error)
        ElMessage.error('清空搜索历史失败')
      }
    }
    
    // 使用搜索历史记录进行搜索
    const useSearchHistory = (keyword) => {
      searchKeyword.value = keyword
      handleSearch()
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
      
      // 加载搜索历史
      if (isLoggedIn.value) {
        loadSearchHistory()
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
      handleLogout,
      showSearchHistory,
      searchHistoryList,
      loadSearchHistory,
      clearSearchHistory,
      useSearchHistory,
      handleSearchBarMouseEnter,
      handleSearchBarMouseLeave
    }
  }
}
</script>

<style scoped>
.main-header {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
  color: white;
  position: relative;
  box-shadow: 0 4px 20px rgba(0, 58, 112, 0.2);
  z-index: 100;
}

.header-wave {
  position: absolute;
  bottom: -15px;
  left: 0;
  width: 100%;
  height: 15px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z' fill='white' opacity='.3'/%3E%3Cpath d='M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z' fill='white' opacity='.5'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  z-index: 1;
  animation: waveMove 12s linear infinite alternate;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  width: 1200px;
  margin: 0 auto;
  position: relative;
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  margin-right: 30px;
  position: relative;
  z-index: 2;
}

.logo-img {
  height: 40px;
  margin-right: 10px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  transition: all var(--transition-normal);
}

.logo:hover .logo-img {
  transform: scale(1.1) rotate(-5deg);
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.logo-text {
  font-size: 22px;
  font-weight: bold;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  position: relative;
}

.logo-text::after {
  content: '';
  position: absolute;
  bottom: -3px;
  left: 0;
  width: 0;
  height: 2px;
  background-color: white;
  transition: width var(--transition-normal);
}

.logo:hover .logo-text::after {
  width: 100%;
}

.search-bar {
  width: 350px;
  position: relative;
}

.search-input {
  border-radius: 20px;
}

.search-input :deep(.el-input__inner) {
  border-radius: 20px;
  border: none;
  height: 40px;
  transition: all var(--transition-normal);
  background-color: rgba(255, 255, 255, 0.9);
  padding-left: 18px;
}

.search-input :deep(.el-input__inner):focus {
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.3);
  background-color: rgba(255, 255, 255, 1);
}

.search-icon {
  color: var(--primary-color);
  cursor: pointer;
  font-size: 18px;
  transition: all var(--transition-normal);
}

.search-icon:hover {
  transform: scale(1.2);
  color: var(--primary-dark);
}

/* 搜索历史悬浮框样式 */
.search-history-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  z-index: 999;
  background-color: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  margin-top: 8px;
  padding: 12px 0;
  animation: fadeIn 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid rgba(0, 120, 215, 0.1);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.search-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px 10px;
  border-bottom: 1px solid var(--border-color);
}

.search-history-header h4 {
  margin: 0;
  color: var(--text-color);
  font-size: 14px;
  font-weight: 500;
}

.clear-history {
  color: var(--primary-color);
  font-size: 12px;
  cursor: pointer;
  transition: color var(--transition-normal);
  position: relative;
  overflow: hidden;
  padding: 2px 5px;
}

.clear-history::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
  background-color: var(--primary-color);
  transform: scaleX(0);
  transform-origin: right;
  transition: transform var(--transition-normal);
}

.clear-history:hover::before {
  transform: scaleX(1);
  transform-origin: left;
}

.search-history-list {
  padding: 5px 0;
}

.search-history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  cursor: pointer;
  transition: all var(--transition-normal);
  position: relative;
  overflow: hidden;
}

.search-history-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 3px;
  height: 100%;
  background: var(--primary-gradient);
  opacity: 0;
  transform: translateX(-3px);
  transition: all var(--transition-normal);
}

.search-history-item:hover::before {
  opacity: 1;
  transform: translateX(0);
}

.search-history-item:hover {
  background-color: rgba(0, 120, 215, 0.05);
  padding-left: 20px;
}

.history-keyword {
  color: var(--text-color);
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-normal);
}

.search-icon-small {
  color: var(--light-text);
  font-size: 14px;
  transition: all var(--transition-normal);
}

.search-history-item:hover .history-keyword {
  color: var(--primary-color);
}

.search-history-item:hover .search-icon-small {
  color: var(--primary-color);
  transform: scale(1.1);
}

.header-right {
  display: flex;
  align-items: center;
}

/* 导航菜单 */
.main-nav {
  margin-right: 20px;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  margin: 0 15px;
  position: relative;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-size: 15px;
  font-weight: 500;
  display: flex;
  align-items: center;
  padding: 8px 0;
  position: relative;
  transition: all var(--transition-normal);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: white;
  transform: scaleX(0);
  transform-origin: center;
  transition: transform var(--transition-normal);
  border-radius: 2px;
}

.nav-link:hover::after,
.router-link-active::after {
  transform: scaleX(0.8);
}

.dropdown-icon {
  margin-left: 5px;
  transition: transform var(--transition-normal);
}

.dropdown-trigger:hover .dropdown-icon {
  transform: rotate(180deg);
}

/* 导航下拉菜单样式 */
.nav-dropdown {
  position: relative;
}

.nav-dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  margin-top: 10px;
  padding: 15px 0;
  min-width: 180px;
  z-index: 1000;
  /* 设置为固定值，不是none，这样阻止误触但不阻止互动 */
  opacity: 0;
  visibility: hidden;
  /* 使用很短的时间来减少延迟感 */
  transition: all 0.2s ease;
  border: 1px solid rgba(0, 120, 215, 0.1);
}

.nav-dropdown:hover .nav-dropdown-menu {
  opacity: 1;
  visibility: visible;
}

.nav-dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  left: 20px;
  width: 12px;
  height: 12px;
  background: white;
  transform: rotate(45deg);
  border-top: 1px solid rgba(0, 120, 215, 0.1);
  border-left: 1px solid rgba(0, 120, 215, 0.1);
}

/* 用户下拉菜单样式 */
.user-dropdown {
  position: relative;
}

.user-dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  left: auto;
  background: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  margin-top: 10px;
  padding: 15px 0;
  min-width: 180px;
  z-index: 1000;
  /* 设置为固定值，不是none，这样阻止误触但不阻止互动 */
  opacity: 0;
  visibility: hidden;
  /* 使用很短的时间来减少延迟感 */
  transition: all 0.2s ease;
  border: 1px solid rgba(0, 120, 215, 0.1);
}

.user-dropdown:hover .user-dropdown-menu {
  opacity: 1;
  visibility: visible;
}

.user-dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  right: 20px;
  width: 12px;
  height: 12px;
  background: white;
  transform: rotate(45deg);
  border-top: 1px solid rgba(0, 120, 215, 0.1);
  border-left: 1px solid rgba(0, 120, 215, 0.1);
}

/* 菜单项样式 */
.dropdown-item {
  display: block;
  padding: 10px 18px;
  color: var(--text-color);
  text-decoration: none;
  font-size: 14px;
  white-space: nowrap;
  transition: all var(--transition-normal);
  border-left: 3px solid transparent;
  position: relative;
  overflow: hidden;
}

.dropdown-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 18px;
  width: calc(100% - 36px);
  height: 1px;
  background: linear-gradient(to right, transparent, var(--border-color) 20%, var(--border-color) 80%, transparent);
  transform: scaleX(0);
  transition: transform var(--transition-normal);
}

.dropdown-item:hover::after {
  transform: scaleX(1);
}

.dropdown-item:hover {
  background-color: rgba(0, 120, 215, 0.05);
  color: var(--primary-color);
  border-left: 3px solid var(--primary-color);
  padding-left: 22px;
}

/* 分类下拉菜单样式 */
.category-dropdown {
  width: 400px;
  padding: 15px;
}

.parent-category {
  margin-bottom: 15px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 15px;
  position: relative;
  overflow: hidden;
}

.parent-category:last-child {
  margin-bottom: 0;
  border-bottom: none;
  padding-bottom: 0;
}

.parent-category-link {
  display: block;
  font-weight: 500;
  color: var(--text-color);
  text-decoration: none;
  font-size: 15px;
  padding: 5px 0;
  transition: all var(--transition-normal);
  position: relative;
}

.parent-category-link::before {
  content: '';
  position: absolute;
  left: -15px;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  background-color: var(--primary-color);
  border-radius: 50%;
  opacity: 0;
  transition: all var(--transition-normal);
}

.parent-category-link:hover::before {
  opacity: 1;
  left: -10px;
}

.parent-category-link:hover {
  color: var(--primary-color);
  padding-left: 8px;
}

.children-categories {
  display: flex;
  flex-wrap: wrap;
  margin-top: 8px;
}

.child-category-link {
  display: block;
  color: var(--light-text);
  text-decoration: none;
  font-size: 13px;
  margin-right: 15px;
  margin-bottom: 8px;
  white-space: nowrap;
  transition: all var(--transition-normal);
  padding-bottom: 2px;
  border-bottom: 1px solid transparent;
  position: relative;
}

.child-category-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
  background: var(--primary-gradient);
  transform: scaleX(0);
  transition: transform var(--transition-normal);
  transform-origin: left;
}

.child-category-link:hover::after {
  transform: scaleX(1);
}

.child-category-link:hover {
  color: var(--primary-color);
}

/* 用户工具栏 */
.user-tools {
  display: flex;
  align-items: center;
}

.tool-item {
  margin-left: 20px;
  position: relative;
}

.tool-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white;
  font-size: 14px;
  cursor: pointer;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
  transition: all var(--transition-normal);
  position: relative;
}

.tool-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  width: 4px;
  height: 4px;
  background-color: white;
  border-radius: 50%;
  transform: translateX(-50%) scale(0);
  transition: transform var(--transition-normal);
}

.tool-link:hover::after {
  transform: translateX(-50%) scale(1);
}

.tool-link:hover {
  transform: translateY(-3px);
}

.tool-link .el-icon {
  font-size: 18px;
  margin-right: 3px;
  transition: transform var(--transition-fast);
}

.tool-link:hover .el-icon {
  transform: scale(1.2);
}

.tool-text {
  margin: 0 5px;
  white-space: nowrap;
}

.cart-badge {
  display: flex;
  align-items: center;
  position: relative;
}

.cart-badge :deep(.el-badge__content) {
  background: var(--secondary-gradient);
  border: none;
  box-shadow: 0 2px 6px rgba(255, 122, 69, 0.3);
}

@media (max-width: 1200px) {
  .search-bar {
    width: 250px;
  }
  
  .header-container {
    padding: 10px;
    width: 100%;
  }
  
  .nav-item {
    margin: 0 8px;
  }
  
  .tool-item {
    margin-left: 12px;
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
    display: none;
  }
}
</style> 