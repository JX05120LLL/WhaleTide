<template>
  <div class="order-list-page">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">我的订单</h1>
      </div>
      
      <!-- 订单筛选区域 -->
      <div class="filter-section">
        <el-tabs v-model="activeTab" @tab-click="handleTabChange">
          <el-tab-pane label="全部订单" name="all"></el-tab-pane>
          <el-tab-pane label="待付款" name="pending"></el-tab-pane>
          <el-tab-pane label="待发货" name="paid"></el-tab-pane>
          <el-tab-pane label="待收货" name="shipped"></el-tab-pane>
          <el-tab-pane label="已完成" name="completed"></el-tab-pane>
          <el-tab-pane label="已取消" name="cancelled"></el-tab-pane>
        </el-tabs>
        
        <div class="search-filter">
          <el-input
            v-model="searchKeyword"
            placeholder="订单号/商品名称"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
          
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          ></el-date-picker>
        </div>
      </div>
      
      <div v-if="!loading">
        <!-- 订单列表 -->
        <div v-if="orders.length" class="order-list">
          <div v-for="order in orders" :key="order.id" class="order-card">
            <!-- 订单头部信息 -->
            <div class="order-header">
              <div class="order-time">下单时间: {{ formatDate(order.createTime) }}</div>
              <div class="order-number">订单号: {{ order.orderSn }}</div>
              <div class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</div>
            </div>
            
            <!-- 订单商品项 -->
            <div class="order-products">
              <div v-for="item in order.orderItems" :key="item.id" class="product-item" @click="viewOrderDetail(order.id)">
                <div class="product-img">
                  <img :src="formatImageUrl(item.productPic)" alt="商品图片">
                </div>
                <div class="product-info">
                  <div class="product-name">{{ item.productName }}</div>
                </div>
                <div class="product-price">
                  ¥{{ ((item.price || item.productPrice || 0) > 0 
                    ? (item.price || item.productPrice) 
                    : calculatePrice(item, order.totalAmount)).toFixed(2) }}
                </div>
                <div class="product-quantity">x {{ item.quantity || item.productQuantity || 1 }}</div>
                <div class="product-subtotal">
                  ¥{{ calculateSubtotal(item, order.totalAmount).toFixed(2) }}
                </div>
              </div>
            </div>
            
            <div class="order-footer">
              <div class="order-amount">
                共 {{ getTotalQuantity(order.orderItems) }} 件商品，
                总计: ¥<span class="price">{{ (order.totalAmount || calculateOrderTotal(order.orderItems)).toFixed(2) }}</span>
                (含运费: ¥{{ (order.freightAmount || 0).toFixed(2) }})
              </div>
              
              <!-- 订单操作按钮 -->
              <div class="order-actions">
                <el-button v-if="order.status === 0" type="primary" size="small" @click="handlePay(order.id)">去支付</el-button>
                <el-button v-if="order.status === 0" size="small" @click="handleCancel(order.id)">取消订单</el-button>
                <el-button v-if="order.status === 2" type="primary" size="small" @click="handleConfirmReceive(order.id)">确认收货</el-button>
                <el-button size="small" @click="viewOrderDetail(order.id)">查看详情</el-button>
              </div>
            </div>
          </div>
          
          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
            ></el-pagination>
          </div>
        </div>
        
        <!-- 无订单数据 -->
        <div v-else class="no-data">
          <el-empty description="暂无订单数据">
            <template #image>
              <el-icon :size="100"><Tickets /></el-icon>
            </template>
            <el-button type="primary" @click="$router.push('/')">去购物</el-button>
          </el-empty>
        </div>
      </div>
      
      <!-- 加载状态 -->
      <div v-else class="loading">
        <el-skeleton :rows="3" animated />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Tickets } from '@element-plus/icons-vue';
import api from '../api';

