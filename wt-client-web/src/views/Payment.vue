/* eslint-disable */
<template>
  <div class="payment-container">
    <div class="payment-header">
      <h1>订单支付</h1>
    </div>
    
    <el-card class="payment-card" v-if="!loading">
      <div class="order-info">
        <div class="info-item">
          <span class="label">订单编号:</span>
          <span class="value">{{ orderSn }}</span>
        </div>
        <div class="info-item">
          <span class="label">支付金额:</span>
          <span class="value price">¥{{ orderAmount.toFixed(2) }}</span>
        </div>
      </div>
      
      <div class="payment-methods">
        <h3>选择支付方式</h3>
        <div class="methods-container">
          <div 
            v-for="method in paymentMethods" 
            :key="method.value"
            class="method-item"
            :class="{ active: selectedMethod === method.value }"
            @click="selectedMethod = method.value"
          >
            <div class="method-icon">
              <i :class="method.icon"></i>
            </div>
            <div class="method-name">{{ method.label }}</div>
          </div>
        </div>
      </div>
      
      <div class="payment-action">
        <el-button type="primary" size="large" @click="handlePay" :loading="payLoading">
          立即支付
        </el-button>
      </div>
      
      <div class="payment-tips">
        <p>温馨提示:</p>
        <ul>
          <li>请在30分钟内完成支付，超时订单将自动取消</li>
          <li>支付成功后，无法修改支付方式</li>
          <li>如遇支付问题，请联系客服: 400-123-4567</li>
        </ul>
      </div>
    </el-card>
    
    <el-card class="payment-card" v-else>
      <div class="loading-content">
        <el-skeleton :rows="6" animated />
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
  name: 'Payment',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const loading = ref(true)
    const payLoading = ref(false)
    const orderId = ref('')
    const orderSn = ref('')
    const orderAmount = ref(0)
    const selectedMethod = ref(1) // 默认微信支付
    
    const paymentMethods = [
      {
        label: '微信支付',
        value: 1,
        icon: 'el-icon-message'
      },
      {
        label: '支付宝',
        value: 2,
        icon: 'el-icon-money'
      },
      {
        label: '银联支付',
        value: 3,
        icon: 'el-icon-bank-card'
      }
    ]
    
    const fetchOrderInfo = async () => {
      try {
        loading.value = true
        
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
          // 检查订单状态，如果已支付则重定向到支付成功页面
          if (res.data.status > 0) {
            ElMessage.info('订单已支付，正在跳转...')
            router.replace({
              path: '/payment/success',
              query: {
                orderId: id,
                orderSn: sn,
                userId
              }
            })
            return
          }
          
          orderAmount.value = res.data.payAmount || res.data.totalAmount || 0
        } else {
          ElMessage.error('获取订单信息失败')
        }
        
        loading.value = false
      } catch (error) {
        console.error('获取订单信息出错:', error)
        ElMessage.error('网络错误，请稍后重试')
        loading.value = false
      }
    }
    
    const handlePay = async () => {
      try {
        payLoading.value = true
        
        // 提交支付请求
        const res = await http.post('/order/paySuccess', {
          orderId: orderId.value,
          payType: selectedMethod.value,
          // 确保订单状态更新为"待发货"状态
          status: 1,
          userId: store.state.user.id
        })
        
        if (res && res.code === 200) {
          ElMessage.success('支付成功')
          
          // 跳转到支付成功页面
          setTimeout(() => {
            router.push({
              path: '/payment/success',
              query: {
                orderId: orderId.value,
                orderSn: orderSn.value
              }
            })
          }, 1000)
        } else {
          ElMessage.error(res.message || '支付失败')
        }
        
        payLoading.value = false
      } catch (error) {
        console.error('支付出错:', error)
        ElMessage.error('网络错误，请稍后重试')
        payLoading.value = false
      }
    }
    
    onMounted(() => {
      fetchOrderInfo()
    })
    
    return {
      loading,
      payLoading,
      orderId,
      orderSn,
      orderAmount,
      selectedMethod,
      paymentMethods,
      handlePay
    }
  }
}
</script>

<style scoped>
.payment-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.payment-header {
  margin-bottom: 20px;
  text-align: center;
}

.payment-card {
  margin-bottom: 20px;
}

.order-info {
  padding: 20px 0;
  border-bottom: 1px solid #EBEEF5;
}

.info-item {
  display: flex;
  margin-bottom: 10px;
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
  font-size: 1.2em;
}

.payment-methods {
  padding: 20px 0;
  border-bottom: 1px solid #EBEEF5;
}

.methods-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: 15px;
}

.method-item {
  width: 120px;
  height: 80px;
  margin-right: 15px;
  margin-bottom: 15px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.method-item:hover {
  border-color: #409EFF;
}

.method-item.active {
  border-color: #409EFF;
  background-color: #ECF5FF;
}

.method-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.payment-action {
  padding: 20px 0;
  text-align: center;
}

.payment-tips {
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
}

.payment-tips p {
  font-weight: bold;
  margin-bottom: 5px;
}

.payment-tips ul {
  padding-left: 20px;
}

.loading-content {
  padding: 20px 0;
}
</style> 