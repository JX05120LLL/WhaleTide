<template>
<!-- eslint-disable -->
  <div class="product-detail-page">
    <app-header></app-header>
    
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: `/category/${product.categoryId}` }">
            {{ product.categoryName || '商品分类' }}
          </el-breadcrumb-item>
          <el-breadcrumb-item>{{ product.name || '商品详情' }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      
      <!-- 加载中 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton style="width: 100%">
          <template #template>
            <div style="display: flex; gap: 20px;">
              <el-skeleton-item variant="image" style="width: 450px; height: 450px" />
              <div style="flex: 1;">
                <el-skeleton-item variant="p" style="width: 80%; height: 30px" />
                <el-skeleton-item variant="p" style="width: 40%; height: 24px; margin-top: 20px" />
                <el-skeleton-item variant="p" style="width: 60%; height: 24px; margin-top: 10px" />
                <el-skeleton-item variant="p" style="width: 60%; height: 24px; margin-top: 40px" />
                <el-skeleton-item variant="p" style="width: 100%; height: 150px; margin-top: 20px" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
      
      <!-- 商品详情内容 -->
      <div v-else class="product-container">
        <div class="product-overview">
          <!-- 商品图片区 -->
          <div class="product-gallery">
            <div class="main-image">
              <el-image 
                :src="currentImage" 
                :alt="product.name" 
                fit="contain"
                :preview-src-list="product.images && product.images.length ? [currentImage] : []"
                :initial-index="0"
                @error="() => console.error('图片加载失败:', currentImage)">
                <template #error>
                  <div class="image-error">
                    <i class="el-icon-picture-outline"></i>
                    <div>图片加载失败</div>
                  </div>
                </template>
              </el-image>
            </div>
            
            <div class="image-thumbnails">
              <div 
                v-for="(image, index) in product.images" 
                :key="index" 
                class="thumbnail" 
                :class="{ active: currentImageIndex === index }"
                @click="currentImageIndex = index"
              >
                <el-image 
                  :src="formatImageUrl(image)" 
                  :alt="`${product.name} - 图片 ${index + 1}`" 
                  fit="contain">
                  <template #error>
                    <div class="thumbnail-error">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </template>
                </el-image>
              </div>
            </div>
          </div>
          
          <!-- 商品信息区 -->
          <div class="product-info">
            <h1 class="product-name">{{ product.name }}</h1>
            
            <div class="product-meta">
              <div class="product-rating">
                <el-rate 
                  v-model="product.rating" 
                  disabled 
                  show-score 
                  text-color="#ff9900"
                />
                <span class="review-count">{{ product.reviewCount }}条评价</span>
              </div>
              <div class="product-sku">商品编号：{{ product.sku }}</div>
            </div>
            
            <div class="product-price-section">
              <div class="product-price">
                <div class="current-price">¥{{ product.price }}</div>
                <div class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice }}</div>
                <div class="discount-tag" v-if="product.originalPrice">{{ calculateDiscount(product.price, product.originalPrice) }}折</div>
              </div>
            </div>
            
            <!-- 规格选择 -->
            <div class="product-options" v-if="product.options && product.options.length">
              <div v-for="option in product.options" :key="option.name" class="option-group">
                <div class="option-name">{{ option.name }}</div>
                <div class="option-values">
                  <div 
                    v-for="value in option.values" 
                    :key="value.id" 
                    class="option-value" 
                    :class="{ selected: selectedOptions[option.name] === value.id }"
                    @click="selectOption(option.name, value.id)"
                  >
                    {{ value.name }}
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 数量选择 -->
            <div class="quantity-selector">
              <div class="option-name">数量</div>
              <el-input-number 
                v-model="quantity" 
                :min="1" 
                :max="product.stock || 99"
                size="large"
              />
              <span class="stock-info">库存 {{ product.stock }} 件</span>
            </div>
            
            <!-- 按钮组 -->
            <div class="action-buttons">
              <el-button class="add-cart-btn" type="primary" size="large" @click="addToCart">加入购物车</el-button>
              <el-button class="buy-now-btn" type="danger" size="large" @click="buyNow">立即购买</el-button>
              <el-button 
                class="collect-btn"
                :class="{ active: isFavorite }"
                :type="isFavorite ? 'success' : 'default'"
                size="large" 
                @click="toggleFavorite"
                :loading="favoriteLoading"
              >
                <el-icon class="favorite-icon"><Star :fill="isFavorite ? '#fff' : '#999'" /></el-icon>
                {{ isFavorite ? '已收藏' : '收藏' }}
              </el-button>
            </div>
            
            <!-- 服务保障 -->
            <div class="service-guarantees">
              <div class="guarantee-item">
                <el-icon><Check /></el-icon>
                <span>正品保障</span>
              </div>
              <div class="guarantee-item">
                <el-icon><Check /></el-icon>
                <span>极速发货</span>
              </div>
              <div class="guarantee-item">
                <el-icon><Check /></el-icon>
                <span>7天无理由退换</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 波浪分隔线 -->
        <div class="wave-divider"></div>
        
        <!-- 商品详情选项卡 -->
        <div class="product-detail-tabs">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="商品详情" name="details">
              <div class="product-description" v-html="product.description"></div>
            </el-tab-pane>
            <el-tab-pane label="规格参数" name="specs">
              <div class="product-specs">
                <div v-for="(spec, index) in product.specifications" :key="index" class="spec-item">
                  <div class="spec-name">{{ spec.name }}</div>
                  <div class="spec-value">{{ spec.value }}</div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="用户评价" name="reviews">
              <div class="product-reviews">
                <div class="review-summary">
                  <div class="rating-average">
                    <div class="average-score">{{ product.averageRating }}</div>
                    <div class="average-stars">
                      <el-rate v-model="product.averageRating" disabled show-score />
                    </div>
                    <div class="review-total">共 {{ product.reviewCount }} 条评价</div>
                  </div>
                  <div class="rating-distribution">
                    <div v-for="(count, stars) in ratingDistribution" :key="stars" class="rating-bar">
                      <div class="stars-label">{{ stars }}星</div>
                      <div class="rating-progress">
                        <div class="progress-bar" :style="{width: ((product.reviewCount > 0 ? count / product.reviewCount * 100 : 0)) + '%'}"></div>
                      </div>
                      <div class="rating-percent">{{ product.reviewCount > 0 ? Math.round(count / product.reviewCount * 100) : 0 }}%</div>
                    </div>
                  </div>
                </div>
                
                <div class="review-filter">
                  <el-button 
                    v-for="tag in reviewTags" 
                    :key="tag.name" 
                    :class="{ active: selectedTags.includes(tag.name) }"
                    @click="toggleReviewTag(tag.name)"
                  >
                    {{ tag.name }} ({{ tag.count }})
                  </el-button>
                </div>
                
                <div class="review-list">
                  <div v-if="reviews.length === 0" class="empty-reviews">
                    暂无评论数据
                  </div>
                  <div v-for="review in reviews" :key="review.id" class="review-item">
                    <div class="review-header">
                      <div class="reviewer">
                        <div class="avatar">
                          <img :src="formatImageUrl(review.userIcon)" :alt="review.userName">
                        </div>
                        <div class="username">{{ review.userName }}</div>
                      </div>
                      <div class="review-rating">
                        <el-rate v-model="review.star" disabled />
                        <div class="review-date">{{ new Date(review.createTime).toLocaleDateString() }}</div>
                      </div>
                    </div>
                    <div class="review-content">{{ review.content }}</div>
                    <div v-if="review.pics && review.pics.length" class="review-images">
                      <div v-for="(image, index) in review.pics" :key="index" class="review-image">
                        <img :src="formatImageUrl(image)" @click="previewImage(review.pics, index)">
                      </div>
                    </div>
                    <div v-if="review.skuInfo" class="review-sku-info">{{ review.skuInfo }}</div>
                  </div>
                </div>
                
                <div class="review-pagination">
                  <el-pagination
                    v-model:current-page="reviewPage"
                    :page-size="10"
                    layout="prev, pager, next"
                    :total="product.reviewCount"
                    @current-change="loadReviews"
                  />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        
        <!-- 波浪分隔线 -->
        <div class="wave-divider blue-wave"></div>
        
        <!-- 相关推荐 -->
        <div class="related-products-section">
          <h2 class="section-title">相关推荐</h2>
          <div class="related-products">
            <div 
              v-for="item in relatedProducts" 
              :key="item.id" 
              class="product-card"
              @click="goToProduct(item.id)"
            >
              <div class="product-card-image">
                <el-image :src="formatImageUrl(item.image)" fit="contain"></el-image>
              </div>
              <div class="product-card-info">
                <h3 class="product-card-title">{{ item.name }}</h3>
                <div class="product-card-price">¥{{ item.price }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <app-footer></app-footer>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check, Star } from '@element-plus/icons-vue'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import request from '@/utils/request'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'
