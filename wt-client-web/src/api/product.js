import { http } from '@/utils/request';

/**
 * 获取商品列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {number|string} params.categoryId - 分类ID
 * @param {number|string} params.brandId - 品牌ID
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.sort - 排序方式
 * @returns {Promise} 返回商品列表数据
 */
export const getProductList = (params) => {
  return http.get('/products', params);
};

/**
 * 获取商品详情
 * @param {number|string} productId - 商品ID
 * @returns {Promise} 返回商品详情数据
 */
export const getProductDetail = (productId) => {
  return http.get(`/product/detail/${productId}`);
};

/**
 * 获取商品评价列表
 * @param {number|string} productId - 商品ID
 * @param {Object} params - 查询参数
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {string} params.sort - 排序方式 (time_desc, time_asc, rating_desc, rating_asc)
 * @param {number} params.rating - 评分过滤 (1-5)
 * @returns {Promise} 返回商品评价列表
 */
export const getProductReviews = (productId, params = {}) => {
  const queryParams = { ...params, productId };
  console.log('发送评论请求:', `/product/comment/list/${productId}`, queryParams);
  return http.get(`/product/comment/list/${productId}`, queryParams);
};

/**
 * 获取相关商品推荐
 * @param {number|string} productId - 商品ID
 * @param {number} limit - 返回数量限制
 * @returns {Promise} 返回相关商品列表
 */
export const getRelatedProducts = (productId, limit = 5) => {
  return http.get(`/product/related/${productId}`, { limit });
};

/**
 * 检查商品库存状态
 * @param {number|string} productId - 商品ID
 * @param {Object} skuData - SKU数据对象
 * @returns {Promise} 返回库存信息
 */
export const checkProductStock = (productId, skuData) => {
  return http.post(`/products/${productId}/stock`, skuData);
};

/**
 * 提交商品评价
 * @param {number|string} productId - 商品ID
 * @param {Object} reviewData - 评价数据
 * @param {number} reviewData.rating - 评分(1-5)
 * @param {string} reviewData.content - 评价内容
 * @param {Array} reviewData.images - 图片数组
 * @param {Object} reviewData.tags - 标签对象
 * @returns {Promise} 返回评价结果
 */
export const submitProductReview = (productId, reviewData) => {
  return http.post(`/products/${productId}/reviews`, reviewData);
};

/**
 * 获取商品SKU信息
 * @param {number|string} productId - 商品ID
 * @returns {Promise} 返回SKU信息
 */
export const getProductSku = (productId) => {
  return http.get(`/products/${productId}/sku`);
};

/**
 * 获取商品分类列表
 * @param {Object} params - 查询参数
 * @param {number} params.parentId - 父分类ID
 * @returns {Promise} 返回分类列表
 */
export const getProductCategories = (params = {}) => {
  return http.get('/product/categories', params);
};

/**
 * 获取品牌列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回品牌列表
 */
export const getBrands = (params = {}) => {
  return http.get('/product/brands', params);
};

/**
 * 获取分类商品列表
 * @param {number|string} categoryId - 分类ID
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @param {string} sort - 排序方式
 * @returns {Promise} 返回分类商品列表
 */
export const getCategoryProducts = (categoryId, page = 1, pageSize = 10, sort = '') => {
  return http.get(`/product/category/${categoryId}/products`, { page, pageSize, sort });
};

/**
 * 记录商品浏览历史
 * @param {number|string} productId - 商品ID
 * @returns {Promise} 返回操作结果
 */
export const addToHistory = (productId) => {
  return http.post('/history/add', null, { params: { productId } });
}; 