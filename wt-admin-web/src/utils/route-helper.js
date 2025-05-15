/**
 * 路由辅助工具函数
 */

/**
 * 检查路径是否为详情页路径
 * @param {string} path 路径
 * @returns {boolean} 是否是详情页
 */
export function isDetailPage(path) {
  return path && path.includes('/detail/');
}

/**
 * 从URL中提取ID
 * @param {string} path 路径
 * @returns {string} ID
 */
export function extractIdFromPath(path) {
  if (!path || !path.includes('/detail/')) {
    return '';
  }
  // 提取ID (支持 /xxx/detail/123 或 /detail/123 的格式)
  const segments = path.split('/');
  const detailIndex = segments.indexOf('detail');
  
  if (detailIndex !== -1 && detailIndex < segments.length - 1) {
    return segments[detailIndex + 1];
  }
  return '';
}

/**
 * 格式化详情页路径
 * @param {string} module 模块名
 * @param {string} id ID
 * @returns {string} 标准化的路径
 */
export function formatDetailPath(module, id) {
  if (!module || !id) return '';
  return `/${module}/detail/${id}`;
}

/**
 * 获取模块类型
 * @param {string} path 路径
 * @returns {string} 模块类型
 */
export function getModuleType(path) {
  if (!path) return '';
  
  const modules = ['activity', 'product', 'order', 'character', 'user', 'message'];
  
  for (const module of modules) {
    if (path.includes(`/${module}/`)) {
      return module;
    }
  }
  
  // 默认返回活动模块
  return 'activity';
} 