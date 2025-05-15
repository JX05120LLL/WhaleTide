<template>
  <el-container class="app-wrapper">
    <el-aside class="sidebar-container" :width="isCollapsed ? '64px' : '220px'">
      <div class="logo-container">
        <span v-if="!isCollapsed">Whale Tide 后台</span>
        <span v-else>G-M</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        :unique-opened="true"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        class="sidebar-menu"
        @select="handleSelect"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-data"></i>
          <span slot="title">仪表盘</span>
        </el-menu-item>

        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-goods"></i>
            <span>商品管理</span>
          </template>
          <el-menu-item index="/product/list">
            <i class="el-icon-shopping-bag-1"></i>
            <span slot="title">商品列表</span>
          </el-menu-item>
          <el-menu-item index="/product/category">
            <i class="el-icon-folder"></i>
            <span slot="title">商品分类</span>
          </el-menu-item>
          <el-menu-item index="/product/add">
            <i class="el-icon-plus"></i>
            <span slot="title">添加商品</span>
          </el-menu-item>
        </el-submenu>

        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-s-order"></i>
            <span>订单管理</span>
          </template>
          <el-menu-item index="/order/list">
            <i class="el-icon-document"></i>
            <span slot="title">订单列表</span>
          </el-menu-item>
          <el-menu-item index="/order/statistics">
            <i class="el-icon-data-analysis"></i>
            <span slot="title">订单统计</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu index="5">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/role">
            <i class="el-icon-s-check"></i>
            <span slot="title">角色管理</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <i class="el-icon-s-fold toggle-button" @click="toggleCollapse"></i>
          <breadcrumb />
        </div>
        <div class="header-right">
          <el-dropdown trigger="click">
            <span class="user-dropdown-link">
              {{ name || '管理员' }} <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="showProfile">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="app-main" :class="{ 'main-expanded': isCollapsed }">
        <router-view />
      </el-main>
    </el-container>

    <!-- 个人中心抽屉组件 -->
    <profile-drawer :visible.sync="profileVisible" />
  </el-container>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Breadcrumb from './components/Breadcrumb'
import ProfileDrawer from '@/views/user/Profile'

export default {
  name: 'Layout',
  components: {
    Breadcrumb,
    ProfileDrawer
  },
  data() {
    return {
      isCollapsed: false,
      profileVisible: false
    }
  },
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar
    }),
    ...mapGetters([
      'name',
      'avatar'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    }
  },
  mounted() {
    // 读取本地存储的折叠状态
    try {
      const savedCollapsedState = localStorage.getItem('sidebarCollapsed');
      if (savedCollapsedState !== null) {
        this.isCollapsed = savedCollapsedState === 'true';
      }
      console.log('菜单折叠状态:', this.isCollapsed ? '已折叠' : '已展开');
    } catch (e) {
      console.error('读取localStorage失败:', e);
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed;
      console.log('切换菜单状态为:', this.isCollapsed ? '已折叠' : '已展开');
      
      // 保存折叠状态到本地存储
      try {
        localStorage.setItem('sidebarCollapsed', this.isCollapsed);
        console.log('菜单状态已保存到localStorage');
      } catch (e) {
        console.error('保存菜单状态失败:', e);
      }
      
      // 触发窗口重绘，确保样式更新
      this.$nextTick(() => {
        window.dispatchEvent(new Event('resize'));
      });
    },
    logout() {
      this.$confirm('确认退出系统?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用Vuex的logout action
        this.$store.dispatch('user/logout')
          .then(() => {
            // 退出成功后显示消息
            this.$message.success('已安全退出系统')
            // 跳转到登录页
            this.$router.push('/login')
          })
          .catch(() => {
            // 即使后端API调用失败，本地登出也已经完成
            // 不显示错误消息，仍然视为退出成功
            this.$message.success('已退出系统')
            // 跳转到登录页
            this.$router.push('/login')
          })
      }).catch(() => {
        // 用户取消退出
        this.$message.info('已取消退出操作')
      })
    },
    handleSelect(key) {
      // 使用 Vue Router 进行路由跳转
      this.$router.push(key);
    },
    showProfile() {
      this.profileVisible = true
    }
  }
}
</script>

<style scoped>
.app-wrapper {
  height: 100%;
}

.app-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  height: 60px;
}

.header-left,
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toggle-button {
  font-size: 20px;
  padding: 0 10px;
  cursor: pointer;
}

.user-dropdown-link {
  cursor: pointer;
  color: #333;
  font-size: 14px;
}

.app-main {
  padding: 20px;
  background-color: #f0f2f5;
  height: calc(100vh - 60px);
  overflow: auto;
  transition: margin-left 0.3s;
  margin-left: 220px;
}

.main-expanded {
  margin-left: 64px;
}

.sidebar-container {
  background-color: #304156;
  height: 100%;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 1001;
  overflow: hidden;
  transition: width 0.3s;
}

.logo-container {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: white;
  border-bottom: 1px solid #1f2d3d;
  overflow: hidden;
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none;
  height: calc(100% - 60px);
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

/* 菜单项样式 */
.el-menu-item, .el-submenu__title {
  height: 50px;
  line-height: 50px;
}

.el-menu-item i, .el-submenu__title i {
  margin-right: 10px;
}

/* 子菜单样式 */
.el-menu--inline {
  background-color: #1f2d3d !important;
}

.el-menu--inline .el-menu-item {
  min-width: 220px !important;
}

/* 折叠状态下的样式 */
.el-menu--collapse .el-menu-item i,
.el-menu--collapse .el-submenu__title i {
  margin-right: 0;
}
</style> 