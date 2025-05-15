<template>
  <div class="app-container">
    <div class="title-container">
      <el-page-header @back="goBack" :content="product.name || '商品详情'" />
      <div class="action-buttons">
        <el-button 
          type="primary" 
          icon="el-icon-edit" 
          @click="handleEdit"
        >
          编辑
        </el-button>
        <el-button 
          :type="product.status ? 'warning' : 'success'" 
          :icon="product.status ? 'el-icon-sold-out' : 'el-icon-shopping-cart-full'" 
          @click="handleStatusChange"
        >
          {{ product.status ? '下架' : '上架' }}
        </el-button>
      </div>
    </div>

    <el-card v-loading="loading">
      <el-row :gutter="20" v-if="!loading">
        <el-col :span="16">
          <div class="product-info">
            <h2 class="product-title">{{ product.name }}</h2>
            <div class="info-item">
              <span class="label">商品价格:</span>
              <span class="value price">￥{{ product.price }}</span>
            </div>
            <div class="info-item">
              <span class="label">商品分类:</span>
              <span class="value">{{ product.category }}</span>
            </div>
            <div class="info-item">
              <span class="label">库存状态:</span>
              <span class="value">{{ product.stock }} 件</span>
            </div>
            <div class="info-item">
              <span class="label">状态:</span>
              <el-tag :type="product.status ? 'success' : 'info'">
                {{ product.status ? '上架中' : '已下架' }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="label">创建时间:</span>
              <span class="value">{{ product.createTime }}</span>
            </div>
            <div class="info-item description">
              <span class="label">商品描述:</span>
              <div class="value-block" v-html="formattedDescription"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="product-image">
            <el-image 
              :src="formatImageUrl(product.image) || 'https://via.placeholder.com/400x300?text=No+Image'" 
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

    <!-- 商品数据分析 -->
    <el-card class="data-container" v-loading="dataLoading">
      <div slot="header" class="clearfix">
        <span>商品数据分析</span>
      </div>

      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="data-item">
            <div class="data-title">库存比例</div>
            <div class="data-value">{{ stockPercentage }}%</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="data-item">
            <div class="data-title">在线天数</div>
            <div class="data-value">{{ onlineDays }} 天</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 商品编辑弹窗 -->
    <el-dialog title="编辑商品" :visible.sync="dialogVisible" width="60%">
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="rules"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="editForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-input v-model="editForm.category" />
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="editForm.price" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number v-model="editForm.stock" :precision="0" :step="1" :min="0" />
        </el-form-item>
        <el-form-item label="商品状态" prop="status">
          <el-switch
            v-model="editForm.status"
            active-text="上架"
            inactive-text="下架"
          />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="editForm.image" :src="editForm.image" class="avatar">
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
import { fetchProductDetail, updateProduct, updateProductStatus } from '@/api/product'

export default {
  name: 'ProductDetail',
  data() {
    return {
      loading: true,
      dataLoading: true,
      product: {},
      dialogVisible: false,
      editForm: {
        id: '',
        name: '',
        description: '',
        category: '',
        price: 0,
        stock: 0,
        status: true,
        image: ''
      },
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入商品描述', trigger: 'blur' }],
        price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }],
        category: [{ required: true, message: '请输入商品分类', trigger: 'blur' }]
      },
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  computed: {
    formattedDescription() {
      return this.product.description ? this.product.description.replace(/\n/g, '<br>') : ''
    },
    stockPercentage() {
      if (!this.product.stock || !this.product.initialStock) {
        return 0
      }
      return Math.round((this.product.stock / this.product.initialStock) * 100)
    },
    onlineDays() {
      if (!this.product.createTime) {
        return 0
      }
      const createDate = new Date(this.product.createTime)
      const today = new Date()
      const diffTime = Math.abs(today - createDate)
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.dataLoading = true
      const id = this.$route.params.id
      if (!id) {
        this.$message.error('缺少商品ID')
        this.goBack()
        return
      }
      
      fetchProductDetail(id).then(response => {
        console.log('获取到的商品详情数据:', response.data)
        this.product = response.data
        this.loading = false
        this.dataLoading = false
      }).catch(error => {
        console.error('获取商品详情失败:', error)
        this.$message.error('获取商品详情失败')
        this.loading = false
        this.dataLoading = false
      })
    },
    goBack() {
      this.$router.push({
        name: 'ProductList'
      })
    },
    handleEdit() {
      this.editForm = {
        id: this.product.id,
        name: this.product.name,
        description: this.product.description,
        category: this.product.category,
        price: this.product.price,
        stock: this.product.stock,
        status: this.product.status,
        image: this.product.image
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['editForm'].clearValidate()
      })
    },
    submitEdit() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          updateProduct(this.editForm).then(() => {
            this.$notify({
              title: '成功',
              message: '商品更新成功',
              type: 'success',
              duration: 2000
            })
            this.dialogVisible = false
            this.fetchData()
          }).catch(error => {
            console.error('更新商品失败:', error)
            this.$message.error('更新商品失败')
          })
        }
      })
    },
    handleStatusChange() {
      const statusText = this.product.status ? '下架' : '上架'
      
      this.$confirm(`确定要${statusText}该商品吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateProductStatus({
          id: this.product.id,
          status: this.product.status ? 0 : 1
        }).then(() => {
          this.$notify({
            title: '成功',
            message: `商品${statusText}成功`,
            type: 'success',
            duration: 2000
          })
          this.fetchData()
        }).catch(error => {
          console.error(`商品${statusText}失败:`, error)
          this.$message.error(`商品${statusText}失败`)
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
        this.editForm.image = res.data.url
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
    // 格式化图片URL
    formatImageUrl(url) {
      if (!url) return '';
      
      // 处理特殊格式的URL（以@开头的时间戳格式）
      if (url.startsWith('@')) {
        return `http://localhost:8080/uploads/products/${url.substring(1)}`;
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

.product-info {
  padding: 20px 0;
  
  .product-title {
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
      
      &.price {
        color: #f56c6c;
        font-weight: bold;
        font-size: 18px;
      }
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

.product-image {
  padding: 20px;
}

.data-container {
  margin-top: 20px;
  
  .data-item {
    background-color: #f5f7fa;
    padding: 20px;
    text-align: center;
    border-radius: 4px;
    margin-bottom: 15px;
    
    .data-title {
      font-size: 14px;
      color: #606266;
      margin-bottom: 10px;
    }
    
    .data-value {
      font-size: 24px;
      font-weight: bold;
      color: #409EFF;
    }
  }
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
</style> 