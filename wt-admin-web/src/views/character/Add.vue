<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-position="top">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>添加动漫角色</span>
        </div>
        
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        
        <el-form-item label="所属作品" prop="anime">
          <el-input v-model="form.anime" placeholder="请输入所属作品名称"></el-input>
        </el-form-item>
        
        <el-form-item label="角色描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="4" placeholder="请输入角色描述"></el-input>
        </el-form-item>
        
        <el-form-item label="角色图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="form.image" :src="formatImageUrl(form.image)" class="preview-image">
            <div v-else class="upload-placeholder">
              <i class="el-icon-plus"></i>
              <div class="upload-text">点击上传图片</div>
            </div>
          </el-upload>
          <div class="image-tips">建议上传1:1比例的图片，最大不超过2MB</div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { createCharacter } from '@/api/character'

export default {
  name: 'CharacterAdd',
  data() {
    return {
      form: {
        name: '',
        anime: '',
        description: '',
        image: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在1到50个字符之间', trigger: 'blur' }
        ],
        anime: [
          { required: true, message: '请输入所属作品名称', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在1到100个字符之间', trigger: 'blur' }
        ],
        image: [
          // 移除required验证，允许图片为空
        ]
      },
      submitting: false,
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          
          // 创建要提交的数据对象，只包含后端期望的字段
          const formData = {
            name: this.form.name,
            description: this.form.description,
            image: this.form.image,
            status: this.form.status
          }
          
          createCharacter(formData)
            .then(response => {
              this.$notify({
                title: '成功',
                message: '角色添加成功',
                type: 'success',
                duration: 2000
              })
              
              // 跳转到列表页
              this.$router.push('/character/list')
            })
            .catch(error => {
              console.error('添加角色失败:', error)
              this.$notify({
                title: '错误',
                message: '添加角色失败: ' + (error.message || '未知错误'),
                type: 'error',
                duration: 3000
              })
            })
            .finally(() => {
              this.submitting = false
            })
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form.image = ''
    },
    goBack() {
      this.$router.push('/character/list')
    },
    handleAvatarSuccess(res, file) {
      if (res.code === 200 && res.data) {
        // 获取正确的图片URL路径
        const imageUrl = res.data.url || res.data.path;
        this.form.image = imageUrl;
        
        // 修改图像预览显示
        this.$nextTick(() => {
          const imageElement = document.querySelector('.preview-image');
          if (imageElement) {
            // 确保URL是完整的
            imageElement.src = this.formatImageUrl(imageUrl);
          }
        });
      } else {
        this.$message.error('上传失败: ' + (res.message || '未知错误'));
      }
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },
    formatImageUrl(url) {
      if (!url) return '';
      
      // 处理特殊格式的URL（以@开头的时间戳格式）
      if (url.startsWith('@')) {
        return `http://localhost:8080/uploads/characters/${url.substring(1)}`;
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

<style scoped>
.app-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.avatar-uploader {
  width: 200px;
  height: 200px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #8c939d;
  width: 100%;
  height: 100%;
}

.upload-placeholder i {
  font-size: 28px;
}

.upload-text {
  margin-top: 10px;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-tips {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
}
</style> 