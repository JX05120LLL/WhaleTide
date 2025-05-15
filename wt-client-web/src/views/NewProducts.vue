<!-- eslint-disable -->
<template>
  <div class="new-products-page">
    <app-header></app-header>
    
    <div class="container">
      <h1 class="page-title">新品上市</h1>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="products.length > 0" class="products-grid">
        <div 
          v-for="product in products" 
          :key="product.id"
          class="product-card"
        >
          <div class="product-image" @click="goToProduct(product.id)">
            <img :src="product.image || '/images/placeholder.png'" :alt="product.name">
            <div class="new-tag">新品</div>
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
        <el-empty description="暂无新品" />
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import api from '@/api'

export default {
  name: 'NewProductsPage',
  components: {
    AppHeader,
    AppFooter,
    ShoppingCart
  },
  setup() {
    const router = useRouter()
    const store = useStore()
    
    const loading = ref(true)
    const products = ref([])
    
    // 获取新品数据
    const fetchNewProducts = async () => {
      loading.value = true
      try {
        // 模拟数据
        await new Promise(resolve => setTimeout(resolve, 300))
        products.value = [
          { id: 21, name: '防水潜水背包', price: 89.99, image: '/images/products/waterproof-diving-bag.png' },
          { id: 22, name: '浮潜水下摄影机', price: 299.99, image: '/images/products/underwater-camera.png' },
          { id: 23, name: '碳纤维船桨', price: 129.99, image: '/images/products/carbon-fiber-paddle.png' },
          { id: 24, name: '防水航海手表', price: 159.99, image: '/images/products/waterproof-watch.png' },
          { id: 25, name: '便携式净水器', price: 79.99, image: '/images/products/portable-water-filter.png' },
          { id: 26, name: '折叠式钓鱼椅', price: 45.99, image: '/images/products/folding-fishing-chair.png' }
        ]
      } catch (error) {
        console.error('获取新品失败:', error)
        ElMessage.error('获取新品失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
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
        image: product.image,
        quantity: 1
      })
      
      ElMessage({
        type: 'success',
        message: `已将 ${product.name} 加入购物车`
      })
    }
    
    onMounted(() => {
      fetchNewProducts()
    })
    
    return {
      loading,
      products,
      goToProduct,
      addToCart
    }
  }
}
</script>

<style scoped>
.new-products-page {
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
  position: relative;
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

.new-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #e4393c;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
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
}

@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .product-image {
    height: 150px;
  }
}
</style> 