<template>
<!-- eslint-disable -->
  <div class="home-page">
    <app-header></app-header>
    
    <div class="container">
      <!-- 轮播图 -->
      <div class="banner-section">
        <el-carousel height="400px" indicator-position="outside" arrow="always">
          <el-carousel-item v-for="item in banners" :key="item.id">
            <div class="banner-item" :style="{ backgroundImage: `url(${getImageUrl(item.image)})` }">
              <!-- 移除遮挡文字内容 -->
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <!-- 增加波浪分隔线 -->
      <div class="wave-divider"></div>
      
      <!-- 快捷入口 -->
      <div class="quick-entry">
        <div class="entry-item" v-for="item in quickEntries" :key="item.id" @click="goToCategory(item.link)">
          <div class="entry-icon">
            <el-icon><component :is="item.icon"></component></el-icon>
          </div>
          <div class="entry-title">{{ item.title }}</div>
        </div>
      </div>
      
      <!-- 热门商品 -->
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">热门商品</h2>
          <el-button link @click="goToCategory('hot')">查看更多 <el-icon><ArrowRight /></el-icon></el-button>
        </div>
        
        <div class="product-grid">
          <div class="product-card" v-for="product in hotProducts" :key="product.id" @click="goToProduct(product.id)">
            <div class="product-image">
              <el-image :src="getImageUrl(product.image)" fit="contain"></el-image>
              <div class="product-tag" v-if="product.tag">{{ product.tag }}</div>
            </div>
            <div class="product-info">
              <h3 class="product-title">{{ product.name }}</h3>
              <div class="product-price">
                <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
                <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toFixed(2) }}</span>
              </div>
              <div class="product-footer">
                <div class="sold-count">已售 {{ product.soldCount }}</div>
                <el-button 
                  type="primary" 
                  size="small" 
                  circle
                  @click.stop="addToCart(product)">
                  <el-icon><ShoppingCart /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 波浪分隔线 -->
      <div class="wave-divider blue-wave"></div>
      
      <!-- 推荐专题 -->
      <div class="feature-section">
        <div class="section-header">
          <h2 class="section-title">精选专题</h2>
          <el-button link @click="goToFeature('all')">查看更多 <el-icon><ArrowRight /></el-icon></el-button>
        </div>
        
        <div class="feature-container">
          <div class="feature-item" v-for="(feature, index) in features" :key="index" @click="goToFeature(feature.id)">
            <div class="feature-image">
              <el-image :src="getImageUrl(feature.image)" fit="cover"></el-image>
              <div class="feature-overlay">
                <span class="view-more">查看详情</span>
              </div>
            </div>
            <div class="feature-content">
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.desc }}</p>
            </div>
            <div class="feature-wave"></div>
          </div>
        </div>
        
        <!-- 气泡背景装饰 -->
        <div class="bubble-bg">
          <div v-for="n in 6" :key="n" class="bubble" 
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
      
      <!-- 新品上市 -->
      <div class="section-container">
        <div class="section-header">
          <h2>新品上市</h2>
          <el-button link @click="goToCategory('new')">查看更多</el-button>
        </div>
        
        <div class="product-grid">
          <div class="product-card" v-for="product in newProducts" :key="product.id" @click="goToProduct(product.id)">
            <div class="product-image">
              <el-image :src="getImageUrl(product.image)" fit="contain"></el-image>
              <div class="product-tag new-tag" v-if="product.tag">{{ product.tag }}</div>
            </div>
            <div class="product-info">
              <h3 class="product-title">{{ product.name }}</h3>
              <div class="product-price">
                <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
                <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toFixed(2) }}</span>
              </div>
              <div class="product-footer">
                <div class="sold-count">已售 {{ product.soldCount }}</div>
                <el-button 
                  type="primary" 
                  size="small" 
                  circle
                  @click.stop="addToCart(product)">
                  <el-icon><ShoppingCart /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 品牌专区 -->
      <div class="brand-section">
        <div class="section-header">
          <h2 class="section-title">品牌专区</h2>
          <el-button link @click="router.push('/brands')">全部品牌 <el-icon><ArrowRight /></el-icon></el-button>
        </div>
        
        <div class="brand-container">
          <div 
            class="brand-card" 
            v-for="brand in brands" 
            :key="brand.id" 
            @click="goToBrand(brand.id)"
          >
            <div class="brand-logo">
              <el-image :src="formatImageUrl(brand.logo)" fit="contain" style="width: 100%; height: 100%;"></el-image>
            </div>
            <h3 class="brand-name">{{ brand.name }}</h3>
            <div class="brand-wave"></div>
          </div>
        </div>
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { 
  ShoppingCart, Goods, Present, MagicStick, 
  Discount, Collection, Tickets, ArrowRight
} from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import api from '@/api'
import { getImageUrl } from '@/utils/api'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'

