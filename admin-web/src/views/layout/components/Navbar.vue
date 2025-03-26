<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
    <breadcrumb></breadcrumb>
    <div class="logo-container">
      <svg class="logo-svg" width="200" height="200" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg">
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
    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  position: relative;

  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }

  .logo-container {
    position: fixed;
    top: 60px;
    right: 20px;
    width: 200px;
    height: 200px;
    z-index: 2000;

    .logo-svg {
      width: 100%;
      height: 100%;
      transition: transform 0.3s;

      &:hover {
        transform: scale(1.05);
      }
    }
  }
}
</style>

