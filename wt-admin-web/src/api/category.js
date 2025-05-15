import request from '@/utils/request'

/**
 * 获取商品分类列表
 */
export function fetchCategoryList() {
  return request({
    url: '/api/admin/category/list',
    method: 'get'
  })
}

/**
 * 添加商品分类
 * @param {Object} data 分类数据
 */
export function createCategory(data) {
  return request({
    url: '/api/admin/category/add',
    method: 'post',
    data
  })
}

/**
 * 更新商品分类
 * @param {Object} data 分类数据
 */
export function updateCategory(data) {
  return request({
    url: '/api/admin/category/update',
    method: 'put',
    data
  })
}

/**
 * 删除商品分类
 * @param {Number} id 分类ID
 */
export function deleteCategory(id) {
  return request({
    url: `/api/admin/category/delete/${id}`,
    method: 'delete'
  })
} 