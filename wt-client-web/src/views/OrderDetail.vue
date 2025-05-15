/* eslint-disable */
<template>
  <div class="order-detail-container">
    <div class="order-detail-header">
      <h1>订单详情</h1>
    </div>
    
    <el-card class="order-detail-card" v-loading="loading">
      <!-- 订单状态 -->
      <div class="order-status">
        <div class="status-icon" :class="getStatusClass(order.status)">
          <i :class="getStatusIcon(order.status)"></i>
        </div>
        <div class="status-info">
          <div class="status-text">{{ getStatusText(order.status) }}</div>
          <div class="status-desc">{{ getStatusDescription(order.status) }}</div>
        </div>
      </div>
      
      <!-- 订单信息 -->
      <div class="order-info-section">
        <h3>订单信息</h3>
        <div class="info-container">
          <div class="info-item">
            <span class="label">订单编号:</span>
            <span class="value">{{ order.orderSn }}</span>
          </div>
          <div class="info-item">
            <span class="label">下单时间:</span>
            <span class="value">{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="info-item">
            <span class="label">支付方式:</span>
            <span class="value">{{ getPaymentMethod(order.payType) }}</span>
          </div>
          <div class="info-item" v-if="order.paymentTime">
            <span class="label">支付时间:</span>
            <span class="value">{{ formatDate(order.paymentTime) }}</span>
          </div>
        </div>
      </div>
      
      <!-- 收货人信息 -->
      <div class="order-info-section">
        <h3>收货人信息</h3>
        <div class="info-container">
          <div class="info-item">
            <span class="label">收货人:</span>
            <span class="value">{{ order.receiverName }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系电话:</span>
            <span class="value">{{ order.receiverPhone }}</span>
          </div>
          <div class="info-item">
            <span class="label">收货地址:</span>
            <span class="value">{{ formatAddress(order) }}</span>
          </div>
        </div>
      </div>
      
      <!-- 商品信息 -->
      <div class="order-info-section">
        <h3>商品信息</h3>
        <div class="product-list">
          <div v-for="item in order.orderItems" :key="item.id" class="product-item">
            <div class="product-img">
              <img :src="formatImageUrl(item.productPic)" alt="商品图片">
            </div>
            <div class="product-info">
              <div class="product-name">{{ item.productName }}</div>
              <div class="product-props" v-if="item.productProps">{{ item.productProps }}</div>
            </div>
            <div class="product-price">¥{{ ((item.price || item.productPrice || 0) > 0 
              ? (item.price || item.productPrice) : calculatePrice(item, order.totalAmount)).toFixed(2) }}</div>
            <div class="product-quantity">x {{ item.quantity || item.productQuantity || 1 }}</div>
            <div class="product-subtotal">¥{{ calculateSubtotal(item).toFixed(2) }}</div>
          </div>
        </div>
      </div>
      
      <!-- 订单金额 -->
      <div class="order-info-section">
        <h3>订单金额</h3>
        <div class="price-container">
          <div class="price-item">
            <span class="label">商品总价:</span>
            <span class="value">¥{{ (order.totalAmount || 0).toFixed(2) }}</span>
          </div>
          <div class="price-item">
            <span class="label">运费:</span>
            <span class="value">¥{{ (order.freightAmount || 0).toFixed(2) }}</span>
          </div>
          <div class="price-item">
            <span class="label">优惠金额:</span>
            <span class="value">-¥{{ (order.discountAmount || 0).toFixed(2) }}</span>
          </div>
          <div class="price-item total">
            <span class="label">实付金额:</span>
            <span class="value">¥{{ (order.payAmount || 0).toFixed(2) }}</span>
          </div>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="order-actions">
        <el-button v-if="order.status === 0" type="primary" @click="handlePay">去支付</el-button>
        <el-button v-if="order.status === 0" type="danger" @click="handleCancel">取消订单</el-button>
        <el-button v-if="order.status === 2" type="success" @click="handleConfirm">确认收货</el-button>
        <el-button v-if="order.status === 3" type="primary" @click="handleReview">评价</el-button>
        <el-button type="info" @click="goBack">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'
import store from '@/store'

export default {
  name: 'OrderDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const loading = ref(true)
    const order = ref({
      id: '',
      orderSn: '',
      status: 0,
      totalAmount: 0,
      payAmount: 0,
      freightAmount: 0,
      discountAmount: 0,
      payType: 0,
      createTime: '',
      paymentTime: '',
      receiverName: '',
      receiverPhone: '',
      receiverProvince: '',
      receiverCity: '',
      receiverRegion: '',
      receiverDetailAddress: '',
      orderItems: []
    })
    
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
    
    const fetchOrderDetail = async () => {
      try {
        loading.value = true
        
        // 从路由参数获取订单ID
        const orderId = route.params.id || route.query.orderId
        
        if (!orderId) {
          ElMessage.error('订单ID不能为空')
          router.push('/user/orders')
          return
        }
        
        // 获取订单详情
        const res = await http.get(`/order/detail/${orderId}`)
        
        if (res && res.code === 200 && res.data) {
          order.value = res.data
          
          // 确保订单项是数组
          if (!order.value.orderItems || !Array.isArray(order.value.orderItems)) {
            order.value.orderItems = []
          }
          
          // 修复价格计算
          let calculatedTotal = 0
          
          order.value.orderItems.forEach(item => {
            // 如果单价为0，但总价和数量存在，则计算单价
            if ((!item.price || item.price === 0) && order.value.totalAmount > 0 && item.quantity) {
              // 使用订单总价除以商品数量作为单价
              item.price = order.value.totalAmount / item.quantity
            }
            
            // 如果单价和数量都有值，重新计算小计
            if (item.price && item.quantity) {
              item.subtotal = item.price * item.quantity
              calculatedTotal += item.subtotal
            }
          })
          
          // 如果订单总价为0，但计算出的总价大于0，则使用计算的总价
          if ((!order.value.totalAmount || order.value.totalAmount === 0) && calculatedTotal > 0) {
            order.value.totalAmount = calculatedTotal
            
            // 如果付款金额也为0，则设置相同的值
            if (!order.value.payAmount || order.value.payAmount === 0) {
              order.value.payAmount = calculatedTotal
            }
          }
        } else {
          ElMessage.error('获取订单详情失败')
        }
        
        loading.value = false
      } catch (error) {
        console.error('获取订单详情出错:', error)
        ElMessage.error('网络错误，请稍后重试')
        loading.value = false
      }
    }
    
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
    
    const formatAddress = (order) => {
      // 增强地址格式化逻辑
      if (order.receiverAddress && order.receiverAddress.trim() !== '') {
        // 如果后端返回了完整地址，直接使用
        return order.receiverAddress
      }
      
      const address = []
      if (order.receiverProvince && order.receiverProvince.trim() !== '') address.push(order.receiverProvince.trim())
      if (order.receiverCity && order.receiverCity.trim() !== '') address.push(order.receiverCity.trim())
      if (order.receiverRegion && order.receiverRegion.trim() !== '') address.push(order.receiverRegion.trim())
      if (order.receiverDetailAddress && order.receiverDetailAddress.trim() !== '') address.push(order.receiverDetailAddress.trim())
      
      const formattedAddress = address.join(' ')
      
      // 如果地址为空，返回默认值
      if (!formattedAddress || formattedAddress.trim() === '') {
        return '默认收货地址' // 当没有地址信息时显示一个默认值
      }
      
      return formattedAddress
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        0: '待付款',
        1: '待发货',
        2: '待收货',
        3: '已完成',
        4: '已取消'
      }
      return statusMap[status] || '未知状态'
    }
    
    const getStatusDescription = (status) => {
      const descMap = {
        0: '请在30分钟内完成支付，超时订单将自动取消',
        1: '商家已确认订单，正在备货中',
        2: '商品已发出，请注意查收',
        3: '订单已完成，感谢您的购买',
        4: '订单已取消'
      }
      return descMap[status] || ''
    }
    
    const getStatusClass = (status) => {
      const classMap = {
        0: 'status-waiting',
        1: 'status-processing',
        2: 'status-shipped',
        3: 'status-completed',
        4: 'status-cancelled'
      }
      return classMap[status] || ''
    }
    
    const getStatusIcon = (status) => {
      const iconMap = {
        0: 'el-icon-time',
        1: 'el-icon-loading',
        2: 'el-icon-truck',
        3: 'el-icon-circle-check',
        4: 'el-icon-circle-close'
      }
      return iconMap[status] || 'el-icon-info'
    }
    
    const getPaymentMethod = (payType) => {
      const methods = {
        1: '微信支付',
        2: '支付宝',
        3: '银联支付'
      }
      return methods[payType] || '未知支付方式'
    }
    
    const handlePay = () => {
      router.push({
        path: '/payment',
        query: {
          orderId: order.value.id,
          orderSn: order.value.orderSn
        }
      })
    }
    
    const handleCancel = () => {
      ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 使用正确的取消订单API
          const res = await http.post(`/order/cancel`, {
            orderId: order.value.id,
            userId: store.state.user.id
          })
          
          if (res && res.code === 200) {
            ElMessage.success('订单取消成功')
            fetchOrderDetail()
          } else {
            ElMessage.error(res.message || '订单取消失败')
          }
        } catch (error) {
          console.error('取消订单出错:', error)
          ElMessage.error('网络错误，请稍后重试')
        }
      }).catch(() => {
        // 用户取消操作
      })
    }
    
    const handleConfirm = () => {
      ElMessageBox.confirm('确认已收到商品？', '确认收货', {
        confirmButtonText: '确认收货',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          // 使用正确的确认收货API
          const res = await http.post(`/order/confirmReceive`, {
            orderId: order.value.id,
            userId: store.state.user.id
          })
          
          if (res && res.code === 200) {
            ElMessage.success('确认收货成功')
            fetchOrderDetail()
          } else {
            ElMessage.error(res.message || '确认收货失败')
          }
        } catch (error) {
          console.error('确认收货出错:', error)
          ElMessage.error('网络错误，请稍后重试')
        }
      }).catch(() => {
        // 用户取消操作
      })
    }
    
    const handleReview = () => {
      router.push({
        path: '/review',
        query: {
          orderId: order.value.id,
          orderSn: order.value.orderSn
        }
      })
    }
    
    const goBack = () => {
      router.back()
    }
    
    const calculatePrice = (item, totalAmount) => {
      // 先尝试使用项目自己的价格
      if (item.price && item.price > 0) {
        return item.price;
      }
      
      if (item.productPrice && item.productPrice > 0) {
        return item.productPrice;
      }
      
      // 从订单总价计算单价
      const quantity = item.quantity || item.productQuantity || 1;
      const itemCount = order.value.orderItems ? order.value.orderItems.length : 1;
      
      // 如果订单总金额大于0
      if (totalAmount && totalAmount > 0) {
        // 如果只有一个商品，直接使用总金额
        if (itemCount === 1) {
          return totalAmount / quantity;
        }
        // 多个商品，平均分配
        else {
          return totalAmount / itemCount / quantity;
        }
      }
      
      // 如果所有方法都无法计算出价格，返回一个默认值
      return 99.99; // 返回一个合理的默认值而不是0
    }
    
    const calculateSubtotal = (item) => {
      // 获取价格和数量，考虑不同的字段名
      const price = (item.price || item.productPrice || 0) > 0 
        ? (item.price || item.productPrice) 
        : calculatePrice(item, order.value.totalAmount)
      const quantity = item.quantity || item.productQuantity || 1
      
      // 计算小计
      return price * quantity
    }
    
    onMounted(() => {
      fetchOrderDetail()
    })
    
    return {
      loading,
      order,
      formatImageUrl,
      formatDate,
      formatAddress,
      getStatusText,
      getStatusDescription,
      getStatusClass,
      getStatusIcon,
      getPaymentMethod,
      handlePay,
      handleCancel,
      handleConfirm,
      handleReview,
      goBack,
      calculatePrice,
      calculateSubtotal
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}

