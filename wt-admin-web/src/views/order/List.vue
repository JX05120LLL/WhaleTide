<template>
  <div class="app-container">
    <!-- 过滤搜索区域 -->
    <div class="filter-container">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <!-- 添加用户ID输入框 -->
          <el-input
            v-model="listQuery.userId"
            placeholder="用户ID"
            clearable
            class="filter-item"
          />
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select
            v-model="listQuery.status"
            placeholder="订单状态"
            clearable
            class="filter-item"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="12">
          <div class="button-group">
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
            >
              搜索
            </el-button>
            <el-button
              icon="el-icon-refresh"
              type="info"
              @click="refreshList"
            >
              尝试获取真实数据
            </el-button>
            <el-button
              type="danger"
              icon="el-icon-download"
              @click="handleExport"
            >
              导出数据
            </el-button>
            <el-button
              type="success"
              icon="el-icon-data-line"
              @click="loadTestData"
            >
              使用测试数据
            </el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 列表区域 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="加载中..."
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column
        label="订单号"
        align="center"
        width="180"
      >
        <template slot-scope="scope">
          <el-link type="primary" @click="handleDetail(scope.row)">{{ scope.row.orderSn }}</el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="用户"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <div class="user-info">
            <el-avatar :size="30" :src="scope.row.userAvatar || ''">{{ scope.row.userName ? scope.row.userName.substring(0, 1) : 'U' }}</el-avatar>
            <span class="user-name">{{ scope.row.userName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="商品数量"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <span>{{ getItemCount(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="订单金额"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <span class="price">￥{{ scope.row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="支付方式"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <span>{{ getpayTypeName(scope.row.payType) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="订单状态"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-tag :type="getOrderStatusType(scope.row.status)">
            {{ getOrderStatusName(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        min-width="200"
      >
        <template slot-scope="scope">
          <div class="action-buttons">
            <div class="button-row">
              <el-tooltip content="订单详情" placement="top">
                <el-button
                  type="primary"
                  size="mini"
                  icon="el-icon-view"
                  @click="handleDetail(scope.row)"
                >
                  详情
                </el-button>
              </el-tooltip>
              
              <el-tooltip v-if="scope.row.status === 1" content="发货" placement="top">
                <el-button
                  type="success"
                  size="mini"
                  icon="el-icon-truck"
                  @click="handleShip(scope.row)"
                >
                  发货
                </el-button>
              </el-tooltip>
            </div>
            <div class="button-row">
              <el-tooltip v-if="scope.row.status === 2" content="确认收货" placement="top">
                <el-button
                  type="warning"
                  size="mini"
                  icon="el-icon-box"
                  @click="handleComplete(scope.row)"
                >
                  完成
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 发货对话框 -->
    <el-dialog title="订单发货" :visible.sync="shipDialogVisible" width="500px">
      <el-form
        ref="shipForm"
        :model="shipForm"
        :rules="shipRules"
        label-width="100px"
      >
        <el-form-item label="订单号">
          <span>{{ currentOrder.orderSn }}</span>
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
import { fetchList, shipOrder, completeOrder } from '@/api/order'
import Pagination from '@/components/Pagination'

export default {
  name: 'OrderList',
  components: { Pagination },
  data() {
    return {
      list: [], // 订单列表
      total: 0, // 总数
      listLoading: true, // 加载状态
      listQuery: { // 查询参数
        pageNum: 1,
        pageSize: 10,
        status: undefined,
        userId: undefined
      },
      // 状态选项
      statusOptions: [
        { value: 0, label: '待支付' },
        { value: 1, label: '已支付' },
        { value: 2, label: '已发货' },
        { value: 3, label: '已完成' },
        { value: 4, label: '已取消' },
        { value: 5, label: '已退款' }
      ],
      // 发货对话框
      shipDialogVisible: false,
      shipSubmitting: false,
      currentOrder: {}, // 当前操作的订单
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
    // 直接使用测试数据
    this.loadTestData()
    
    // 添加注释，原来的方法保留供需要时使用
    // this.getList()
  },
  methods: {
    // 获取订单列表
    getList() {
      this.listLoading = true
      console.log('请求订单列表，参数:', this.listQuery)
      
      fetchList(this.listQuery).then(response => {
        console.log('订单API响应:', response)
        console.log('订单API响应原始数据:', JSON.stringify(response))
        const { data, code } = response
        
        // 添加更详细的日志
        console.log('API响应数据详情:', {
          code: code,
          dataType: typeof data,
          hasData: !!data,
          dataKeys: data ? Object.keys(data) : []
        })
        
        // 使用console.table查看数据结构
        if (data && (data.list || Array.isArray(data))) {
          console.table(data.list || data);
        }
        
        if (code !== 200 || !data) {
          console.error('API返回数据异常:', response)
          this.list = []
          this.total = 0
          this.listLoading = false
          return
        }
        
        // 处理后端返回的订单列表数据 - 优先检查data本身是否就是数组
        if (Array.isArray(data)) {
          this.list = data;
          this.total = data.length;
          console.log('API直接返回数组数据:', data.length);
        } 
        // 然后检查是否有嵌套的list属性
        else if (data.list && Array.isArray(data.list)) {
          this.list = data.list;
          this.total = data.total || data.list.length;
          console.log('API返回标准分页格式数据:', data.list.length);
        }
        // 检查是否有records属性(MyBatis-Plus分页格式)
        else if (data.records && Array.isArray(data.records)) {
          this.list = data.records;
          this.total = data.total || data.records.length;
          console.log('API返回MyBatis-Plus格式数据:', data.records.length);
        }
        // 其他情况
        else {
          this.list = [];
          this.total = 0;
          console.warn('无法识别的响应数据格式:', data);
        }
        
        if (this.list.length === 0) {
          console.log('没有订单数据，尝试加载测试数据');
          this.loadTestData();
          return;
        }
        
        // 处理每个订单项的数据完整性
        this.list = this.list.map(order => {
          return {
            id: order.id || 0,
            orderSn: order.orderSn || '未知订单号',
            userName: order.userName || '未知用户',
            userAvatar: order.userAvatar || '',
            status: order.status !== undefined ? order.status : 0,
            totalAmount: order.totalAmount || 0,
            payType: order.payType !== undefined ? order.payType : 0,
            createTime: order.createTime || '未知时间',
            orderItemVoList: order.orderItemVoList || [],
            ...order
          }
        })
        
        console.log('处理后的订单列表:', this.list);
        console.log('订单数据示例:', this.list.length > 0 ? this.list[0] : 'No orders');
        this.listLoading = false
      }).catch(error => {
        console.error('获取订单列表失败:', error)
        this.listLoading = false
        this.$message.error('获取订单列表失败: ' + (error.message || '未知错误'))
        this.list = []
        this.total = 0
      })
    },
    
    // 获取订单商品数量
    getItemCount(order) {
      if (order.itemCount !== undefined) {
        return order.itemCount
      }
      
      if (order.orderItemVoList && Array.isArray(order.orderItemVoList)) {
        return order.orderItemVoList.reduce((sum, item) => sum + (item.quantity || 0), 0)
      }
      
      return 0
    },
    // 处理搜索
    handleSearch() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // 刷新列表
    refreshList() {
      // 尝试从服务器获取真实数据
      this.getList()
    },
    // 导出数据
    handleExport() {
      this.$message({
        message: '导出订单数据功能开发中...',
        type: 'info'
      })
    },
    // 查看订单详情
    handleDetail(row) {
      this.$router.push({
        name: 'OrderDetail',
        params: { id: row.id }
      })
    },
    // 处理发货
    handleShip(row) {
      this.currentOrder = row
      this.shipForm = {
        orderId: row.id,
        shippingCompany: '',
        trackingNumber: '',
        remark: ''
      }
      this.shipDialogVisible = true
      this.$nextTick(() => {
        this.$refs['shipForm'].clearValidate()
      })
    },
    // 提交发货表单
    submitShipForm() {
      this.$refs['shipForm'].validate((valid) => {
        if (valid) {
          this.shipSubmitting = true
          shipOrder(this.currentOrder.id, this.shipForm).then(() => {
            this.shipDialogVisible = false
            this.$notify({
              title: '成功',
              message: '订单发货成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.$message.error('订单发货失败')
          }).finally(() => {
            this.shipSubmitting = false
          })
        }
      })
    },
    // 完成订单
    handleComplete(row) {
      this.$confirm('确认将此订单标记为已完成?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        completeOrder(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '订单已完成',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }).catch(() => {
          this.$message.error('操作失败')
        })
      })
    },
    // 获取支付方式名称
    getpayTypeName(method) {
      const methodMap = {
        0: '支付宝',
        1: '微信支付',
        2: '信用卡',
        3: '现金'
      }
      return methodMap[method] || method
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
      return statusMap[status] || status
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
    // 加载测试数据
    loadTestData() {
      // 创建模拟数据
      const testData = [
        {
          id: 1001,
          orderSn: 'TEST-1001',
          userName: '测试用户1',
          userAvatar: '',
          status: 0,
          totalAmount: 199.00,
          payType: 0,
          createTime: '2025-04-15 12:00:00',
          orderItemVoList: [
            {
              id: 10001,
              productId: 1,
              productName: '测试商品1',
              productImage: 'https://via.placeholder.com/100',
              price: 99.00,
              quantity: 1
            },
            {
              id: 10002,
              productId: 2,
              productName: '测试商品2',
              productImage: 'https://via.placeholder.com/100',
              price: 100.00,
              quantity: 1
            }
          ]
        },
        {
          id: 1002,
          orderSn: 'TEST-1002',
          userName: '测试用户2',
          userAvatar: '',
          status: 1,
          totalAmount: 299.00,
          payType: 1,
          createTime: '2025-04-15 13:00:00',
          orderItemVoList: [
            {
              id: 10003,
              productId: 3,
              productName: '测试商品3',
              productImage: 'https://via.placeholder.com/100',
              price: 299.00,
              quantity: 1
            }
          ]
        },
        {
          id: 1003,
          orderSn: 'TEST-1003',
          userName: '测试用户3',
          userAvatar: '',
          status: 2,
          totalAmount: 399.00,
          payType: 2,
          createTime: '2025-04-15 14:00:00',
          orderItemVoList: [
            {
              id: 10004,
              productId: 4,
              productName: '测试商品4',
              productImage: 'https://via.placeholder.com/100',
              price: 199.00,
              quantity: 2
            }
          ]
        },
        {
          id: 1004,
          orderSn: 'TEST-1004',
          userName: '测试用户4',
          userAvatar: '',
          status: 3,
          totalAmount: 499.00,
          payType: 3,
          createTime: '2025-04-15 15:00:00',
          orderItemVoList: [
            {
              id: 10005,
              productId: 5,
              productName: '测试商品5',
              productImage: 'https://via.placeholder.com/100',
              price: 499.00,
              quantity: 1
            }
          ]
        },
        {
          id: 1005,
          orderSn: 'TEST-1005',
          userName: '测试用户5',
          userAvatar: '',
          status: 4,
          totalAmount: 599.00,
          payType: 0,
          createTime: '2025-04-15 16:00:00',
          orderItemVoList: [
            {
              id: 10006,
              productId: 6,
              productName: '测试商品6',
              productImage: 'https://via.placeholder.com/100',
              price: 599.00,
              quantity: 1
            }
          ]
        }
      ]
      
      // 使用测试数据
      this.list = testData
      this.total = testData.length
      this.listLoading = false
      
      this.$message({
        message: '已加载测试数据',
        type: 'success'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  background-color: #fff;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .filter-item {
    width: 100%;
    margin-bottom: 10px;
  }

  .button-group {
    display: flex;
    justify-content: flex-end;
    
    .el-button {
      margin-left: 10px;
    }
    
    @media (max-width: 768px) {
      justify-content: center;
      margin-top: 10px;
    }
  }
}

.action-buttons {
  display: flex;
  flex-direction: column;
  justify-content: center;
  
  .button-row {
    display: flex;
    justify-content: center;
    margin: 3px 0;
    
    .el-button {
      margin: 2px;
      min-width: 80px;
    }
  }
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  
  .user-name {
    margin-left: 8px;
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 80px;
  }
}

.price {
  color: #f56c6c;
  font-weight: bold;
}
</style> 