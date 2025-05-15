<template>
  <div class="app-container">
    <div class="filter-container">
      <el-date-picker
        v-model="timeRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"
        :picker-options="pickerOptions"
        style="width: 350px"
        @change="handleDateChange"
      />
      <el-button v-waves type="primary" icon="el-icon-search" @click="fetchData">
        查询
      </el-button>
      <el-button v-waves icon="el-icon-refresh" @click="resetFilter">
        重置
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>订单总数</span>
          </div>
          <div class="card-content">
            <div class="statistics-number">{{ statistics.totalOrders }}</div>
            <div class="statistics-label">笔订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>成交金额</span>
          </div>
          <div class="card-content">
            <div class="statistics-number">¥{{ statistics.totalAmount.toFixed(2) }}</div>
            <div class="statistics-label">总金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>商品销量</span>
          </div>
          <div class="card-content">
            <div class="statistics-number">{{ statistics.totalItems }}</div>
            <div class="statistics-label">销售数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>客单价</span>
          </div>
          <div class="card-content">
            <div class="statistics-number">¥{{ statistics.averageOrderValue.toFixed(2) }}</div>
            <div class="statistics-label">平均订单金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card class="box-card" style="height: 400px">
          <div slot="header" class="clearfix">
            <span>销售趋势</span>
            <el-radio-group v-model="salesChartType" size="mini" style="float: right" @change="updateSalesChart">
              <el-radio-button label="daily">日</el-radio-button>
              <el-radio-button label="weekly">周</el-radio-button>
              <el-radio-button label="monthly">月</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-wrapper">
            <div ref="salesChart" class="chart-container" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card" style="height: 400px">
          <div slot="header" class="clearfix">
            <span>订单状态分布</span>
          </div>
          <div class="chart-wrapper">
            <div ref="statusChart" class="chart-container" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>热销商品 Top 10</span>
          </div>
          <el-table
            :data="statistics.topProducts"
            style="width: 100%"
            border
            v-loading="loading"
          >
            <el-table-column
              label="排名"
              width="80"
              align="center"
            >
              <template slot-scope="scope">
                <el-tag :type="getRankType(scope.$index)">{{ scope.$index + 1 }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              label="商品图片"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                <el-image
                  style="width: 60px; height: 60px"
                  :src="scope.row.image"
                  fit="cover"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              label="商品名称"
              min-width="200"
            />
            <el-table-column
              prop="price"
              label="单价"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                <span class="price">¥{{ scope.row.price.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="salesCount"
              label="销量"
              width="100"
              align="center"
              sortable
            />
            <el-table-column
              label="销售额"
              width="150"
              align="center"
            >
              <template slot-scope="scope">
                <span class="price">¥{{ (scope.row.price * scope.row.salesCount).toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                <el-button 
                  type="text" 
                  size="small" 
                  @click="goToProduct(scope.row.id)"
                >
                  商品详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import waves from '@/directive/waves/index'
import { fetchOrderStatistics } from '@/api/order'

export default {
  name: 'OrderStatistics',
  directives: { waves },
  data() {
    return {
      loading: false,
      timeRange: [
        this.getLastMonthDate(),
        this.getCurrentDate()
      ],
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      salesChartType: 'daily',
      salesChart: null,
      statusChart: null,
      statistics: {
        totalOrders: 0,
        totalAmount: 0,
        totalItems: 0,
        averageOrderValue: 0,
        statusDistribution: {},
        salesTrend: {},
        topProducts: []
      }
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.salesChart) {
      this.salesChart.dispose()
    }
    if (this.statusChart) {
      this.statusChart.dispose()
    }
  },
  methods: {
    getCurrentDate() {
      const date = new Date()
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    getLastMonthDate() {
      const date = new Date()
      date.setMonth(date.getMonth() - 1)
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    handleDateChange() {
      // Will be triggered by the fetchData method
    },
    resetFilter() {
      this.timeRange = [
        this.getLastMonthDate(),
        this.getCurrentDate()
      ]
      this.fetchData()
    },
    fetchData() {
      this.loading = true
      
      const params = {
        startDate: this.timeRange[0],
        endDate: this.timeRange[1],
        type: this.salesChartType
      }
      
      fetchOrderStatistics(params).then(response => {
        console.log('Statistics data:', response.data)
        this.statistics = response.data
        
        // Default values if data is missing
        if (!this.statistics.statusDistribution) {
          this.statistics.statusDistribution = {
            PENDING: 0,
            PAID: 0,
            SHIPPED: 0,
            COMPLETED: 0,
            CANCELLED: 0,
            REFUNDED: 0
          }
        }
        
        if (!this.statistics.salesTrend) {
          this.statistics.salesTrend = {
            labels: [],
            sales: [],
            orders: []
          }
        }
        
        this.initCharts()
        this.loading = false
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        this.$message.error('获取统计数据失败')
        this.loading = false
      })
    },
    initCharts() {
      this.initSalesChart()
      this.initStatusChart()
    },
    initSalesChart() {
      if (this.salesChart) {
        this.salesChart.dispose()
      }
      
      const salesChartDom = this.$refs.salesChart
      this.salesChart = echarts.init(salesChartDom)
      
      const { labels, sales, orders } = this.statistics.salesTrend
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        legend: {
          data: ['销售额', '订单数']
        },
        xAxis: [
          {
            type: 'category',
            data: labels,
            axisPointer: {
              type: 'shadow'
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '销售额',
            min: 0,
            axisLabel: {
              formatter: '¥{value}'
            }
          },
          {
            type: 'value',
            name: '订单数',
            min: 0,
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: '销售额',
            type: 'bar',
            data: sales
          },
          {
            name: '订单数',
            type: 'line',
            yAxisIndex: 1,
            data: orders
          }
        ]
      }
      
      this.salesChart.setOption(option)
    },
    initStatusChart() {
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      
      const statusChartDom = this.$refs.statusChart
      this.statusChart = echarts.init(statusChartDom)
      
      const { statusDistribution } = this.statistics
      const statusData = Object.entries(statusDistribution).map(([status, count]) => ({
        name: this.getStatusName(status),
        value: count
      }))
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: statusData.map(item => item.name)
        },
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9B59B6'],
        series: [
          {
            name: '订单状态',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: statusData
          }
        ]
      }
      
      this.statusChart.setOption(option)
    },
    handleResize() {
      if (this.salesChart) {
        this.salesChart.resize()
      }
      if (this.statusChart) {
        this.statusChart.resize()
      }
    },
    updateSalesChart() {
      this.fetchData()
    },
    getStatusName(status) {
      const statusMap = {
        PENDING: '待支付',
        PAID: '已支付',
        SHIPPED: '已发货',
        COMPLETED: '已完成',
        CANCELLED: '已取消',
        REFUNDED: '已退款'
      }
      return statusMap[status] || status
    },
    getRankType(index) {
      const types = ['danger', 'warning', 'success', '', '', '', '', '', '', '']
      return types[index] || ''
    },
    goToProduct(id) {
      this.$router.push({
        path: `/product/detail/${id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  padding-bottom: 15px;
  display: flex;
  gap: 10px;
}

.box-card {
  margin-bottom: 15px;
}

.card-content {
  text-align: center;
  padding: 20px 0;
  
  .statistics-number {
    font-size: 28px;
    font-weight: bold;
    color: #409EFF;
    margin-bottom: 5px;
  }
  
  .statistics-label {
    font-size: 14px;
    color: #606266;
  }
}

.chart-wrapper {
  height: 340px;
  width: 100%;
  
  .chart-container {
    height: 100%;
    width: 100%;
  }
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.trending-up {
  color: #67C23A;
}

.trending-down {
  color: #F56C6C;
}
</style> 