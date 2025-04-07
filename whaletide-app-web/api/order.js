import request from '@/utils/requestUtil'

export function generateConfirmOrder(data) {
	// 处理不同格式的输入
	let requestData;
	
	// 如果传入的是数组
	if (Array.isArray(data)) {
		// 确保所有cartIds都是数字类型
		const cartIds = data.map(id => Number(id));
		requestData = { cartIds: cartIds };
	} 
	// 如果传入的是字符串（可能是逗号分隔的ID列表）
	else if (typeof data === 'string') {
		const cartIds = data.split(',').map(id => Number(id.trim()));
		requestData = { cartIds: cartIds };
	}
	// 如果已经是对象格式
	else if (data && typeof data === 'object') {
		requestData = data;
		// 确保cartIds是数组并且元素是数字
		if (data.cartIds) {
			if (Array.isArray(data.cartIds)) {
				requestData.cartIds = data.cartIds.map(id => Number(id));
			} else if (typeof data.cartIds === 'string') {
				requestData.cartIds = data.cartIds.split(',').map(id => Number(id.trim()));
			}
		}
	} else {
		// 默认空对象
		requestData = {};
	}
	
	console.log('确认订单请求数据:', JSON.stringify(requestData));
	
	return request({
		method: 'POST',
		url: '/order/generateConfirmOrder',
		data: requestData,
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		}
	})
}

export function generateOrder(data) {
	console.log('发送订单请求数据:', JSON.stringify(data));
	
	// 确保关键字段有效
	if (!data.addressId) {
		console.error('订单地址ID不能为空');
	}
	
	return request({
		method: 'POST',
		url: '/order/generateOrder',
		data: data,
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		}
	})
}

export function fetchOrderList(params) {
	return request({
		method: 'GET',
		url: '/order/get/orderList',
		params: params
	})
}

export function payOrderSuccess(data) {
	return request({
		method: 'POST',
		url: '/order/paySuccess',
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		},
		data: data
	})
}

export function fetchOrderDetail(orderId) {
	return request({
		method: 'GET',
		url: `/order/detail/${orderId}`
	})
}

export function cancelUserOrder(data) {
	return request({
		method: 'POST',
		url: '/order/cancelUserOrder',
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		},
		data: data
	})
}

export function confirmReceiveOrder(data) {
	return request({
		method: 'POST',
		url: '/order/confirmReceiveOrder',
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		},
		data: data
	})
}

export function deleteUserOrder(data) {
	return request({
		method: 'POST',
		url: '/order/deleteOrder',
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		},
		data: data
	})
}

export function fetchAliapyStatus(params) {
	return request({
		method: 'GET',
		url: '/alipay/query',
		params: params
	})
}

/**
 * 商品直接购买接口
 * @param {Object} data - 包含商品ID、SKU ID和数量的对象
 * @returns {Promise} 返回包含临时购物车项ID的Promise
 */
export function directBuy(data) {
	// 确保数据格式正确
	const requestData = {
		productId: Number(data.productId) || 0,
		productSkuId: Number(data.productSkuId) || 0,
		quantity: Number(data.quantity) || 1
	};
	
	console.log('直接购买请求数据:', JSON.stringify(requestData));
	
	return request({
		method: 'POST',
		url: '/order/directBuy',
		data: requestData,
		headers: {
			'Content-Type': 'application/json; charset=UTF-8'
		}
	});
}