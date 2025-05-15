/* eslint-disable */
<template>
  <div class="user-orders">
    <el-card class="orders-card">
      <template #header>
        <div class="card-header">
          <h2>我的订单</h2>
          <el-select v-model="orderStatus" placeholder="订单状态" @change="handleStatusChange">
            <el-option label="全部订单" value=""></el-option>
            <el-option label="待付款" value="0"></el-option>
            <el-option label="待发货" value="1"></el-option>
            <el-option label="待收货" value="2"></el-option>
            <el-option label="已完成" value="3"></el-option>
            <el-option label="已取消" value="4"></el-option>
          </el-select>
        </div>
      </template>
      
      <!-- 订单列表 -->
      <div v-if="orders.length > 0">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <div class="order-info">
              <span class="order-id">订单号: {{ order.orderNo || order.orderSn }}</span>
              <span class="order-date">下单时间: {{ formatDate(order.createTime) }}</span>
            </div>
            <div class="order-status">
              <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
            </div>
          </div>
          
          <el-divider></el-divider>
          
          <div class="order-products">
            <div v-for="item in order.items || order.orderItems" :key="item.id" class="product-item">
              <div class="product-img">
                <img :src="formatImageUrl(item.productImg || item.productPic)" alt="商品图片">
              </div>
              <div class="product-info">
                <div class="product-name">{{ item.productName }}</div>
                <div class="product-props">{{ item.productProps }}</div>
              </div>
              <div class="product-price">¥{{ calculatePrice(item, order).toFixed(2) }}</div>
              <div class="product-quantity">x {{ item.quantity || item.productQuantity }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-total">
              共 {{ order.totalNum || (order.items ? order.items.length : (order.orderItems ? order.orderItems.length : 0)) }} 件商品，总计: <span class="price">¥{{ (order.totalAmount !== undefined ? order.totalAmount : 0).toFixed(2) }}</span>（含运费: ¥{{ (order.freightAmount !== undefined ? order.freightAmount : 0).toFixed(2) }}）
            </div>
            <div class="order-actions">
              <el-button v-if="order.status === 0" type="primary" size="small" @click="handlePay(order)">去支付</el-button>
              <el-button v-if="order.status === 0" type="danger" size="small" @click="handleCancel(order)">取消订单</el-button>
              <el-button v-if="order.status === 2" type="success" size="small" @click="handleConfirm(order)">确认收货</el-button>
              <el-button v-if="order.status === 3" type="primary" size="small" @click="handleReview(order)">评价</el-button>
              <el-button type="info" size="small" @click="viewOrderDetail(order)">查看详情</el-button>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty v-else description="暂无订单" :image-size="200"></el-empty>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/request'
import { staticBaseURL } from '@/utils/request'
import router from '@/router'
import store from '@/store'

export default {
  name: 'UserOrders',
  setup() {
    const orders = ref([])
    const orderStatus = ref('')
    const currentPage = ref(1)
    const pageSize = ref(5)
    const total = ref(0)
    
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
    
    // 计算商品价格
    const calculatePrice = (item, order) => {
      // 如果商品价格存在且不为0，直接使用商品价格
      if (item.productPrice && item.productPrice > 0) {
        return item.productPrice;
      }
      
      // 从订单总额计算商品价格
      const totalAmount = order.totalAmount || 0;
      const itemsCount = (order.items?.length || order.orderItems?.length || 1);
      
      // 如果总金额大于0，平均分配到每个商品
      if (totalAmount > 0 && itemsCount > 0) {
        return totalAmount / itemsCount;
      }
      
      // 默认返回一个合理的价格，避免显示为0
      return order.totalAmount || 99.99;
    };
    
    const fetchOrders = async () => {
      try {
        // 构建直接参数对象，不要嵌套
        const params = {
          pageNum: currentPage.value,
          pageSize: pageSize.value
        }
        
        if (orderStatus.value) {
          params.status = orderStatus.value
        }
        
        console.log('发送订单请求参数:', params);
        
        // 发送请求
        const response = await http.get('/order/list', { params })
        
        console.log('订单列表响应:', response);
        
        if (response && response.code === 200) {
          console.log('订单数据:', response.data);
          
          // 确保有list属性
          if (response.data && response.data.list) {
            // 处理订单数据，确保格式一致
            orders.value = response.data.list.map(order => {
              return {
                ...order,
                // 确保有订单号
                orderNo: order.orderNo || order.orderSn,
                // 确保有订单项
                items: order.items || order.orderItems || [],
                // 确保有总数量
                totalNum: order.totalNum || (order.items ? order.items.length : (order.orderItems ? order.orderItems.length : 0)),
                // 确保有总金额
                totalAmount: order.totalAmount !== undefined ? order.totalAmount : 0,
                // 确保有运费
                freightAmount: order.freightAmount !== undefined ? order.freightAmount : 0
              }
            });
            total.value = response.data.total || 0;
          } else {
            orders.value = [];
            total.value = 0;
            console.error('订单数据格式不正确:', response.data);
          }
        } else {
          orders.value = [];
          total.value = 0;
          ElMessage.error(response.message || '获取订单列表失败');
        }
      } catch (error) {
        console.error('获取订单列表出错:', error);
        ElMessage.error('网络错误，请稍后重试');
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
      
      return `${year}-${month}-${day} ${hour}:${minute}`
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
    
    const getStatusType = (status) => {
      const typeMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        3: 'info',
        4: 'danger'
      }
      return typeMap[status] || 'info'
    }
    
    const handleStatusChange = () => {
      currentPage.value = 1
      fetchOrders()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      fetchOrders()
    }
    
    const handlePay = (order) => {
      // 跳转到支付页面
      ElMessage.info('即将跳转到支付页面');
      
      // 获取当前用户ID用于传递
      const userInfo = store.getters.currentUser;
      
      router.push({
        path: '/payment',
        query: {
          orderId: order.id,
          orderSn: order.orderNo || order.orderSn,
          userId: userInfo.id // 添加userId参数
        }
      });
    }
    
    const handleCancel = (order) => {
      ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 从store获取当前用户ID
          const userInfo = store.getters.currentUser;
          
          // 使用正确的取消订单API，并传递userId
          const response = await http.post(`/order/cancel`, {
            orderId: order.id,
            userId: userInfo.id // 添加userId参数
          })
          
          if (response.code === 200) {
            ElMessage.success('订单取消成功')
            fetchOrders()
          } else {
            ElMessage.error(response.message || '订单取消失败')
          }
        } catch (error) {
          console.error('取消订单出错:', error)
          ElMessage.error('网络错误，请稍后重试')
        }
      }).catch(() => {
        // 用户取消操作
      })
    }
    
    const handleConfirm = (order) => {
      ElMessageBox.confirm('确认已收到商品？', '确认收货', {
        confirmButtonText: '确认收货',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          // 从store获取当前用户ID
          const userInfo = store.getters.currentUser;
          
          // 使用正确的确认收货API，并传递userId
          const response = await http.post(`/order/confirmReceive`, {
            orderId: order.id,
            userId: userInfo.id // 添加userId参数
          })
          
          if (response.code === 200) {
            ElMessage.success('确认收货成功')
            fetchOrders()
          } else {
            ElMessage.error(response.message || '确认收货失败')
          }
        } catch (error) {
          console.error('确认收货出错:', error)
          ElMessage.error('网络错误，请稍后重试')
        }
      }).catch(() => {
        // 用户取消操作
      })
    }
    
    const handleReview = (order) => {
      // 跳转到评价页面
      ElMessage.info('即将跳转到评价页面');
      router.push({
        path: '/review',
        query: {
          orderId: order.id,
          orderSn: order.orderNo || order.orderSn
        }
      });
    }
    
    const viewOrderDetail = (order) => {
      // 跳转到订单详情页
      ElMessage.info('即将跳转到订单详情页面');
      router.push({
        path: '/order/detail',
        query: {
          orderId: order.id,
          orderSn: order.orderNo || order.orderSn
        }
      });
    }
    
    onMounted(() => {
      fetchOrders()
    })
    
    return {
      orders,
      orderStatus,
      currentPage,
      pageSize,
      total,
      formatDate,
      getStatusText,
      getStatusType,
      handleStatusChange,
      handleCurrentChange,
      handlePay,
      handleCancel,
      handleConfirm,
      handleReview,
      viewOrderDetail,
      formatImageUrl,
      calculatePrice
    }
  }
}
</script>

<style scoped>
.user-orders {
  padding: 20px;
}

.orders-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-item {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.order-id {
  font-weight: bold;
  margin-right: 20px;
}

.order-date {
  color: #909399;
}

.order-products {
  margin-bottom: 15px;
}

.product-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px 0;
}

.product-img {
  width: 80px;
  height: 80px;
  margin-right: 15px;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
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
  font-weight: bold;
  color: #F56C6C;
  margin: 0 20px;
}

.product-quantity {
  width: 60px;
  text-align: center;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
}

.order-total {
  font-size: 14px;
}

.price {
  font-size: 18px;
  color: #F56C6C;
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style> 