import { deepClone } from '@/utils/deep-clone'
import api from '../api'
import * as orderApi from '../api/order'

export default {
  name: 'ProductDetailPage',
  components: {
    AppHeader,
    AppFooter,
    Check,
    Star
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const store = useStore()
    
    const loading = ref(true)
    const product = ref({
      id: 0,
      name: '',
      brief: '',
      price: 0,
      originalPrice: 0,
      discount: '',
      stock: 0,
      categoryId: '',
      categoryName: '',
      images: [],
      specs: [],
      parameters: [],
      detail: '',
      commentCount: 0,
      rating: 4.5,
      reviewCount: 0,
      sku: '',
      specifications: [],
      description: '',
      averageRating: 4.5
    })
    
    // 将可能导致循环引用的数据分离出来
    const ratingDistribution = ref({
      '5': 7,
      '4': 2,
      '3': 1,
      '2': 0,
      '1': 0
    })
    
    const currentImageIndex = ref(0)
    const quantity = ref(1)
    const activeTab = ref('details')
    const selectedOptions = reactive({})
    const reviewPage = ref(1)
    const selectedTags = ref([])
    const reviews = ref([])
    const relatedProducts = ref([])
    const reviewTags = ref([])
    const isFavorite = ref(false)
    const favoriteLoading = ref(false)
    
    // 处理图片路径，确保缩略图和评论图片都使用正确的URL
    const formatImageUrl = (path) => {
      if (!path) return '/images/placeholder.png';
      
      // 检查路径是否已经包含完整URL
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      
      // 处理相对路径，确保以/开头
      const normalizedPath = path.startsWith('/') ? path : `/${path}`;
      
      // 完整URL
      const fullUrl = `${staticBaseURL}${normalizedPath}`;
      
      return fullUrl;
    };
    
    // 计算属性
    const currentImage = computed(() => {
      if (!product.value || !product.value.images || product.value.images.length === 0) {
        return '';
      }
      
      const imagePath = product.value.images[currentImageIndex.value];
      return formatImageUrl(imagePath);
    })
    
    // 方法
    const loadProductDetail = async () => {
      loading.value = true
      try {
        const res = await api.product.getProductDetail(route.params.id)
        
        if (res && res.code === 200 && res.data) {
          // 使用深拷贝处理数据，防止循环引用
          const data = deepClone(res.data)
          
          // 处理图片路径
          let images = [];
          
          // 如果有albumPics（副图数据），优先使用
          if (data.albumPics && Array.isArray(data.albumPics) && data.albumPics.length > 0) {
            images = [...data.albumPics];
          } else if (data.albumPics && typeof data.albumPics === 'string' && data.albumPics.trim() !== '') {
            images = data.albumPics.split(',');
          } else if (data.pic) {
            // 如果没有副图但有主图，则使用主图
            images = [data.pic];
          } else if (data.image) {
            images = [data.image];
          } else {
            // 最后使用默认图
            images = ['/images/placeholder.png'];
          }
          
          // 使用解构赋值而不是直接引用，避免循环引用
          product.value = {
            id: data.id,
            name: data.name,
            brief: data.description,
            price: data.price || 0,
            originalPrice: data.originalPrice,
            stock: data.stock || 0,
            categoryId: data.categoryId,
            categoryName: data.categoryName,
            images: [...images], // 使用浅拷贝
            rating: data.rating || 4.5, 
            reviewCount: data.commentCount || 0,
            sku: data.productSn || data.id, 
            specifications: Array.isArray(data.productAttributeList) ? [...data.productAttributeList] : 
                         (Array.isArray(data.params) ? [...data.params] : []),
            description: data.description || '暂无商品描述',
            averageRating: data.rating || 4.5
          }
          
          // 将评分分布分离存储
          if (data.ratingDistribution) {
            ratingDistribution.value = deepClone(data.ratingDistribution)
          }
          
          // 确保images数组有值
          if (!product.value.images || product.value.images.length === 0) {
            product.value.images = ['/images/placeholder.png']
          }
          
          // 记录浏览历史
          recordBrowsingHistory(route.params.id);
        }
        loading.value = false
        
      } catch (error) {
        console.error('获取商品详情失败:', error)
        ElMessage.error('获取商品详情失败，请稍后重试')
        loading.value = false
      }
    }
    
    // 记录用户浏览历史
    const recordBrowsingHistory = async (productId) => {
      try {
        // 调用添加浏览历史接口
        await api.product.addToHistory(productId);
        console.log('记录浏览历史成功:', productId);
      } catch (error) {
        console.error('记录浏览历史失败:', error);
        // 这里选择不向用户展示错误，因为这是一个静默的后台操作
      }
    }
    
    const loadReviews = async () => {
      try {
        const params = {
          productId: route.params.id,
          pageNum: reviewPage.value,
          pageSize: 10
        }
        
        if (selectedTags.value.length > 0) {
          params.tags = selectedTags.value.join(',')
        }
        
        // 使用API调用获取评论
        const response = await api.product.getProductReviews(route.params.id, params)
        
        if (response && response.code === 200 && response.data) {
          // 使用深拷贝确保没有循环引用
          const reviewData = deepClone(response.data)
          
          // 处理评论数据
          if (reviewData.list && Array.isArray(reviewData.list)) {
            reviews.value = reviewData.list;
          } else if (Array.isArray(reviewData)) {
            reviews.value = reviewData;
          } else {
            reviews.value = [];
          }
          
          // 如果有评论数据，对评论进行一些额外处理
          if (reviews.value && reviews.value.length > 0) {
            // 确保每个评论对象有必要的字段
            reviews.value = reviews.value.map(review => {
              // 创建新对象而非修改原对象
              const newReview = { ...review };
              
              // 如果没有用户图标，设置默认图标
              if (!newReview.userIcon) {
                newReview.userIcon = '/images/default-avatar.png';
              }
              
              // 处理评论图片
              if (newReview.pics && typeof newReview.pics === 'string') {
                newReview.pics = newReview.pics.split(',');
              } else if (!newReview.pics) {
                newReview.pics = [];
              }
              
              // 确保有星级评分
              if (typeof newReview.star === 'undefined') {
                newReview.star = 5;
              }
              
              return newReview;
            });
          } else {
            reviews.value = [];
          }
          
          // 更新评论总数
          if (reviewData.total !== undefined) {
            product.value.reviewCount = reviewData.total;
          }
          
          // 处理评论标签
          if (reviewData.tags) {
            reviewTags.value = deepClone(reviewData.tags);
          } else {
            // 设置默认标签
            const totalReviews = product.value.reviewCount || 0;
            reviewTags.value = [
              { name: '全部', count: totalReviews },
              { name: '好评', count: Math.ceil(totalReviews * 0.9) },
              { name: '中评', count: Math.ceil(totalReviews * 0.07) },
              { name: '差评', count: Math.ceil(totalReviews * 0.03) },
              { name: '有图', count: Math.ceil(totalReviews * 0.4) }
            ];
          }
        } else {
          reviews.value = [];
        }
      } catch (error) {
        console.error('获取评价失败:', error);
        ElMessage.error('评论加载失败');
        reviews.value = [];
      }
    }
    
    const loadRelatedProducts = async () => {
      try {
        // 获取最多8个相关商品
        const response = await api.product.getRelatedProducts(route.params.id, 8)
        
        if (response && response.code === 200) {
          // 使用深拷贝处理数据，防止循环引用
          const relatedData = deepClone(response.data || []);
          // 创建新对象而非修改原对象
          relatedProducts.value = relatedData.map(item => {
            return {
              id: item.id,
              name: item.name,
              image: item.pic || '/images/placeholder.png',
              price: item.price || 0,
              soldCount: item.soldCount || 0
            }
          });
        }
      } catch (error) {
        console.error('获取相关商品失败:', error)
        relatedProducts.value = []
      }
    }
    
    const selectOption = (optionName, valueId) => {
      selectedOptions[optionName] = valueId
    }
    
    const toggleReviewTag = (tagName) => {
      const index = selectedTags.value.indexOf(tagName)
      if (index === -1) {
        selectedTags.value.push(tagName)
      } else {
        selectedTags.value.splice(index, 1)
      }
      loadReviews()
    }
    
    const previewImage = (images, index) => {
      // 转换图片路径
      const formattedImages = images.map(img => formatImageUrl(img));
      
      // 使用Element Plus的图片预览
      ElMessageBox.alert('', '图片预览', {
        dangerouslyUseHTMLString: true,
        customClass: 'image-preview-dialog',
        center: true,
        showConfirmButton: false,
        closeOnClickModal: true,
        message: `<img src="${formattedImages[index]}" style="max-width: 100%; max-height: 80vh;">`
      });
    }
    
    const addToCart = async () => {
      try {
        await api.cart.addToCart({
          productId: route.params.id,
          quantity: quantity.value,
          sku: deepClone(selectedOptions) // 使用深拷贝断开引用
        })
        
        ElMessage.success('已添加到购物车')
      } catch (error) {
        ElMessage.error('添加到购物车失败')
        console.error(error)
      }
    }
    
    const buyNow = async () => {
      try {
        console.log('===== 直接购买 =====');
        
        // 先检查并同步登录状态
        const loginState = await store.dispatch('checkLoginStatus');
        console.log('登录状态检查结果:', loginState);
        
        // 再获取最新的登录状态
        const isLoggedIn = store.getters.isLoggedIn;
        const userInfo = store.getters.currentUser;
        console.log('检查后的登录状态:', isLoggedIn);
        console.log('检查后的用户信息:', userInfo);
        
        // 如果未登录，跳转到登录页面
        if (!isLoggedIn || !userInfo) {
          ElMessage.warning('请先登录')
          const currentPath = router.currentRoute.value.fullPath
          console.log('未登录，准备跳转到登录页面，当前路径:', currentPath);
          router.push({
            path: '/login',
            query: { redirect: currentPath }
          })
          return
        }
        
        console.log('准备调用directBuy API，商品ID:', route.params.id, '数量:', quantity.value);
        
        // 直接购买商品
        const res = await orderApi.directBuy({
          productId: Number(route.params.id),
          quantity: quantity.value,
          userId: userInfo.id // 显式传递用户ID
        })
        
        console.log('directBuy API调用结果:', res);
        
        // 如果成功，跳转到结算页面
        if (res && res.code === 200 && res.data) {
          console.log('调用成功，准备跳转到结算页面，cartItemId:', res.data);
          router.push({
            path: '/checkout',
            query: {
              cartItemIds: res.data.toString(),
              directBuy: 'true' // 添加直接购买标记
            }
          })
        } else {
          ElMessage.error('直接购买失败')
          console.error('直接购买返回错误:', res);
        }
      } catch (error) {
        console.error('直接购买失败:', error)
        // 如果是401未授权错误，提示用户登录
        if (error.response && error.response.status === 401) {
          console.log('收到401错误，准备跳转登录页');
          ElMessage.warning('请先登录')
          const currentPath = router.currentRoute.value.fullPath
          router.push({
            path: '/login',
            query: { redirect: currentPath }
          })
        } else {
          ElMessage.error('操作失败，请稍后重试')
        }
      }
    }
    
    const goToProduct = (productId) => {
      // 如果是同一个商品，刷新页面
      if (Number(route.params.id) === Number(productId)) {
        window.location.reload();
      } else {
        // 跳转到新的商品详情页
        router.push(`/product/${productId}`);
      }
    }
    
    // 检查是否已收藏
    const checkFavorite = async () => {
      try {
        // 检查用户是否已登录
        const isLoggedIn = store.getters.isLoggedIn
        const userInfo = store.getters.currentUser
        
        console.log('检查收藏状态 - 登录状态:', isLoggedIn)
        console.log('检查收藏状态 - 用户信息:', userInfo)
        
        if (!isLoggedIn || !userInfo) {
          isFavorite.value = false
          return
        }
        
        favoriteLoading.value = true
        const productId = route.params.id
        const res = await api.user.checkFavorite(productId)
        if (res.code === 200) {
          isFavorite.value = res.data
          console.log('收藏状态:', res.data)
        }
      } catch (error) {
        console.error('检查收藏状态失败:', error)
        isFavorite.value = false
      } finally {
        favoriteLoading.value = false
      }
    }
    
    // 收藏/取消收藏
    const toggleFavorite = async () => {
      try {
        // 检查用户是否已登录
        const isLoggedIn = store.getters.isLoggedIn
        const userInfo = store.getters.currentUser
        
        console.log('收藏操作 - 登录状态:', isLoggedIn)
        console.log('收藏操作 - 用户信息:', userInfo)
        
        // 如果未登录，跳转到登录页面
        if (!isLoggedIn || !userInfo) {
          ElMessage.warning('请先登录')
          const currentPath = router.currentRoute.value.fullPath
          router.push({
            path: '/login',
            query: { redirect: currentPath }
          })
          return
        }
        
        favoriteLoading.value = true
        const productId = route.params.id
        
        if (isFavorite.value) {
          // 已收藏，取消收藏
          const res = await api.user.removeFromFavorites(productId)
          if (res.code === 200) {
            isFavorite.value = false
            ElMessage.success('已取消收藏')
          } else {
            ElMessage.error(res.message || '取消收藏失败')
          }
        } else {
          // 未收藏，添加收藏
          const res = await api.user.addToFavorites(productId)
          if (res.code === 200) {
            isFavorite.value = true
            ElMessage.success('收藏成功')
          } else {
            ElMessage.error(res.message || '收藏失败')
          }
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        if (error.response && error.response.status === 401) {
          ElMessage.warning('请先登录')
          const currentPath = router.currentRoute.value.fullPath
          router.push({
            path: '/login',
            query: { redirect: currentPath }
          })
        } else {
          ElMessage.error('操作失败，请稍后重试')
        }
      } finally {
        favoriteLoading.value = false
      }
    }
    
    const calculateDiscount = (currentPrice, originalPrice) => {
      if (!originalPrice || originalPrice <= 0) return 10;
      let discount = (currentPrice / originalPrice * 10).toFixed(1);
      return discount;
    }
    
    onMounted(() => {
      loadProductDetail()
      loadReviews()
      loadRelatedProducts()
      checkFavorite() // 检查收藏状态
    })
    
    return {
      loading,
      product,
      ratingDistribution, // 导出分离的数据
      currentImage,
      currentImageIndex,
      quantity,
      activeTab,
      selectedOptions,
      reviewPage,
      selectedTags,
      reviews,
      relatedProducts,
      reviewTags,
      addToCart,
      buyNow,
      loadReviews,
      formatImageUrl,
      previewImage,
      goToProduct,
      toggleReviewTag,
      isFavorite,
      favoriteLoading,
      toggleFavorite,
      calculateDiscount
    }
  }
}
</script>

<style scoped>
.product-detail-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  flex: 1;
  padding: 20px 0;
}

