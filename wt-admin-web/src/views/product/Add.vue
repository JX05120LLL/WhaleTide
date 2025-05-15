<template>
  <div class="app-container">
    <div class="title-container">
      <h2>添加商品</h2>
    </div>

    <el-card>
      <el-form
        ref="productForm"
        :model="productForm"
        :rules="rules"
        label-position="top"
        class="product-form"
      >
        <el-row :gutter="20">
          <el-col :sm="24" :md="12">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="productForm.name" placeholder="请输入商品名称" />
            </el-form-item>
            
            <el-form-item label="商品分类" prop="categoryId">
              <el-select v-model="productForm.categoryId" placeholder="请选择商品分类" style="width: 100%">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="商品价格" prop="price">
                  <el-input-number
                    v-model="productForm.price"
                    :precision="2"
                    :step="0.1"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品库存" prop="stock">
                  <el-input-number
                    v-model="productForm.stock"
                    :precision="0"
                    :step="1"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="商品状态" prop="status">
              <el-switch
                v-model="productForm.status"
                active-text="上架"
                inactive-text="下架"
              />
            </el-form-item>
          </el-col>
          
          <el-col :sm="24" :md="12">
            <el-form-item label="商品图片" prop="image">
              <upload-image v-model="productForm.image" type="products"></upload-image>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="productForm.description"
            type="textarea"
            :rows="6"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">
            保存
          </el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { createProduct } from '@/api/product'
import { fetchCategoryList } from '@/api/category'
import UploadImage from '@/components/common/UploadImage'

export default {
  name: 'ProductAdd',
  components: {
    UploadImage
  },
  data() {
    return {
      categoryList: [], // 商品分类列表
      productForm: {
        name: '',
        description: '',
        price: 0,
        stock: 0,
        categoryId: '',
        image: '',
        status: true
      },
      rules: {
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入商品价格', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '请输入商品库存', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请输入商品分类', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入商品描述', trigger: 'blur' },
          { min: 10, max: 2000, message: '长度在 10 到 2000 个字符', trigger: 'blur' }
        ]
      },
      submitting: false
    }
  },
  methods: {
    // 获取商品分类列表
    getCategoryList() {
      this.loading = true
      fetchCategoryList()
        .then(response => {
          if (response.code === 200) {
            this.categoryList = response.data
          } else {
            this.$message.error('获取商品分类失败：' + response.message)
          }
        })
        .catch(error => {
          console.error('获取商品分类失败:', error)
          this.$message.error('获取商品分类失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    submitForm() {
      this.$refs.productForm.validate((valid) => {
        if (valid) {
          this.submitting = true
          
          // 创建一个新对象，避免直接修改表单对象
          const formData = { ...this.productForm }
          
          // 将状态从布尔值转换为整数
          formData.status = formData.status ? 1 : 0
          
          createProduct(formData)
            .then(response => {
              this.$notify({
                title: '成功',
                message: '商品创建成功',
                type: 'success',
                duration: 2000
              })
              
              // 跳转到商品详情页面
              this.$router.push({
                name: 'ProductDetail',
                params: { id: response.data.id }
              })
            })
            .catch(error => {
              console.error('创建商品失败:', error)
              this.$message.error('创建商品失败')
            })
            .finally(() => {
              this.submitting = false
            })
        } else {
          this.$message.error('请完善表单信息')
          return false
        }
      })
    },
    resetForm() {
      this.$refs.productForm.resetFields()
      this.productForm.image = ''
    },
    goBack() {
      this.$router.push({
        name: 'ProductList'
      })
    }
  },
  created() {
    // 获取商品分类列表
    this.getCategoryList()
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.title-container {
  margin-bottom: 20px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 500;
    color: #303133;
  }
}

.product-form {
  margin-top: 20px;
}
</style> 