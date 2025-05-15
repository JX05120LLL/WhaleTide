import { http } from '@/utils/request';

/**
 * 获取购物车列表
 * @returns {Promise} 返回购物车数据
 */
export const getCartList = () => {
  return http.get('/cart/list');
};

/**
 * 添加商品到购物车
 * @param {Object} productData - 商品数据
 * @param {number|string} productData.productId - 商品ID
 * @param {number} productData.quantity - 数量
 * @returns {Promise} 返回添加结果
 */
export const addToCart = (productData) => {
  return http.post('/cart/add', productData);
};

/**
 * 更新购物车商品数量
 * @param {Object} updateData - 更新数据
 * @param {number|string} updateData.id - 购物车项ID
 * @param {number} updateData.quantity - 新数量
 * @returns {Promise} 返回更新结果
 */
export const updateCartItemQuantity = (updateData) => {
  return http.put('/cart/update/quantity', updateData);
};

/**
 * 更新购物车商品选中状态
 * @param {Object} updateData - 更新数据
 * @param {number|string} updateData.id - 购物车项ID
 * @param {number} updateData.checked - 选中状态 (0未选中, 1选中)
 * @returns {Promise} 返回更新结果
 */
export const updateCartItemChecked = (updateData) => {
  return http.put('/cart/update/checked', updateData);
};

/**
 * 删除购物车商品
 * @param {Array<number|string>} ids - 购物车项ID数组
 * @returns {Promise} 返回删除结果
 */
export const removeCartItems = (ids) => {
  return http.delete(`/cart/delete/${ids.join(',')}`);
};

/**
 * 清空购物车
 * @returns {Promise} 返回清空结果
 */
export const clearCart = () => {
  return http.delete('/cart/clear');
};

/**
 * 获取购物车商品数量
 * @returns {Promise} 返回购物车商品数量
 */
export const getCartCount = () => {
  return http.get('/cart/count');
};

/**
 * 选择/取消选择购物车项
 * @param {number|string} cartItemId - 购物车项ID
 * @param {boolean} selected - 是否选中
 * @returns {Promise} 返回更新结果
 */
export const selectCartItem = (cartItemId, selected) => {
  return http.put(`/cart/items/${cartItemId}/select`, { selected });
};

/**
 * 全选/取消全选购物车
 * @param {boolean} selected - 是否全选
 * @returns {Promise} 返回更新结果
 */
export const selectAllCartItems = (selected) => {
  return http.put('/cart/select-all', { selected });
};

/**
 * 获取购物车结算信息
 * @returns {Promise} 返回结算信息
 */
export const getCartCheckoutInfo = () => {
  return http.get('/cart/checkout-info');
}; 