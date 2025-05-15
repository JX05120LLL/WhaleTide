<!-- eslint-disable -->
<template>
  <div class="brands-page">
    <app-header></app-header>
    
    <div class="container">
      <div class="page-header">
        <h1 class="section-title">品牌专区</h1>
        <div class="page-description">精选海洋品牌，为您提供高品质的海洋产品</div>
      </div>
      
      <!-- 波浪分隔线 -->
      <div class="double-wave"></div>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="brands.length > 0" class="brands-grid">
        <div 
          v-for="brand in brands" 
          :key="brand.id"
          class="brand-card"
          @click="goToBrand(brand.id)"
        >
          <div class="brand-logo">
            <img :src="formatImageUrl(brand.logo) || '/images/placeholder.png'" :alt="brand.name">
          </div>
          <h3 class="brand-name">{{ brand.name }}</h3>
          <p class="brand-description" v-if="brand.description">{{ brand.description }}</p>
          
          <!-- 波浪装饰 -->
          <div class="brand-wave"></div>
        </div>
      </div>
      
      <div v-else class="empty-brands">
        <el-empty description="暂无品牌" />
      </div>
      
      <!-- 气泡背景 -->
      <div class="bubble-bg">
        <div v-for="n in 10" :key="n" class="bubble" 
            :style="{
              left: `${Math.random() * 100}%`,
              width: `${20 + Math.random() * 30}px`,
              height: `${20 + Math.random() * 30}px`,
              animationDuration: `${3 + Math.random() * 7}s`,
              animationDelay: `${Math.random() * 5}s`
            }">
        </div>
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'
import { ElMessage } from 'element-plus'

export default {
  name: 'BrandsPage',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const router = useRouter()
    
    const loading = ref(true)
    const brands = ref([])
    
    // 格式化图片URL
    const formatImageUrl = (path) => {
      if (!path) return '/images/placeholder.png';
      
      // 检查路径是否已经包含完整URL
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      
      // 处理相对路径，确保以/开头
      const normalizedPath = path.startsWith('/') ? path : `/${path}`;
      
      // 完整URL
      return `${staticBaseURL}${normalizedPath}`;
    };
    
    // 获取品牌数据
    const fetchBrands = async () => {
      loading.value = true
      try {
        // 调用真实API获取品牌列表
        const response = await http.get('/brand/list');
        
        if (response && response.code === 200 && response.data) {
          if (Array.isArray(response.data)) {
            brands.value = response.data;
            console.log('品牌列表:', brands.value);
          } else {
            brands.value = [];
            ElMessage.warning('获取品牌列表为空');
          }
        } else {
          ElMessage.error(response?.message || '获取品牌列表失败');
          brands.value = [];
        }
      } catch (error) {
        console.error('获取品牌数据失败:', error)
        ElMessage.error('获取品牌列表失败，请稍后重试')
        brands.value = [];
      } finally {
        loading.value = false
      }
    }
    
    // 跳转到品牌详情
    const goToBrand = (id) => {
      router.push(`/brand/${id}`)
    }
    
    onMounted(() => {
      fetchBrands()
    })
    
    return {
      loading,
      brands,
      goToBrand,
      formatImageUrl
    }
  }
}
</script>

<style scoped>
.brands-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  flex: 1;
  position: relative;
}

.page-header {
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}

.page-description {
  color: var(--light-text);
  font-size: 16px;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.loading-container {
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

.brands-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  margin-top: 30px;
  position: relative;
  z-index: 2;
}

.brand-card {
  padding-top: 0;
  position: relative;
  overflow: visible;
}

.brand-card::before {
  content: '';
  position: absolute;
  top: 15px;
  right: 15px;
  width: 40px;
  height: 40px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%230078d7' opacity='0.2' d='M12 2L8 6h4v2H2v2h10v2H4v2h8v2H2v2h10v4l4-4-4-4v2H8v-2h8v-2H6v-2h10V8H6V6h6V2z'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
  z-index: 2;
  opacity: 0.7;
  transform: rotate(-15deg);
  transition: all var(--transition-normal);
}

.brand-card:hover::before {
  transform: rotate(0);
  opacity: 1;
}

.brand-logo {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(248, 249, 250, 0.7));
  position: relative;
  overflow: hidden;
  border-radius: var(--border-radius-lg) var(--border-radius-lg) 0 0;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
}

.brand-logo::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 40%;
  background: linear-gradient(to bottom, transparent, rgba(0, 120, 215, 0.05));
  z-index: 1;
}

.brand-logo img {
  max-width: 80%;
  max-height: 80%;
  object-fit: contain;
  filter: drop-shadow(0 5px 10px rgba(0, 0, 0, 0.1));
  transition: all var(--transition-normal);
  position: relative;
  z-index: 2;
}

.brand-card:hover .brand-logo img {
  transform: scale(1.1) translateY(-5px);
  filter: drop-shadow(0 10px 15px rgba(0, 120, 215, 0.15));
}

.brand-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--deep-blue);
  margin: 15px 15px 8px;
  text-align: center;
  position: relative;
  padding-bottom: 8px;
}

.brand-name::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 2px;
  background: var(--primary-gradient);
  border-radius: 2px;
  transition: width var(--transition-normal);
}

.brand-card:hover .brand-name::after {
  width: 60px;
}

.brand-description {
  font-size: 14px;
  color: var(--light-text);
  margin: 0 15px 15px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.brand-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 15px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='rgba(0, 120, 215, 0.1)'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  transform: rotate(180deg);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.brand-card:hover .brand-wave {
  opacity: 1;
}

.empty-brands {
  padding: 60px 0;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .brands-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 20px;
  }
  
  .brand-logo {
    height: 150px;
    padding: 20px;
  }
}
</style> 