/* eslint-disable */
<template>
  <div class="payment-success">
    <el-card class="success-card">
      <div class="success-icon">
        <i class="el-icon-check"></i>
      </div>
      <h1>支付成功</h1>
      <div class="order-info">
        <div class="info-item">
          <span class="label">订单编号:</span>
          <span class="value">{{ orderSn }}</span>
        </div>
        <div class="info-item">
          <span class="label">支付金额:</span>
          <span class="value price">¥{{ orderAmount.toFixed(2) }}</span>
        </div>
        <div class="info-item">
          <span class="label">支付方式:</span>
          <span class="value">{{ getPaymentMethod() }}</span>
        </div>
        <div class="info-item">
          <span class="label">支付时间:</span>
          <span class="value">{{ formatDate(paymentTime) }}</span>
        </div>
      </div>
      <div class="action-buttons">
        <el-button type="primary" @click="goToOrderDetail">查看订单详情</el-button>
        <el-button @click="goToHome">继续购物</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/request'
import store from '@/store'

export default {
  name: 'PaymentSuccess',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const orderSn = ref('')
    const orderId = ref('')
    const orderAmount = ref(0)
    const paymentMethod = ref(0)
    const paymentTime = ref(new Date())
    
    const fetchOrderInfo = async () => {
      try {
        // 从路由参数获取订单ID和订单编号
        const id = route.query.orderId
        const sn = route.query.orderSn
        const userId = route.query.userId || (store.state.user ? store.state.user.id : null)
        
        if (!id) {
          ElMessage.error('订单ID不能为空')
          router.push('/user/orders')
          return
        }
        
        orderId.value = id
        orderSn.value = sn || '订单编号获取失败'
        
        // 获取订单详情
        const res = await http.get(`/order/detail/${id}`, { 
          params: { userId }
        })
        
        if (res && res.code === 200 && res.data) {
          orderAmount.value = res.data.payAmount || res.data.totalAmount || 0
          paymentMethod.value = res.data.payType || 0
          paymentTime.value = res.data.paymentTime ? new Date(res.data.paymentTime) : new Date()
        } else {
          ElMessage.error('获取订单信息失败')
        }
      } catch (error) {
        console.error('获取订单信息出错:', error)
        ElMessage.error('网络错误，请稍后重试')
      }
    }
    
    const getPaymentMethod = () => {
      const methods = {
        1: '微信支付',
        2: '支付宝',
        3: '银联支付'
      }
      return methods[paymentMethod.value] || '未知支付方式'
    }
    
    const formatDate = (date) => {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      const hour = d.getHours().toString().padStart(2, '0')
      const minute = d.getMinutes().toString().padStart(2, '0')
      const second = d.getSeconds().toString().padStart(2, '0')
      
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
    
    const goToOrderDetail = () => {
      router.push({
        path: '/order/detail',
        query: {
          orderId: orderId.value,
          orderSn: orderSn.value
        }
      })
    }
    
    const goToHome = () => {
      router.push('/')
    }
    
    onMounted(() => {
      fetchOrderInfo()
    })
    
    return {
      orderSn,
      orderAmount,
      paymentMethod,
      paymentTime,
      getPaymentMethod,
      formatDate,
      goToOrderDetail,
      goToHome
    }
  }
}
</script>

<style scoped>
.payment-success {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 20px;
}

.success-card {
  text-align: center;
}

.success-icon {
  font-size: 60px;
  color: #67C23A;
  margin: 20px 0;
}

h1 {
  color: #67C23A;
  margin-bottom: 30px;
}

.order-info {
  width: 80%;
  margin: 20px auto;
  text-align: left;
}

.info-item {
  display: flex;
  margin-bottom: 15px;
}

.label {
  width: 100px;
  color: #606266;
}

.value {
  flex: 1;
  font-weight: bold;
}

.price {
  color: #F56C6C;
}

.action-buttons {
  margin: 30px 0;
}

.action-buttons .el-button {
  margin: 0 10px;
}
</style> 