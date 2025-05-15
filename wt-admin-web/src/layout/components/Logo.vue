<template>
  <div class="logo-container" :class="{ 'collapse': collapse }">
    <router-link to="/dashboard">
      <h1 class="logo-title">Ghost-M 后台</h1>
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'Logo',
  props: {
    collapse: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="scss" scoped>
.logo-container {
  height: 60px;
  padding: 10px 0;
  text-align: center;
  overflow: hidden;
  background-color: #304156;
  
  &.collapse {
    .logo-title {
      display: none;
    }
  }
  
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
</style> 