<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>系统日志</span>
        <el-button
          style="float: right; margin-left: 10px;" 
          type="danger"
          @click="handleClearLogs"
        >
          清空日志
        </el-button>
        <el-button 
          style="float: right;" 
          type="primary"
          icon="el-icon-refresh"
          @click="fetchData"
        >
          刷新
        </el-button>
      </div>
      
      <div class="filter-container">
        <el-input
          v-model="listQuery.keyword"
          placeholder="关键词"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select 
          v-model="listQuery.level" 
          placeholder="日志级别" 
          clearable 
          style="width: 130px" 
          class="filter-item"
        >
          <el-option label="INFO" value="INFO" />
          <el-option label="WARN" value="WARN" />
          <el-option label="ERROR" value="ERROR" />
          <el-option label="DEBUG" value="DEBUG" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="filter-item"
          @change="handleDateRangeChange"
        />
        <el-button 
          class="filter-item" 
          type="primary" 
          icon="el-icon-search" 
          @click="handleFilter"
        >
          搜索
        </el-button>
      </div>
      
      <el-table
        v-loading="listLoading"
        :data="logs"
        element-loading-text="加载中..."
        border
        fit
        highlight-current-row
      >
        <el-table-column label="ID" align="center" width="80">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作用户" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="IP地址" width="130" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.ip }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作类型" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.operationType }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作描述" min-width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作结果" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.result === 'SUCCESS' ? 'success' : 'danger'">
              {{ scope.row.result }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作时间" width="160" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
      </el-table>
      
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="getList"
      />
    </el-card>
  </div>
</template>

<script>
import { getLogs, clearLogs } from '@/api/system'
import Pagination from '@/components/Pagination'

export default {
  name: 'SystemLogs',
  components: { Pagination },
  data() {
    return {
      logs: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: '',
        level: '',
        startDate: undefined,
        endDate: undefined
      },
      dateRange: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.getList()
    },
    getList() {
      this.listLoading = true
      getLogs(this.listQuery).then(response => {
        this.logs = response.data.items
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取系统日志失败')
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDateRangeChange(val) {
      if (val) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    handleClearLogs() {
      this.$confirm('确定要清空所有系统日志吗? 此操作不可恢复', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        clearLogs().then(() => {
          this.$message({
            type: 'success',
            message: '系统日志已清空!'
          })
          this.getList()
        }).catch(() => {
          this.$message.error('清空系统日志失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消清空操作'
        })
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
  padding-bottom: 10px;
  
  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}
</style> 
 