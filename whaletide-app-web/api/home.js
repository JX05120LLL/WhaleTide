import request from '@/utils/requestUtil'

// 提取分页响应中的数据列表
function extractListFromPageResponse(response) {
	console.log("开始提取分页响应数据:", response);
	
	// 如果响应已经是数组，直接返回
	if (Array.isArray(response)) {
		console.log("响应已经是数组，直接返回");
		return response;
	}
	
	// 如果响应是普通对象，检查是否有list字段
	if (response && typeof response === 'object') {
		// 如果有list字段，返回list
		if (response.list && Array.isArray(response.list)) {
			console.log("从response.list中提取数据，数量:", response.list.length);
			return response.list;
		}
		
		// Spring Boot分页结构可能包含content字段
		if (response.content && Array.isArray(response.content)) {
			console.log("从response.content中提取数据，数量:", response.content.length);
			return response.content;
		}
		
		// 可能有records字段(MyBatis-Plus)
		if (response.records && Array.isArray(response.records)) {
			console.log("从response.records中提取数据，数量:", response.records.length);
			return response.records;
		}
		
		// 可能直接是分页信息，需要检查常见字段
		if ((response.pageNum !== undefined || response.current !== undefined) && 
		    (response.total !== undefined)) {
			// 寻找可能的数据字段
			const possibleDataFields = ['data', 'items', 'rows'];
			for (const field of possibleDataFields) {
				if (response[field] && Array.isArray(response[field])) {
					console.log(`从response.${field}中提取数据，数量:`, response[field].length);
					return response[field];
				}
			}
		}
	}
	
	// 如果没有找到列表数据，返回空数组
	console.log("找不到列表数据，返回空数组");
	return [];
}

// 统一的响应数据处理函数
function processApiResponse(response) {
	// 记录原始响应
	console.log("API原始响应:", response);
	
	// 如果响应无效，返回null
	if (!response) {
		console.error("API响应无效 (null/undefined)");
		return null;
	}
	
	// 标准响应格式: {code: 200, message: "success", data: {...}}
	// 提取数据
	let responseData = null;
	
	if (response.data !== undefined) {
		console.log("检测到标准响应格式 response.data");
		responseData = response.data;
	} else if (response.list !== undefined || response.records !== undefined || 
		       response.content !== undefined || response.items !== undefined) {
		console.log("检测到直接分页响应格式");
		responseData = response;
	} else {
		console.log("未检测到已知响应格式，使用原始响应");
		responseData = response;
	}
	
	console.log("处理后的响应数据:", responseData);
	return responseData;
}

export function fetchContent() {
	return request({
		method: 'GET',
		url: '/home/content'
	}).then(processApiResponse);
}

export function fetchRecommendProductList(params) {
	return request({
		method: 'GET',
		url: '/home/recommendProductList',
		params: params
	}).then(processApiResponse);
}

export function fetchProductCateList(parentId) {
	return request({
		method: 'GET',
		url: '/home/productCateList/'+parentId,
	}).then(processApiResponse);
}

export function fetchNewProductList(params) {
	return request({
		method: 'GET',
		url: '/home/newProductList',
		params:params
	}).then(processApiResponse);
}

export function fetchHotProductList(params) {
	return request({
		method: 'GET',
		url: '/home/hotProductList',
		params:params
	}).then(processApiResponse);
}
