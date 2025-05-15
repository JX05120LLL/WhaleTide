<template>
  <div class="role-container">
    <div class="header">
      <h2>角色管理</h2>
      <el-button type="primary" @click="handleAdd">添加角色</el-button>
    </div>

    <el-table :data="roleList" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="角色名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status ? 'success' : 'danger'">
            {{ scope.row.status ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑角色对话框 -->
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            active-text="正常"
            inactive-text="禁用"
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
  name: 'RoleManagement',
  data() {
    return {
      loading: false,
      roleList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        description: '',
        status: true
      },
      rules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 200, message: '描述不能超过200个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getRoleList()
  },
  methods: {
    // 获取角色列表
    async getRoleList() {
      try {
        this.loading = true
        const res = await request.get('/api/admin/role/list')
        if (res.code === 200) {
          this.roleList = res.data
        }
      } catch (error) {
        console.error('获取角色列表失败:', error)
        Message.error('获取角色列表失败')
      } finally {
        this.loading = false
      }
    },

    // 添加角色
    handleAdd() {
      this.dialogTitle = '添加角色'
      this.form = {
        id: null,
        name: '',
        description: '',
        status: true
      }
      this.dialogVisible = true
    },

    // 编辑角色
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = {
        id: row.id,
        name: row.name,
        description: row.description,
        status: row.status
      }
      this.dialogVisible = true
    },

    // 删除角色
    handleDelete(row) {
      MessageBox.confirm('确认删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request.delete(`/api/admin/role/delete/${row.id}`)
          if (res.code === 200) {
            Message.success('删除成功')
            this.getRoleList()
          }
        } catch (error) {
          console.error('删除角色失败:', error)
          Message.error('删除角色失败')
        }
      }).catch(() => {})
    },

    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const isEdit = this.form.id !== null
            const api = isEdit ? '/api/admin/role/update' : '/api/admin/role/add'
            const res = await request[isEdit ? 'put' : 'post'](api, this.form)
            
            if (res.code === 200) {
              Message.success(isEdit ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.getRoleList()
            }
          } catch (error) {
            console.error(isEdit ? '更新角色失败:' : '添加角色失败:', error)
            Message.error(isEdit ? '更新角色失败' : '添加角色失败')
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.role-container {
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