import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/admin/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/admin/user/info',
    method: 'get',
    params: { token },
    headers: {
      'Authorization': `Bearer ${token}`,
      'X-Access-Token': token,
      'Admin-Token': token
    }
  })
}

export function logout() {
  return request({
    url: '/api/admin/user/logout',
    method: 'post'
  })
}

// 以下是用户管理相关接口

export function fetchUserList(query) {
  return request({
    url: '/api/admin/user/list',
    method: 'get',
    params: query
  })
}

export function fetchUserDetail(id) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'get'
  })
}

export function updateUser(data) {
  return request({
    url: '/api/admin/user/update',
    method: 'put',
    data
  })
}

export function changeUserStatus(data) {
  return request({
    url: '/api/admin/user/status',
    method: 'put',
    data
  })
}

export function resetUserPassword(data) {
  return request({
    url: '/api/admin/user/reset-password',
    method: 'put',
    data
  })
} 