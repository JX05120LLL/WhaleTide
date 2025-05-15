/**
 * 安全的深拷贝函数，可以处理循环引用
 * @param {*} obj - 需要深拷贝的对象
 * @returns {*} - 拷贝后的新对象
 */
export function deepClone(obj) {
  // 处理基本类型和null
  if (obj === null || typeof obj !== 'object') {
    return obj;
  }
  
  // 使用WeakMap记录已拷贝过的对象，避免循环引用
  const visited = new WeakMap();
  
  function clone(target) {
    // 基本类型直接返回
    if (target === null || typeof target !== 'object') {
      return target;
    }
    
    // 处理Date
    if (target instanceof Date) {
      return new Date(target.getTime());
    }
    
    // 处理RegExp
    if (target instanceof RegExp) {
      return new RegExp(target);
    }
    
    // 处理已访问过的对象（处理循环引用）
    if (visited.has(target)) {
      return visited.get(target);
    }
    
    // 根据不同类型创建空对象
    let result;
    if (Array.isArray(target)) {
      result = [];
    } else {
      result = Object.create(Object.getPrototypeOf(target));
    }
    
    // 将对象标记为已访问
    visited.set(target, result);
    
    // 处理Map
    if (target instanceof Map) {
      const mapResult = new Map();
      target.forEach((value, key) => {
        mapResult.set(clone(key), clone(value));
      });
      return mapResult;
    }
    
    // 处理Set
    if (target instanceof Set) {
      const setResult = new Set();
      target.forEach(value => {
        setResult.add(clone(value));
      });
      return setResult;
    }
    
    // 递归处理所有属性
    const keys = Array.isArray(target) 
      ? Array.from({ length: target.length }, (_, i) => i) 
      : Object.keys(target);
    
    for (const key of keys) {
      if (Object.prototype.hasOwnProperty.call(target, key)) {
        result[key] = clone(target[key]);
      }
    }
    
    return result;
  }
  
  return clone(obj);
}

/**
 * 安全的JSON解析函数
 * @param {string} json - JSON字符串
 * @param {*} defaultValue - 解析失败时的默认返回值
 * @returns {*} - 解析结果或默认值
 */
export function safeJSONParse(json, defaultValue = null) {
  if (!json) return defaultValue;
  try {
    return JSON.parse(json);
  } catch (e) {
    console.error('JSON解析错误:', e);
    return defaultValue;
  }
}

/**
 * 安全的JSON序列化函数
 * @param {*} data - 要序列化的数据
 * @param {string} defaultValue - 序列化失败时的默认返回值
 * @returns {string} - 序列化结果或默认值
 */
export function safeJSONStringify(data, defaultValue = '{}') {
  try {
    // 先进行深拷贝处理循环引用
    const cloned = deepClone(data);
    return JSON.stringify(cloned);
  } catch (e) {
    console.error('JSON序列化错误:', e);
    return defaultValue;
  }
}

export default {
  deepClone,
  safeJSONParse,
  safeJSONStringify
}; 