export default {
  name: 'HomePage',
  components: {
    AppHeader,
    AppFooter,
    ShoppingCart,
    Goods,
    Present,
    MagicStick,
    Discount,
    Collection,
    Tickets,
    ArrowRight
  },
  setup() {
    const router = useRouter()
    const store = useStore()
    
    // 轮播图数据
    const banners = ref([])
    
    // 快捷入口
    const quickEntries = ref([
      {
        id: 1,
        title: '新品发布',
        icon: 'Present',
        link: 'new'
      },
      {
        id: 2,
        title: '热卖商品',
        icon: 'Goods',
        link: 'hot'
      },
      {
        id: 3,
        title: '限时特惠',
        icon: 'Discount',
        link: 'discount'
      },
      {
        id: 4,
        title: '优选好物',
        icon: 'MagicStick',
        link: 'selected'
      },
      {
        id: 5,
        title: '品牌专区',
        icon: 'Collection',
        link: '/brands'
      }
    ])
    
    // 热门商品
    const hotProducts = ref([])
    
    // 新品上市
    const newProducts = ref([])
    
    // 精选专题
    const features = ref([])
    
    // 品牌专区
    const brands = ref([])
    
    // 跳转到商品详情
    const goToProduct = (id) => {
      router.push(`/product/${id}`)
    }
    
    // 跳转到分类页面
    const goToCategory = (category) => {
      // 处理不同路径格式
      if (category.startsWith('/')) {
        // 如果已经是完整路径（以/开头），则直接跳转
        router.push(category);
      } else {
        // 否则将其视为category ID，添加前缀
        router.push(`/category/${category}`);
      }
    }
    
    // 跳转到专题页面
    const goToFeature = (id) => {
      router.push(`/feature/${id}`)
    }
    
    // 跳转到品牌页面
    const goToBrand = (id) => {
      router.push(`/brand/${id}`)
    }
    
    // 添加到购物车
    const addToCart = (product) => {
      store.dispatch('addToCart', {
        id: product.id,
        name: product.name,
        price: product.price,
        image: product.image,
        quantity: 1
      })
      
      ElMessage({
        type: 'success',
        message: `已将 ${product.name} 加入购物车`
      })
    }
    
    // 获取轮播图数据
    const fetchBanners = async () => {
      try {
        const res = await api.home.getBanners()
        banners.value = res.data || []
      } catch (error) {
        console.error('获取轮播图失败:', error)
        ElMessage.error('获取轮播图数据失败')
      }
    }
    
    // 获取热门商品
    const fetchHotProducts = async () => {
      try {
        const res = await api.home.getHotProducts({ limit: 8 })
        hotProducts.value = res.data || []
      } catch (error) {
        console.error('获取热门商品失败:', error)
        ElMessage.error('获取热门商品数据失败')
      }
    }
    
    // 获取新品
    const fetchNewProducts = async () => {
      try {
        const res = await api.home.getNewProducts({ limit: 8 })
        newProducts.value = res.data || []
      } catch (error) {
        console.error('获取新品失败:', error)
        ElMessage.error('获取新品数据失败')
      }
    }
    
    // 获取品牌
    const fetchBrands = async () => {
      try {
        // 使用真实API获取精选品牌列表
        const res = await http.get('/brand/featured')
        if (res && res.code === 200 && res.data) {
          brands.value = res.data || []
        } else {
          console.error('获取品牌数据失败:', res.message)
          brands.value = []
        }
      } catch (error) {
        console.error('获取品牌失败:', error)
        ElMessage.error('获取品牌数据失败')
        brands.value = []
      }
    }
    
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
    
    // 初始化
    onMounted(() => {
      fetchBanners()
      fetchHotProducts()
      fetchNewProducts()
      fetchBrands()
    })
    
    return {
      banners,
      quickEntries,
      hotProducts,
      newProducts,
      features,
      brands,
      goToProduct,
      goToCategory,
      goToFeature,
      goToBrand,
      addToCart,
      getImageUrl,
      formatImageUrl,
      router
    }
  }
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 轮播图样式 */
.banner-section {
  margin-bottom: 20px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.banner-item {
  height: 400px;
  background-size: cover;
  background-position: center;
  border-radius: 12px;
  position: relative;
}

.banner-section :deep(.el-carousel__arrow) {
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  transition: all 0.3s;
}

.banner-section :deep(.el-carousel__arrow:hover) {
  background-color: var(--primary-color);
  transform: scale(1.1);
}

.banner-section :deep(.el-carousel__indicators) {
  bottom: 20px;
}

.banner-section :deep(.el-carousel__indicator) {
  padding: 2px;
}

.banner-section :deep(.el-carousel__indicator--active .el-carousel__button) {
  background-color: var(--primary-color);
  width: 30px;
  border-radius: 4px;
}

.banner-section :deep(.el-carousel__button) {
  background-color: rgba(255, 255, 255, 0.6);
  border-radius: 4px;
  height: 8px;
  width: 15px;
  transition: all 0.3s;
}

/* 波浪分隔线 */
.wave-divider {
  height: 40px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='%23ffffff'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  margin: 30px 0;
  position: relative;
  animation: waveMove 8s ease-in-out infinite alternate;
}

.blue-wave {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='rgba(0, 120, 215, 0.15)'/%3E%3C/svg%3E");
}

/* 快捷入口 */
.quick-entry {
  display: flex;
  justify-content: space-between;
  margin-bottom: 40px;
  background-color: var(--card-bg);
  padding: 25px;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  position: relative;
  overflow: hidden;
}

.quick-entry::before {
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

.entry-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: all var(--transition-normal);
  padding: 15px 10px;
  border-radius: var(--border-radius-md);
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.entry-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(0, 120, 215, 0.05), rgba(91, 235, 235, 0.05));
  border-radius: var(--border-radius-md);
  z-index: -1;
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.entry-item:hover::before {
  opacity: 1;
}

.entry-item:hover {
  transform: translateY(-5px);
}

.entry-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: var(--primary-gradient);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  margin-bottom: 10px;
  box-shadow: var(--shadow-md);
  position: relative;
  overflow: hidden;
  transition: all var(--transition-normal);
}

.entry-icon::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: rgba(255, 255, 255, 0.1);
  transform: rotate(45deg);
  animation: shineEffect 3s infinite ease-in-out;
}