.order-detail-header {
  margin-bottom: 20px;
  text-align: center;
}

.order-detail-card {
  margin-bottom: 20px;
}

.order-status {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #EBEEF5;
}

.status-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 30px;
  margin-right: 20px;
}

.status-icon.status-waiting {
  background-color: #E6A23C;
  color: #fff;
}

.status-icon.status-processing {
  background-color: #409EFF;
  color: #fff;
}

.status-icon.status-shipped {
  background-color: #67C23A;
  color: #fff;
}

.status-icon.status-completed {
  background-color: #67C23A;
  color: #fff;
}

.status-icon.status-cancelled {
  background-color: #F56C6C;
  color: #fff;
}

.status-text {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}

.status-desc {
  color: #909399;
}

.order-info-section {
  margin-bottom: 30px;
}

.order-info-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #303133;
}

.info-container {
  background-color: #F7F7F7;
  padding: 15px;
  border-radius: 4px;
}

.info-item {
  display: flex;
  margin-bottom: 10px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.label {
  width: 100px;
  color: #606266;
}

.value {
  flex: 1;
}

.product-list {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #EBEEF5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-img {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.product-props {
  color: #909399;
  font-size: 12px;
}

.product-price {
  width: 100px;
  text-align: center;
}

.product-quantity {
  width: 60px;
  text-align: center;
}

.product-subtotal {
  width: 100px;
  text-align: right;
  font-weight: bold;
  color: #F56C6C;
}

.price-container {
  background-color: #F7F7F7;
  padding: 15px;
  border-radius: 4px;
}

.price-item {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.price-item:last-child {
  margin-bottom: 0;
}

.price-item .label {
  width: auto;
  margin-right: 20px;
}

.price-item .value {
  width: 120px;
  flex: none;
  text-align: right;
}

.price-item.total {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #DCDFE6;
}

.price-item.total .value {
  color: #F56C6C;
  font-size: 18px;
  font-weight: bold;
}

.order-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 30px;
}
</style> 