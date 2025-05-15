<!-- eslint-disable -->
<template>
  <div class="brand-detail-page">
    <app-header></app-header>
    
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/brands' }">品牌专区</el-breadcrumb-item>
          <el-breadcrumb-item>{{ brand.name || '品牌详情' }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton style="width: 100%">
          <template #template>
            <div style="display: flex; gap: 20px;">
              <el-skeleton-item variant="image" style="width: 240px; height: 240px" />
              <div style="flex: 1;">
                <el-skeleton-item variant="p" style="width: 50%; height: 30px" />
                <el-skeleton-item variant="p" style="width: 80%; height: 24px; margin-top: 20px" />
                <el-skeleton-item variant="p" style="width: 80%; height: 24px; margin-top: 10px" />
                <el-skeleton-item variant="p" style="width: 60%; height: 24px; margin-top: 10px" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
      
      <template v-else>
        <!-- 品牌头部 -->
        <div class="brand-header">
          <div class="brand-logo">
            <img :src="formatImageUrl(brand.logo) || '/images/placeholder.png'" :alt="brand.name">
          </div>
          <div class="brand-info">
            <h1 class="brand-title">{{ brand.name }}</h1>
            <p class="brand-desc">{{ brand.description }}</p>
            <div class="brand-badges">
              <span v-if="brand.isFeatured === 1" class="badge featured">精选品牌</span>
            </div>
          </div>
        </div>
        
        <!-- 品牌商品列表 -->
        <div class="brand-products">
          <div class="section-header">
            <h2 class="section-title">品牌商品</h2>
            <div class="sort-filter">
              <el-select v-model="sortOption" placeholder="排序方式">
                <el-option label="默认排序" value="default" />
                <el-option label="价格从低到高" value="price_asc" />
                <el-option label="价格从高到低" value="price_desc" />
                <el-option label="销量优先" value="sales" />
              </el-select>
            </div>
          </div>
          
          <div v-if="products.length > 0" class="products-grid">
            <div 
              v-for="product in sortedProducts" 
              :key="product.id"
              class="product-card"
            >
              <div class="product-image" @click="goToProduct(product.id)">
                <img :src="formatImageUrl(product.pic)" :alt="product.name">
              </div>
              <div class="product-info">
                <h3 class="product-name" @click="goToProduct(product.id)">{{ product.name }}</h3>
                <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
                <div class="product-actions">
                  <el-button type="primary" size="small" @click="addToCart(product)">
                    <el-icon><ShoppingCart /></el-icon> 加入购物车
                  </el-button>
                </div>
              </div>
            </div>
          </div>
          
          <div v-else class="empty-products">
            <el-empty description="暂无商品" />
          </div>
        </div>
      </template>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'

export default {
  name: 'BrandDetailPage',
  components: {
    AppHeader,
    AppFooter,
    ShoppingCart
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    
    const loading = ref(true)
    const brandId = computed(() => route.params.id)
    const brand = ref({})
    const products = ref([])
    const sortOption = ref('default')
    
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
    const fetchBrandData = async () => {
      loading.value = true
      try {
        // 调用真实API获取品牌详情
        const response = await http.get(`/brand/detail/${brandId.value}`);
        
        if (response && response.code === 200 && response.data) {
          // 设置品牌信息
          brand.value = response.data;
          
          // 获取品牌相关产品
          if (response.data.products && Array.isArray(response.data.products)) {
            products.value = response.data.products;
          } else {
            products.value = [];
          }
          
          console.log('品牌详情:', brand.value);
          console.log('品牌产品:', products.value);
        } else {
          ElMessage.error(response?.message || '获取品牌详情失败');
          brand.value = {};
          products.value = [];
        }
      } catch (error) {
        console.error('获取品牌数据失败:', error)
        ElMessage.error('获取品牌数据失败，请稍后重试')
        brand.value = {};
        products.value = [];
      } finally {
        loading.value = false
      }
    }
    
    // 根据排序选项对商品进行排序
    const sortedProducts = computed(() => {
      if (!products.value || products.value.length === 0) return [];
      
      const productsCopy = [...products.value];
      
      switch(sortOption.value) {
        case 'price_asc':
          return productsCopy.sort((a, b) => a.price - b.price);
        case 'price_desc':
          return productsCopy.sort((a, b) => b.price - a.price);
        case 'sales':
          return productsCopy.sort((a, b) => (b.saleCount || 0) - (a.saleCount || 0));
        default:
          return productsCopy;
      }
    });
    
    // 跳转到商品详情
    const goToProduct = (id) => {
      router.push(`/product/${id}`)
    }
    
    // 添加到购物车
    const addToCart = (product) => {
      store.dispatch('addToCart', {
        id: product.id,
        name: product.name,
        price: product.price,
        image: product.pic,
        quantity: 1
      })
      
      ElMessage({
        type: 'success',
        message: `已将 ${product.name} 加入购物车`
      })
    }
    
    onMounted(() => {
      fetchBrandData()
    })
    
    return {
      loading,
      brand,
      products,
      sortOption,
      sortedProducts,
      goToProduct,
      addToCart,
      formatImageUrl
    }
  }
}
</script>

<style scoped>
.brand-detail-page {
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

.breadcrumb {
  margin-bottom: 20px;
  padding: 10px 0;
}

.loading-container {
  padding: 20px;
}

.brand-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #eee;
}

.brand-logo {
  width: 240px;
  height: 240px;
  background-color: #f8f8f8;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 15px;
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

.brand-logo:hover {
  transform: scale(1.02);
  box-shadow: 0 6px 16px rgba(0,0,0,0.15);
}

.brand-logo img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  transition: transform 0.4s ease-out;
}

.brand-logo:hover img {
  transform: scale(1.05);
}

.brand-info {
  flex: 1;
}

.brand-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 15px;
}

.brand-desc {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.brand-badges {
  display: flex;
  gap: 10px;
}

.badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: bold;
}

.badge.featured {
  background-color: #f39c12;
  color: white;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 22px;
  color: #333;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.product-image {
  height: 200px;
  overflow: hidden;
  cursor: pointer;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-image:hover img {
  transform: scale(1.05);
}

.product-info {
  padding: 15px;
}

.product-name {
  font-weight: 500;
  margin-bottom: 10px;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-name:hover {
  color: #0078d7;
}

.product-price {
  font-weight: bold;
  color: #e4393c;
  font-size: 18px;
  margin-bottom: 10px;
}

.product-actions {
  display: flex;
  justify-content: center;
}

.empty-products {
  padding: 40px 0;
  text-align: center;
}

@media (max-width: 768px) {
  .brand-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .brand-logo {
    margin-bottom: 20px;
  }
  
  .brand-badges {
    justify-content: center;
  }
  
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .product-image {
    height: 150px;
  }
}
</style> 