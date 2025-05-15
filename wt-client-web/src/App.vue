<template>
  <router-view />
</template>

<script>
import { onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const store = useStore()
    const router = useRouter()
    
    onMounted(() => {
      // 如果已有token则自动获取用户信息
      const token = localStorage.getItem('token')
      if (token) {
        console.log('App.vue: 检测到token存在，准备获取用户信息')
        console.log('当前token:', token)
        
        store.dispatch('getUserInfo')
          .then(userInfo => {
            console.log('用户信息获取成功:', userInfo)
          })
          .catch(error => {
            console.error('自动获取用户信息失败:', error)
            // 如果token无效，自动清除
            if (error.message && error.message.includes('登录已过期')) {
              console.log('清除无效token')
              localStorage.removeItem('token')
              store.commit('logout')
              
              // 如果用户在需要登录的页面，重定向到登录页
              const currentPath = router.currentRoute.value.path
              if (currentPath.startsWith('/user/') || currentPath === '/cart') {
                router.push('/login')
              }
            }
          })
      } else {
        console.log('App.vue: 未检测到token')
      }
    })
  }
}
</script>

<style>
:root {
  --primary-color: #0078d7;
  --primary-light: #4aa3f5;
  --primary-dark: #005ea6;
  --secondary-color: #ffa500;
  --text-color: #333;
  --light-text: #666;
  --bg-color: #f5f7fa;
  --card-bg: #ffffff;
  --border-color: #e0e6ed;
  --wave-color: rgba(0, 120, 215, 0.1);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

body {
  background-color: var(--bg-color);
  color: var(--text-color);
}

a {
  text-decoration: none;
  color: inherit;
  transition: color 0.3s, transform 0.3s;
}

.container {
  width: 1200px;
  margin: 0 auto;
}

/* 波浪效果类 */
.wave-divider {
  height: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='%23ffffff'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  margin: 20px 0;
}

/* 修改Element Plus默认样式 */
.el-button--primary {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  transition: all 0.3s;
}

.el-button--primary:hover {
  background-color: var(--primary-dark);
  border-color: var(--primary-dark);
  transform: translateY(-2px);
}

.el-card {
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.el-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* 通用样式 */
.text-center {
  text-align: center;
}

.mt-3 {
  margin-top: 15px;
}

.mb-3 {
  margin-bottom: 15px;
}

/* 卡片和模块通用样式 */
.card {
  background-color: var(--card-bg);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* 标题美化 */
.section-title {
  position: relative;
  padding-left: 15px;
  margin-bottom: 20px;
  font-weight: 500;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 5px;
  height: 20px;
  width: 5px;
  background-color: var(--primary-color);
  border-radius: 5px;
}
</style> 