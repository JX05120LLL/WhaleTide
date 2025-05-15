<template>
  <div class="app-container">
    <h1>订单详情页面</h1>
    <p>订单ID: {{ $route.params.id }}</p>
  </div>
</template>

<script>
export default {
  name: 'OrderDetail'
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style> 