export default {
  name: 'OrderList',
  components: {
    Tickets
  },
  setup() {
    const router = useRouter();
    const orders = ref([]);
    const loading = ref(false);
    const total = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const activeTab = ref('all');
    const searchKeyword = ref('');
    const dateRange = ref(null);
    
    // 获取订单列表
    const fetchOrders = async () => {
      try {
        loading.value = true;
        
        const params = {
          pageNum: currentPage.value,
          pageSize: pageSize.value,
          status: activeTab.value === 'all' ? null : parseInt(activeTab.value)
        };
        
        const res = await api.order.getOrderList(params);
        
        if (res && res.code === 200 && res.data) {
          orders.value = res.data.list || [];
          total.value = res.data.total || 0;
          
          // 处理每个订单的价格问题
          orders.value.forEach(order => {
            if (!order.orderItems || !Array.isArray(order.orderItems)) {
              order.orderItems = [];
              return;
            }
            
            // 订单金额修正
            let calculatedTotal = 0;
            
            order.orderItems.forEach(item => {
              // 如果单价为0，但总价和数量存在，则计算单价
              if ((!item.price && !item.productPrice) || 
                  ((item.price === 0 || item.productPrice === 0) && 
                   order.totalAmount > 0 && 
                   (item.quantity || item.productQuantity))) {
                
                const quantity = item.quantity || item.productQuantity || 1;
                const price = order.totalAmount / quantity;
                
                // 更新价格，优先使用productPrice字段
                if ('productPrice' in item) {
                  item.productPrice = price;
                } else {
                  item.price = price;
                }
                
                console.log(`为商品${item.productName}计算价格: ${price}`);
              }
              
              // 计算小计并累加总额
              const subtotal = calculateSubtotal(item, order.totalAmount);
              item.subtotal = subtotal;
              calculatedTotal += subtotal;
            });
            
            // 如果订单总额为0，但计算出的总额大于0，则更新订单总额
            if ((!order.totalAmount || order.totalAmount === 0) && calculatedTotal > 0) {
              order.totalAmount = calculatedTotal;
              console.log(`更新订单${order.id}总额为: ${calculatedTotal}`);
            }
          });
        } else {
          orders.value = [];
          total.value = 0;
          ElMessage.error(res?.message || '获取订单列表失败');
        }
        
        loading.value = false;
      } catch (error) {
        console.error('获取订单列表出错:', error);
        ElMessage.error('网络错误，请稍后重试');
        loading.value = false;
      }
    };
    
    // 处理标签页切换
    const handleTabChange = () => {
      currentPage.value = 1;
      fetchOrders();
    };
    
    // 处理搜索
    const handleSearch = () => {
      currentPage.value = 1;
      fetchOrders();
    };
    
    // 处理日期范围变化
    const handleDateChange = () => {
      currentPage.value = 1;
      fetchOrders();
    };
    
    // 处理分页
    const handlePageChange = (page) => {
      currentPage.value = page;
      fetchOrders();
    };
    
    // 获取状态文字
    const getStatusText = (status) => {
      const statusMap = {
        pending: '待付款',
        paid: '待发货',
        shipped: '待收货',
        completed: '已完成',
        cancelled: '已取消'
      };
      return statusMap[status] || status;
    };
    
    // 立即付款
    const handlePay = (orderId) => {
      router.push(`/pay?orderId=${orderId}`);
    };
    
    // 取消订单
    const handleCancel = (orderId) => {
      ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await api.order.cancelOrder(orderId, '用户取消');
          ElMessage.success('订单已取消');
          fetchOrders();
        } catch (error) {
          ElMessage.error('取消订单失败');
          console.error(error);
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 确认收货
    const handleConfirmReceive = (orderId) => {
      ElMessageBox.confirm('确认商品已收到？', '确认收货', {
        confirmButtonText: '确认收货',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          await api.order.confirmReceived(orderId);
          ElMessage.success('已确认收货');
          fetchOrders();
        } catch (error) {
          ElMessage.error('操作失败');
          console.error(error);
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 查看订单详情
    const viewOrderDetail = (orderId) => {
      router.push(`/order/detail/${orderId}`);
    };
    
    // 格式化日期
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    };
    
    // 格式化价格
    const formatPrice = (price) => {
      return price.toFixed(2);
    };
    
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
    
    // 根据订单总额和商品数量计算单价
    const calculatePrice = (item, totalAmount) => {
      // 如果单价为0，但总价和数量存在，则计算单价
      if (totalAmount && totalAmount > 0 && (item.quantity || item.productQuantity)) {
        // 使用订单总价除以商品数量作为单价
        return totalAmount / (item.quantity || item.productQuantity || 1);
      }
      return 0;
    };
    
    // 计算商品小计金额
    const calculateSubtotal = (item, totalAmount) => {
      // 获取价格和数量，考虑不同的字段名
      const price = (item.price || item.productPrice || 0) > 0 
        ? (item.price || item.productPrice) 
        : calculatePrice(item, totalAmount);
      const quantity = item.quantity || item.productQuantity || 1;
      
      // 计算小计
      return price * quantity;
    };
    
    // 计算订单总数量
    const getTotalQuantity = (items) => {
      if (!items || !Array.isArray(items)) return 0;
      return items.reduce((total, item) => {
        return total + (item.quantity || item.productQuantity || 1);
      }, 0);
    };
    
    // 计算订单总金额
    const calculateOrderTotal = (items) => {
      if (!items || !Array.isArray(items)) return 0;
      return items.reduce((total, item) => {
        const subtotal = calculateSubtotal(item);
        return total + subtotal;
      }, 0);
    };
    
    onMounted(() => {
      fetchOrders();
    });
    
    return {
      orders,
      loading,
      total,
      currentPage,
      pageSize,
      activeTab,
      searchKeyword,
      dateRange,
      formatDate,
      formatPrice,
      getStatusText,
      handleTabChange,
      handleSearch,
      handleDateChange,
      handlePageChange,
      handlePay,
      handleCancel,
      handleConfirmReceive,
      viewOrderDetail,
      formatImageUrl,
      calculatePrice,
      calculateSubtotal,
      getTotalQuantity,
      calculateOrderTotal
    };
  }
};
</script>

<style scoped>
.order-list-page {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 500;
  color: #333;
}

.filter-section {
  background-color: white;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
}

.search-filter {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.search-filter .el-input {
  width: 300px;
}

.order-list {
  margin-bottom: 40px;
}

.order-card {
  background-color: white;
  border-radius: 4px;
  margin-bottom: 20px;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
}

.order-time {
  color: #666;
}

.order-number {
  font-weight: 500;
}

.order-status {
  font-weight: 500;
}

.status-pending {
  color: #e6a23c;
}

.status-paid {
  color: #409eff;
}

.status-shipped {
  color: #67c23a;
}

.status-completed {
  color: #67c23a;
}

.status-cancelled {
  color: #909399;
}

.order-products {
  padding: 0 20px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.product-item:last-child {
  border-bottom: none;
}

.product-img {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-right: 15px;
}

.product-img img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price {
  width: 120px;
  text-align: center;
}

.product-quantity {
  width: 80px;
  text-align: center;
  color: #666;
}

.product-subtotal {
  width: 120px;
  text-align: right;
  color: #ff6700;
  font-weight: 500;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #fafafa;
  border-top: 1px solid #f0f0f0;
}

.order-amount {
  font-size: 14px;
}

.price {
  font-size: 18px;
  color: #ff6700;
  font-weight: 500;
}

.order-actions {
  display: flex;
  gap: 15px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.no-data {
  background-color: white;
  border-radius: 4px;
  padding: 60px 0;
}

.loading {
  background-color: white;
  border-radius: 4px;
  padding: 20px;
}
</style> 