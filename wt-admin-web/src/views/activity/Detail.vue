<template>
  <div class="app-container">
    <div class="title-container">
      <el-page-header @back="goBack" :content="activity.title || '活动详情'" />
      <div class="action-buttons">
        <el-button 
          type="primary" 
          icon="el-icon-edit" 
          @click="handleEdit"
        >
          编辑
        </el-button>
        <el-button 
          :type="activity.status === 'published' ? 'warning' : 'success'" 
          :icon="activity.status === 'published' ? 'el-icon-close' : 'el-icon-check'" 
          @click="handleStatusChange"
        >
          {{ activity.status === 'published' ? '下架' : '发布' }}
        </el-button>
      </div>
    </div>

    <el-card v-loading="loading">
      <el-row :gutter="20" v-if="!loading">
        <el-col :span="16">
          <div class="activity-info">
            <h2 class="activity-title">{{ activity.title }}</h2>
            <div class="info-item">
              <span class="label">活动时间:</span>
              <span class="value">{{ activity.startTime }} - {{ activity.endTime }}</span>
            </div>
            <div class="info-item">
              <span class="label">活动地点:</span>
              <span class="value">{{ activity.location }}</span>
            </div>
            <div class="info-item">
              <span class="label">参与人数:</span>
              <span class="value">{{ activity.participantCount }}/{{ activity.maxParticipants }}</span>
            </div>
            <div class="info-item">
              <span class="label">状态:</span>
              <el-tag :type="statusType">{{ statusText }}</el-tag>
            </div>
            <div class="info-item">
              <span class="label">创建时间:</span>
              <span class="value">{{ activity.createTime }}</span>
            </div>
            <div class="info-item description">
              <span class="label">活动描述:</span>
              <div class="value-block" v-html="formattedDescription"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="activity-image">
            <el-image 
              :src="formatImageUrl(activity.coverUrl || activity.image) || 'https://via.placeholder.com/400x300?text=No+Image'" 
              fit="cover"
              style="width: 100%; border-radius: 4px;"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="participants-container" v-loading="participantsLoading">
      <div slot="header" class="clearfix">
        <span>活动参与者</span>
        <el-button 
          style="float: right;" 
          size="mini" 
          type="primary" 
          icon="el-icon-refresh" 
          @click="fetchParticipants"
        >
          刷新列表
        </el-button>
      </div>

      <el-table
        :data="participants"
        border
        style="width: 100%"
        v-if="participants.length > 0"
      >
        <el-table-column label="用户ID" align="center" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.userId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用户名" min-width="120">
          <template slot-scope="scope">
            <div class="user-info">
              <el-avatar :src="scope.row.avatar" :size="30"></el-avatar>
              <span style="margin-left: 10px">{{ scope.row.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="报名时间" width="180" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.joinTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系方式" min-width="150" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.contact || '未提供' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleRemoveParticipant(scope.row)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="empty-data" v-else>
        <el-empty description="暂无参与者"></el-empty>
      </div>
    </el-card>

    <!-- 活动编辑弹窗 -->
    <el-dialog title="编辑活动" :visible.sync="dialogVisible" width="60%">
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="rules"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="editForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="editForm.location" />
        </el-form-item>
        <el-form-item label="活动开始时间" prop="startTime">
          <el-date-picker
            v-model="editForm.startTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="活动结束时间" prop="endTime">
          <el-date-picker
            v-model="editForm.endTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="人数限制" prop="maxParticipants">
          <el-input-number v-model="editForm.maxParticipants" :min="1" />
        </el-form-item>
        <el-form-item label="活动封面" prop="coverUrl">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="editForm.coverUrl" :src="formatImageUrl(editForm.coverUrl)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchActivity, updateActivity, changeActivityStatus, getActivityParticipants, removeParticipant } from '@/api/activity'

export default {
  name: 'ActivityDetail',
  data() {
    return {
      loading: true,
      participantsLoading: false,
      activity: {},
      participants: [],
      dialogVisible: false,
      editForm: {
        id: '',
        title: '',
        description: '',
        location: '',
        startTime: '',
        endTime: '',
        maxParticipants: 30,
        status: '',
        coverUrl: ''
      },
      rules: {
        title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        maxParticipants: [{ required: true, message: '请输入人数限制', trigger: 'blur' }],
        description: [{ required: true, message: '请输入活动描述', trigger: 'blur' }]
      },
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  computed: {
    statusType() {
      const statusMap = {
        'published': 'success',
        'draft': 'info',
        'finished': 'warning',
        'deleted': 'danger'
      }
      return statusMap[this.activity.status] || 'info'
    },
    statusText() {
      const statusMap = {
        'published': '已发布',
        'draft': '草稿',
        'finished': '已结束',
        'deleted': '已删除'
      }
      return statusMap[this.activity.status] || '未知状态'
    },
    formattedDescription() {
      return this.activity.description ? this.activity.description.replace(/\n/g, '<br>') : ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      const id = this.$route.params.id
      if (!id) {
        this.$message.error('缺少活动ID')
        this.goBack()
        return
      }
      
      fetchActivity(id).then(response => {
        console.log('获取到的活动详情数据:', response.data)
        // 将API返回的数据映射到页面所需的字段
        this.activity = {
          id: response.data.id,
          title: response.data.title,
          description: response.data.content, // 后端返回的是content字段
          location: response.data.location,
          startTime: response.data.startTime,
          endTime: response.data.endTime,
          participantCount: response.data.currentParticipants,
          maxParticipants: response.data.maxParticipants,
          status: response.data.status === true ? 'published' : 'finished', // 布尔值转为状态文本
          coverUrl: response.data.image, // 可能是image字段
          createTime: response.data.createTime
        }
        this.loading = false
        this.fetchParticipants()
      }).catch(error => {
        console.error('获取活动详情失败:', error)
        this.$message.error('获取活动详情失败')
        this.loading = false
      })
    },
    goBack() {
      this.$router.push({
        name: 'ActivityList'
      })
    },
    handleEdit() {
      this.editForm = {
        id: this.activity.id,
        title: this.activity.title,
        description: this.activity.description, // 存储为description
        location: this.activity.location,
        startTime: new Date(this.activity.startTime.replace(/-/g, '/')),
        endTime: new Date(this.activity.endTime.replace(/-/g, '/')),
        maxParticipants: this.activity.maxParticipants,
        status: this.activity.status === 'published',
        coverUrl: this.activity.coverUrl
      }
      this.dialogVisible = true
    },
    submitEdit() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          // 转换数据结构以匹配API期望的格式
          const submitData = {
            id: this.editForm.id,
            title: this.editForm.title,
            content: this.editForm.description, // description转为content提交
            location: this.editForm.location,
            startTime: this.formatTime(this.editForm.startTime),
            endTime: this.formatTime(this.editForm.endTime),
            maxParticipants: this.editForm.maxParticipants,
            status: this.editForm.status,
            image: this.editForm.coverUrl
          }
          
          updateActivity(submitData).then(() => {
            this.$notify({
              title: '成功',
              message: '活动更新成功',
              type: 'success',
              duration: 2000
            })
            this.dialogVisible = false
            this.fetchData()
          }).catch(error => {
            console.error('更新活动失败:', error)
            this.$message.error('更新活动失败')
          })
        }
      })
    },
    handleStatusChange() {
      const isPublished = this.activity.status === 'published'
      const action = isPublished ? '下架' : '发布'
      
      this.$confirm(`确定要${action}该活动吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 直接使用布尔值，而不是字符串状态
        const newStatus = !isPublished
        
        updateActivity({
          id: this.activity.id,
          status: newStatus
        }).then(() => {
          this.$notify({
            title: '成功',
            message: `活动${action}成功`,
            type: 'success',
            duration: 2000
          })
          this.fetchData()
        }).catch(error => {
          console.error(`活动${action}失败:`, error)
          this.$message.error(`活动${action}失败`)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    fetchParticipants() {
      this.participantsLoading = true
      getActivityParticipants(this.activity.id).then(response => {
        this.participants = response.data || []
        this.participantsLoading = false
      }).catch(error => {
        console.error('获取参与者列表失败:', error)
        this.$message.error('获取参与者列表失败')
        this.participantsLoading = false
      })
    },
    handleRemoveParticipant(participant) {
      this.$confirm('确定要将该用户从活动中移除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const data = {
          activityId: this.activity.id,
          userId: participant.userId
        }
        removeParticipant(data).then(() => {
          this.$notify({
            title: '成功',
            message: '移除成功',
            type: 'success',
            duration: 2000
          })
          this.fetchParticipants()
          this.fetchData() // 刷新活动信息
        }).catch(error => {
          console.error('移除参与者失败:', error)
          this.$message.error('移除参与者失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    handleUploadSuccess(res, file) {
      if (res.code === 200) {
        this.editForm.coverUrl = res.data.url
      } else {
        this.$message.error('上传失败: ' + res.message)
      }
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      
      return isImage && isLt2M
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      const seconds = date.getSeconds().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    // 格式化图片URL
    formatImageUrl(url) {
      if (!url) return '';
      
      // 处理特殊格式的URL（以@开头的时间戳格式）
      if (url.startsWith('@')) {
        return `http://localhost:8080/uploads/activities/${url.substring(1)}`;
      }
      
      // 确保URL是完整的
      if (url.startsWith('/uploads/')) {
        return `http://localhost:8080${url}`;
      }
      
      // 已经是完整URL或其他情况，直接返回
      return url;
    }
  }
}
</script>

<style lang="scss" scoped>
.title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .action-buttons {
    display: flex;
    gap: 10px;
  }
}

.activity-info {
  padding: 20px 0;
  
  .activity-title {
    font-size: 24px;
    margin-bottom: 20px;
    color: #303133;
  }
  
  .info-item {
    margin-bottom: 15px;
    
    .label {
      font-weight: bold;
      color: #606266;
      margin-right: 10px;
      display: inline-block;
      width: 80px;
    }
    
    .value {
      color: #333;
    }
    
    &.description {
      margin-top: 20px;
      
      .label {
        display: block;
        margin-bottom: 10px;
      }
      
      .value-block {
        background-color: #f5f7fa;
        padding: 15px;
        border-radius: 4px;
        line-height: 1.6;
      }
    }
  }
}

.activity-image {
  padding: 20px;
}

.participants-container {
  margin-top: 20px;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  
  &:hover {
    border-color: #409EFF;
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.empty-data {
  padding: 30px 0;
}
</style> 