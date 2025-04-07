import Request from '@/js_sdk/luch-request/request.js'
import { API_BASE_URL} from '@/utils/appConfig.js';

// 全局调试对象 - 存储最近的API响应
window._lastAPIResponse = null;

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
	
	// 保存原始响应用于调试
	window._lastAPIResponse = {
		url: response.config.url,
		data: response.data,
		originalResponse: response,
		time: new Date().toISOString()
	};
	
	try {
		// 1. 预处理响应数据
		let res = response.data;
		
		// 2. 如果响应是字符串，尝试解析为JSON
		if (typeof res === 'string') {
			try {
				res = JSON.parse(res);
			} catch (e) {
				console.error('响应不是有效的JSON字符串:', res);
				return Promise.reject(response);
			}
		}
		
		// 3. 处理各种响应格式并标准化
		
		// 3.1 标准的API响应格式 {code: 200, message: '操作成功', data: ...}
		if (res && typeof res === 'object' && res.code !== undefined) {
			console.log('标准API响应格式', res);
			
			// 检查应答码
			if (res.code !== 200) {
				// 处理错误响应
				console.error('API错误响应:', res);
				uni.showToast({
					title: res.message || '请求失败',
					duration: 1500,
					icon: 'none'
				});
				
				// 401未登录处理
				if (res.code === 401) {
					uni.showModal({
						title: '提示',
						content: '你已被登出，可以取消继续留在该页面，或者重新登录',
						confirmText:'重新登录',
						cancelText:'取消',
						success: function(modalRes) {
							if (modalRes.confirm) {
								uni.navigateTo({
									url: '/pages/public/login'
								});
							}
						}
					});
				}
				
				return Promise.reject(response);
			}
			
			// 成功响应，直接返回完整响应
			return res;
		}
		
		// 3.2 如果是数组，直接包装
		if (Array.isArray(res)) {
			console.log('数组响应格式，包装为标准格式');
			return {
				code: 200,
				message: 'success',
				data: res
			};
		}
		
		// 3.3 如果是分页响应 {list: [], total: 100, ...}
		if (res && typeof res === 'object' && (res.list !== undefined || res.records !== undefined)) {
			console.log('分页响应格式，包装为标准格式');
			return {
				code: 200,
				message: 'success',
				data: res
			};
		}
		
		// 3.4 其他对象响应，没有明确的状态码
		if (res && typeof res === 'object') {
			console.log('对象响应格式，包装为标准格式');
			return {
				code: 200,
				message: 'success',
				data: res
			};
		}
		
		// 3.5 其他情况，如null、undefined、原始类型值等
		console.warn('未识别的响应格式:', res);
		return {
			code: 200,
			message: 'success',
			data: res || null
		};
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
	// 为空则返回空字符串
	if (!url) {
		console.warn("图片URL为空");
		return '';
	}
	
	// 判断是否是完整URL (http/https开头)
	if (url.startsWith('http://') || url.startsWith('https://')) {
		return url;
	}
	
	// 确保URL以/开头
	if (!url.startsWith('/')) {
		url = '/' + url;
	}
	
	return API_BASE_URL + url;
}

/**
 * 从API响应中提取数据列表
 * @param {Object|Array} response - API响应对象或数组
 * @param {String} defaultType - 默认数据类型，用于日志输出
 * @return {Array} 数据列表数组，如果无法提取则返回空数组
 */
