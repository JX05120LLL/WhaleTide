<template>
  <div class="sidebar-container">
    <logo />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        :unique-opened="false"
        :collapse-transition="false"
        router
        mode="vertical"
      >
        <sidebar-item 
          v-for="route in permission_routes" 
          :key="route.path" 
          :item="route" 
          :base-path="route.path" 
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      // 配置直接输出路由日志，帮助调试
      routeLogging: true
    }
  },
  mounted() {
    if (this.routeLogging) {
      console.log('侧边栏加载的路由配置:', this.permission_routes);
      
      // 检查商品管理是否在路由配置中
      const hasProductRoute = this.permission_routes.some(route => route.path === '/product');
      console.log('商品管理路由存在:', hasProductRoute);
      
      if (hasProductRoute) {
        const productRoute = this.permission_routes.find(route => route.path === '/product');
        console.log('商品管理路由详情:', productRoute);
      }
      
      
      console.log('当前激活的菜单项:', this.activeMenu);
    }
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar',
      'routes'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      
      console.log('计算当前激活菜单，路径:', path);
      
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-container {
  height: 100%;
  background-color: $menuBg;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .logo-container {
    height: 60px;
    padding: 10px 0;
    text-align: center;
    overflow: hidden;

    .logo-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      font-size: 18px;
      line-height: 40px;
      vertical-align: middle;
    }
  }

  .scrollbar-wrapper {
    overflow-x: hidden;
  }

  .el-scrollbar__view {
    height: 100%;
  }

  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
  }
}
</style> 