.breadcrumb {
  margin-bottom: 20px;
  padding: 10px 0;
}

.loading-container {
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
}

.product-container {
  margin-bottom: 40px;
}

.product-overview {
  display: flex;
  padding: 30px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  border: 1px solid var(--border-color);
  position: relative;
  overflow: hidden;
}

/* 添加产品详情背景波浪效果 */
.product-overview::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 300px;
  height: 300px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 200 200'%3E%3Cpath fill='%230078d7' opacity='0.05' d='M39.9,-67.1C52.8,-59.5,65.2,-51.1,70,-39.3C74.9,-27.5,72.2,-12.4,69.9,1.3C67.5,15,65.6,27.4,60.3,39.4C55.1,51.3,46.5,62.7,35.2,68.3C23.9,74,11.9,73.9,-0.2,74.1C-12.3,74.3,-24.6,74.9,-35.8,69.7C-47,64.5,-57.1,53.6,-65.2,41C-73.4,28.5,-79.5,14.2,-80.3,-0.5C-81.1,-15.2,-76.6,-30.5,-68.1,-43C-59.6,-55.5,-47.2,-65.2,-34.1,-72.8C-21,-80.4,-7.4,-85.9,4.3,-93.3C16.1,-100.7,32.2,-110,39.9,-67.1Z' transform='translate(100 100)' /%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-size: contain;
  z-index: 0;
  opacity: 0.5;
}

