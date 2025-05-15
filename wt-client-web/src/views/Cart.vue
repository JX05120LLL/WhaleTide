<template>
  <div class="cart-page container">
    <h1 class="page-title">我的购物车</h1>
    
    <!-- 加载中 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    
    <!-- 购物车为空 -->
    <div v-else-if="cartItems.length === 0" class="empty-cart">
      <el-empty description="您的购物车还是空的">
        <template #image>
          <el-icon :size="80"><ShoppingCart /></el-icon>
        </template>
        <el-button type="primary" @click="$router.push('/')">去购物</el-button>
      </el-empty>
    </div>
    
    <!-- 购物车内容 -->
    <div v-else class="cart-content">
      <!-- 购物车表头 -->
      <div class="cart-header">
        <div class="col-checkbox">
          <el-checkbox v-model="allSelected" @change="handleSelectAll">全选</el-checkbox>
        </div>
        <div class="col-product">商品信息</div>
        <div class="col-price">单价</div>
        <div class="col-quantity">数量</div>
        <div class="col-subtotal">小计</div>
        <div class="col-action">操作</div>
      </div>
      
      <!-- 购物车商品列表 -->
      <div class="cart-items">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <div class="col-checkbox">
            <el-checkbox 
              :model-value="item.checked === 1" 
              @change="(val) => updateItemCheck(item, val)"
            ></el-checkbox>
          </div>
          
          <div class="col-product">
            <div class="product-info">
              <router-link :to="`/product/${item.productId}`" class="product-image">
                <img :src="formatImage(item.productImage)" :alt="item.productName">
              </router-link>
              <div class="product-details">
                <router-link :to="`/product/${item.productId}`" class="product-name">
                  {{ item.productName }}
                </router-link>
                <div v-if="item.skuSpecs" class="product-specs">{{ item.skuSpecs }}</div>
              </div>
            </div>
          </div>
          
          <div class="col-price">
            <div class="price">¥{{ formatPrice(item.price) }}</div>
          </div>
          
          <div class="col-quantity">
            <el-input-number 
              v-model="item.quantity" 
              :min="1" 
              :max="99"
              size="small"
              @change="(val) => updateItemQuantity(item, val)"
            ></el-input-number>
          </div>
          
          <div class="col-subtotal">
            <div class="subtotal">¥{{ formatPrice(item.subtotal) }}</div>
          </div>
          
          <div class="col-action">
            <el-button type="danger" link @click="handleRemoveItem(item.id)">删除</el-button>
          </div>
        </div>
      </div>
      
      <!-- 购物车底部 -->
      <div class="cart-footer">
        <div class="footer-left">
          <el-checkbox v-model="allSelected" @change="handleSelectAll">全选</el-checkbox>
          <el-button type="text" @click="handleRemoveSelected">删除选中</el-button>
          <el-button type="text" @click="handleClearCart">清空购物车</el-button>
        </div>
        <div class="footer-right">
          <div class="summary">
            <span>已选 <span class="selected-count">{{ selectedCount }}</span> 件商品</span>
            <span class="total-amount">总计: <span class="amount">¥{{ formatPrice(totalAmount) }}</span></span>
          </div>
          <el-button 
            type="primary" 
            size="large" 
            :disabled="selectedCount === 0"
            @click="handleCheckout"
          >
            去结算
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ShoppingCart } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import * as cartApi from '@/api/cart';
import { staticBaseURL } from '@/utils/request';

