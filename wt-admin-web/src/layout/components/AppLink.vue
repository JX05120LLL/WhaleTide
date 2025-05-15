<template>
  <component :is="type" v-bind="linkProps" @click="handleClick">
    <slot />
  </component>
</template>

<script>
export default {
  name: 'AppLink',
  props: {
    to: {
      type: [String, Object],
      required: true
    }
  },
  computed: {
    isExternal() {
      return /^(https?:|mailto:|tel:)/.test(this.to)
    },
    type() {
      if (this.isExternal) {
        return 'a'
      }
      return 'router-link'
    },
    linkProps() {
      if (this.isExternal) {
        return {
          href: this.to,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        to: this.to
      }
    }
  },
  methods: {
    handleClick(e) {
      console.log('AppLink点击:', this.to);
      
      // 只处理外部链接，内部链接由router-link自动处理
      if (this.isExternal) {
        console.log('打开外部链接:', this.to);
      } else {
        console.log('内部导航将由router-link处理:', this.to);
        
        // 尝试手动导航 - 添加此逻辑以确保导航正常工作
        if (typeof this.to === 'string' && !this.$router.resolve(this.to).resolved.matched.length) {
          console.warn('路由未找到，请检查路由配置:', this.to);
        }
      }
      
      // 广播点击事件供父组件使用
      this.$emit('link-click', this.to);
    }
  }
}
</script> 