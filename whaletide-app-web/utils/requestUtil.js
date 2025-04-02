import Request from '@/js_sdk/luch-request/request.js'
import { API_BASE_URL} from '@/utils/appConfig.js';

const http = new Request()

http.setConfig((config) => { /* 设置全局配置 */
	config.baseUrl = API_BASE_URL /* 根域名不同 */
	config.header = {
		...config.header
	}
	return config
})

/**
 * 自定义验证器，如果返回true 则进入响应拦截器的响应成功函数(resolve)，否则进入响应拦截器的响应错误函数(reject)
 * @param { Number } statusCode - 请求响应体statusCode（只读）
 * @return { Boolean } 如果为true,则 resolve, 否则 reject
 */
http.validateStatus = (statusCode) => {
	return statusCode === 200
}

http.interceptor.request((config, cancel) => { /* 请求之前拦截器 */
	const authorization = uni.getStorageSync('Authorization');
	
	console.log('发送请求URL:', config.url);
	
	if(authorization) {
		config.header = {
			'Authorization': authorization,
			...config.header
		}
		console.log('Authorization头:', authorization);
	} else {
		console.log('无Authorization头，匿名请求');
		config.header = {
			...config.header
		}
	}
	
	return config
})

http.interceptor.response((response) => { /* 请求之后拦截器 */
	console.log('收到响应:', response.config.url, response);
	try {
		// 1. 预处理响应数据
		let res = response.data;
		
		// 2. 标准化响应格式，处理各种后端返回格式
		// 我们希望统一格式为 {code: 200, message: "", data: {...}}
		if (typeof res === 'string') {
			try {
				res = JSON.parse(res);
			} catch (e) {
				console.error('响应不是有效的JSON字符串:', res);
				return Promise.reject(response);
			}
		}
		
		// 3. 处理各种可能的响应格式
		// 3.1 如果是纯数据对象，没有code/message/data结构
		if (res && typeof res === 'object' && res.code === undefined) {
			// 假设这是成功响应，包装成标准格式
			res = { code: 200, message: "success", data: res };
		}
		
		// 4. 校验响应状态
		if (res.code !== 200) {
			//提示错误信息
			console.error('API错误响应:', res);
			uni.showToast({
				title: res.message || '请求失败',
				duration: 1500,
				icon: 'none'
			})
			//401未登录处理
			if (res.code === 401) {
				uni.showModal({
					title: '提示',
					content: '你已被登出，可以取消继续留在该页面，或者重新登录',
					confirmText:'重新登录',
					cancelText:'取消',
					success: function(res) {
						if (res.confirm) {
							uni.navigateTo({
								url: '/pages/public/login'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
			return Promise.reject(response);
		} else {
			// 5. 返回标准化的响应数据
			console.log('API成功响应(标准化后):', res);
			return res;
		}
	} catch (error) {
		console.error('处理响应时出错:', error);
		return Promise.reject(response);
	}
}, (response) => {
	//提示错误信息
	console.error('请求异常:', response);
	uni.showToast({
		title: response.errMsg || '网络错误',
		duration: 1500,
		icon: 'none'
	})
	return Promise.reject(response);
})

export function request (options = {}) {
	return http.request(options);
}

export default request

// 处理图片URL的函数，将相对路径转为完整URL
export function getFullImageUrl(url) {
	if (!url) return '';
	if (url.startsWith('http') || url.startsWith('https')) {
		return url;
	}
	return API_BASE_URL + url;
}