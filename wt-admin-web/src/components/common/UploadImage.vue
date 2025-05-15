<template>
  <div class="upload-container">
    <el-upload
      class="image-uploader"
      :action="uploadUrl"
      :headers="headers"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      :data="uploadData"
    >
      <img v-if="imageUrl" :src="imageUrl" class="preview-image" />
      <div v-else class="upload-placeholder">
        <i class="el-icon-plus"></i>
        <div class="upload-text">点击上传图片</div>
      </div>
    </el-upload>
    <div class="upload-tip">
      <span>支持jpg、png格式，大小不超过5MB</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UploadImage',
  props: {
    value: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'common' // 可选: products, activities, characters
    }
  },
  data() {
    return {
      uploadUrl: `http://localhost:8082/api/file/upload?type=${this.type}`,
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      },
      uploadData: {}
    }
  },
  computed: {
    imageUrl() {
      if (!this.value) return '';
      
      if (this.value.startsWith('@')) {
        return `http://localhost:8082/uploads/products/${this.value.substring(1)}`;
      }
      
      if (this.value.startsWith('/uploads/')) {
        return `http://localhost:8082${this.value}`;
      }
      
      return this.value;
    }
  },
  methods: {
    // 上传前验证
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt5M = file.size / 1024 / 1024 < 5;
      
      if (!isImage) {
        this.$message.error('只能上传图片文件!');
        return false;
      }
      
      if (!isLt5M) {
        this.$message.error('图片大小不能超过5MB!');
        return false;
      }
      
      return true;
    },
    
    // 上传成功回调
    handleSuccess(response) {
      if (response.code === 200) {
        const url = response.data.url || response.data.path;
        this.$emit('input', url);
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.message || '上传失败');
      }
    },
    
    // 上传失败回调
    handleError() {
      this.$message.error('上传失败，请重试');
    }
  }
};
</script>

<style lang="scss" scoped>
.upload-container {
  width: 100%;
}

.image-uploader {
  width: 100%;
  
  ::v-deep .el-upload {
    width: 100%;
    cursor: pointer;
  }
}

.upload-placeholder {
  width: 100%;
  height: 250px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  background-color: #f8f8f8;
  
  &:hover {
    border-color: #409EFF;
    background-color: #f1f8ff;
  }
  
  i {
    font-size: 28px;
    color: #8c939d;
    margin-bottom: 10px;
  }
  
  .upload-text {
    font-size: 14px;
    color: #606266;
  }
}

.preview-image {
  width: 100%;
  height: 250px;
  object-fit: contain;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
  text-align: center;
}
</style> 