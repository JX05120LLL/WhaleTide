import { http } from '@/utils/request';

// 导入各模块API
import * as productApi from './product';
import * as cartApi from './cart';
import * as orderApi from './order';

// 用户相关API
const userApi = {
  // 用户登录
  login(data) {
    return http.post('/user/login', data);
  },
  
  // 用户注册
  register(data) {
    return http.post('/user/register', data);
  },
  
  // 获取用户信息
  getUserInfo() {
    return http.get('/user/info');
  },
  
  // 更新用户信息
  updateUserInfo(data) {
    return http.put('/user/info', data);
  },
  
  // 获取用户地址列表
  getUserAddresses() {
    return http.get('/user/addresses');
  },
  
  // 添加用户地址
  addUserAddress(data) {
    return http.post('/user/addresses', data);
  },
  
  // 更新用户地址
  updateUserAddress(id, data) {
    return http.put(`/user/addresses/${id}`, data);
  },
  
  // 删除用户地址
  deleteUserAddress(id) {
    return http.delete(`/user/addresses/${id}`);
  },
  
  // 设置默认地址
  setDefaultAddress(id) {
    return http.put(`/user/addresses/${id}/default`);
  },
  
  // 获取用户收藏列表
  getUserFavorites(params) {
    return http.get('/favorite/list', params);
  },
  
  // 添加收藏
  addToFavorites(productId) {
    return http.post('/favorite/add', { productId });
  },
  
  // 删除收藏
  removeFromFavorites(productId) {
    return http.delete(`/favorite/delete/${productId}`);
  },

  // 检查是否已收藏
  checkFavorite(productId) {
    return http.get(`/favorite/check/${productId}`);
  }
};

// 首页API
const homeApi = {
  // 获取首页轮播图
  getBanners() {
    return http.get('/home/banners');
  },
  
  // 获取推荐商品
  getRecommendProducts(params) {
    return http.get('/home/recommend', params);
  },
  
  // 获取新品上架
  getNewProducts(params) {
    return http.get('/home/new', params);
  },
  
  // 获取热销商品
  getHotProducts(params) {
    return http.get('/home/hot', params);
  },
  
  // 获取促销商品
  getPromotionProducts(params) {
    return http.get('/home/promotion', params);
  },
  
  // 获取限时特惠商品
  getDiscountProducts(params) {
    return http.get('/home/discount', params);
  },
  
  // 获取优选好物商品
  getSelectedProducts(params) {
    return http.get('/home/selected', params);
  }
};

// 历史记录API
const historyApi = {
  // 获取浏览历史列表
  getHistoryList(params) {
    return http.get('/history/list', params);
  },
  
  // 清空浏览历史
  clearHistory() {
    return http.delete('/history/clear');
  },
  
  // 删除指定浏览历史
  deleteHistory(ids) {
    return http.delete(`/history/delete/${ids}`);
  },
  
  // 添加浏览历史
  addHistory(productId) {
    return http.post('/history/add', null, { params: { productId } });
  },
  
  // 获取搜索历史列表
  getSearchHistory(limit = 10) {
    return http.get('/history/search', { limit });
  },
  
  // 添加搜索历史
  addSearchHistory(keyword) {
    return http.post('/history/search/add', null, { params: { keyword } });
  },
  
  // 清空搜索历史
  clearSearchHistory() {
    return http.delete('/history/search/clear');
  }
};

// 导出API
export default {
  user: userApi,
  product: productApi,
  cart: cartApi,
  order: orderApi,
  home: homeApi,
  history: historyApi,
}; 