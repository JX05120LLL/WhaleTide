<template>
  <div class="profile-drawer-container" :class="{ 'show': visible }">
    <div class="profile-drawer-mask" @click="closeDrawer"></div>
    <div class="profile-drawer">
      <div class="drawer-header">
        <h3>个人中心</h3>
        <i class="el-icon-close close-icon" @click="closeDrawer"></i>
      </div>
      
      <div class="profile-content">
        <div class="user-info-section">
          <div class="avatar-container">
            <el-avatar :size="80" :src="userInfo.avatar">
              {{ userInfo.name ? userInfo.name.substring(0, 1) : 'U' }}
            </el-avatar>
          </div>
          <div class="user-detail">
            <h2>{{ userInfo.name || '未设置姓名' }}</h2>
            <p class="user-role">{{ userRoleText }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="info-section">
          <h4 class="section-title">基本信息</h4>
          <el-form label-position="left" label-width="80px">
            <el-form-item label="用户名">
              <span>{{ userInfo.username }}</span>
            </el-form-item>
            <el-form-item label="邮箱">
              <span>{{ userInfo.email || '未设置' }}</span>
            </el-form-item>
            <el-form-item label="手机">
              <span>{{ userInfo.phone || '未设置' }}</span>
            </el-form-item>
            <el-form-item label="注册时间">
              <span>{{ userInfo.createTime }}</span>
            </el-form-item>
            <el-form-item label="最后登录">
              <span>{{ lastLoginTimeFormatted }}</span>
            </el-form-item>
          </el-form>
        </div>
        
        <el-divider></el-divider>
        
        <div class="action-section">
          <el-button type="primary" @click="editProfile">修改资料</el-button>
          <el-button @click="changePassword">修改密码</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { formatLocalDateTime } from '@/utils'

export default {
  name: 'UserProfile',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 这里可以添加本地数据
    }
  },
  computed: {
    ...mapGetters([
      'token',
      'name',
      'avatar',
      'roles',
      'username',
      'email',
      'phone',
      'createTime',
      'lastLoginTime'
    ]),
    userInfo() {
      return {
        name: this.name,
        username: this.username || this.name, // 如果没有专门的用户名，则使用显示名称
        avatar: this.avatar,
        email: this.email || '未设置邮箱',
        phone: this.phone || '未设置手机号',
        createTime: this.formatDateTime(this.createTime) || '未知'
      }
    },
    userRoleText() {
      if (!this.roles || this.roles.length === 0) {
        return '普通用户'
      }
      
      if (this.roles.includes('admin')) {
        return '系统管理员'
      }
      
      return this.roles.join(',')
    },
    lastLoginTimeFormatted() {
      return this.formatDateTime(this.lastLoginTime) || '未知';
    }
  },
  methods: {
    closeDrawer() {
      this.$emit('update:visible', false)
    },
    editProfile() {
      this.$message.info('编辑个人资料功能即将上线')
    },
    changePassword() {
      this.$message.info('修改密码功能即将上线')
    },
    formatDateTime(dateTime) {
      return formatLocalDateTime(dateTime, 'yyyy-MM-dd HH:mm:ss');
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-drawer-container {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  pointer-events: none;
  z-index: 2000;
  
  &.show {
    pointer-events: auto;
    
    .profile-drawer-mask {
      opacity: 1;
    }
    
    .profile-drawer {
      transform: translateX(0);
    }
  }
}

.profile-drawer-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.profile-drawer {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  width: 50%;
  max-width: 500px;
  min-width: 320px;
  background-color: #fff;
  transition: transform 0.3s cubic-bezier(0.23, 1, 0.32, 1);
  transform: translateX(100%);
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #f9f9f9;
  
  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 500;
  }
  
  .close-icon {
    font-size: 20px;
    cursor: pointer;
    padding: 5px;
    
    &:hover {
      color: #409EFF;
    }
  }
}

.profile-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.user-info-section {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  
  .avatar-container {
    margin-right: 20px;
  }
  
  .user-detail {
    h2 {
      margin: 0 0 5px;
      font-size: 20px;
      font-weight: 500;
    }
    
    .user-role {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}

.section-title {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.info-section {
  margin-bottom: 30px;
}

.action-section {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 20px;
}

.el-divider {
  margin: 20px 0;
}
</style> 