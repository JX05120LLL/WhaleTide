<template>
  <div class="dashboard-container">
    <el-row type="flex" justify="center" :gutter="20" class="data-cards-row">
      <el-col :span="4" :xs="12" :sm="8" :md="6" :lg="4">
        <el-card shadow="hover" class="data-card user-card" :body-style="{ padding: '20px' }">
          <div slot="header" class="card-header">
            <span>用户总数</span>
          </div>
          <div class="card-content">
            <div class="card-panel-num">{{ stats.userCount || 0 }}</div>
            <div class="card-panel-icon">
              <i class="el-icon-user"></i>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" :xs="12" :sm="8" :md="6" :lg="4">
        <el-card shadow="hover" class="data-card message-card" :body-style="{ padding: '20px' }">
          <div slot="header" class="card-header">
            <span>留言总数</span>
          </div>
          <div class="card-content">
            <div class="card-panel-num">{{ stats.messageCount || 0 }}</div>
            <div class="card-panel-icon">
              <i class="el-icon-chat-line-square"></i>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" :xs="12" :sm="8" :md="6" :lg="4">
        <el-card shadow="hover" class="data-card product-card" :body-style="{ padding: '20px' }">
          <div slot="header" class="card-header">
            <span>商品总数</span>
          </div>
          <div class="card-content">
            <div class="card-panel-num">{{ stats.productCount || 0 }}</div>
            <div class="card-panel-icon">
              <i class="el-icon-goods"></i>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover" class="info-card notice-card" :body-style="{ padding: '15px 20px' }">
          <div slot="header" class="card-header">
            <span>系统公告</span>
          </div>
          <div v-if="announcements.length === 0" class="empty-data">暂无公告</div>
          <el-timeline v-else class="notice-timeline">
            <el-timeline-item
              v-for="(announcement, index) in announcements"
              :key="index"
              :timestamp="announcement.time"
              placement="top"
              type="primary"
            >
              <el-card class="announcement-card">
                <h4 class="announcement-title">{{ announcement.title }}</h4>
                <p class="announcement-content">{{ announcement.content }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="info-card" :body-style="{ padding: '15px 20px' }">
          <div slot="header" class="card-header">
            <span>待办事项</span>
          </div>
          <div v-if="todos.length === 0" class="empty-data">暂无待办事项</div>
          <el-table v-else :data="todos" style="width: 100%" :row-class-name="tableRowClassName">
            <el-table-column prop="content" label="内容"></el-table-column>
            <el-table-column prop="date" label="时间" width="180"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDashboardData } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        userCount: 0,
        messageCount: 0,
        productCount: 0
      },
      announcements: [],
      todos: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      console.log('开始获取仪表盘数据');
      
      // 开发环境也尝试请求后端数据，便于测试
      const useDefaultDataInDev = true; // 设为false表示开发环境也请求后端数据
      
      if (process.env.NODE_ENV === 'development' && useDefaultDataInDev) {
        console.log('开发环境使用模拟数据');
        setTimeout(() => {
          this.useDefaultData();
        }, 500);
        return;
      }
      
      // 显示加载提示
      this.$notify({
        title: '正在加载',
        message: '正在从后端加载仪表盘数据',
        type: 'info',
        duration: 2000
      });
      
      getDashboardData()
        .then(response => {
          console.log('仪表盘数据获取成功:', response);
          const { data } = response;
          
          if (data) {
            this.stats = data.stats || this.stats;
            this.announcements = data.announcements || [];
            this.todos = data.todos || [];
            
            this.$notify({
              title: '加载成功',
              message: '仪表盘数据加载成功',
              type: 'success',
              duration: 2000
            });
          } else {
            console.warn('API响应缺少data字段:', response);
            this.useDefaultData();
          }
        })
        .catch(error => {
          console.error('获取仪表盘数据失败:', error);
          
          // 错误详情记录
          if (error.response) {
            // 请求成功发出且服务器响应了状态码，但状态码超出了2xx的范围
            console.log('错误状态码:', error.response.status);
            console.log('错误响应数据:', error.response.data);
            
            // 如果是401错误，尝试重新获取一次，可能是token问题
            if (error.response.status === 401) {
              console.log('未授权，尝试重新获取仪表盘数据');
              // 一秒后重试
              setTimeout(() => {
                getDashboardData().catch(retryError => {
                  console.log('重试失败:', retryError);
                  this.useDefaultData();
                  this.$notify.error({
                    title: '重试失败',
                    message: '身份验证失败，使用默认数据'
                  });
                });
              }, 1000);
              return;
            }
          } else if (error.request) {
            // 请求已经成功发起，但没有收到响应
            console.log('请求已发送但无响应');
          } else {
            // 在设置请求时发生了错误
            console.log('请求配置错误:', error.message);
          }
          
          // 显示错误通知
          this.$notify.error({
            title: '数据加载失败',
            message: '仪表盘数据加载失败，将使用默认数据'
          });
          
          // 使用默认数据
          this.useDefaultData();
        });
    },
    useDefaultData() {
      // 模拟数据(实际开发中应根据需求保留或删除)
      this.stats = {
        userCount: 245,
        messageCount: 56,
        productCount: 32,
      }
      this.announcements = [
        { 
          title: '系统更新通知', 
          content: '系统将于2023-06-01进行版本更新，新增活动管理功能，请各位管理员注意。', 
          time: '2023-05-25 10:00:00' 
        },
        { 
          title: '活动筹备通知', 
          content: '即将在7月份开展暑期动漫活动，请提前做好筹备工作。', 
          time: '2023-05-20 14:30:00' 
        }
      ]
      this.todos = [
        { content: '审核新提交的活动申请', date: '2023-05-26', status: '未完成' },
        { content: '回复用户留言', date: '2023-05-25', status: '已完成' },
        { content: '更新网站首页轮播图', date: '2023-05-28', status: '未完成' }
      ]
    },
    tableRowClassName({row}) {
      if (row.status === '已完成') {
        return 'success-row';
      } else {
        return 'warning-row';
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px 20px 20px 40px; /* 增加左侧内边距 */
  margin: 0 0 0 200px; /* 添加左侧外边距，避开侧边栏 */
  
  .data-cards-row {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
  }

  .card-header {
    display: flex;
    align-items: center;
    font-weight: bold;
    font-size: 16px;
  }

  .data-card {
    height: 160px;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;
    border-radius: 8px;
    border: none;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    }

    .card-content {
      position: relative;
      height: 100px;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
    
    .card-panel-num {
      font-size: 28px;
      font-weight: bold;
      margin-top: 10px;
      z-index: 2;
      position: relative;
    }
    
    .card-panel-icon {
      position: absolute;
      right: 15px;
      bottom: 10px;
      font-size: 45px;
      opacity: 0.2;
      z-index: 1;
    }

    &.activity-card {
      background: linear-gradient(to right, #f46b45, #eea849);
      color: white;
      
      .el-card__header {
        background-color: rgba(255, 255, 255, 0.1);
        color: white;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
    }

    &.user-card {
      background: linear-gradient(to right, #36d1dc, #5b86e5);
      color: white;
      
      .el-card__header {
        background-color: rgba(255, 255, 255, 0.1);
        color: white;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
    }
    
    &.message-card {
      background: linear-gradient(to right, #ff5f6d, #ffc371);
      color: white;
      
      .el-card__header {
        background-color: rgba(255, 255, 255, 0.1);
        color: white;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
    }
    
    &.product-card {
      background: linear-gradient(to right, #11998e, #38ef7d);
      color: white;
      
      .el-card__header {
        background-color: rgba(255, 255, 255, 0.1);
        color: white;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
    }
    
    &.role-card {
      background: linear-gradient(to right, #834d9b, #d04ed6);
      color: white;
      
      .el-card__header {
        background-color: rgba(255, 255, 255, 0.1);
        color: white;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
    }
  }
  
  .info-card {
    margin-bottom: 20px;
    
    .empty-data {
      text-align: center;
      color: #909399;
      padding: 30px 0;
      font-size: 14px;
    }
    
    .announcement-card {
      margin-bottom: 10px;
      
      .announcement-title {
        margin: 0 0 10px;
        font-size: 16px;
      }
      
      .announcement-content {
        margin: 0;
        color: #606266;
        font-size: 14px;
      }
    }
  }
  
  .notice-timeline {
    padding-top: 10px;
  }
  
  .warning-row {
    background-color: rgba(255, 236, 204, 0.3);
  }
  
  .success-row {
    background-color: rgba(227, 255, 233, 0.3);
  }
}
</style> 