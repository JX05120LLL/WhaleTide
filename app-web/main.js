import Vue from 'vue'
import store from './store'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css/normalize.css'
import './styles/index.scss'

// 引入全局样式
import './uni.scss'

Vue.use(ElementUI)

const msg = (title, duration=1500, mask=false, icon='none')=>{
	//统一提示方便全局修改
	if(Boolean(title) === false){
		return;
	}
	// 适配H5环境
	ElementUI.Message({
		message: title,
		type: icon === 'success' ? 'success' : 'info',
		duration: duration
	});
}

const prePage = ()=>{
	let pages = getCurrentPages();
	let prePage = pages[pages.length - 2];
	// 适配H5环境
	return prePage;
}

Vue.config.productionTip = false
Vue.prototype.$fire = new Vue();
Vue.prototype.$store = store;
Vue.prototype.$api = {msg, prePage};

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})