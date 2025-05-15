<template>
  <div class="category-container">
    <div class="header">
      <h2>商品分类管理</h2>
      <el-button type="primary" @click="handleAdd">添加分类</el-button>
    </div>

    <el-table :data="categoryList" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑分类对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { MessageBox, Message } from 'element-ui'
import request from '@/utils/request'

export default {
  name: 'CategoryManagement',
  data() {
    return {
      loading: false,
      categoryList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        description: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 200, message: '描述不能超过200个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getCategoryList()
  },
  methods: {
    // 获取分类列表
    async getCategoryList() {
      try {
        this.loading = true
        const res = await request.get('/api/admin/category/list')
        if (res.code === 200) {
          this.categoryList = res.data
        }
      } catch (error) {
        console.error('获取分类列表失败:', error)
        Message.error('获取分类列表失败')
      } finally {
        this.loading = false
      }
    },

    // 添加分类
    handleAdd() {
      this.dialogTitle = '添加分类'
      this.form = {
        id: null,
        name: '',
        description: ''
      }
      this.dialogVisible = true
    },

    // 编辑分类
    handleEdit(row) {
      this.dialogTitle = '编辑分类'
      this.form = {
        id: row.id,
        name: row.name,
        description: row.description
      }
      this.dialogVisible = true
    },

    // 删除分类
    handleDelete(row) {
      MessageBox.confirm('确认删除该分类吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request.delete(`/api/admin/category/delete/${row.id}`)
          if (res.code === 200) {
            Message.success('删除成功')
            this.getCategoryList()
          }
        } catch (error) {
          console.error('删除分类失败:', error)
          Message.error('删除分类失败')
        }
      }).catch(() => {})
    },

    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const isEdit = this.form.id !== null
            const api = isEdit ? '/api/admin/category/update' : '/api/admin/category/add'
            
            // 创建要提交的数据对象
            let submitData = { ...this.form }
            
            // 如果是添加分类，则删除id字段
            if (!isEdit) {
              delete submitData.id
            }
            
            const res = await request[isEdit ? 'put' : 'post'](api, submitData)
            
            if (res.code === 200) {
              Message.success(isEdit ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.getCategoryList()
            }
          } catch (error) {
            console.error(isEdit ? '更新分类失败:' : '添加分类失败:', error)
            Message.error(isEdit ? '更新分类失败' : '添加分类失败')
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.category-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  font-size: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 