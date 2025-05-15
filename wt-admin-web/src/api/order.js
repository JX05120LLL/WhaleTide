import request from '@/utils/request'

/**
 * 获取订单列表
 * @param {Object} query 查询参数
 */
export function fetchList(query) {
  console.log('获取订单列表，参数:', query);
  // 确保query对象是普通对象，不是Vue的响应式对象
  const params = {
    pageNum: query.pageNum || 1,
    pageSize: query.pageSize || 10
  };
  
  // 仅当status有值时才添加
  if (query.status !== undefined && query.status !== null) {
    params.status = query.status;
  }
  
  // 添加用户ID参数
  if (query.userId !== undefined && query.userId !== null && query.userId !== '') {
    params.userId = query.userId;
  }
  
  console.log('处理后的请求参数:', params);
  
  // 使用管理员API获取所有订单
  return request({
    url: '/api/admin/order/list',
    method: 'get',
    params
  })
  .then(response => {
    console.log('订单列表获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取订单列表失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 获取订单详情
 * @param {Number} id 订单ID
 */
export function fetchOrder(id) {
  console.log('获取订单详情，ID:', id);
  // 使用管理员API获取订单详情
  return request({
    url: `/api/admin/order/detail/${id}`,
    method: 'get'
  })
  .then(response => {
    console.log('订单详情获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取订单详情失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 发货订单
 * @param {Object} data 包含订单ID和物流信息
 */
export function shipOrder(id, data) {
  console.log('发货订单，数据:', data);
  return request({
    url: `/api/order/ship/${id}`,
    method: 'put',
    data
  })
  .then(response => {
    console.log('订单发货成功:', response);
    return response;
  })
  .catch(error => {
    console.error('订单发货失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 取消订单
 * @param {Number} id 订单ID
 */
export function cancelOrder(id, data) {
  console.log('取消订单，ID:', id);
  return request({
    url: `/api/order/cancel/${id}`,
    method: 'post',
    data
  })
  .then(response => {
    console.log('订单取消成功:', response);
    return response;
  })
  .catch(error => {
    console.error('订单取消失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 完成订单
 * @param {Number} id 订单ID
 */
export function completeOrder(id) {
  console.log('完成订单，ID:', id);
  return request({
    url: `/api/order/complete/${id}`,
    method: 'post'
  })
  .then(response => {
    console.log('订单完成成功:', response);
    return response;
  })
  .catch(error => {
    console.error('订单完成失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 删除订单
 * @param {Number} id 订单ID
 */
export function deleteOrder(id) {
  console.log('删除订单，ID:', id);
  return request({
    url: `/api/order/${id}`,
    method: 'delete'
  })
  .then(response => {
    console.log('订单删除成功:', response);
    return response;
  })
  .catch(error => {
    console.error('订单删除失败:', error);
    return Promise.reject(error);
  });
}

export function getProductOrders(productId) {
  return request({
    url: `/api/order/product/${productId}`,
    method: 'get'
  })
}

export function updateOrder(id, data) {
  return request({
    url: `/api/order/${id}`,
    method: 'put',
    data
  })
}

export function refundOrder(id, data) {
  return request({
    url: `/api/order/refund/${id}`,
    method: 'post',
    data
  })
}

export function fetchOrderStatistics(query) {
  return request({
    url: '/api/admin/order/statistics',
    method: 'get',
    params: query
  })
} 