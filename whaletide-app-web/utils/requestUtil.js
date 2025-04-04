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
	const token = uni.getStorageSync('Authorization');
	const fullAuth = uni.getStorageSync('FullAuthorization');
	
	console.log('发送请求URL:', config.url);
	console.log('存储的token值:', token);
	console.log('存储的完整授权头:', fullAuth);
	
	// 尝试验证token格式是否正确
	const validateToken = (token) => {
		if (!token) return false;
		
		try {
			// 如果带有Bearer前缀，先移除前缀
			let rawToken = token;
			if (token.startsWith('Bearer ')) {
				rawToken = token.substring(7);
			}
			
			// 检查是否符合JWT格式（三段由点分隔的字符串）
			const parts = rawToken.split('.');
			if (parts.length !== 3) {
				console.error('Token格式错误: 不是三段式JWT格式');
				return false;
			}
			
			// 检查但不进行Base64解码，防止不兼容的解码导致错误
			// JWT使用的是URL安全的Base64，允许特殊字符
			return true;
		} catch (e) {
			console.error('Token格式验证失败:', e);
			return false;
		}
	};
	
	// 优先使用完整授权头进行尝试
	if (fullAuth && validateToken(fullAuth)) {
		console.log('使用完整授权头:', fullAuth);
		
		// 确保授权头格式正确
		const effectiveAuth = fullAuth.startsWith('Bearer ') ? fullAuth : 'Bearer ' + fullAuth;
		
		config.header = {
			'Authorization': effectiveAuth,
			...config.header
		}
	} 
	// 如果没有完整授权头但有token，则构建授权头
	else if (token && validateToken(token)) {
		// 确保token格式正确，包含Bearer前缀
		const authHeader = token.startsWith('Bearer ') ? token : 'Bearer ' + token;
		
		config.header = {
			'Authorization': authHeader,
			...config.header
		}
		console.log('构建的Authorization头:', authHeader);
	} else {
		console.log('无有效的Authorization头，匿名请求');
		
		// 如果token无效但存在，尝试清理并重新登录
		if ((token && !validateToken(token)) || (fullAuth && !validateToken(fullAuth))) {
			console.warn('存储的token格式无效，清理并准备重新登录');
			
			// 检查token长度和格式
			if (token) console.log('无效token详情:', {token, length: token.length, format: token.split('.').length});
			if (fullAuth) console.log('无效fullAuth详情:', {fullAuth, length: fullAuth.length, format: fullAuth.split('.').length});
			
			uni.removeStorageSync('Authorization');
			uni.removeStorageSync('FullAuthorization');
			
			// 检查是否是需要登录的请求
			if (config.url.includes('/member/') || config.url.includes('/order/') || config.url.includes('/cart/') || config.url.includes('/sso/info')) {
				setTimeout(() => {
					uni.showModal({
						title: '登录状态异常',
						content: '您的登录凭证无效，请重新登录',
						confirmText: '去登录',
						success: (res) => {
							if (res.confirm) {
								uni.navigateTo({
									url: '/pages/public/login'
								});
							}
						}
					});
				}, 100);
			}
		}
		
		// 保持原有请求头
		config.header = {
			...config.header
		}
		
		// 检查是否是需要登录的请求
		if (config.url.includes('/member/') || config.url.includes('/order/') || config.url.includes('/cart/')) {
			// 如果是需要授权的API，但没有token，引导用户登录
			console.warn('尝试访问需要登录的API，但没有token');
			setTimeout(() => {
				uni.showModal({
					title: '提示',
					content: '您的登录已过期，请重新登录',
					confirmText: '去登录',
					success: (res) => {
						if (res.confirm) {
							uni.navigateTo({
								url: '/pages/public/login'
							});
						}
					}
				});
			}, 100);
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