export function extractApiData(response, defaultType = '数据') {
	console.log(`从API响应中提取${defaultType}:`, response);
	
	if (!response) {
		console.error(`${defaultType}响应为空`);
		return [];
	}
	
	// 1. 如果响应本身就是数组
	if (Array.isArray(response)) {
		console.log(`检测到直接数组格式的${defaultType}`);
		return response;
	}
	
	// 2. 标准响应结构: {code: 200, message: '成功', data: [...]}
	if (response.code !== undefined && response.data !== undefined) {
		// 如果code不是200，返回空数组
		if (response.code !== 200) {
			console.warn(`${defaultType}响应状态码不是200:`, response.code);
			return [];
		}
		
		// 2.1 如果data是数组
		if (Array.isArray(response.data)) {
			console.log(`从response.data中获取${defaultType}数组`);
			return response.data;
		}
		
		// 2.2 如果data是分页对象 {list: [], total: 100}
		if (response.data && response.data.list && Array.isArray(response.data.list)) {
			console.log(`从response.data.list中获取${defaultType}数组`);
			return response.data.list;
		}
		
		// 2.3 如果data是MyBatis-Plus分页 {records: [], total: 100}
		if (response.data && response.data.records && Array.isArray(response.data.records)) {
			console.log(`从response.data.records中获取${defaultType}数组`);
			return response.data.records;
		}
		
		// 2.4 返回data本身（可能不是数组）
		console.warn(`${defaultType}数据不是预期的格式，返回response.data`);
		return Array.isArray(response.data) ? response.data : [];
	}
	
	// 3. 直接检查各种常见的分页数据结构
	// 3.1 MyBatis-Plus分页结构: {records: [], total: 100}
	if (response.records && Array.isArray(response.records)) {
		console.log(`从response.records中获取${defaultType}数组`);
		return response.records;
	}
	
	// 3.2 普通分页结构: {list: [], total: 100}
	if (response.list && Array.isArray(response.list)) {
		console.log(`从response.list中获取${defaultType}数组`);
		return response.list;
	}
	
	// 3.3 其他分页结构: {content: [], total: 100} (Spring Data)
	if (response.content && Array.isArray(response.content)) {
		console.log(`从response.content中获取${defaultType}数组`);
		return response.content;
	}
	
	// 3.4 其他可能的数据字段
	if (response.data && Array.isArray(response.data)) {
		console.log(`从response.data中获取${defaultType}数组`);
		return response.data;
	}
	
	if (response.items && Array.isArray(response.items)) {
		console.log(`从response.items中获取${defaultType}数组`);
		return response.items;
	}
	
	if (response.rows && Array.isArray(response.rows)) {
		console.log(`从response.rows中获取${defaultType}数组`);
		return response.rows;
	}
	
	// 4. 如果找不到任何可识别的数据结构，返回空数组
	console.error(`未能从响应中识别出${defaultType}数组:`, response);
	return [];
}

/**
 * 从API响应中提取分页信息
 * @param {Object} response - API响应对象
 * @param {Object} defaultInfo - 默认分页信息
 * @return {Object} 分页信息对象 {total, pageSize, pageNum, pages}
 */
export function extractPaginationInfo(response, defaultInfo = {}) {
	const defaultPagination = {
		total: 0,         // 总记录数
		pageSize: 10,     // 每页大小
		pageNum: 1,       // 当前页码
		pages: 0          // 总页数
	};
	
	// 合并默认值
	const result = { ...defaultPagination, ...defaultInfo };
	
	if (!response || typeof response !== 'object') {
		console.warn('分页信息响应无效');
		return result;
	}
	
	// 标准响应结构: {code: 200, message: '成功', data: {...}}
	const data = response.data || response;
	
	// 提取总记录数
	if (data.total !== undefined) result.total = data.total;
	else if (data.totalCount !== undefined) result.total = data.totalCount;
	else if (data.totalElements !== undefined) result.total = data.totalElements;
	else if (data.count !== undefined) result.total = data.count;
	
	// 提取页大小
	if (data.pageSize !== undefined) result.pageSize = data.pageSize;
	else if (data.size !== undefined) result.pageSize = data.size;
	else if (data.limit !== undefined) result.pageSize = data.limit;
	
	// 提取当前页码
	if (data.pageNum !== undefined) result.pageNum = data.pageNum;
	else if (data.current !== undefined) result.pageNum = data.current;
	else if (data.page !== undefined) result.pageNum = data.page;
	else if (data.pageNumber !== undefined) result.pageNum = data.pageNumber;
	
	// 提取总页数
	if (data.pages !== undefined) result.pages = data.pages;
	else if (data.totalPages !== undefined) result.pages = data.totalPages;
	else if (result.total > 0 && result.pageSize > 0) {
		result.pages = Math.ceil(result.total / result.pageSize);
	}
	
	// 检查嵌套对象
	if (data.pagination && typeof data.pagination === 'object') {
		const p = data.pagination;
		if (p.total !== undefined) result.total = p.total;
		if (p.pageSize !== undefined) result.pageSize = p.pageSize;
		if (p.pageNum !== undefined) result.pageNum = p.pageNum;
		if (p.pages !== undefined) result.pages = p.pages;
	}
	
	console.log('提取的分页信息:', result);
	return result;
}