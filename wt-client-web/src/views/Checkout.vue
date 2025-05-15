<template>
  <div class="checkout-page">
    <app-header></app-header>
    
    <div class="container">
      <h1 class="page-title">订单确认</h1>

      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <!-- 订单内容 -->
      <div v-else class="checkout-content">
        <!-- 收货地址 -->
        <div class="checkout-section address-section">
          <div class="section-header">
            <h2>收货地址</h2>
            <el-button type="primary" link @click="showAddressDialog">管理收货地址</el-button>
          </div>
          
          <div v-if="addresses.length === 0" class="empty-address">
            <el-empty description="您还没有添加收货地址">
              <el-button type="primary" @click="showAddressDialog">添加收货地址</el-button>
            </el-empty>
          </div>
          
          <div v-else class="address-list">
            <div 
              v-for="address in addresses" 
              :key="address.id" 
              class="address-item"
              :class="{ active: selectedAddressId === address.id }"
              @click="selectedAddressId = address.id"
            >
              <div class="address-content">
                <div class="address-name">{{ address.name }} <span class="address-phone">{{ address.phone }}</span></div>
                <div class="address-detail">
                  {{ address.province }} {{ address.city }} {{ address.region }} {{ address.detailAddress }}
                </div>
                <div v-if="address.defaultStatus === 1" class="address-default-tag">默认</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 商品信息 -->
        <div class="checkout-section order-items-section">
          <div class="section-header">
            <h2>商品信息</h2>
          </div>
          
          <div class="order-items">
            <div class="item-header">
              <div class="col-product">商品信息</div>
              <div class="col-price">单价</div>
              <div class="col-quantity">数量</div>
              <div class="col-subtotal">小计</div>
            </div>
            
            <div v-if="orderItems.length === 0" class="empty-cart">
              <el-empty description="暂无商品" :image-size="100"></el-empty>
            </div>
            
            <div v-else v-for="item in orderItems" :key="item.id" class="item-row">
              <div class="col-product">
                <div class="product-info">
                  <div class="product-image">
                    <img :src="formatImage(item.productPic)" :alt="item.productName">
                  </div>
                  <div class="product-details">
                    <div class="product-name">{{ item.productName }}</div>
                    <div v-if="item.skuSpecs" class="product-specs">{{ item.skuSpecs }}</div>
                  </div>
                </div>
              </div>
              <div class="col-price">¥{{ formatPrice(item.price) }}</div>
              <div class="col-quantity">× {{ item.quantity }}</div>
              <div class="col-subtotal">¥{{ formatPrice(item.subtotal) }}</div>
            </div>
          </div>
        </div>
        
        <!-- 支付方式 -->
        <div class="checkout-section payment-section">
          <div class="section-header">
            <h2>支付方式</h2>
          </div>
          
          <div class="payment-methods">
            <div 
              v-for="method in paymentMethods" 
              :key="method.value" 
              class="payment-method-item"
              :class="{ active: selectedPaymentMethod === method.value }"
              @click="selectedPaymentMethod = method.value"
            >
              <i :class="method.icon"></i>
              <span>{{ method.label }}</span>
            </div>
          </div>
        </div>
        
        <!-- 订单备注 -->
        <div class="checkout-section note-section">
          <div class="section-header">
            <h2>订单备注</h2>
          </div>
          
          <el-input
            v-model="orderNote"
            type="textarea"
            :rows="2"
            placeholder="请输入订单备注（选填）"
            maxlength="200"
            show-word-limit
          />
        </div>
        
        <!-- 订单金额 -->
        <div class="checkout-section order-amount-section">
          <div class="amount-item">
            <span class="label">商品总价：</span>
            <span class="value">¥{{ formatPrice(totalProductAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="label">运费：</span>
            <span class="value">¥{{ formatPrice(shippingAmount) }}</span>
          </div>
          <div class="amount-item total">
            <span class="label">实付金额：</span>
            <span class="value">¥{{ formatPrice(totalAmount) }}</span>
          </div>
        </div>
        
        <!-- 提交订单 -->
        <div class="checkout-submit">
          <div class="submit-info">
            <div>收货地址：{{ selectedAddress ? `${selectedAddress.name} ${selectedAddress.phone} ${selectedAddress.province} ${selectedAddress.city} ${selectedAddress.district} ${selectedAddress.detail}` : '请选择收货地址' }}</div>
            <div class="total-amount">实付金额：<span class="price">¥{{ formatPrice(totalAmount) }}</span></div>
          </div>
          
          <el-button 
            type="primary" 
            size="large" 
            :disabled="!canSubmit" 
            :loading="submitting"
            @click="submitOrder"
          >
            提交订单
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 地址管理对话框 -->
    <el-dialog
      v-model="addressDialogVisible"
      title="管理收货地址"
      width="600px"
    >
      <address-list @select="handleAddressSelect" @close="addressDialogVisible = false"></address-list>
    </el-dialog>
    
    <!-- 支付对话框 -->
    <el-dialog
      v-model="payDialogVisible"
      title="订单支付"
      width="500px"
      center
      :close-on-click-modal="false"
      :show-close="false"
    >
      <div class="pay-dialog-content">
        <div class="pay-info">
          <div class="pay-order-no">订单号：{{ generatedOrder.orderSn }}</div>
          <div class="pay-amount">支付金额：<span class="price">¥{{ formatPrice(generatedOrder.totalAmount) }}</span></div>
        </div>
        
        <div class="pay-qrcode">
          <div class="qrcode-placeholder">
            <i class="el-icon-picture-outline"></i>
            <div>模拟支付二维码</div>
          </div>
        </div>
        
        <div class="pay-tips">
          <p>请使用{{ getPaymentMethodName() }}扫描二维码完成支付</p>
          <p class="small">（本项目仅作演示，支付为模拟操作）</p>
        </div>
        
        <div class="pay-actions">
          <el-button @click="cancelPay">取消支付</el-button>
          <el-button type="primary" @click="simulatePaySuccess">模拟支付成功</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { computed, onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import * as orderApi from '@/api/order';
import * as cartApi from '@/api/cart';
import { staticBaseURL } from '@/utils/request';
import AppHeader from '@/components/AppHeader.vue';
import AddressList from '@/components/user/AddressList.vue';

export default {
  name: 'CheckoutPage',
  components: {
    AppHeader,
    AddressList
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    
    // 状态
    const loading = ref(true);
    const submitting = ref(false);
    const addressDialogVisible = ref(false);
    const payDialogVisible = ref(false);
    
    // 数据
    const confirmOrder = ref(null);
    const addresses = ref([]);
    const orderItems = ref([]);
    const selectedAddressId = ref(null);
    const orderNote = ref('');
    const generatedOrder = ref({});
    
    // 支付方式
    const selectedPaymentMethod = ref(1);
    const paymentMethods = [
      { label: '支付宝', value: 1, icon: 'el-icon-money' },
      { label: '微信支付', value: 2, icon: 'el-icon-chat-round' },
      { label: '银联支付', value: 3, icon: 'el-icon-credit-card' }
    ];
    
    // 计算属性
    const totalProductAmount = computed(() => {
      if (!orderItems.value.length) return 0;
      return orderItems.value.reduce((sum, item) => sum + item.subtotal, 0);
    });
    
    const shippingAmount = computed(() => {
      return confirmOrder.value?.freightAmount || 0;
    });
    
    const totalAmount = computed(() => {
      return totalProductAmount.value + shippingAmount.value;
    });
    
    const selectedAddress = computed(() => {
      return addresses.value.find(addr => addr.id === selectedAddressId.value);
    });
    
    const canSubmit = computed(() => {
      return selectedAddressId.value && orderItems.value.length > 0;
    });
    
    // 初始化订单确认页
    const initCheckout = async () => {
      loading.value = true;
      try {
        // 获取URL参数中的cartItemIds
        const cartItemIds = route.query.cartItemIds ? 
          route.query.cartItemIds.split(',').map(id => parseInt(id)) : 
          [];
        
        // 判断是否是直接购买模式
        const isDirectBuy = route.query.directBuy === 'true';
        
        // 如果是直接购买模式，则只使用传递的购物车项ID
        let itemIds = cartItemIds;
        
        // 如果不是直接购买且没有指定购物车项，则获取所有选中的购物车项
        if (!isDirectBuy && !itemIds.length) {
          const cartRes = await cartApi.getCartList();
          if (cartRes && cartRes.data) {
            itemIds = cartRes.data
              .filter(item => item.checked === 1)
              .map(item => item.id);
          }
        }
        
        if (!itemIds.length) {
          ElMessage.warning('请至少选择一件商品');
          router.push('/cart');
          return;
        }
        
        // 生成确认订单
        const res = await orderApi.generateConfirmOrder({
          cartItemIds: itemIds
        });
        
        if (res && res.data) {
          confirmOrder.value = res.data;
          orderItems.value = res.data.orderItems || [];
          addresses.value = res.data.addresses || [];
          
          // 如果有默认地址，自动选中
          const defaultAddress = addresses.value.find(addr => addr.isDefault === true);
          if (defaultAddress) {
            selectedAddressId.value = defaultAddress.id;
          } else if (addresses.value.length > 0) {
            selectedAddressId.value = addresses.value[0].id;
          }
        }
      } catch (error) {
        console.error('初始化结算页面失败:', error);
        ElMessage.error('加载订单数据失败');
        router.push('/cart');
      } finally {
        loading.value = false;
      }
    };
    
    // 提交订单
    const submitOrder = async () => {
      if (!selectedAddressId.value) {
        ElMessage.warning('请选择收货地址');
        return;
      }
      
      if (orderItems.value.length === 0) {
        ElMessage.warning('订单中没有商品');
        return;
      }
      
      submitting.value = true;
      try {
        // 构建订单数据
        const orderData = {
          cartItemIds: orderItems.value.map(item => item.id),
          addressId: selectedAddressId.value,
          payType: selectedPaymentMethod.value,
          note: orderNote.value
        };
        
        // 提交订单
        const res = await orderApi.generateOrder(orderData);
        
        if (res && res.data) {
          generatedOrder.value = res.data;
          ElMessage.success('订单创建成功');
          
          // 显示支付对话框
          payDialogVisible.value = true;
        } else {
          ElMessage.error(res?.message || '提交订单失败');
        }
      } catch (error) {
        console.error('提交订单失败:', error);
        ElMessage.error('提交订单失败');
      } finally {
        submitting.value = false;
      }
    };
    
    // 模拟支付成功
    const simulatePaySuccess = async () => {
      try {
        // 调用支付成功回调接口
        const res = await orderApi.paySuccess({
          orderId: generatedOrder.value.orderId,
          payType: selectedPaymentMethod.value
        });
        
        if (res && res.code === 200) {
          payDialogVisible.value = false;
          ElMessage.success('支付成功');
          
          // 跳转到订单详情页
          ElMessageBox.alert('支付成功！感谢您的购买。', '订单支付', {
            confirmButtonText: '查看订单',
            callback: () => {
              router.push(`/user/orders`);
            }
          });
        } else {
          ElMessage.error(res?.message || '支付处理失败');
        }
      } catch (error) {
        console.error('模拟支付失败:', error);
        ElMessage.error('支付处理失败');
      }
    };
    
    // 取消支付
    const cancelPay = () => {
      ElMessageBox.confirm('确定要取消支付吗？您可以稍后在订单中心完成支付。', '取消支付', {
        confirmButtonText: '确定',
        cancelButtonText: '继续支付',
        type: 'warning'
      }).then(() => {
        payDialogVisible.value = false;
        router.push('/user/orders');
      }).catch(() => {});
    };
    
    // 显示地址管理对话框
    const showAddressDialog = () => {
      addressDialogVisible.value = true;
    };
    
    // 处理地址选择
    const handleAddressSelect = (address) => {
      selectedAddressId.value = address.id;
      addressDialogVisible.value = false;
    };
    
    // 获取支付方式名称
    const getPaymentMethodName = () => {
      const method = paymentMethods.find(m => m.value === selectedPaymentMethod.value);
      return method ? method.label : '支付宝';
    };
    
    // 格式化价格
    const formatPrice = (price) => {
      if (price === undefined || price === null || isNaN(price)) {
        return '0.00';
      }
      if (typeof price === 'string') {
        price = parseFloat(price);
      }
      return price.toFixed(2);
    };
    
    // 格式化图片
    const formatImage = (image) => {
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
    };
    
    onMounted(() => {
      initCheckout();
    });
    
    return {
      loading,
      submitting,
      addresses,
      orderItems,
      selectedAddressId,
      orderNote,
      paymentMethods,
      selectedPaymentMethod,
      addressDialogVisible,
      payDialogVisible,
      generatedOrder,
      totalProductAmount,
      shippingAmount,
      totalAmount,
      selectedAddress,
      canSubmit,
      submitOrder,
      showAddressDialog,
      handleAddressSelect,
      simulatePaySuccess,
      cancelPay,
      getPaymentMethodName,
      formatPrice,
      formatImage
    };
  }
};
</script>

<style scoped>
.checkout-page {
  background-color: var(--bg-color);
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 添加海洋背景装饰 */
.checkout-page::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 400px;
  height: 400px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 200 200'%3E%3Cpath fill='%230078d7' opacity='0.03' d='M39.9,-67.1C52.8,-59.5,65.2,-51.1,70,-39.3C74.9,-27.5,72.2,-12.4,69.9,1.3C67.5,15,65.6,27.4,60.3,39.4C55.1,51.3,46.5,62.7,35.2,68.3C23.9,74,11.9,73.9,-0.2,74.1C-12.3,74.3,-24.6,74.9,-35.8,69.7C-47,64.5,-57.1,53.6,-65.2,41C-73.4,28.5,-79.5,14.2,-80.3,-0.5C-81.1,-15.2,-76.6,-30.5,-68.1,-43C-59.6,-55.5,-47.2,-65.2,-34.1,-72.8C-21,-80.4,-7.4,-85.9,4.3,-93.3C16.1,-100.7,32.2,-110,39.9,-67.1Z' transform='translate(100 100)' /%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-size: contain;
  z-index: 0;
  opacity: 0.7;
  pointer-events: none;
}

.checkout-page::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 120px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z' fill='rgba(0, 120, 215, 0.05)'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  z-index: 0;
  opacity: 0.5;
  pointer-events: none;
}

.container {
  position: relative;
  z-index: 1;
  padding: 30px 0 80px;
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

.checkout-content {
  animation: fadeIn 0.5s ease;
}

.checkout-section {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.checkout-section:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 120, 215, 0.1);
}

.section-header {
  padding: 18px 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(90deg, #f8f9fa, #edf3f8);
}

.section-header h2 {
  font-size: 18px;
  font-weight: 500;
  color: var(--text-color);
  margin: 0;
  display: flex;
  align-items: center;
}

.section-header h2::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 18px;
  background: linear-gradient(to bottom, var(--primary-color), var(--primary-light));
  border-radius: 2px;
  margin-right: 10px;
}

/* 地址样式 */
.address-section .section-header .el-button {
  color: var(--primary-color);
  transition: all 0.3s;
}

.address-section .section-header .el-button:hover {
  color: var(--primary-dark);
  transform: translateY(-2px);
}

.empty-address {
  padding: 40px 0;
  text-align: center;
}

.address-list {
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.address-item {
  border: 1px solid #e0e6ed;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.address-item:hover {
  border-color: var(--primary-light);
  background-color: rgba(0, 120, 215, 0.02);
  transform: translateY(-2px);
}

.address-item.active {
  border-color: var(--primary-color);
  background-color: rgba(0, 120, 215, 0.05);
  box-shadow: 0 3px 10px rgba(0, 120, 215, 0.1);
}

.address-item.active::before {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  border-style: solid;
  border-width: 0 24px 24px 0;
  border-color: transparent var(--primary-color) transparent transparent;
}

.address-name {
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 16px;
}

.address-phone {
  color: #666;
  margin-left: 10px;
  font-weight: normal;
}

.address-detail {
  color: #666;
  line-height: 1.5;
}

.address-default-tag {
  display: inline-block;
  background-color: var(--primary-color);
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  margin-top: 8px;
}

/* 商品信息样式 */
.order-items-section {
  overflow: hidden;
}

.order-items {
  padding: 0;
}

.item-header {
  display: flex;
  padding: 14px 24px;
  background-color: #f9fafc;
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.item-row {
  display: flex;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
  transition: background-color 0.3s;
}

.item-row:hover {
  background-color: rgba(0, 120, 215, 0.02);
}

.item-row:last-child {
  border-bottom: none;
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

.product-info {
  display: flex;
  align-items: center;
}

.product-image {
  width: 80px;
  height: 80px;
  background-color: #f8f9fa;
  border-radius: 6px;
  overflow: hidden;
  margin-right: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #eee;
  transition: all 0.3s;
}

.product-image:hover {
  transform: scale(1.05);
  border-color: #ddd;
}

.product-image img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  transition: transform 0.5s;
}

.product-image:hover img {
  transform: scale(1.1);
}

.product-name {
  font-size: 15px;
  margin-bottom: 8px;
  color: var(--text-color);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-specs {
  font-size: 13px;
  color: #999;
  background-color: #f5f5f5;
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
}

/* 支付方式样式 */
.payment-methods {
  display: flex;
  padding: 20px;
  flex-wrap: wrap;
  gap: 20px;
}

.payment-method-item {
  border: 2px solid #e0e6ed;
  border-radius: 8px;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
  min-width: 180px;
}

.payment-method-item:hover {
  border-color: var(--primary-light);
  background-color: rgba(0, 120, 215, 0.02);
  transform: translateY(-2px);
}

.payment-method-item.active {
  border-color: var(--primary-color);
  background-color: rgba(0, 120, 215, 0.05);
  box-shadow: 0 3px 10px rgba(0, 120, 215, 0.1);
}

.payment-method-item i {
  font-size: 24px;
  color: var(--primary-color);
}

/* 订单备注样式 */
.note-section {
  padding-bottom: 20px;
}

.note-section .el-textarea {
  padding: 20px;
}

.note-section .el-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  padding: 12px;
  transition: all 0.3s;
  border-color: #e0e6ed;
}

.note-section .el-textarea :deep(.el-textarea__inner:focus) {
  border-color: var(--primary-color);
  box-shadow: 0 0 8px rgba(0, 120, 215, 0.1);
}

/* 订单金额样式 */
.order-amount-section {
  padding: 20px 24px;
}

.amount-item {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 8px 0;
  color: #666;
}

.amount-item .label {
  margin-right: 10px;
}

.amount-item .value {
  font-size: 16px;
  color: var(--text-color);
  min-width: 100px;
  text-align: right;
}

.amount-item.total {
  padding-top: 15px;
  margin-top: 10px;
  border-top: 1px dashed #e0e6ed;
}

.amount-item.total .label {
  font-size: 16px;
  color: var(--text-color);
}

.amount-item.total .value {
  font-size: 24px;
  font-weight: bold;
  color: var(--secondary-color);
}

/* 提交订单样式 */
.checkout-submit {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 30px;
  position: relative;
  overflow: hidden;
}

/* 添加波浪背景 */
.checkout-submit::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 8px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  opacity: 0.8;
}

.submit-info {
  color: #666;
  line-height: 1.6;
}

.submit-info .total-amount {
  margin-top: 5px;
  font-weight: 500;
  color: var(--text-color);
}

.submit-info .price {
  font-size: 24px;
  font-weight: bold;
  color: var(--secondary-color);
}

.checkout-submit .el-button {
  height: 46px;
  padding: 0 30px;
  font-size: 16px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  border-radius: 8px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
}

.checkout-submit .el-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 120, 215, 0.35);
}

.checkout-submit .el-button:active {
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(0, 120, 215, 0.25);
}

.checkout-submit .el-button.is-disabled {
  background: linear-gradient(90deg, #a0cfff, #8bbeed);
  opacity: 0.8;
  box-shadow: none;
}

/* 支付对话框样式 */
.pay-dialog-content {
  padding: 20px 0;
  text-align: center;
}

.pay-info {
  margin-bottom: 30px;
}

.pay-order-no {
  color: #666;
  margin-bottom: 10px;
}

.pay-amount {
  font-size: 18px;
  color: var(--text-color);
  font-weight: 500;
}

.pay-amount .price {
  font-size: 28px;
  font-weight: bold;
  color: var(--secondary-color);
}

.pay-qrcode {
  width: 200px;
  height: 200px;
  margin: 0 auto 30px;
  background-color: #f5f7fa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 1px dashed #ddd;
  position: relative;
  overflow: hidden;
}

.pay-qrcode::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, rgba(0, 120, 215, 0.05) 25%, transparent 25%, transparent 50%, rgba(0, 120, 215, 0.05) 50%, rgba(0, 120, 215, 0.05) 75%, transparent 75%);
  background-size: 20px 20px;
  opacity: 0.5;
  animation: moveBackground 30s linear infinite;
}

@keyframes moveBackground {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: 400px 400px;
  }
}

