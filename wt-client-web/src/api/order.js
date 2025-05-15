import { http } from '@/utils/request';

/**
 * 直接购买商品
 * @param {Object} data - 直接购买商品数据
 * @param {number} data.productId - 商品ID
 * @param {number} data.quantity - 商品数量
 * @param {number} data.userId - 用户ID (可选，从store获取)
 * @returns {Promise} 返回临时购物项ID
 */
export const directBuy = (data) => {
  console.log('调用directBuy API，参数:', data);
  
  // 从localStorage获取userId，确保API调用带上用户ID
  const userInfo = JSON.parse(localStorage.getItem('user') || '{}');
  if (userInfo && userInfo.id && !data.userId) {
    data.userId = userInfo.id;
    console.log('从localStorage添加userId:', data.userId);
  }
  
  return http.post('/order/directBuy', data)
    .then(response => {
      console.log('directBuy API响应:', response);
      return response;
    })
    .catch(error => {
      console.error('directBuy API错误:', error);
      throw error;
    });
};

/**
 * 生成确认订单
 * @param {Object} data - 确认订单数据
 * @param {Array} data.cartItemIds - 购物车项ID列表
 * @returns {Promise} 返回确认订单数据
 */
export const generateConfirmOrder = (data) => {
  return http.post('/order/generateConfirmOrder', data);
};

/**
 * 提交订单
 * @param {Object} data - 提交订单数据
 * @param {Array} data.cartItemIds - 购物车项ID列表
 * @param {number} data.addressId - 收货地址ID
 * @param {string} data.payType - 支付方式
 * @param {string} data.note - 订单备注
 * @returns {Promise} 返回生成的订单数据
 */
export const generateOrder = (data) => {
  return http.post('/order/generateOrder', data);
};

/**
 * 支付成功回调
 * @param {Object} data - 支付成功数据
 * @param {number} data.orderId - 订单ID
 * @param {number} data.payType - 支付方式：1-支付宝；2-微信支付；3-银联支付
 * @returns {Promise} 返回支付结果
 */
export const paySuccess = (data) => {
  return http.post('/order/paySuccess', data);
};

/**
 * 获取订单列表
 * @param {Object} params - 查询参数
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {number} params.status - 订单状态
 * @returns {Promise} 返回订单列表
 */
export const getOrderList = (params) => {
  return http.get('/order/list', { params });
};

/**
 * 获取订单详情
 * @param {number|string} orderId - 订单ID
 * @returns {Promise} 返回订单详情
 */
export const getOrderDetail = (orderId) => {
  return http.get(`/order/detail/${orderId}`);
};

/**
 * 取消订单
 * @param {Object} data - 取消订单数据
 * @param {number|string} data.orderId - 订单ID
 * @returns {Promise} 返回取消结果
 */
export const cancelOrder = (data) => {
  return http.post('/order/cancel', data);
};

/**
 * 确认收货
 * @param {Object} data - 确认收货数据
 * @param {number|string} data.orderId - 订单ID
 * @returns {Promise} 返回确认结果
 */
export const confirmReceiveOrder = (data) => {
  return http.post('/order/confirmReceive', data);
};

/**
 * 删除订单
 * @param {Object} data - 删除订单数据
 * @param {number|string} data.orderId - 订单ID
 * @returns {Promise} 返回删除结果
 */
export const deleteOrder = (data) => {
  return http.post('/order/delete', data);
};

/**
 * 创建订单
 * @param {Object} orderData - 订单数据
 * @param {Array} orderData.items - 商品列表
 * @param {Object} orderData.address - 收货地址
 * @param {string} orderData.paymentMethod - 支付方式
 * @param {string} orderData.couponCode - 优惠券代码
 * @returns {Promise} 返回创建的订单信息
 */
export const createOrder = (orderData) => {
  return http.post('/orders', orderData);
};

/**
 * 申请退款
 * @param {number|string} orderId - 订单ID
 * @param {Object} refundData - 退款数据
 * @param {string} refundData.reason - 退款原因
 * @param {string} refundData.description - 问题描述
 * @param {Array} refundData.images - 图片证据
 * @returns {Promise} 返回申请结果
 */
export const applyRefund = (orderId, refundData) => {
  return http.post(`/orders/${orderId}/refund`, refundData);
};

/**
 * 获取订单物流信息
 * @param {number|string} orderId - 订单ID
 * @returns {Promise} 返回物流信息
 */
export const getOrderTracking = (orderId) => {
  return http.get(`/orders/${orderId}/tracking`);
};

/**
 * 延长收货时间
 * @param {number|string} orderId - 订单ID
 * @param {number} days - 延长天数
 * @returns {Promise} 返回延长结果
 */
export const extendReceivingTime = (orderId, days) => {
  return http.put(`/orders/${orderId}/extend-receiving`, { days });
};

/**
 * 获取可用于结算的地址列表
 * @returns {Promise} 返回地址列表
 */
export const getCheckoutAddresses = () => {
  return http.get('/user/addresses');
};

/**
 * 获取可用优惠券
 * @returns {Promise} 返回优惠券列表
 */
export const getAvailableCoupons = () => {
  return http.get('/user/coupons/available');
}; 