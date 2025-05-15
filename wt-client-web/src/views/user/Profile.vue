/* eslint-disable */
<template>
  <div class="user-profile">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人信息</h2>
        </div>
      </template>
      
      <el-form :model="profileForm" :rules="rules" ref="profileFormRef" label-width="100px" class="profile-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="profileForm.username" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        
        <el-form-item label="性别">
          <el-radio-group v-model="profileForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="生日">
          <el-date-picker
            v-model="profileForm.birthday"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存修改</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api' // 导入封装好的API服务

export default {
  name: 'UserProfile',
  components: {
    Plus
  },
  setup() {
    const profileForm = reactive({
      id: '',
      username: '',
      nickname: '',
      phone: '',
      email: '',
      gender: 0,
      birthday: '',
      avatar: ''
    })
    
    const rules = {
      nickname: [
        { required: true, message: '请输入昵称', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }
    
    const profileFormRef = ref(null)
    
    const uploadHeaders = {
      Authorization: `Bearer ${localStorage.getItem('token')}`
    }
    
    const fetchUserProfile = async () => {
      try {
        const response = await api.user.getUserInfo() // 使用封装好的API获取用户信息
        if (response.code === 200) {
          const { id, username, nickname, phone, email, gender, birthday, avatar } = response.data
          Object.assign(profileForm, { id, username, nickname, phone, email, gender, birthday, avatar })
          console.log('获取到的用户信息:', profileForm)
        } else {
          ElMessage.error(response.message || '获取个人信息失败')
        }
      } catch (error) {
        console.error('获取个人信息出错:', error)
        ElMessage.error('网络错误，请稍后重试')
      }
    }
    
    const submitForm = async () => {
      if (!profileFormRef.value) return
      
      await profileFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            console.log('提交的用户信息:', profileForm)
            const response = await api.user.updateUserInfo(profileForm) // 使用封装好的API更新用户信息
            if (response.code === 200) {
              ElMessage.success('个人信息更新成功')
            } else {
              ElMessage.error(response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新个人信息出错:', error)
            if (error.response && error.response.data) {
              ElMessage.error(error.response.data.message || '更新失败')
            } else {
              ElMessage.error('网络错误，请稍后重试')
            }
          }
        } else {
          return false
        }
      })
    }
    
    const resetForm = () => {
      if (profileFormRef.value) {
        profileFormRef.value.resetFields()
        fetchUserProfile()
      }
    }
    
    const handleAvatarSuccess = (response) => {
      if (response.code === 200) {
        profileForm.avatar = response.data.url
        ElMessage.success('头像上传成功')
      } else {
        ElMessage.error(response.message || '上传失败')
      }
    }
    
    const beforeAvatarUpload = (file) => {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isJPG && !isPNG) {
        ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
      }
      return (isJPG || isPNG) && isLt2M
    }
    
    onMounted(() => {
      fetchUserProfile()
    })
    
    return {
      profileForm,
      rules,
      profileFormRef,
      uploadHeaders,
      submitForm,
      resetForm,
      handleAvatarSuccess,
      beforeAvatarUpload
    }
  }
}
</script>

<style scoped>
.user-profile {
  padding: 20px;
}

.profile-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}
</style> 