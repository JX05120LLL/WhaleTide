<template>
  <div class="sidebar-wrapper">
    <div class="logo-container">
      <svg class="logo-svg" width="32" height="32" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg">
        <!-- 背景圆形 -->
        <circle cx="100" cy="100" r="90" fill="#E6F3FF" stroke="#409EFF" stroke-width="4"/>
        
        <!-- 波浪 -->
        <path d="M30 120 Q50 100 70 120 Q90 140 110 120 Q130 100 150 120 Q170 140 170 120" 
              fill="none" 
              stroke="#409EFF" 
              stroke-width="4" 
              stroke-linecap="round"/>
        
        <!-- 鲸鱼身体 -->
        <path d="M60 100 C80 80 120 80 140 100 C160 120 160 140 140 160 C120 180 80 180 60 160 C40 140 40 120 60 100" 
              fill="#409EFF"/>
        
        <!-- 鲸鱼尾巴 -->
        <path d="M60 160 C40 160 30 150 30 140 C30 130 40 120 60 120" 
              fill="#409EFF"/>
        
        <!-- 鲸鱼眼睛 -->
        <circle cx="120" cy="120" r="5" fill="white"/>
        <circle cx="120" cy="120" r="2" fill="black"/>
        
        <!-- 鲸鱼喷水 -->
        <path d="M100 80 Q100 60 90 50" 
              fill="none" 
              stroke="#409EFF" 
              stroke-width="3" 
              stroke-linecap="round"/>
      </svg>
      <span v-if="!isCollapse" class="logo-text">鲸浪商城</span>
    </div>
    <scroll-bar>
      <el-menu
        mode="vertical"
        :show-timeout="200"
        :default-active="$route.path"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <sidebar-item :routes="routes"></sidebar-item>
      </el-menu>
    </scroll-bar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import ScrollBar from '@/components/ScrollBar'

export default {
  components: { SidebarItem, ScrollBar },
  computed: {
    ...mapGetters([
      'sidebar',
      'routers'
    ]),
    routes() {
      // return this.$router.options.routes
      return this.routers
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style scoped>
.sidebar-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-container {
  height: 100%;
  overflow: hidden;
}

.logo-container {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #304156;
  height: 50px;
  overflow: hidden;
}

.logo-svg {
  min-width: 32px;
  margin-right: 10px;
}

.logo-text {
  color: #fff;
  font-size: 20px;
  font-weight: 600;
  white-space: nowrap;
  transition: opacity 0.3s;
}
</style>