export default {
  name: 'CartPage',
  components: {
    ShoppingCart
  },
  data() {
    return {
      loading: true,
      cartItems: [],
      allSelected: false,
      selectedCount: 0,
      totalAmount: 0
    };
  },
  mounted() {
    this.fetchCartData();
  },
  methods: {
    // 获取购物车数据
    async fetchCartData() {
      this.loading = true;
      try {
        const res = await cartApi.getCartList();
        
        if (res && res.data) {
          this.cartItems = res.data;
          this.updateCartSummary();
        } else {
          this.cartItems = [];
        }
      } catch (error) {
        console.error('获取购物车数据失败:', error);
        ElMessage.error('获取购物车数据失败');
        this.cartItems = [];
      } finally {
        this.loading = false;
      }
    },
    
    // 更新购物车汇总信息
    updateCartSummary() {
      if (!this.cartItems || this.cartItems.length === 0) {
        this.selectedCount = 0;
        this.totalAmount = 0;
        this.allSelected = false;
        return;
      }
      
      let selectedItems = this.cartItems.filter(item => item.checked === 1);
      this.selectedCount = selectedItems.length;
      this.totalAmount = selectedItems.reduce((sum, item) => {
        return sum + (item.subtotal || (item.price * item.quantity));
      }, 0);
      
      this.allSelected = this.cartItems.length > 0 && selectedItems.length === this.cartItems.length;
    },
    
    // 处理全选/取消全选
    async handleSelectAll(val) {
      if (this.cartItems.length === 0) return;
      
      // 更新本地数据
      this.cartItems.forEach(item => {
        item.checked = val ? 1 : 0;
      });
      
      // 更新购物车汇总信息
      this.updateCartSummary();
      
      // TODO: 实现批量选中/取消选中的后端API
      // 目前后端没有提供批量更新API，所以需要逐个更新
      try {
        await Promise.all(this.cartItems.map(item => {
          return cartApi.updateCartItemChecked({
            id: item.id,
            checked: val ? 1 : 0
          });
        }));
      } catch (error) {
        console.error('更新选中状态失败:', error);
        ElMessage.error('操作失败');
        // 刷新数据
        this.fetchCartData();
      }
    },
    
    // 更新商品选中状态
    async updateItemCheck(item, checked) {
      const checkedValue = checked ? 1 : 0;
      
      // 更新本地数据
      item.checked = checkedValue;
      this.updateCartSummary();
      
      // 同步到服务器
      try {
        await cartApi.updateCartItemChecked({
          id: item.id,
          checked: checkedValue
        });
      } catch (error) {
        console.error('更新选中状态失败:', error);
        ElMessage.error('操作失败');
        // 刷新数据
        this.fetchCartData();
      }
    },
    
    // 更新商品数量
    async updateItemQuantity(item, quantity) {
      if (quantity < 1) quantity = 1;
      
      // 更新本地数据
      const oldQuantity = item.quantity;
      item.quantity = quantity;
      item.subtotal = item.price * quantity;
      this.updateCartSummary();
      
      // 同步到服务器
      try {
        await cartApi.updateCartItemQuantity({
          id: item.id,
          quantity: quantity
        });
      } catch (error) {
        console.error('更新商品数量失败:', error);
        ElMessage.error('更新失败');
        
        // 恢复原数量
        item.quantity = oldQuantity;
        item.subtotal = item.price * oldQuantity;
        this.updateCartSummary();
      }
    },
    
    // 删除单个商品
    handleRemoveItem(itemId) {
      ElMessageBox.confirm('确定要从购物车中删除该商品吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await cartApi.removeCartItems([itemId]);
          this.cartItems = this.cartItems.filter(item => item.id !== itemId);
          this.updateCartSummary();
          ElMessage.success('已从购物车移除');
        } catch (error) {
          console.error('删除商品失败:', error);
          ElMessage.error('删除失败');
        }
      }).catch(() => {});
    },
    
    // 删除选中商品
    handleRemoveSelected() {
      const selectedItems = this.cartItems.filter(item => item.checked === 1);
      if (selectedItems.length === 0) {
        ElMessage.warning('请先选择商品');
        return;
      }
      
      ElMessageBox.confirm(`确定要删除选中的 ${selectedItems.length} 件商品吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const selectedIds = selectedItems.map(item => item.id);
          await cartApi.removeCartItems(selectedIds);
          this.cartItems = this.cartItems.filter(item => item.checked !== 1);
          this.updateCartSummary();
          ElMessage.success('已删除选中商品');
        } catch (error) {
          console.error('删除选中商品失败:', error);
          ElMessage.error('删除失败');
        }
      }).catch(() => {});
    },
    
    // 清空购物车
    handleClearCart() {
      if (this.cartItems.length === 0) {
        ElMessage.warning('购物车已经是空的');
        return;
      }
      
      ElMessageBox.confirm('确定要清空购物车吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await cartApi.clearCart();
          this.cartItems = [];
          this.updateCartSummary();
          ElMessage.success('购物车已清空');
        } catch (error) {
          console.error('清空购物车失败:', error);
          ElMessage.error('操作失败');
        }
      }).catch(() => {});
    },
    
    // 去结算
    handleCheckout() {
      if (this.selectedCount === 0) {
        ElMessage.warning('请先选择商品');
        return;
      }
      this.$router.push('/checkout');
    },
    
    // 格式化价格
    formatPrice(price) {
      if (price === undefined || price === null || isNaN(price)) {
        return '0.00';
      }
      if (typeof price === 'string') {
        price = parseFloat(price);
      }
      return price.toFixed(2);
    },
    
    // 格式化图片
    formatImage(image) {
      if (!image) return '/images/placeholder.png';
      
      // 已经是完整URL的情况
      if (image.startsWith('http://') || image.startsWith('https://')) {
        return image;
      }
      
      // 相对路径处理
      if (!image.startsWith('/')) {
        image = '/' + image;
      }
      
      return `${staticBaseURL}${image}`;
    }
  }
};
</script>

<style scoped>
.cart-page {
  padding: 40px 0;
  background-color: var(--bg-color);
  min-height: 80vh;
  position: relative;
}

/* 页面背景装饰 */
.cart-page::before {
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
  opacity: 0.7;
  pointer-events: none;
}

.container {
  position: relative;
  z-index: 1;
}

.page-title {
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 500;
  color: var(--text-color);
  position: relative;
  padding-left: 20px;
}

.page-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  height: 24px;
  width: 5px;
  background: linear-gradient(to bottom, var(--primary-color), var(--primary-light));
  border-radius: 5px;
}

.loading-container {
  background-color: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.empty-cart {
  background-color: #fff;
  border-radius: 12px;
  padding: 80px 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.5s ease;
}

.empty-cart :deep(.el-empty) {
  margin-bottom: 20px;
}

.empty-cart :deep(.el-icon) {
  font-size: 80px;
  color: var(--primary-color);
  opacity: 0.5;
  transition: all 0.5s ease;
}

.empty-cart:hover :deep(.el-icon) {
  transform: translateY(-5px);
  color: var(--primary-dark);
  opacity: 0.8;
}

.empty-cart :deep(.el-button) {
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.empty-cart :deep(.el-button:hover) {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 120, 215, 0.35);
}

.cart-content {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  animation: fadeIn 0.5s ease;
}

.cart-header {
  display: flex;
  align-items: center;
  padding: 18px 24px;
  background: linear-gradient(90deg, #f8f9fa, #edf3f8);
  border-bottom: 1px solid #e8e8e8;
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.cart-items .cart-item {
  display: flex;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.3s ease;
}

.cart-item:hover {
  background-color: rgba(0, 120, 215, 0.02);
}

.cart-item:last-child {
  border-bottom: none;
}

.col-checkbox {
  width: 60px;
  display: flex;
  justify-content: center;
}

.col-checkbox :deep(.el-checkbox__inner) {
  border-color: #ccc;
  transition: all 0.3s ease;
}

.col-checkbox :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.col-product {
  flex: 1;
  min-width: 300px;
}

.col-price,
.col-quantity,
.col-subtotal {
  width: 120px;
  text-align: center;
}

.col-action {
  width: 100px;
  text-align: center;
}

.product-info {
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-right: 20px;
  overflow: hidden;
  border: 1px solid #eee;
  transition: all 0.3s ease;
}

.product-image:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  border-color: #ddd;
}

.product-image img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  transition: transform 0.5s ease;
}

.product-image:hover img {
  transform: scale(1.1);
}

.product-details {
  flex: 1;
}

.product-name {
  display: block;
  font-size: 16px;
  color: var(--text-color);
  margin-bottom: 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-decoration: none;
  transition: color 0.3s ease;
}

.product-name:hover {
  color: var(--primary-color);
}

.product-specs {
  font-size: 13px;
  color: #999;
  background-color: #f5f5f5;
  display: inline-block;
  padding: 3px 10px;
  border-radius: 4px;
}

.price {
  font-size: 15px;
  color: #666;
}

.subtotal {
  font-weight: 500;
  color: var(--secondary-color);
  font-size: 18px;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  background: linear-gradient(90deg, #f8f9fa, #edf3f8);
  border-top: 1px solid #e8e8e8;
  position: relative;
}

/* 添加波浪底边 */
.cart-footer::before {
  content: '';
  position: absolute;
  top: -5px;
  left: 0;
  width: 100%;
  height: 5px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z' fill='rgba(0, 120, 215, 0.05)'/%3E%3C/svg%3E");
  background-size: cover;
  transform: rotate(180deg);
  z-index: 1;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.footer-left :deep(.el-button) {
  color: #666;
  transition: all 0.3s ease;
  position: relative;
}

.footer-left :deep(.el-button:hover) {
  color: var(--primary-color);
}

.footer-left :deep(.el-button::after) {
  content: '';
  position: absolute;
  left: 0;
  bottom: -2px;
  width: 0;
  height: 1px;
  background-color: var(--primary-color);
  transition: width 0.3s ease;
}

.footer-left :deep(.el-button:hover::after) {
  width: 100%;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.summary {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.selected-count {
  color: var(--primary-color);
  font-weight: bold;
}

.total-amount {
  margin-top: 5px;
  font-size: 15px;
}

.amount {
  color: var(--secondary-color);
  font-size: 24px;
  font-weight: bold;
}

.footer-right :deep(.el-button) {
  height: 46px;
  padding: 0 30px;
  font-size: 16px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
  transition: all 0.3s ease;
}

.footer-right :deep(.el-button:hover) {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 120, 215, 0.35);
}

.footer-right :deep(.el-button:active) {
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(0, 120, 215, 0.25);
}

.footer-right :deep(.el-button.is-disabled) {
  background: linear-gradient(90deg, #a0cfff, #8bbeed);
  opacity: 0.8;
  box-shadow: none;
}

/* 输入数字样式 */
.col-quantity :deep(.el-input-number) {
  width: 120px;
}

.col-quantity :deep(.el-input-number__decrease),
.col-quantity :deep(.el-input-number__increase) {
  background-color: #f5f7fa;
  color: #606266;
  transition: all 0.3s ease;
}

.col-quantity :deep(.el-input-number__decrease:hover),
.col-quantity :deep(.el-input-number__increase:hover) {
  color: var(--primary-color);
  background-color: #e6f1fc;
}

/* 删除按钮样式 */
.col-action :deep(.el-button) {
  color: #ff6700;
  transition: all 0.3s ease;
}

.col-action :deep(.el-button:hover) {
  color: #ff4500;
  transform: scale(1.1);
}

@media (max-width: 992px) {
  .cart-header {
    padding: 15px;
  }
  
  .cart-items .cart-item {
    padding: 15px;
  }
  
  .cart-footer {
    padding: 15px;
    flex-direction: column;
    gap: 15px;
  }
  
  .footer-left {
    width: 100%;
  }
  
  .footer-right {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .cart-content {
    border-radius: 8px;
  }
  
  .col-price,
  .col-quantity,
  .col-subtotal {
    width: auto;
    flex: 1;
  }
  
  .product-image {
    width: 80px;
    height: 80px;
    margin-right: 15px;
  }
  
  .product-name {
    font-size: 14px;
  }
  
  .product-specs {
    font-size: 12px;
  }
  
  .col-quantity :deep(.el-input-number) {
    width: 100px;
  }
}
</style>