@keyframes shineEffect {
  0% {
    transform: rotate(45deg) translateY(-100%);
  }
  50% {
    transform: rotate(45deg) translateY(100%);
  }
  100% {
    transform: rotate(45deg) translateY(100%);
  }
}

.entry-item:hover .entry-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: var(--shadow-lg);
}

.entry-title {
  font-size: 15px;
  font-weight: 500;
  margin-top: 10px;
  transition: color var(--transition-normal);
}

.entry-item:hover .entry-title {
  color: var(--primary-color);
}

/* 商品部分 */
.section-container {
  margin-bottom: 50px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  position: relative;
  padding-left: 18px;
  display: inline-block;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  height: 22px;
  width: 6px;
  background: var(--primary-gradient);
  border-radius: 6px;
  animation: titlePulse 2s infinite alternate;
}

.section-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -5px;
  width: 50px;
  height: 2px;
  background: var(--primary-gradient);
  border-radius: 2px;
}

.section-header .el-button {
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: 500;
  color: var(--primary-color);
  transition: all var(--transition-normal);
  overflow: hidden;
  position: relative;
}

.section-header .el-button::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
  background: var(--primary-gradient);
  transform: scaleX(0);
  transform-origin: left;
  transition: transform var(--transition-normal);
}

.section-header .el-button:hover::after {
  transform: scaleX(1);
}

.section-header .el-button:hover {
  transform: translateX(5px);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
}

.product-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  position: relative;
  border: 1px solid var(--border-color);
  transform-style: preserve-3d;
  perspective: 1000px;
}

.product-card::before {
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
  z-index: 1;
}

.product-card:hover {
  transform: translateY(-8px) scale(1.02) rotateX(2deg) rotateY(-2deg);
  box-shadow: var(--shadow-highlight);
  border-color: rgba(0, 120, 215, 0.3);
}

.product-image {
  height: 220px;
  position: relative;
  overflow: hidden;
  background-color: rgba(248, 249, 250, 0.5);
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  transition: transform 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  filter: drop-shadow(0 5px 10px rgba(0, 0, 0, 0.1));
}

.product-card:hover .product-image img {
  transform: scale(1.1);
}

.product-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background: var(--secondary-gradient);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  z-index: 2;
  box-shadow: 0 3px 10px rgba(255, 122, 69, 0.3);
  font-weight: 500;
  letter-spacing: 0.5px;
}

