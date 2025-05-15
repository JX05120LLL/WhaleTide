import request from '@/utils/request'

/**
 * 获取商品列表
 * @param {Object} query 查询参数
 */
export function fetchProductList(query) {
  console.log('获取商品列表，参数:', query);
  return request({
    url: '/api/admin/product/list',
    method: 'get',
    params: query
  })
  .then(response => {
    console.log('商品列表获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取商品列表失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 获取所有商品列表（后台管理）
 * 包括已上架和已下架的商品
 * @param {Object} query 查询参数
 */
export function fetchAllProductList(query) {
  console.log('获取所有商品列表(后台管理)，参数:', query);
  return request({
    url: '/api/admin/product/list',
    method: 'get',
    params: query
  })
  .then(response => {
    console.log('所有商品列表获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取所有商品列表失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 获取商品详情
 * @param {Number} id 商品ID
 */
export function fetchProductDetail(id) {
  console.log('获取商品详情，ID:', id);
  return request({
    url: `/api/admin/product/detail/${id}`,
    method: 'get'
  })
  .then(response => {
    console.log('商品详情获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取商品详情失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 创建商品
 * @param {Object} data 商品数据
 */
export function createProduct(data) {
  console.log('创建商品，数据:', data);
  return request({
    url: '/api/admin/product/add',
    method: 'post',
    data
  })
  .then(response => {
    console.log('商品创建成功:', response);
    return response;
  })
  .catch(error => {
    console.error('创建商品失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 更新商品
 * @param {Object} data 商品数据
 */
export function updateProduct(data) {
  console.log('更新商品，数据:', data);
  return request({
    url: '/api/admin/product/update',
    method: 'put',
    data
  })
  .then(response => {
    console.log('商品更新成功:', response);
    return response;
  })
  .catch(error => {
    console.error('更新商品失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 删除商品
 * @param {Number} id 商品ID
 */
export function deleteProduct(id) {
  console.log('删除商品，ID:', id);
  return request({
    url: `/api/admin/product/delete/${id}`,
    method: 'delete'
  })
  .then(response => {
    console.log('商品删除成功:', response);
    return response;
  })
  .catch(error => {
    console.error('删除商品失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 更新商品状态
 * @param {Object} data 包含id和status的对象
 */
export function updateProductStatus(data) {
  console.log('更新商品状态，数据:', data);
  return request({
    url: '/api/admin/product/updateStatus',
    method: 'put',
    data
  })
  .then(response => {
    console.log('商品状态更新成功:', response);
    return response;
  })
  .catch(error => {
    console.error('更新商品状态失败:', error);
    return Promise.reject(error);
  });
} 