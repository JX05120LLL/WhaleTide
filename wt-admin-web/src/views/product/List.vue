<template>
  <div class="app-container">
    <!-- 过滤搜索区域 -->
    <div class="filter-container">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-input
            v-model="listQuery.keyword"
            placeholder="商品名称"
            clearable
            class="filter-item"
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select
            v-model="listQuery.status"
            placeholder="商品状态"
            clearable
            class="filter-item"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="12">
          <div class="button-group">
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
            >
              搜索
            </el-button>
            <el-button
              type="success"
              icon="el-icon-plus"
              @click="handleCreate"
            >
              添加商品
            </el-button>
            <el-button
              icon="el-icon-refresh"
              @click="refreshList"
            >
              刷新
            </el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 列表区域 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="加载中..."
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column
        label="ID"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="商品图片"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 80px"
            :src="formatImageUrl(scope.row.image)"
            fit="cover"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        label="商品名称"
        align="center"
        min-width="200"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="价格"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <span>￥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="库存"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.stock }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="状态"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-tag :type="scope.row.status ? 'success' : 'info'">
            {{ scope.row.status ? '上架中' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        min-width="200"
      >
        <template slot-scope="scope">
          <div class="action-buttons">
            <div class="button-row">
              <el-tooltip content="编辑商品" placement="top">
                <el-button
                  type="primary"
                  size="mini"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                >
                  编辑
                </el-button>
              </el-tooltip>
              <el-tooltip content="查看详情" placement="top">
                <el-button
                  type="info"
                  size="mini"
                  icon="el-icon-view"
                  @click="handleDetail(scope.row)"
                >
                  详情
                </el-button>
              </el-tooltip>
            </div>
            <div class="button-row">
              <el-tooltip :content="scope.row.status ? '下架商品' : '上架商品'" placement="top">
                <el-button
                  :type="scope.row.status ? 'warning' : 'success'"
                  size="mini"
                  :icon="scope.row.status ? 'el-icon-sold-out' : 'el-icon-shopping-cart-full'"
                  @click="handleStatusChange(scope.row)"
                >
                  {{ scope.row.status ? '下架' : '上架' }}
                </el-button>
              </el-tooltip>
              <el-tooltip content="删除商品" placement="top">
                <el-button
                  type="danger"
                  size="mini"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                >
                  删除
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 商品表单弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="temp.price" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number v-model="temp.stock" :precision="0" :step="1" :min="0" />
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select v-model="temp.categoryId" placeholder="请选择商品分类" style="width: 100%">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="temp.image" :src="temp.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchProductList, fetchAllProductList, createProduct, updateProduct, deleteProduct, updateProductStatus } from '@/api/product'
import { fetchCategoryList } from '@/api/category'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'ProductList',
  components: { Pagination },
  data() {
    return {
      list: [], // 商品列表
      total: 0, // 总数
      listLoading: true, // 加载状态
      categoryList: [], // 商品分类列表
      listQuery: { // 查询参数
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        status: undefined
      },
      // 状态选项
      statusOptions: [
        { value: true, label: '上架中' },
        { value: false, label: '已下架' }
      ],
      temp: {
        id: undefined,
        name: '',
        description: '',
        price: 0,
        stock: 0,
        categoryId: '',
        image: '',
        status: true
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑商品',
        create: '添加商品'
      },
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入商品描述', trigger: 'blur' }],
        price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }]
      },
      // 上传相关
      uploadHeaders: {
        Authorization: localStorage.getItem('token') ? `Bearer ${localStorage.getItem('token')}` : ''
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryList() // 获取商品分类列表
  },
  methods: {
    // 获取商品列表
    getList() {
      this.listLoading = true
      fetchAllProductList(this.listQuery).then(response => {
        const { data } = response
        this.list = data.list
        this.total = data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取商品列表失败')
      })
    },
    // 获取商品分类列表
    getCategoryList() {
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
    },
    // 处理搜索
    handleSearch() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // 刷新列表
    refreshList() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // 重置表单
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        description: '',
        price: 0,
        stock: 0,
        categoryId: '',
        image: '',
        status: true
      }
    },
    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.getCategoryList() // 获取最新分类列表
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 创建数据
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 创建一个新对象，避免直接修改表单对象
          const formData = { ...this.temp }
          
          // 将状态从布尔值转换为整数
          formData.status = formData.status ? 1 : 0
          
          createProduct(formData).then(response => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建商品成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.$message.error('创建商品失败')
          })
        }
      })
    },
    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // 复制对象
      this.getCategoryList() // 获取最新分类列表
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 创建一个新对象，只包含后端接受的字段
          const tempData = {
            id: this.temp.id,
            name: this.temp.name,
            description: this.temp.description,
            price: this.temp.price,
            stock: this.temp.stock,
            categoryId: this.temp.categoryId,
            image: this.temp.image,
            status: this.temp.status ? 1 : 0
          }
          
          // 如果有图片列表，也包含它
          if (this.temp.images && this.temp.images.length > 0) {
            tempData.images = this.temp.images;
          }
          
          updateProduct(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新商品成功',
              type: 'success',
              duration: 2000
            })
            // 更新列表中的数据
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
          }).catch(() => {
            this.$message.error('更新商品失败')
          })
        }
      })
    },
    // 处理商品详情
    handleDetail(row) {
      this.$router.push({
        name: 'ProductDetail',
        params: { id: row.id }
      })
    },
    // 处理删除商品
    handleDelete(row) {
      this.$confirm('确认删除该商品?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProduct(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除商品成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }).catch(() => {
          this.$message.error('删除商品失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 切换商品状态
    handleStatusChange(row) {
      const statusText = row.status ? '下架' : '上架'
      this.$confirm(`确认${statusText}该商品?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateProductStatus({
          id: row.id,
          status: row.status ? 0 : 1  // 转换为整数: true->0(下架), false->1(上架)
        }).then(() => {
          this.$notify({
            title: '成功',
            message: `商品${statusText}成功`,
            type: 'success',
            duration: 2000
          })
          // 更新本地状态
          row.status = !row.status
        }).catch(() => {
          this.$message.error(`商品${statusText}失败`)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    // 处理封面上传成功
    handleCoverSuccess(res, file) {
      if (res.code === 200) {
        this.temp.image = res.data.url
      } else {
        this.$message.error('上传失败: ' + res.message)
      }
    },
    // 封面上传前检查
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
    },
    // 格式化图片URL
    formatImageUrl(url) {
      if (!url) return '';
      
      // 处理特殊格式的URL（以@开头的时间戳格式）
      if (url.startsWith('@')) {
        return `http://localhost:8080/images/products/${url.substring(1)}`;
      }
      
      // 确保URL是完整的
      if (url.startsWith('/uploads/')) {
        return `http://localhost:8080/images/products${url.replace('/uploads/', '/')}`;
      }
      
      // 已经是完整URL或其他情况，直接返回
      return url;
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  background-color: #fff;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .filter-item {
    width: 100%;
    margin-bottom: 10px;
  }

  .button-group {
    display: flex;
    justify-content: flex-end;
    
    .el-button {
      margin-left: 10px;
    }
    
    @media (max-width: 768px) {
      justify-content: center;
      margin-top: 10px;
    }
  }
}

.action-buttons {
  display: flex;
  flex-direction: column;
  justify-content: center;
  
  .button-row {
    display: flex;
    justify-content: center;
    margin: 3px 0;
    
    .el-button {
      margin: 2px;
      min-width: 80px;
    }
  }
}

.pagination-container {
  margin-top: 20px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
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