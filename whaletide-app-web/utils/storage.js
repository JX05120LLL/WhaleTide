/**
 * 存储工具类
 */

// 搜索历史的存储键名
const SEARCH_HISTORY_KEY = 'SEARCH_HISTORY';

/**
 * 获取搜索历史
 * @returns {Array} 搜索历史列表
 */
export function getSearchHistory() {
  try {
    const history = uni.getStorageSync(SEARCH_HISTORY_KEY);
    return history ? JSON.parse(history) : [];
  } catch (e) {
    console.error('获取搜索历史失败', e);
    return [];
  }
}

/**
 * 保存搜索历史
 * @param {Array} history 搜索历史列表
 */
export function saveSearchHistory(history) {
  try {
    uni.setStorageSync(SEARCH_HISTORY_KEY, JSON.stringify(history));
  } catch (e) {
    console.error('保存搜索历史失败', e);
  }
}

/**
 * 清除搜索历史
 */
export function clearSearchHistory() {
  try {
    uni.removeStorageSync(SEARCH_HISTORY_KEY);
  } catch (e) {
    console.error('清除搜索历史失败', e);
  }
}

/**
 * 保存数据到本地存储
 * @param {String} key 键名
 * @param {any} data 数据
 */
export function saveData(key, data) {
  try {
    uni.setStorageSync(key, typeof data === 'object' ? JSON.stringify(data) : data);
  } catch (e) {
    console.error(`保存数据[${key}]失败`, e);
  }
}

/**
 * 从本地存储获取数据
 * @param {String} key 键名
 * @param {Boolean} isObject 是否为对象
 * @returns {any} 存储的数据
 */
export function getData(key, isObject = true) {
  try {
    const data = uni.getStorageSync(key);
    if (data && isObject) {
      return JSON.parse(data);
    }
    return data;
  } catch (e) {
    console.error(`获取数据[${key}]失败`, e);
    return isObject ? null : '';
  }
}

/**
 * 从本地存储删除数据
 * @param {String} key 键名
 */
export function removeData(key) {
  try {
    uni.removeStorageSync(key);
  } catch (e) {
    console.error(`删除数据[${key}]失败`, e);
  }
}

/**
 * 清除所有本地存储
 */
export function clearStorage() {
  try {
    uni.clearStorageSync();
  } catch (e) {
    console.error('清除所有存储失败', e);
  }
} 