<!-- eslint-disable -->
<template>
  <div class="category-page">
    <app-header></app-header>
    
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ categoryInfo.name || '商品分类' }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      
      <!-- 分类内容 -->
      <div class="category-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="5" animated />
        </div>
        
        <template v-else>
          <!-- 分类头部 -->
          <div class="category-header">
            <h1 class="category-title">{{ categoryInfo.name || '商品分类' }}</h1>
            <p class="category-desc">{{ categoryInfo.description || '探索我们多样化的海洋产品，为您的海洋体验提供完美支持。' }}</p>
          </div>
          
          <!-- 子分类展示 -->
          <div v-if="subcategories.length > 0" class="subcategories-section">
            <h2 class="section-title">子分类</h2>
            <div class="subcategories-grid">
              <div 
                v-for="subcategory in subcategories" 
                :key="subcategory.id"
                class="subcategory-card"
                @click="goToSubcategory(subcategory.id)"
              >
                <div class="subcategory-image">
                  <img :src="formatImageUrl(subcategory.image)" :alt="subcategory.name">
                </div>
                <div class="subcategory-name">{{ subcategory.name }}</div>
              </div>
            </div>
          </div>
          
          <!-- 分类商品列表 -->
          <div class="products-section">
            <div class="section-header">
              <h2 class="section-title">商品列表</h2>
              <div class="sort-filter">
                <el-select v-model="sortOption" placeholder="排序方式">
                  <el-option label="默认排序" value="default" />
                  <el-option label="价格从低到高" value="price_asc" />
                  <el-option label="价格从高到低" value="price_desc" />
                  <el-option label="销量优先" value="sales" />
                  <el-option label="评分优先" value="rating" />
                </el-select>
              </div>
            </div>
            
            <div v-if="products.length > 0" class="products-grid">
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
              <el-empty description="暂无商品" />
            </div>
            
            <!-- 分页 -->
            <div v-if="products.length > 0" class="pagination">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[12, 24, 36, 48]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalProducts"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </div>
        </template>
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import { staticBaseURL } from '@/utils/request'

export default {
  name: 'CategoryPage',
  components: {
    AppHeader,
    AppFooter,
    ShoppingCart
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    
    // 状态
    const loading = ref(true)
    const categoryId = computed(() => route.params.id)
    const categoryInfo = ref({})
    const subcategories = ref([])
    const products = ref([])
    const totalProducts = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(12)
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
      
      // 返回完整URL
      return `${staticBaseURL}${normalizedPath}`;
    };
    
    // 获取分类数据
    const fetchCategoryData = async () => {
      loading.value = true
      try {
        // 从store获取分类数据
        const data = await store.dispatch('fetchCategoryData', categoryId.value)
        if (data) {
          categoryInfo.value = {
            id: categoryId.value,
            name: getCategoryName(),
            description: '探索我们多样化的海洋产品，为您的海洋体验提供完美支持。'
          }
          subcategories.value = data.subcategories || []
          products.value = data.featuredProducts || []
          totalProducts.value = products.value.length
          
          // 调试输出
          console.log('分类商品数据:', products.value);
        }
      } catch (error) {
        console.error('获取分类数据失败:', error)
        ElMessage.error('获取分类数据失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 根据ID获取分类名称
    const getCategoryName = () => {
      // 定义特殊分类名称映射
      const specialCategories = {
        'new': '新品发布',
        'hot': '热卖商品',
        'discount': '限时特惠',
        'selected': '优选好物'
      };
      
      // 如果是特殊分类，直接返回映射的名称
      if (specialCategories[categoryId.value]) {
        return specialCategories[categoryId.value];
      }
      
      // 普通分类，从store中查找
      const categories = store.getters.allCategories
      const category = categories.find(c => c.id == categoryId.value)
      return category ? category.name : '商品分类'
    }
    
    // 跳转到子分类
    const goToSubcategory = (id) => {
      router.push(`/category/${id}`)
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
    
    // 处理分页大小变化
    const handleSizeChange = (size) => {
      pageSize.value = size
      currentPage.value = 1
      fetchCategoryData()
    }
    
    // 处理页码变化
    const handleCurrentChange = (page) => {
      currentPage.value = page
      fetchCategoryData()
    }
    
    // 监听路由参数变化，重新获取数据
    watch(() => route.params.id, (newId, oldId) => {
      if (newId !== oldId) {
        fetchCategoryData()
      }
    })
    
    // 初始化
    onMounted(() => {
      fetchCategoryData()
    })
    
    return {
      loading,
      categoryInfo,
      subcategories,
      products,
      totalProducts,
      currentPage,
      pageSize,
      sortOption,
      formatImageUrl,
      goToSubcategory,
      goToProduct,
      addToCart,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.category-page {
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

.category-header {
  margin-bottom: 30px;
  text-align: center;
}

.category-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.category-desc {
  font-size: 16px;
  color: #666;
  max-width: 800px;
  margin: 0 auto;
}

.section-title {
  font-size: 22px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.subcategories-section {
  margin-bottom: 40px;
}

.subcategories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 20px;
}

.subcategory-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.subcategory-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.subcategory-image {
  height: 120px;
  overflow: hidden;
}

.subcategory-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.subcategory-name {
  padding: 10px;
  text-align: center;
  font-weight: 500;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
  display: flex;
  flex-direction: column;
  position: relative;
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
  object-fit: contain;
  transition: transform 0.3s;
  background-color: #f8f8f8;
  padding: 5px;
}

.product-image:hover img {
  transform: scale(1.05);
}

.product-info {
  padding: 15px;
  z-index: 2;
  background-color: white;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
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
  min-height: 42px; /* 确保名称有足够的空间，即使是空的 */
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
  margin-top: auto; /* 将按钮推到底部 */
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.empty-products {
  padding: 40px 0;
}

@media (max-width: 768px) {
  .subcategories-grid,
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .product-image {
    height: 150px;
  }
}
</style> 