.new-tag {
  background: linear-gradient(135deg, #67c23a, #85da5f);
  box-shadow: 0 3px 10px rgba(103, 194, 58, 0.3);
}

.product-info {
  padding: 18px;
  z-index: 2;
  background-color: white;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  border-top: 1px solid rgba(0, 120, 215, 0.05);
}

.product-title {
  font-size: 16px;
  margin-bottom: 12px;
  min-height: 44px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  color: var(--text-color);
  font-weight: 500;
}

.product-price {
  margin-bottom: 12px;
}

.current-price {
  color: var(--secondary-color);
  font-size: 20px;
  font-weight: bold;
}

.original-price {
  color: #999;
  text-decoration: line-through;
  font-size: 14px;
  margin-left: 5px;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.sold-count {
  font-size: 13px;
  color: var(--light-text);
}

.product-footer .el-button {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
  border: none;
  box-shadow: 0 4px 10px rgba(0, 120, 215, 0.25);
  transition: all 0.3s;
}

.product-footer .el-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 15px rgba(0, 120, 215, 0.35);
}

/* 专题部分 */
.feature-section {
  margin-bottom: 50px;
  position: relative;
}

.feature-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  position: relative;
  z-index: 2;
}

.feature-item {
  background-color: white;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
  border: 1px solid var(--border-color);
  position: relative;
  transform-style: preserve-3d;
  perspective: 1000px;
  display: flex;
  flex-direction: column;
}

.feature-item::before {
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
  z-index: 1;
}

.feature-item:hover {
  transform: translateY(-8px) scale(1.02) rotateX(1deg) rotateY(-1deg);
  box-shadow: var(--shadow-highlight);
  border-color: rgba(0, 120, 215, 0.3);
}

.feature-image {
  height: 200px;
  position: relative;
  overflow: hidden;
}

.feature-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.8s cubic-bezier(0.19, 1, 0.22, 1);
}

.feature-item:hover .feature-image img {
  transform: scale(1.05);
}

.feature-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 58, 112, 0.7), transparent 60%);
  opacity: 0;
  transition: opacity var(--transition-normal);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 30px;
}

.feature-item:hover .feature-overlay {
  opacity: 1;
}

.view-more {
  background: rgba(255, 255, 255, 0.9);
  color: var(--primary-color);
  padding: 8px 20px;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 500;
  transform: translateY(20px);
  transition: all var(--transition-normal);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.feature-item:hover .view-more {
  transform: translateY(0);
}

.feature-content {
  padding: 20px;
  flex-grow: 1;
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
  position: relative;
}

.feature-content h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: var(--text-color);
  position: relative;
  display: inline-block;
  transition: color var(--transition-normal);
}

.feature-content h3::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -3px;
  width: 0;
  height: 2px;
  background: var(--primary-gradient);
  transition: width var(--transition-normal);
}

.feature-item:hover .feature-content h3 {
  color: var(--primary-color);
}

.feature-item:hover .feature-content h3::after {
  width: 100%;
}

.feature-content p {
  color: var(--light-text);
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 0;
}

.feature-wave {
  height: 15px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='rgba(0, 120, 215, 0.08)'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  margin-top: auto;
}

/* 品牌专区 */
.brand-section {
  margin-bottom: 60px;
}

.brand-container {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
}

.brand-card {
  height: 160px;
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  border: 1px solid var(--border-color);
  padding: 15px;
  position: relative;
}

.brand-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 120, 215, 0.15);
  border-color: rgba(0, 120, 215, 0.3);
}

.brand-logo {
  height: 110px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  margin-bottom: 5px;
}

.brand-logo :deep(.el-image) {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 100%;
  max-height: 100%;
}

.brand-logo :deep(.el-image__inner) {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  transition: transform 0.4s ease-out;
}

.brand-card:hover .brand-logo :deep(.el-image__inner) {
  transform: scale(1.1);
}

.brand-name {
  font-size: 16px;
  font-weight: 500;
  margin-top: 5px;
  margin-bottom: 3px;
  color: var(--text-color);
  text-align: center;
}

.brand-wave {
  height: 15px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='%23ffffff'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  margin-top: auto;
  transition: opacity 0.3s ease-in;
  opacity: 0;
}

.brand-card:hover .brand-wave {
  opacity: 1;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .brand-container {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 992px) {
  .quick-entry {
    flex-wrap: wrap;
  }
  
  .entry-item {
    width: 33.33%;
  }
  
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .feature-container {
    grid-template-columns: 1fr;
  }
  
  .brand-container {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .entry-item {
    width: 50%;
  }
  
  .product-grid {
    grid-template-columns: 1fr;
  }
  
  .brand-container {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style> 