.qrcode-placeholder {
  position: relative;
  z-index: 1;
  font-size: 14px;
  color: #666;
}

.qrcode-placeholder i {
  font-size: 50px;
  color: #999;
  margin-bottom: 10px;
}

.pay-tips {
  margin-bottom: 30px;
  color: #666;
}

.pay-tips .small {
  font-size: 13px;
  color: #999;
  margin-top: 5px;
}

.pay-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.pay-actions .el-button {
  min-width: 120px;
  transition: all 0.3s;
}

.pay-actions .el-button--primary {
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
}

.pay-actions .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0, 120, 215, 0.35);
}

/* 响应式样式 */
@media (max-width: 992px) {
  .address-list {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
  
  .checkout-submit {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .submit-info {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 20px 15px 60px;
  }
  
  .address-list {
    grid-template-columns: 1fr;
    padding: 15px;
  }
  
  .payment-methods {
    padding: 15px;
    gap: 15px;
    justify-content: center;
  }
  
  .payment-method-item {
    min-width: 130px;
    padding: 12px 15px;
  }
  
  .item-header,
  .item-row {
    padding: 15px;
  }
  
  .col-price,
  .col-quantity,
  .col-subtotal {
    width: auto;
    flex: 1;
  }
  
  .product-image {
    width: 60px;
    height: 60px;
  }
  
  .product-name {
    font-size: 14px;
  }
}
</style> 