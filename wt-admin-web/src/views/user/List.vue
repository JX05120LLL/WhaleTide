<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="用户名/邮箱"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select 
        v-model="listQuery.status" 
        placeholder="用户状态" 
        clearable 
        style="width: 130px" 
        class="filter-item"
      >
        <el-option label="正常" value="normal" />
        <el-option label="禁用" value="disabled" />
      </el-select>
      <el-button 
        class="filter-item" 
        type="primary" 
        icon="el-icon-search" 
        @click="handleFilter"
      >
        搜索
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="加载中..."
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column label="ID" align="center" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="用户名" min-width="120px">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="头像" width="100" align="center">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar" :size="40">
            <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
          </el-avatar>
        </template>
      </el-table-column>
      
      <el-table-column label="邮箱" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="手机" width="140" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.phone }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="注册时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
            {{ row.status === 'normal' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button 
            size="mini" 
            :type="row.status === 'normal' ? 'danger' : 'success'" 
            @click="handleStatusChange(row)"
          >
            {{ row.status === 'normal' ? '禁用' : '启用' }}
          </el-button>
          <el-button 
            size="mini" 
            type="warning" 
            @click="handleResetPassword(row)"
          >
            重置密码
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 用户编辑弹窗 -->
    <el-dialog title="编辑用户" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" disabled />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="temp.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="temp.roleId" placeholder="请选择角色" class="filter-item">
            <el-option 
              v-for="item in roleOptions" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="temp.status" class="filter-item">
            <el-option label="正常" value="normal" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确认</el-button>
      </div>
    </el-dialog>

    <!-- 重置密码弹窗 -->
    <el-dialog title="重置密码" :visible.sync="resetPwdDialogVisible" width="30%">
      <el-form
        ref="pwdForm"
        :model="pwdForm"
        :rules="pwdRules"
        label-width="100px"
      >
        <el-form-item label="新密码" prop="password">
          <el-input
            v-model="pwdForm.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="pwdForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetPwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmResetPassword">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchUserList, updateUser, changeUserStatus, resetUserPassword } from '@/api/user'
import Pagination from '@/components/Pagination'
import request from '@/utils/request'

export default {
  name: 'UserList',
  components: { Pagination },
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.pwdForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: undefined,
        status: undefined
      },
      temp: {
        id: undefined,
        username: '',
        email: '',
        phone: '',
        roleId: undefined,
        status: 'normal'
      },
      dialogFormVisible: false,
      rules: {
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      resetPwdDialogVisible: false,
      currentUserId: null,
      pwdForm: {
        password: '',
        confirmPassword: ''
      },
      pwdRules: {
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      roleOptions: []
    }
  },
  created() {
    this.getList()
    this.getRoleOptions()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchUserList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取用户列表失败')
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      if (!this.temp.roleId) {
        this.temp.roleId = undefined
      }
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateUser(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$message({
              message: '更新成功',
              type: 'success'
            })
          }).catch(() => {
            this.$message.error('更新失败')
          })
        }
      })
    },
    handleStatusChange(row) {
      const statusText = row.status === 'normal' ? '禁用' : '启用'
      const newStatus = row.status === 'normal' ? 'disabled' : 'normal'
      
      this.$confirm(`确认要${statusText}该用户吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        changeUserStatus({
          userId: row.id,
          status: newStatus
        }).then(() => {
          this.$message({
            message: `${statusText}成功`,
            type: 'success'
          })
          row.status = newStatus
        }).catch(() => {
          this.$message.error(`${statusText}失败`)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    handleResetPassword(row) {
      this.currentUserId = row.id
      this.pwdForm = {
        password: '',
        confirmPassword: ''
      }
      this.resetPwdDialogVisible = true
      this.$nextTick(() => {
        this.$refs['pwdForm'].clearValidate()
      })
    },
    confirmResetPassword() {
      this.$refs['pwdForm'].validate((valid) => {
        if (valid) {
          resetUserPassword({
            userId: this.currentUserId,
            newPassword: this.pwdForm.password
          }).then(() => {
            this.resetPwdDialogVisible = false
            this.$message({
              message: '密码重置成功',
              type: 'success'
            })
          }).catch(() => {
            this.$message.error('密码重置失败')
          })
        }
      })
    },
    getRoleOptions() {
      request.get('/api/role/list').then(response => {
        if (response.code === 200) {
          this.roleOptions = response.data
        }
      }).catch(error => {
        console.error('获取角色列表失败:', error)
        this.$message.error('获取角色列表失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  padding-bottom: 10px;
  
  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}
</style> 