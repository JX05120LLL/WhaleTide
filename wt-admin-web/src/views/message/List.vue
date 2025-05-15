<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="留言内容/用户名"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select 
        v-model="listQuery.status" 
        placeholder="回复状态" 
        clearable 
        style="width: 130px" 
        class="filter-item"
      >
        <el-option label="未回复" value="pending" />
        <el-option label="已回复" value="replied" />
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
      :data="list"
      element-loading-text="加载中..."
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column label="ID" align="center" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="用户" min-width="120px">
        <template slot-scope="scope">
          <div class="user-info">
            <el-avatar :src="scope.row.avatar" :size="30"></el-avatar>
            <span style="margin-left: 10px">{{ scope.row.username }}</span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="留言内容" min-width="200px">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="留言时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 'replied' ? 'success' : 'warning'">
            {{ row.status === 'replied' ? '已回复' : '未回复' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button 
            type="primary" 
            size="mini" 
            @click="handleReply(row)"
          >
            {{ row.status === 'replied' ? '查看回复' : '回复' }}
          </el-button>
          <el-button 
            type="danger" 
            size="mini" 
            @click="handleDelete(row)"
          >
            删除
          </el-button>
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

    <!-- 回复弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <div class="message-detail">
        <div class="message-user">
          <el-avatar :src="currentMessage.avatar" :size="40"></el-avatar>
          <div class="user-detail">
            <div class="username">{{ currentMessage.username }}</div>
            <div class="time">{{ currentMessage.createTime }}</div>
          </div>
        </div>
        <div class="message-content">
          {{ currentMessage.content }}
        </div>

        <div v-if="currentMessage.status === 'replied'" class="reply-content">
          <div class="reply-header">
            <div class="admin-tag">管理员回复</div>
            <div class="time">{{ currentMessage.replyTime }}</div>
          </div>
          <div class="reply-text">{{ currentMessage.reply }}</div>
        </div>
      </div>
      
      <el-form v-if="!isReadOnly" ref="replyForm" :model="replyForm" :rules="replyRules">
        <el-form-item prop="reply">
          <el-input
            v-model="replyForm.reply"
            type="textarea"
            :rows="4"
            placeholder="请输入回复内容"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button v-if="!isReadOnly" type="primary" @click="submitReply">提交回复</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchMessageList, replyMessage, deleteMessage } from '@/api/message'
import Pagination from '@/components/Pagination'

export default {
  name: 'MessageList',
  components: { Pagination },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: undefined,
        status: undefined,
        startDate: undefined,
        endDate: undefined
      },
      dateRange: [],
      dialogVisible: false,
      isReadOnly: false,
      dialogTitle: '回复留言',
      currentMessage: {},
      replyForm: {
        id: undefined,
        reply: ''
      },
      replyRules: {
        reply: [{ required: true, message: '回复内容不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchMessageList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取留言列表失败')
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
    handleReply(row) {
      this.currentMessage = Object.assign({}, row)
      this.isReadOnly = row.status === 'replied'
      this.dialogTitle = this.isReadOnly ? '查看留言与回复' : '回复留言'
      this.replyForm = {
        id: row.id,
        reply: row.reply || ''
      }
      this.dialogVisible = true
      if (!this.isReadOnly) {
        this.$nextTick(() => {
          this.$refs['replyForm'].clearValidate()
        })
      }
    },
    submitReply() {
      this.$refs['replyForm'].validate(valid => {
        if (valid) {
          replyMessage(this.replyForm).then(() => {
            this.dialogVisible = false
            this.$message({
              message: '回复成功',
              type: 'success'
            })
            // 更新当前行状态
            const index = this.list.findIndex(item => item.id === this.currentMessage.id)
            if (index !== -1) {
              this.list[index].status = 'replied'
              this.list[index].reply = this.replyForm.reply
              this.list[index].replyTime = new Date().toLocaleString()
            }
          }).catch(() => {
            this.$message.error('回复失败')
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除这条留言吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMessage(row.id).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          const index = this.list.findIndex(item => item.id === row.id)
          if (index !== -1) {
            this.list.splice(index, 1)
          }
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
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

.user-info {
  display: flex;
  align-items: center;
}

.message-detail {
  margin-bottom: 20px;
  
  .message-user {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    
    .user-detail {
      margin-left: 10px;
      
      .username {
        font-weight: bold;
      }
      
      .time {
        font-size: 12px;
        color: #909399;
      }
    }
  }
  
  .message-content {
    background-color: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 15px;
    word-break: break-all;
  }
  
  .reply-content {
    background-color: #f0f9eb;
    padding: 15px;
    border-radius: 4px;
    
    .reply-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      
      .admin-tag {
        color: #67c23a;
        font-weight: bold;
      }
      
      .time {
        font-size: 12px;
        color: #909399;
      }
    }
    
    .reply-text {
      word-break: break-all;
    }
  }
}
</style> 