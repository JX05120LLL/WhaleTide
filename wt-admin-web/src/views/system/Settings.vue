<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>系统设置</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">新增配置</el-button>
      </div>
      
      <el-table
        v-loading="listLoading"
        :data="settingsList"
        element-loading-text="加载中..."
        border
        fit
        highlight-current-row
      >
        <el-table-column label="ID" align="center" width="80">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="配置键" min-width="150px">
          <template slot-scope="scope">
            <span>{{ scope.row.configKey }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="配置值" min-width="200px">
          <template slot-scope="scope">
            <span>{{ scope.row.configValue }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="描述" min-width="200px">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="更新时间" width="160" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.updateTime }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="{row, $index}">
            <el-button 
              type="primary" 
              size="mini" 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="mini" 
              @click="handleDelete(row, $index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 设置表单弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="temp.configKey" :disabled="dialogStatus==='update'" />
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input v-model="temp.configValue" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSettings, createSetting, updateSetting, deleteSetting } from '@/api/system'

export default {
  name: 'SystemSettings',
  data() {
    return {
      settingsList: [],
      listLoading: true,
      temp: {
        id: undefined,
        configKey: '',
        configValue: '',
        description: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑配置',
        create: '新增配置'
      },
      rules: {
        configKey: [{ required: true, message: '配置键不能为空', trigger: 'blur' }],
        configValue: [{ required: true, message: '配置值不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getSettings().then(response => {
        this.settingsList = response.data
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取系统设置失败')
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        configKey: '',
        configValue: '',
        description: ''
      }
    },
    handleAdd() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createSetting(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '创建成功',
              type: 'success'
            })
            this.fetchData()
          }).catch(() => {
            this.$message.error('创建失败')
          })
        }
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateSetting(tempData).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '更新成功',
              type: 'success'
            })
            this.fetchData()
          }).catch(() => {
            this.$message.error('更新失败')
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('确定要删除这个配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSetting(row.id).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.settingsList.splice(index, 1)
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}
</style> 