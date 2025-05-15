<template>
  <div class="app-container" v-loading="loading">
    <div class="detail-header">
      <el-page-header @back="goBack" content="订单详情" />
      <div class="status-container">
        <el-tag :type="getOrderStatusType(orderData.status)">
          {{ getOrderStatusName(orderData.status) }}
        </el-tag>
      </div>
    </div>
    
    <div v-if="!loading" class="order-container">
      <!-- 基本信息卡片 -->
      <el-card class="box-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <div class="card-content">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">订单编号:</span>
                <span class="value">{{ orderData.orderNo || '暂无' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">下单时间:</span>
                <span class="value">{{ orderData.createTime || '暂无' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">支付方式:</span>
                <span class="value">{{ getPaymentMethodName(orderData.paymentMethod) }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">支付时间:</span>
                <span class="value">{{ orderData.payTime || '暂无' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">订单金额:</span>
                <span class="value price">￥{{ orderData.totalAmount || '0.00' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">运费:</span>
                <span class="value">￥{{ orderData.shippingFee || '0.00' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 收货信息卡片 -->
      <el-card class="box-card">
        <div slot="header" class="card-header">
          <span>收货信息</span>
        </div>
        <div class="card-content" v-if="orderData.addressInfo">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">收货人:</span>
                <span class="value">{{ orderData.addressInfo.receiver || '暂无' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="info-item">
                <span class="label">联系电话:</span>
                <span class="value">{{ orderData.addressInfo.phone || '暂无' }}</span>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24">
              <div class="info-item">
                <span class="label">收货地址:</span>
                <span class="value">
                  {{ formatAddress(orderData.addressInfo) }}
                </span>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="card-content" v-else>
          <el-empty description="暂无收货信息"></el-empty>
        </div>
      </el-card>

      <!-- 商品信息卡片 -->
      <el-card class="box-card">
        <div slot="header" class="card-header">
          <span>商品信息</span>
        </div>
        <div class="card-content">
          <el-table
            :data="orderData.orderItemVoList || []"
            border
            style="width: 100%"
          >
            <el-table-column
              label="商品图片"
              width="100"
              align="center"
            >
              <template slot-scope="scope">
                <el-image
                  style="width: 60px; height: 60px"
                  :src="scope.row.productImage"
                  fit="cover"
                  :preview-src-list="[scope.row.productImage]"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </template>
            </el-table-column>
            <el-table-column
              label="商品名称"
              prop="productName"
              min-width="200"
              show-overflow-tooltip
            />
            <el-table-column
              label="单价"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                ￥{{ scope.row.price || '0.00' }}
              </template>
            </el-table-column>
            <el-table-column
              label="数量"
              prop="quantity"
              width="100"
              align="center"
            />
            <el-table-column
              label="小计"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                ￥{{ calculateItemTotal(scope.row) }}
              </template>
            </el-table-column>
          </el-table>

          <div class="order-summary">
            <div class="summary-item">
              <span>商品总价:</span>
              <span>￥{{ orderData.totalPrice || '0.00' }}</span>
            </div>
            <div class="summary-item">
              <span>运费:</span>
              <span>￥{{ orderData.shippingFee || '0.00' }}</span>
            </div>
            <div class="summary-item total">
              <span>实付金额:</span>
              <span class="price">￥{{ orderData.paymentAmount || '0.00' }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 操作按钮区域 -->
      <div class="action-area">
        <el-button v-if="orderData.status === 1" type="success" @click="handleShip">订单发货</el-button>
        <el-button v-if="orderData.status === 2" type="warning" @click="handleComplete">确认收货</el-button>
        <el-button type="info" @click="goBack">返回</el-button>
      </div>
    </div>

    <!-- 订单为空时显示 -->
    <el-empty v-if="!loading && !orderData.id" description="未找到订单信息"></el-empty>

    <!-- 发货对话框 -->
    <el-dialog title="订单发货" :visible.sync="shipDialogVisible" width="500px">
      <el-form
        ref="shipForm"
        :model="shipForm"
        :rules="shipRules"
        label-width="100px"
      >
        <el-form-item label="订单号">
          <span>{{ orderData.orderNo }}</span>
        </el-form-item>
        <el-form-item label="物流公司" prop="shippingCompany">
          <el-select v-model="shipForm.shippingCompany" placeholder="请选择物流公司" style="width: 100%">
            <el-option label="顺丰速运" value="SF" />
            <el-option label="中通快递" value="ZTO" />
            <el-option label="圆通速递" value="YTO" />
            <el-option label="韵达快递" value="YD" />
            <el-option label="申通快递" value="STO" />
            <el-option label="京东物流" value="JD" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="物流单号" prop="trackingNumber">
          <el-input v-model="shipForm.trackingNumber" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="shipForm.remark" type="textarea" :rows="3" placeholder="可选备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitShipForm" :loading="shipSubmitting">确认发货</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchOrder, shipOrder, completeOrder } from '@/api/order'

export default {
  name: 'OrderDetail',
  data() {
    return {
      loading: true,
      orderId: null,
      orderData: {},
      // 发货对话框
      shipDialogVisible: false,
      shipSubmitting: false,
      shipForm: {
        orderId: '',
        shippingCompany: '',
        trackingNumber: '',
        remark: ''
      },
      shipRules: {
        shippingCompany: [
          { required: true, message: '请选择物流公司', trigger: 'change' }
        ],
        trackingNumber: [
          { required: true, message: '请输入物流单号', trigger: 'blur' },
          { min: 5, max: 30, message: '长度在 5 到 30 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.orderId = this.$route.params.id
    this.fetchOrderDetail()
  },
  methods: {
    // 获取订单详情
    fetchOrderDetail() {
      this.loading = true
      fetchOrder(this.orderId).then(response => {
        console.log('订单详情响应:', response)
        if (response.code === 200 && response.data) {
          this.orderData = response.data
        } else {
          this.$message.error('获取订单详情失败: ' + (response.message || '未知错误'))
          this.orderData = {}
        }
      }).catch(error => {
        console.error('获取订单详情错误:', error)
        this.$message.error('获取订单详情失败: ' + (error.message || '未知错误'))
        this.orderData = {}
      }).finally(() => {
        this.loading = false
      })
    },
    // 返回上一页
    goBack() {
      this.$router.push('/order/list')
    },
    // 处理发货
    handleShip() {
      this.shipForm = {
        orderId: this.orderData.id,
        shippingCompany: '',
        trackingNumber: '',
        remark: ''
      }
      this.shipDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.shipForm) {
          this.$refs.shipForm.clearValidate()
        }
      })
    },
    // 提交发货表单
    submitShipForm() {
      this.$refs.shipForm.validate((valid) => {
        if (valid) {
          this.shipSubmitting = true
          shipOrder(this.orderData.id, this.shipForm).then(response => {
            if (response.code === 200) {
              this.shipDialogVisible = false
              this.$notify({
                title: '成功',
                message: '订单发货成功',
                type: 'success',
                duration: 2000
              })
              this.fetchOrderDetail() // 刷新订单数据
            } else {
              this.$message.error('订单发货失败: ' + (response.message || '未知错误'))
            }
          }).catch(error => {
            this.$message.error('订单发货失败: ' + (error.message || '未知错误'))
          }).finally(() => {
            this.shipSubmitting = false
          })
        }
      })
    },
    // 完成订单
    handleComplete() {
      this.$confirm('确认将此订单标记为已完成?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        completeOrder(this.orderData.id).then(response => {
          if (response.code === 200) {
            this.$notify({
              title: '成功',
              message: '订单已完成',
              type: 'success',
              duration: 2000
            })
            this.fetchOrderDetail() // 刷新订单数据
          } else {
            this.$message.error('操作失败: ' + (response.message || '未知错误'))
          }
        }).catch(error => {
          this.$message.error('操作失败: ' + (error.message || '未知错误'))
        })
      }).catch(() => {
        // 取消操作，不做处理
      })
    },
    // 获取支付方式名称
    getPaymentMethodName(method) {
      const methodMap = {
        alipay: '支付宝',
        wechat: '微信支付',
        creditcard: '信用卡',
        cash: '现金',
        0: '支付宝',
        1: '微信支付',
        2: '信用卡',
        3: '现金'
      }
      return methodMap[method] || '未知'
    },
    // 获取订单状态名称
    getOrderStatusName(status) {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已发货',
        3: '已完成',
        4: '已取消',
        5: '已退款'
      }
      return statusMap[status] || '未知状态'
    },
    // 获取订单状态类型
    getOrderStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'primary',
        2: 'info',
        3: 'success',
        4: 'danger',
        5: 'info'
      }
      return statusMap[status] || ''
    },
    // 格式化地址
    formatAddress(addressInfo) {
      if (!addressInfo) return '暂无地址信息'
      
      const { province, city, district, detailAddress } = addressInfo
      let fullAddress = ''
      
      if (province) fullAddress += province
      if (city) fullAddress += ' ' + city
      if (district) fullAddress += ' ' + district
      if (detailAddress) fullAddress += ' ' + detailAddress
      
      return fullAddress || '暂无地址信息'
    },
    // 计算商品小计
    calculateItemTotal(item) {
      if (!item) return '0.00'
      
      const price = parseFloat(item.price) || 0
      const quantity = parseInt(item.quantity) || 0
      
      return (price * quantity).toFixed(2)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .status-container {
    .el-tag {
      font-size: 14px;
      padding: 8px 12px;
    }
  }
}

.order-container {
  .box-card {
    margin-bottom: 20px;
    
    .card-header {
      font-weight: bold;
    }
    
    .card-content {
      .info-item {
        display: flex;
        margin-bottom: 12px;
        
        .label {
          width: 100px;
          color: #909399;
        }
        
        .value {
          flex: 1;
          &.price {
            color: #f56c6c;
            font-weight: bold;
          }
        }
      }
    }
  }
  
  .order-summary {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    
    .summary-item {
      margin-bottom: 8px;
      font-size: 14px;
      
      &.total {
        font-size: 16px;
        font-weight: bold;
        margin-top: 5px;
        
        .price {
          color: #f56c6c;
          font-size: 18px;
        }
      }
    }
  }
  
  .action-area {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
    
    .el-button {
      margin-left: 10px;
    }
  }
}
</style> 