.product-gallery {
  width: 450px;
  flex-shrink: 0;
  margin-right: 30px;
  position: relative;
  z-index: 1;
}

.main-image {
  height: 450px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f8f9fa;
  margin-bottom: 15px;
  border: 1px solid var(--border-color);
  transition: all 0.3s;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.main-image:hover {
  transform: scale(1.02);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.image-thumbnails {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s;
  background-color: #f8f9fa;
}

.thumbnail:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
}

.thumbnail.active {
  border-color: var(--primary-color);
  box-shadow: 0 3px 10px rgba(0, 120, 215, 0.2);
}

.image-error, .thumbnail-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
  font-size: 14px;
  background-color: #f5f7fa;
}

.product-info {
  flex: 1;
  position: relative;
  z-index: 1;
}

.product-name {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--text-color);
  position: relative;
  padding-bottom: 15px;
}

.product-name::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), rgba(0, 120, 215, 0.3));
  border-radius: 3px;
}

.product-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px dashed var(--border-color);
}

.product-rating {
  display: flex;
  align-items: center;
}

.review-count {
  margin-left: 10px;
  color: var(--light-text);
  font-size: 14px;
}

.product-sku {
  color: var(--light-text);
  font-size: 14px;
}

.product-price-section {
  background-color: rgba(0, 120, 215, 0.05);
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.product-price {
  display: flex;
  align-items: baseline;
}

.current-price {
  font-size: 28px;
  font-weight: bold;
  color: var(--secondary-color);
  margin-right: 10px;
}

.original-price {
  font-size: 16px;
  color: #999;
  text-decoration: line-through;
  margin-right: 10px;
}

.discount-tag {
  display: inline-block;
  background-color: var(--secondary-color);
  color: white;
  padding: 3px 8px;
  border-radius: 20px;
  font-size: 14px;
}

.option-group {
  margin-bottom: 20px;
}

.option-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 10px;
  color: var(--text-color);
}

