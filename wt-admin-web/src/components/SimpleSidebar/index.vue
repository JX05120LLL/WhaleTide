<template>
  <div class="simple-sidebar" :class="{ 'collapsed': isCollapsed }">
    <div class="simple-logo">
      <span v-if="!isCollapsed">Whale Tide 后台</span>
      <span v-else>G-M</span>
    </div>
    
    <div class="simple-menu">
      <a href="#/dashboard" class="menu-item" :class="{ active: currentPath === '/dashboard' }">
        <i class="el-icon-s-data"></i>
        <span v-if="!isCollapsed">仪表盘</span>
      </a>

      <div class="menu-group">
        <div class="menu-group-title">
          <i class="el-icon-goods"></i>
          <span v-if="!isCollapsed">商品管理</span>
        </div>
        
        <div class="menu-group-items" :class="{ 'collapsed-items': isCollapsed }">
          <a href="#/product/list" class="menu-item" :class="{ active: currentPath === '/product/list' }">
            <i class="el-icon-shopping-bag-1"></i>
            <span v-if="!isCollapsed">商品列表</span>
          </a>
          
          <a href="#/product/add" class="menu-item" :class="{ active: currentPath === '/product/add' }">
            <i class="el-icon-plus"></i>
            <span v-if="!isCollapsed">添加商品</span>
          </a>
        </div>
      </div>
      
      <div class="menu-group">
        <div class="menu-group-title">
          <i class="el-icon-s-order"></i>
          <span v-if="!isCollapsed">订单管理</span>
        </div>
        
        <div class="menu-group-items" :class="{ 'collapsed-items': isCollapsed }">
          <a href="#/order/list" class="menu-item" :class="{ active: currentPath === '/order/list' }">
            <i class="el-icon-document"></i>
            <span v-if="!isCollapsed">订单列表</span>
          </a>
          
          <a href="#/order/statistics" class="menu-item" :class="{ active: currentPath === '/order/statistics' }">
            <i class="el-icon-data-analysis"></i>
            <span v-if="!isCollapsed">订单统计</span>
          </a>
        </div>
      </div>
      
    </div>
    
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="toggleCollapse">
      <i :class="isCollapsed ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SimpleSidebar',
  props: {
    currentPath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      isCollapsed: false
    }
  },
  mounted() {
    // 读取本地存储的折叠状态
    try {
      const savedCollapsedState = localStorage.getItem('sidebarCollapsed');
      if (savedCollapsedState !== null) {
        this.isCollapsed = savedCollapsedState === 'true';
      }
      console.log('SimpleSidebar - 菜单折叠状态:', this.isCollapsed ? '已折叠' : '已展开');
    } catch (e) {
      console.error('SimpleSidebar - 读取localStorage失败:', e);
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed;
      console.log('SimpleSidebar - 切换菜单状态为:', this.isCollapsed ? '已折叠' : '已展开');
      
      // 保存折叠状态到本地存储
      try {
        localStorage.setItem('sidebarCollapsed', this.isCollapsed);
        console.log('SimpleSidebar - 菜单状态已保存到localStorage');
      } catch (e) {
        console.error('SimpleSidebar - 保存菜单状态失败:', e);
      }
      
      // 触发窗口重绘，确保样式更新
      this.$nextTick(() => {
        window.dispatchEvent(new Event('resize'));
      });
    }
  }
}
</script>

<style scoped>
.simple-sidebar {
  background-color: #304156;
  color: #bfcbd9;
  height: 100%;
  width: 210px;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1001;
  overflow-y: auto;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  transition: width 0.3s;
}

.simple-sidebar.collapsed {
  width: 64px;
  overflow: hidden;
}

.simple-logo {
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

.simple-menu {
  padding: 10px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  height: 50px;
  padding: 0 16px;
  color: #bfcbd9;
  text-decoration: none;
  transition: all 0.3s;
  overflow: hidden;
  white-space: nowrap;
}

.menu-item:hover {
  background-color: #263445;
  color: #409EFF;
}

.menu-item.active {
  background-color: #263445;
  color: #409EFF;
}

.menu-item i {
  margin-right: 10px;
  font-size: 16px;
}

.simple-sidebar.collapsed .menu-item i {
  margin-right: 0;
}

.menu-group {
  margin-bottom: 10px;
}

.menu-group-title {
  display: flex;
  align-items: center;
  height: 50px;
  padding: 0 16px;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
}

.menu-group-title i {
  margin-right: 10px;
  font-size: 16px;
}

.simple-sidebar.collapsed .menu-group-title i {
  margin-right: 0;
}

.menu-group-items {
  padding-left: 16px;
  border-left: 1px solid #1f2d3d;
  margin-left: 16px;
  transition: all 0.3s;
}

.collapsed-items {
  padding-left: 0;
  border-left: none;
  margin-left: 0;
}

.collapse-btn {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 36px;
  height: 36px;
  background-color: #1f2d3d;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.collapse-btn:hover {
  background-color: #409EFF;
}
</style> 