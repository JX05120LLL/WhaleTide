<template>
  <div class="app-container">
    <div v-loading="loading" class="character-detail-container">
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span>角色信息</span>
          <el-button 
            style="float: right; margin-left: 10px" 
            type="primary" 
            size="small" 
            @click="goBack"
          >
            返回列表
          </el-button>
          <el-button 
            style="float: right;" 
            type="success" 
            size="small" 
            @click="handleEdit"
          >
            编辑角色
          </el-button>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="character-avatar">
              <el-image 
                :src="character.avatarUrl" 
                fit="cover"
                style="width: 100%; height: 100%"
                :preview-src-list="[character.avatarUrl]"
              />
            </div>
          </el-col>
          <el-col :span="16">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="角色名称">{{ character.name }}</el-descriptions-item>
              <el-descriptions-item label="所属动漫">{{ character.anime }}</el-descriptions-item>
              <el-descriptions-item label="角色类型">{{ character.type }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="character.status === 'active' ? 'success' : 'info'">
                  {{ character.status === 'active' ? '启用' : '禁用' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="性别">{{ character.gender === 'male' ? '男' : '女' }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ character.age }}</el-descriptions-item>
              <el-descriptions-item label="作品">{{ character.from }}</el-descriptions-item>
              <el-descriptions-item label="热度">
                <el-rate 
                  v-model="character.popularity" 
                  disabled 
                  text-color="#ff9900"
                />
              </el-descriptions-item>
              <el-descriptions-item label="创建时间" :span="2">{{ character.createdAt }}</el-descriptions-item>
              <el-descriptions-item label="最近更新" :span="2">{{ character.updatedAt }}</el-descriptions-item>
              <el-descriptions-item label="角色介绍" :span="2">
                <div class="character-description">{{ character.description }}</div>
              </el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
      </el-card>
      
      <el-card class="box-card" shadow="never" style="margin-top: 20px">
        <div slot="header" class="clearfix">
          <span>相关产品</span>
        </div>
        <el-table
          :data="relatedProducts"
          border
          style="width: 100%"
        >
          <el-table-column label="商品图片" width="100" align="center">
            <template slot-scope="scope">
              <el-image 
                style="width: 60px; height: 60px"
                :src="scope.row.imageUrl" 
                :preview-src-list="[scope.row.imageUrl]"
                fit="cover"
              />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称" min-width="200">
            <template slot-scope="scope">
              <router-link :to="'/product/detail/'+scope.row.id" class="link-type">
                <span>{{ scope.row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120" align="center">
            <template slot-scope="scope">
              <span class="price">¥ {{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="100" align="center" />
          <el-table-column prop="sales" label="销量" width="100" align="center" />
          <el-table-column label="状态" class-name="status-col" width="100">
            <template slot-scope="{row}">
              <el-tag :type="row.status | statusFilter">
                {{ row.status | statusTextFilter }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <el-card class="box-card" shadow="never" style="margin-top: 20px">
        <div slot="header" class="clearfix">
          <span>相关活动</span>
        </div>
        <el-table
          :data="relatedActivities"
          border
          style="width: 100%"
        >
          <el-table-column label="活动图片" width="100" align="center">
            <template slot-scope="scope">
              <el-image 
                style="width: 60px; height: 60px"
                :src="scope.row.coverUrl" 
                :preview-src-list="[scope.row.coverUrl]"
                fit="cover"
              />
            </template>
          </el-table-column>
          <el-table-column prop="title" label="活动名称" min-width="200">
            <template slot-scope="scope">
              <router-link :to="'/activity/detail/'+scope.row.id" class="link-type">
                <span>{{ scope.row.title }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="150" align="center" />
          <el-table-column prop="endTime" label="结束时间" width="150" align="center" />
          <el-table-column label="状态" class-name="status-col" width="100">
            <template slot-scope="{row}">
              <el-tag :type="row.status | activityStatusFilter">
                {{ row.status | activityStatusTextFilter }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 角色编辑弹窗 -->
    <el-dialog title="编辑角色" :visible.sync="dialogFormVisible" width="60%">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="temp.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="temp.age" :min="0" />
        </el-form-item>
        <el-form-item label="作品" prop="from">
          <el-input v-model="temp.from" />
        </el-form-item>
        <el-form-item label="角色头像" prop="avatarUrl">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="热度" prop="popularity">
          <el-rate v-model="temp.popularity" :max="5" />
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色介绍" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="6" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchCharacter, updateCharacter } from '@/api/character'

export default {
  name: 'CharacterDetail',
  filters: {
    statusFilter(status) {
      const statusMap = {
        on_sale: 'success',
        off_sale: 'info',
        sold_out: 'danger'
      }
      return statusMap[status]
    },
    statusTextFilter(status) {
      const statusMap = {
        on_sale: '上架',
        off_sale: '下架',
        sold_out: '售罄'
      }
      return statusMap[status]
    },
    activityStatusFilter(status) {
      const statusMap = {
        upcoming: 'info',
        ongoing: 'success',
        finished: 'warning',
        cancelled: 'danger'
      }
      return statusMap[status]
    },
    activityStatusTextFilter(status) {
      const statusMap = {
        upcoming: '未开始',
        ongoing: '进行中',
        finished: '已结束',
        cancelled: '已取消'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      loading: true,
      characterId: null,
      character: {
        id: null,
        name: '',
        gender: 'male',
        age: 0,
        from: '',
        avatarUrl: '',
        popularity: 0,
        status: 'active',
        description: '',
        createdAt: '',
        updatedAt: ''
      },
      relatedProducts: [],
      relatedActivities: [],
      dialogFormVisible: false,
      temp: {
        id: undefined,
        name: '',
        gender: 'male',
        age: 18,
        from: '',
        avatarUrl: '',
        popularity: 3,
        status: 'active',
        description: ''
      },
      fileList: [],
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }],
        gender: [{ required: true, message: '性别不能为空', trigger: 'change' }],
        age: [{ required: true, message: '年龄不能为空', trigger: 'blur' }],
        from: [{ required: true, message: '作品不能为空', trigger: 'blur' }],
        description: [{ required: true, message: '角色介绍不能为空', trigger: 'blur' }]
      },
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  created() {
    this.characterId = this.$route.params.id
    this.getCharacterDetail()
  },
  methods: {
    getCharacterDetail() {
      this.loading = true
      fetchCharacter(this.characterId).then(response => {
        this.character = response.data
        this.relatedProducts = response.data.products || []
        this.relatedActivities = response.data.activities || []
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取角色详情失败')
      })
    },
    goBack() {
      this.$router.push('/character/list')
    },
    handleEdit() {
      this.temp = Object.assign({}, this.character)
      this.fileList = this.temp.avatarUrl ? [{ name: '角色头像', url: this.temp.avatarUrl }] : []
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCharacter(tempData.id, tempData).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '更新成功',
              type: 'success'
            })
            this.getCharacterDetail()
          }).catch(() => {
            this.$message.error('更新失败')
          })
        }
      })
    },
    handleAvatarSuccess(response, file) {
      this.temp.avatarUrl = response.data.url
      this.$message.success('上传成功')
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.character-detail-container {
  .box-card {
    margin-bottom: 20px;
  }
}

.character-avatar {
  width: 100%;
  height: 300px;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 20px;
}

.character-description {
  line-height: 1.8;
  white-space: pre-wrap;
}

.link-type {
  color: #409EFF;
  text-decoration: none;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}
</style> 