import request from '@/utils/requestUtil'

export function searchProductList(params) {
	return request({
		method: 'GET',
		url: '/product/search',
		params: params
	})
}

// 搜索商品
export function searchProducts(params) {
	return request({
		method: 'GET',
		url: '/product/search',
		params: params
	})
}

// 获取热门搜索关键词
export function getHotKeywords() {
	return request({
		method: 'GET',
		url: '/product/hotKeywords'
	})
}

// 获取搜索建议
export function getSuggestions(keyword) {
	return request({
		method: 'GET',
		url: '/product/suggestions',
		params: { keyword }
	})
}

export function fetchCategoryTreeList() {
	return request({
		method: 'GET',
		url: '/product/categoryTreeList'
	})
}

export function fetchProductDetail(id) {
	// 构建与后端API完全匹配的URL格式: /product/detail/{id}
	return request({
		method: 'GET',
		url: `/product/detail/${id}`
	})
}

export function fetchProductCommentList(productId, params) {
	return request({
		method: 'GET',
		url: `/product/comment/list/${productId}`,
		params: params
	})
}

export function addProductComment(data) {
	return request({
		method: 'POST',
		url: '/product/comment/add',
		data: data
	})
}
