<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <div 
        :id="'menu-item-' + (onlyOneChild.name || Math.random().toString(36).substr(2, 9))"
        class="menu-link" 
        @click="forcePushRoute(resolvePath(onlyOneChild.path))"
      >
        <el-menu-item :index="resolvePath(onlyOneChild.path)">
          <i v-if="onlyOneChild.meta && onlyOneChild.meta.icon" :class="onlyOneChild.meta.icon"></i>
          <span>{{ onlyOneChild.meta && onlyOneChild.meta.title }}</span>
        </el-menu-item>
      </div>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)">
      <template slot="title">
        <i v-if="item.meta && item.meta.icon" :class="item.meta.icon"></i>
        <span>{{ item.meta && item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'

export default {
  name: 'SidebarItem',
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    // To fix the warning of "this.onlyOneChild was accessed during render but is not defined on the instance."
    this.onlyOneChild = null
    return {}
  },
  mounted() {
    // 输出调试信息
    console.log('SidebarItem 挂载:', this.item.path, this.basePath);
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (!routePath) {
        return this.basePath;
      }
      
      if (isExternal(routePath)) {
        return routePath
      }
      
      if (routePath.startsWith('/')) {
        return routePath;
      }
      
      return path.resolve(this.basePath, routePath)
    },
    // 使用最直接的方式强制导航
    forcePushRoute(path) {
      // 打印点击调试信息
      console.log('!!!强制导航被触发!!!');
      console.log('点击了菜单项:', path);
      console.log('当前路由:', this.$route.fullPath);
      console.log('路由实例:', !!this.$router);
      
      // 如果是外部链接
      if (isExternal(path)) {
        window.open(path, '_blank');
        return;
      }
      
      // 直接修改URL - 这是最直接的方法
      try {
        // 尝试多种方法导航
        console.log('方法1: 使用router.push');
        this.$router.push(path);
        
        // 延时执行备用方法 - 防止第一个方法失败
        setTimeout(() => {
          if (this.$route.path !== path) {
            console.log('方法2: 使用location.href');
            window.location.href = '#' + path;
          }
        }, 100);
        
        // 二次备用 - 更激进的方法
        setTimeout(() => {
          if (this.$route.path !== path) {
            console.log('方法3: 使用location.replace');
            window.location.replace('#' + path);
          }
        }, 200);
        
        // 最终备用 - 完全重载页面
        setTimeout(() => {
          if (this.$route.path !== path) {
            console.log('方法4: 完全重载页面');
            window.location.href = window.location.origin + window.location.pathname + '#' + path;
            window.location.reload();
          }
        }, 300);
      } catch (e) {
        console.error('所有导航方法均失败:', e);
        alert('导航失败，请查看控制台');
      }
    }
  }
}
</script>

<style scoped>
.el-menu-item, .el-submenu__title {
  display: flex;
  align-items: center;
}

.menu-link {
  cursor: pointer;
  width: 100%;
  position: relative;
  z-index: 10; /* 确保元素在上层能接收点击事件 */
}
</style> 