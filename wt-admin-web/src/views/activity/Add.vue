<template>
  <div class="app-container">
    <el-card class="form-card">
      <div slot="header" class="card-header">
        <span>添加活动</span>
      </div>
      
      <el-form 
        ref="activityForm" 
        :model="activityForm" 
        :rules="rules" 
        label-width="120px" 
        class="activity-form"
      >
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="activityForm.title" placeholder="请输入活动名称"></el-input>
        </el-form-item>
        
        <el-form-item label="活动封面" prop="image">
          <upload-image v-model="activityForm.image" type="activities"></upload-image>
        </el-form-item>
        
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="activityForm.location" placeholder="请输入活动地点"></el-input>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="activityForm.startTime"
                type="datetime"
                placeholder="选择开始时间"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="activityForm.endTime"
                type="datetime"
                placeholder="选择结束时间"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="人数限制" prop="maxParticipants">
              <el-input-number 
                v-model="activityForm.maxParticipants" 
                :min="1" 
                :max="1000"
                style="width: 100%;"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动状态" prop="status">
              <el-select v-model="activityForm.status" placeholder="请选择状态" style="width: 100%;">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="活动描述" prop="content">
          <el-input 
            type="textarea" 
            v-model="activityForm.content" 
            :rows="6" 
            placeholder="请输入活动详细描述"
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">创建活动</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { createActivity } from '@/api/activity'
import { formatTime } from '@/utils'
import UploadImage from '@/components/common/UploadImage'

export default {
  name: 'ActivityAdd',
  components: {
    UploadImage
  },
  data() {
    // 验证结束时间大于开始时间
    const validateEndTime = (rule, value, callback) => {
      if (value && this.activityForm.startTime) {
        if (value <= this.activityForm.startTime) {
          callback(new Error('结束时间必须大于开始时间'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    
    return {
      activityForm: {
        title: '',
        content: '',
        location: '',
        startTime: new Date(new Date().getTime() + 3600000), // 默认1小时后开始
        endTime: new Date(new Date().getTime() + 7200000), // 默认2小时后结束
        maxParticipants: 30,
        status: 1, // 默认为进行中
        image: '',
      },
      statusOptions: [
        { value: 0, label: '已结束' },
        { value: 1, label: '进行中' },
        { value: 2, label: '未开始' }
      ],
      rules: {
        title: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入活动地点', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' },
          { validator: validateEndTime, trigger: 'change' }
        ],
        maxParticipants: [
          { required: true, message: '请设置人数限制', trigger: 'blur' },
          { type: 'number', message: '人数必须为数字值', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入活动描述', trigger: 'blur' },
          { min: 10, message: '描述不能少于10个字符', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择活动状态', trigger: 'change' }
        ]
      },
      submitting: false
    }
  },
  methods: {
    submitForm() {
      this.$refs.activityForm.validate((valid) => {
        if (valid) {
          this.submitting = true
          
          const activityData = {
            ...this.activityForm,
            startTime: formatTime(this.activityForm.startTime),
            endTime: formatTime(this.activityForm.endTime)
          }
          
          createActivity(activityData).then(response => {
            this.$notify({
              title: '成功',
              message: '活动创建成功',
              type: 'success',
              duration: 2000
            })
            
            setTimeout(() => {
              this.submitting = false
              this.$router.push({
                name: 'ActivityList'
              })
            }, 1000)
          }).catch(error => {
            console.error('创建活动失败:', error)
            this.submitting = false
            this.$notify.error({
              title: '失败',
              message: '创建活动失败: ' + (error.message || '未知错误'),
              duration: 3000
            })
          })
        }
      })
    },
    resetForm() {
      this.$refs.activityForm.resetFields()
    },
    goBack() {
      this.$router.push({
        name: 'ActivityList'
      })
    },
    handleCoverSuccess(res, file) {
      if (res.code === 200) {
        this.activityForm.image = res.data.url
      } else {
        this.$message.error('上传失败: ' + res.message)
      }
    },
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isImage) {
        this.$message.error('只能上传图片文件!')
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
      }
      
      return isImage && isLt2M
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.form-card {
  margin-bottom: 20px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.activity-form {
  margin-top: 20px;
  
  .el-input-number {
    width: 100%;
  }
}

.avatar-uploader {
  .el-upload {
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
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.el-tag {
  margin-right: 10px;
  margin-bottom: 5px;
}
</style> 