.option-values {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.option-value {
  padding: 6px 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.option-value:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.option-value.selected {
  border-color: var(--primary-color);
  background-color: rgba(0, 120, 215, 0.05);
  color: var(--primary-color);
}

.quantity-selector {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.stock-info {
  margin-left: 15px;
  color: var(--light-text);
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.action-buttons .el-button {
  flex: 1;
  height: 46px;
  font-size: 16px;
  border-radius: 6px;
  transition: all 0.3s;
}

.add-cart-btn {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
  border: none;
  box-shadow: 0 5px 15px rgba(0, 120, 215, 0.3);
}

.add-cart-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 120, 215, 0.4);
}

.buy-now-btn {
  background: linear-gradient(135deg, #ff9500, #ff5722);
  border: none;
  box-shadow: 0 5px 15px rgba(255, 87, 34, 0.3);
}

.buy-now-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(255, 87, 34, 0.4);
}

.collect-btn {
  flex: 0 0 auto;
  width: 46px;
  transition: all 0.3s;
}

.collect-btn:hover, .collect-btn.active {
  transform: translateY(-3px);
}

.service-guarantees {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px dashed var(--border-color);
}

.guarantee-item {
  display: flex;
  align-items: center;
  color: var(--light-text);
  font-size: 14px;
}

.guarantee-item .el-icon {
  color: var(--primary-color);
  margin-right: 5px;
}

.wave-divider {
  height: 30px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='rgba(0, 120, 215, 0.05)'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  margin: 20px 0;
}

.blue-wave {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='rgba(0, 120, 215, 0.1)'/%3E%3C/svg%3E");
}

.product-detail-tabs {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  border: 1px solid var(--border-color);
}

.product-detail-tabs :deep(.el-tabs__header) {
  margin: 0;
  background: linear-gradient(90deg, #f8f9fa, white);
  border-bottom: 1px solid var(--border-color);
}

.product-detail-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  padding: 15px 20px;
  height: auto;
  transition: all 0.3s;
}

.product-detail-tabs :deep(.el-tabs__item.is-active) {
  color: var(--primary-color);
  font-weight: 500;
}

.product-detail-tabs :deep(.el-tabs__active-bar) {
  background-color: var(--primary-color);
  height: 3px;
}

.product-detail-tabs :deep(.el-tabs__content) {
  padding: 30px;
}

.product-description {
  color: var(--text-color);
  line-height: 1.7;
}

.product-description img {
  max-width: 100%;
  border-radius: 8px;
  margin: 15px 0;
}

.product-specs {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.spec-item {
  display: flex;
  padding: 10px 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.spec-name {
  width: 100px;
  color: var(--light-text);
  font-weight: 500;
}

.spec-value {
  flex: 1;
}

/* 相关推荐 */
.related-products-section {
  margin-top: 30px;
}

.section-title {
  font-size: 22px;
  font-weight: 500;
  margin-bottom: 20px;
  position: relative;
  padding-left: 15px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  height: 20px;
  width: 5px;
  background-color: var(--primary-color);
  border-radius: 5px;
}

.related-products {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  border: 1px solid var(--border-color);
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 10px 25px rgba(0, 120, 215, 0.15);
  border-color: rgba(0, 120, 215, 0.3);
}

.product-card-image {
  height: 180px;
  background-color: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 10px;
}

.product-card-image :deep(.el-image) {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-card-image :deep(.el-image__inner) {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  transition: transform 0.5s;
}

.product-card:hover .product-card-image :deep(.el-image__inner) {
  transform: scale(1.08);
}

.product-card-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-card-title {
  font-size: 15px;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  min-height: 42px;
  word-break: break-word;
}

.product-card-price {
  margin-top: auto;
  color: var(--secondary-color);
  font-weight: bold;
  font-size: 18px;
}

/* 响应式 */
@media (max-width: 992px) {
  .product-overview {
    flex-direction: column;
  }
  
  .product-gallery {
    width: 100%;
    margin-right: 0;
    margin-bottom: 30px;
  }
  
  .product-specs {
    grid-template-columns: 1fr;
  }
  
  .related-products {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
  }
  
  .collect-btn {
    width: 100%;
  }
  
  .product-detail-tabs :deep(.el-tabs__item) {
    font-size: 14px;
    padding: 12px 15px;
  }
  
  .product-detail-tabs :deep(.el-tabs__content) {
    padding: 20px;
  }
  
  .related-products {
    grid-template-columns: 1fr;
  }
}
</style> 