import Vue from 'vue'

import 'normalize.css/normalize.css' // 重置浏览器默认样式

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // 全局样式

import App from './App'
import store from './store'
import router from './router'

import '@/permission' // 权限控制

// 使用ElementUI
Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
}) 