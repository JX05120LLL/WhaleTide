<!-- eslint-disable -->
<template>
  <div class="search-page">
    <app-header></app-header>
    
    <div class="container">
      <div class="search-header">
        <h1 class="page-title">搜索结果: "{{ keyword }}"</h1>
        
        <div class="search-filter">
          <el-select v-model="sortOption" placeholder="排序方式">
            <el-option label="默认排序" value="default" />
            <el-option label="价格从低到高" value="price_asc" />
            <el-option label="价格从高到低" value="price_desc" />
            <el-option label="销量优先" value="sales" />
            <el-option label="评分优先" value="rating" />
          </el-select>
        </div>
      </div>
      
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
            <img :src="formatImageUrl(product.image)" :alt="product.name">
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
        <el-empty :description="`未找到与 '${keyword}' 相关的商品`" />
        <el-button type="primary" class="back-home" @click="goHome">返回首页</el-button>
      </div>
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
import api from '@/api'
import { staticBaseURL } from '@/utils/request'

export default {
  name: 'SearchPage',
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
    const products = ref([])
    const sortOption = ref('default')
    const keyword = computed(() => route.query.keyword || '')
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    
    // 处理图片URL
    const formatImageUrl = (path) => {
      if (!path) return '/images/placeholder.png';
      
      // 检查路径是否已经包含完整URL
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      
      // 对于以/images开头的路径，将路径改为使用8080端口
      if (path.startsWith('/images/')) {
        // 确保图片扩展名为.png
        let imgPath = path;
        if (imgPath.endsWith('.jpg')) {
          imgPath = imgPath.replace('.jpg', '.png');
        }
        return `http://localhost:8080${imgPath}`;
      }
      
      // 处理相对路径，确保以/开头
      const normalizedPath = path.startsWith('/') ? path : `/${path}`;
      
      // 使用8080端口完整URL
      let fullPath = `http://localhost:8080${normalizedPath}`;
      
      // 确保图片扩展名为.png
      if (fullPath.endsWith('.jpg')) {
        fullPath = fullPath.replace('.jpg', '.png');
      }
      
      return fullPath;
    };
    
    // 添加搜索历史
    const addToSearchHistory = async (searchKeyword) => {
      try {
        if (isLoggedIn.value && searchKeyword.trim()) {
          await api.history.addSearchHistory(searchKeyword.trim())
          console.log('已添加搜索历史:', searchKeyword)
        }
      } catch (error) {
        console.error('添加搜索历史失败:', error)
      }
    }
    
    const searchProducts = async () => {
      if (!keyword.value) {
        products.value = []
        loading.value = false
        return
      }
      
      // 添加搜索历史
      await addToSearchHistory(keyword.value)
      
      loading.value = true
      try {
        // 模拟搜索
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // 生成模拟搜索结果
        if (keyword.value.includes('潜水') || keyword.value.includes('dive')) {
          products.value = [
            { id: 6, name: '专业潜水调节器', price: 299.99, image: '/images/products/diving-regulator.jpg' },
            { id: 7, name: '潜水电脑表', price: 459.99, image: '/images/products/dive-computer.jpg' },
            { id: 8, name: '高级潜水面镜', price: 79.99, image: '/images/products/diving-mask.jpg' },
            { id: 10, name: '碳纤维潜水蛙鞋', price: 149.99, image: '/images/products/dive-fins.jpg' }
          ]
        } else if (keyword.value.includes('海滩') || keyword.value.includes('beach')) {
          products.value = [
            { id: 11, name: '折叠海滩椅', price: 59.99, image: '/images/products/beach-chair.jpg' },
            { id: 12, name: '带沙锚的海滩伞', price: 49.99, image: '/images/products/beach-umbrella.jpg' },
            { id: 13, name: '防水海滩垫', price: 29.99, image: '/images/products/beach-blanket.jpg' },
            { id: 15, name: '海滩遮阳帐篷', price: 79.99, image: '/images/products/sun-shelter.jpg' }
          ]
        } else if (keyword.value.includes('航海') || keyword.value.includes('boat')) {
          products.value = [
            { id: 1, name: '专业航海指南针', price: 129.99, image: '/images/products/marine-compass.jpg' },
            { id: 3, name: '航海双筒望远镜', price: 199.99, image: '/images/products/marine-binoculars.jpg' },
            { id: 4, name: '高级船舶护舷', price: 89.99, image: '/images/products/boat-fenders.jpg' },
            { id: 5, name: '船用急救箱', price: 45.99, image: '/images/products/marine-first-aid.jpg' }
          ]
        } else {
          products.value = []
        }
        
        // 根据排序选项排序
        if (sortOption.value === 'price_asc') {
          products.value.sort((a, b) => a.price - b.price)
        } else if (sortOption.value === 'price_desc') {
          products.value.sort((a, b) => b.price - a.price)
        }
      } catch (error) {
        console.error('搜索失败:', error)
        ElMessage.error('搜索失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 跳转到商品详情
    const goToProduct = (id) => {
      router.push(`/product/${id}`)
    }
    
    // 返回首页
    const goHome = () => {
      router.push('/')
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
    
    // 监听关键词变化，重新搜索
    watch(() => route.query.keyword, (newKeyword, oldKeyword) => {
      if (newKeyword !== oldKeyword) {
        searchProducts()
      }
    })
    
    // 监听排序变化，重新排序结果
    watch(sortOption, () => {
      if (products.value.length > 0) {
        if (sortOption.value === 'price_asc') {
          products.value.sort((a, b) => a.price - b.price)
        } else if (sortOption.value === 'price_desc') {
          products.value.sort((a, b) => b.price - a.price)
        }
      }
    })
    
    onMounted(() => {
      searchProducts()
    })
    
    return {
      loading,
      products,
      keyword,
      sortOption,
      isLoggedIn,
      formatImageUrl,
      goToProduct,
      goHome,
      addToCart
    }
  }
}
</script>

<style scoped>
.search-page {
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

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  color: #333;
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

.back-home {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .search-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-filter {
    margin-top: 15px;
  }
  
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .product-image {
    height: 150px;
  }
}
</style> 