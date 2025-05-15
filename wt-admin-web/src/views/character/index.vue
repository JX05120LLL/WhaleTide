<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="角色名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="角色名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="作品名称" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.anime }}
        </template>
      </el-table-column>
      <el-table-column label="图片" width="110" align="center">
        <template slot-scope="scope">
          <el-image 
            style="width: 60px; height: 60px"
            :src="formatImageUrl(scope.row.image)" 
            :preview-src-list="[formatImageUrl(scope.row.image)]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="作品名称" prop="anime">
          <el-input v-model="temp.anime" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="temp.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            :show-file-list="false"
            :on-success="handleImageSuccess"
            :before-upload="beforeImageUpload">
            <img v-if="temp.image" :src="formatImageUrl(temp.image)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchCharacterList, createCharacter, updateCharacter, deleteCharacter } from '@/api/character'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'CharacterList',
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: undefined
      },
      temp: {
        id: undefined,
        name: '',
        anime: '',
        description: '',
        image: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '角色名称是必填项', trigger: 'blur' }],
        anime: [{ required: true, message: '作品名称是必填项', trigger: 'blur' }],
        image: []
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 构建查询参数
      const params = {
        page: this.listQuery.page,
        pageSize: this.listQuery.limit,
        search: this.listQuery.name
      }
      
      fetchCharacterList(params)
        .then(response => {
          // 适应不同的后端响应格式
          if (response.data && response.data.records) {
            this.list = response.data.records
            this.total = response.data.total
          } else if (response.data && response.data.items) {
            this.list = response.data.items
            this.total = response.data.total
          } else if (Array.isArray(response.data)) {
            this.list = response.data
            this.total = response.data.length
          } else {
            console.error('未知的响应格式:', response)
            this.list = []
            this.total = 0
          }
          this.listLoading = false
        })
        .catch(error => {
          console.error('获取角色列表失败:', error)
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        anime: '',
        description: '',
        image: ''
      }
    },
    handleCreate() {
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
          // 创建要提交的数据对象，只包含后端期望的字段
          const formData = {
            name: this.temp.name,
            description: this.temp.description,
            image: this.temp.image,
            status: this.temp.status
          }
          
          createCharacter(formData)
            .then(response => {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            })
            .catch(error => {
              console.error('创建角色失败:', error)
              this.$notify({
                title: '错误',
                message: '创建失败: ' + (error.message || '未知错误'),
                type: 'error',
                duration: 3000
              })
            })
        }
      })
    },
    handleUpdate(row) {
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
          // 创建要提交的数据对象，只包含后端期望的字段
          const tempData = {
            id: this.temp.id,  // 更新时需要保留id字段
            name: this.temp.name,
            description: this.temp.description,
            image: this.temp.image,
            status: this.temp.status
          }
          
          updateCharacter(tempData.id, tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该角色?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCharacter(row.id)
          .then(response => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
          .catch(error => {
            console.error('删除角色失败:', error)
            this.$notify({
              title: '错误',
              message: '删除失败: ' + (error.message || '未知错误'),
              type: 'error',
              duration: 3000
            })
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleImageSuccess(res, file) {
      this.temp.image = URL.createObjectURL(file.raw)
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
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

<style>
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