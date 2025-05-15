<!-- eslint-disable -->
<template>
  <div class="promotion-page">
    <app-header></app-header>
    
    <div class="container">
      <h1 class="page-title">优惠专区</h1>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="promotions.length > 0" class="promotions-grid">
        <div 
          v-for="promotion in promotions" 
          :key="promotion.id"
          class="promotion-card"
          @click="goToPromotion(promotion.id)"
        >
          <div class="promotion-image">
            <img :src="promotion.image || '/images/placeholder.png'" :alt="promotion.title">
            <div class="discount-tag">{{ promotion.discount }}</div>
          </div>
          <div class="promotion-info">
            <h3 class="promotion-title">{{ promotion.title }}</h3>
            <p class="promotion-period">{{ promotion.period }}</p>
          </div>
        </div>
      </div>
      
      <div v-else class="empty-promotions">
        <el-empty description="暂无促销活动" />
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

export default {
  name: 'PromotionPage',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const router = useRouter()
    
    const loading = ref(true)
    const promotions = ref([])
    
    // 获取促销数据
    const fetchPromotions = async () => {
      loading.value = true
      try {
        // 模拟数据加载
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // 模拟促销数据
        promotions.value = [
          {
            id: 1,
            title: '夏季特惠',
            discount: '满300减50',
            period: '2023-06-01至2023-08-31',
            image: '/images/promotions/summer-sale.png'
          },
          {
            id: 2,
            title: '潜水装备7折起',
            discount: '低至7折',
            period: '2023-06-15至2023-07-15',
            image: '/images/promotions/diving-sale.png'
          },
          {
            id: 3,
            title: '新人专享优惠',
            discount: '首单8.5折',
            period: '长期有效',
            image: '/images/promotions/new-user.png'
          },
          {
            id: 4,
            title: '会员日特惠',
            discount: '额外9折',
            period: '每月15日',
            image: '/images/promotions/member-day.png'
          }
        ]
      } catch (error) {
        console.error('获取促销数据失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 跳转到促销详情
    const goToPromotion = (id) => {
      router.push(`/promotion/${id}`)
    }
    
    onMounted(() => {
      fetchPromotions()
    })
    
    return {
      loading,
      promotions,
      goToPromotion
    }
  }
}
</script>

<style scoped>
.promotion-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  flex: 1;
}

.page-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.loading-container {
  padding: 20px;
}

.promotions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
}

.promotion-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.promotion-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.15);
}

.promotion-image {
  height: 180px;
  overflow: hidden;
  position: relative;
}

.promotion-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.promotion-card:hover .promotion-image img {
  transform: scale(1.05);
}

.discount-tag {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: #e4393c;
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-weight: bold;
  font-size: 14px;
}

.promotion-info {
  padding: 15px;
}

.promotion-title {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.promotion-period {
  color: #666;
  font-size: 14px;
}

.empty-promotions {
  padding: 40px 0;
  text-align: center;
}

@media (max-width: 768px) {
  .promotions-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}
</style> 