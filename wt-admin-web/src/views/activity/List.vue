<template>
  <div class="app-container">
    <div class="filter-container">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-input
            v-model="listQuery.keyword"
            placeholder="活动名称"
            clearable
            class="filter-item"
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select
            v-model="listQuery.status"
            placeholder="状态"
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
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">
              搜索
            </el-button>
            <el-tooltip content="创建新活动" placement="top">
              <el-button type="success" icon="el-icon-plus" @click="handleCreate">
                新增活动
              </el-button>
            </el-tooltip>
            <el-tooltip content="刷新活动列表" placement="top">
              <el-button type="info" icon="el-icon-refresh" @click="getList">
                刷新
              </el-button>
            </el-tooltip>
          </div>
        </el-col>
      </el-row>
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
      
      <el-table-column label="活动名称" min-width="150px">
        <template slot-scope="{row}">
          <router-link :to="{ name: 'ActivityDetail', params: { id: row.id } }" class="link-type">
            <span>{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column>
      
      <el-table-column label="地点" align="center" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.location }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="开始时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="结束时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="报名人数" align="center" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.currentParticipants }}/{{ scope.row.maxParticipants || '不限' }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.statusDesc || (row.status | statusTextFilter) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" align="center" min-width="200">
        <template slot-scope="scope">
          <div class="action-buttons">
            <div class="button-row">
              <el-tooltip content="编辑活动" placement="top">
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
              <el-tooltip content="管理参与者" placement="top">
                <el-button
                  type="success"
                  size="mini"
                  icon="el-icon-user"
                  @click="handleParticipants(scope.row)"
                >
                  参与者
                </el-button>
              </el-tooltip>
              <el-tooltip content="删除活动" placement="top">
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

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 活动表单弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="活动内容" prop="content">
          <el-input v-model="temp.content" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="temp.location" />
        </el-form-item>
        <el-form-item label="活动开始时间" prop="startTime">
          <el-date-picker
            v-model="temp.startTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="活动结束时间" prop="endTime">
          <el-date-picker
            v-model="temp.endTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="人数限制" prop="maxParticipants">
          <el-input-number v-model="temp.maxParticipants" :min="1" />
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload">
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

    <!-- 参与者弹窗 -->
    <el-dialog title="活动参与者" :visible.sync="participantsDialogVisible" width="800px">
      <el-table 
        :data="participants" 
        v-loading="participantsLoading" 
        border 
        fit 
        highlight-current-row
      >
        <el-table-column label="用户ID" align="center" width="80">
          <template slot-scope="scope">
            <span>{{ scope.row.userId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用户名" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报名时间" align="center" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.joinTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系方式" align="center" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.contact || '未提供' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleRemoveParticipant(scope.row)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
        <el-button @click="participantsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchActivity, createActivity, updateActivity, deleteActivity, getActivityParticipants, removeParticipant } from '@/api/activity'
import Pagination from '@/components/Pagination'
import { parseTime, formatTime } from '@/utils'

export default {
  name: 'ActivityList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'danger',   // 已结束
        1: 'success',  // 进行中
        2: 'warning'   // 未开始
      }
      return statusMap[status] || 'info'
    },
    statusTextFilter(status) {
      const statusMap = {
        0: '已结束',
        1: '进行中',
        2: '未开始'
      }
      return statusMap[status] || '未知'
    }
  },
  data() {
    return {
      list: [], // 活动列表
      total: 0, // 总数
      listLoading: true, // 加载状态
      listQuery: { // 查询参数
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        status: undefined
      },
      // 活动状态选项
      statusOptions: [
        { value: false, label: '已结束' },
        { value: true, label: '进行中' },
        { value: 2, label: '未开始' }
      ],
      temp: {
        id: undefined,
        title: '',
        content: '',
        location: '',
        startTime: '',
        endTime: '',
        image: '',
        maxParticipants: 30,
        status: true
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑活动',
        create: '添加活动'
      },
      rules: {
        title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        content: [{ required: true, message: '请输入活动内容', trigger: 'blur' }],
        location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
      },
      // 参与者相关
      participantsDialogVisible: false,
      participants: [],
      participantsLoading: false,
      currentActivityId: null,
      // 上传相关
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取活动列表
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        const { data } = response
        this.list = data.records
        this.total = data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
        this.$message.error('获取活动列表失败')
      })
    },
    // 处理搜索
    handleSearch() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // 重置表单
    resetTemp() {
      this.temp = {
        id: undefined,
        title: '',
        content: '',
        location: '',
        startTime: new Date(new Date().getTime() + 3600 * 1000), // 默认一小时后
        endTime: new Date(new Date().getTime() + 7200 * 1000),   // 默认两小时后
        image: '',
        maxParticipants: 30,
        status: true
      }
    },
    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
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
          // 格式化时间
          const tempData = Object.assign({}, this.temp)
          if (tempData.startTime) {
            tempData.startTime = formatTime(tempData.startTime)
          }
          if (tempData.endTime) {
            tempData.endTime = formatTime(tempData.endTime)
          }
          
          createActivity(tempData).then(response => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建活动成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // 复制对象
      
      // 转换字符串时间为Date对象
      if (typeof this.temp.startTime === 'string') {
        this.temp.startTime = new Date(this.temp.startTime.replace(/-/g, '/'))
      }
      if (typeof this.temp.endTime === 'string') {
        this.temp.endTime = new Date(this.temp.endTime.replace(/-/g, '/'))
      }
      
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
          // 创建一个新对象，只包含后端需要的字段
          const tempData = {
            id: this.temp.id,
            title: this.temp.title,
            content: this.temp.content,
            location: this.temp.location,
            image: this.temp.image,
            maxParticipants: this.temp.maxParticipants
          }
          
          // 格式化时间
          if (this.temp.startTime) {
            tempData.startTime = formatTime(this.temp.startTime)
          }
          if (this.temp.endTime) {
            tempData.endTime = formatTime(this.temp.endTime)
          }
          
          // 确保status是布尔类型
          if (this.temp.status === 0 || this.temp.status === '0') {
            tempData.status = false
          } else if (this.temp.status === 1 || this.temp.status === '1') {
            tempData.status = true
          } else if (this.temp.status === 2 || this.temp.status === '2') {
            // 将"未开始"状态映射为"进行中"
            tempData.status = true
          } else {
            tempData.status = !!this.temp.status
          }
          
          updateActivity(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新活动成功',
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
          })
        }
      })
    },
    // 处理活动详情
    handleDetail(row) {
      this.$router.push({
        name: 'ActivityDetail',
        params: { id: row.id }
      })
    },
    // 处理删除活动
    handleDelete(row) {
      this.$confirm('确认删除该活动?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteActivity(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除活动成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 查看参与者
    handleParticipants(row) {
      this.currentActivityId = row.id
      this.participantsLoading = true
      this.participantsDialogVisible = true
      this.participants = []
      
      getActivityParticipants(row.id).then(response => {
        this.participants = response.data.records || []
        this.participantsLoading = false
      }).catch(() => {
        this.participantsLoading = false
        this.$message.error('获取参与者列表失败')
      })
    },
    // 移除参与者
    handleRemoveParticipant(participant) {
      this.$confirm('确认移除该参与者?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeParticipant({
          activityId: this.currentActivityId,
          userId: participant.userId
        }).then(() => {
          this.$notify({
            title: '成功',
            message: '移除参与者成功',
            type: 'success',
            duration: 2000
          })
          // 从列表中移除
          const index = this.participants.indexOf(participant)
          this.participants.splice(index, 1)
          // 刷新活动列表以更新人数
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消移除'
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
        return `http://localhost:8080/uploads/activities/${url.substring(1)}`;
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
    justify-content: flex-start;
    flex-wrap: wrap;
    
    @media (max-width: 767px) {
      justify-content: space-between;
    }
    
    .el-button {
      margin-right: 10px;
      margin-bottom: 10px;
      
      @media (max-width: 767px) {
        flex: 1;
        min-width: calc(50% - 10px);
      }
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

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 