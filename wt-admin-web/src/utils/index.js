/**
 * 日期格式化工具
 * @param {Date} date 日期对象
 * @param {String} fmt 格式字符串，默认yyyy-MM-dd HH:mm:ss
 * @returns {String} 格式化后的日期字符串
 */
export function formatDate(date, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!date) return '';
  if (typeof date === 'string') {
    date = new Date(date.replace(/-/g, '/'));
  }
  
  const o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'H+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  };
  
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    }
  }
  
  return fmt;
}

/**
 * 时间格式化
 * @param {Date|String} time 时间对象或时间字符串
 * @returns {String} 格式化后的时间字符串（yyyy-MM-dd HH:mm:ss）
 */
export function formatTime(time) {
  if (!time) return '';
  return formatDate(time);
}

/**
 * 解析时间
 * @param {String} timeStr 时间字符串
 * @returns {String} 格式化后的时间字符串
 */
export function parseTime(timeStr) {
  if (!timeStr) return '';
  const date = new Date(timeStr.replace(/-/g, '/'));
  return formatDate(date);
}

/**
 * 获取URL查询参数对象
 * @param {String} url URL字符串
 * @returns {Object} 参数对象
 */
export function getQueryObject(url) {
  url = url || window.location.href;
  const search = url.substring(url.lastIndexOf('?') + 1);
  const obj = {};
  const reg = /([^?&=]+)=([^?&=]*)/g;
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1);
    let val = decodeURIComponent($2);
    val = String(val);
    obj[name] = val;
    return rs;
  });
  return obj;
}

/**
 * 防抖函数
 * @param {Function} func 要执行的函数
 * @param {Number} wait 等待时间（毫秒）
 * @returns {Function} 防抖后的函数
 */
export function debounce(func, wait) {
  let timeout;
  return function() {
    const context = this;
    const args = arguments;
    clearTimeout(timeout);
    timeout = setTimeout(() => {
      func.apply(context, args);
    }, wait);
  };
}

/**
 * 节流函数
 * @param {Function} func 要执行的函数
 * @param {Number} limit 限制时间（毫秒）
 * @returns {Function} 节流后的函数
 */
export function throttle(func, limit) {
  let inThrottle;
  return function() {
    const context = this;
    const args = arguments;
    if (!inThrottle) {
      func.apply(context, args);
      inThrottle = true;
      setTimeout(() => {
        inThrottle = false;
      }, limit);
    }
  };
}

/**
 * 解析后端返回的LocalDateTime格式时间
 * 格式示例：2023-07-15T15:30:45 或 2023-07-15T15:30:45.123456
 * @param {String} dateTimeStr LocalDateTime格式时间字符串
 * @param {String} fmt 格式化字符串，默认yyyy-MM-dd HH:mm:ss
 * @returns {String} 格式化后的时间字符串
 */
export function formatLocalDateTime(dateTimeStr, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!dateTimeStr) return '';
  
  // 处理带毫秒的格式和不带毫秒的格式
  let processedStr = dateTimeStr;
  
  // 如果是标准ISO格式，直接创建Date对象
  if (dateTimeStr.includes('Z') || dateTimeStr.includes('+')) {
    return formatDate(new Date(dateTimeStr), fmt);
  }
  
  // 处理标准的LocalDateTime格式(去掉可能的毫秒部分)
  if (dateTimeStr.includes('T')) {
    // 去掉毫秒部分
    processedStr = dateTimeStr.split('.')[0];
    // 替换T为空格以便正确解析
    processedStr = processedStr.replace('T', ' ');
  }
  
  // 将处理后的字符串转换为日期对象
  try {
    const date = new Date(processedStr.replace(/-/g, '/'));
    if (isNaN(date.getTime())) {
      console.warn('无效的日期格式:', dateTimeStr);
      return dateTimeStr;
    }
    return formatDate(date, fmt);
  } catch (error) {
    console.error('日期解析错误:', error);
    return dateTimeStr;
  }
} 