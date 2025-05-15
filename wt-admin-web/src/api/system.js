import request from '@/utils/request'

// 系统设置
export function getSettings() {
  return request({
    url: '/config/list',
    method: 'get'
  })
}

export function createSetting(data) {
  return request({
    url: '/config/save',
    method: 'post',
    data
  })
}

export function updateSetting(data) {
  return request({
    url: '/config/save',
    method: 'post',
    data
  })
}

export function deleteSetting(id) {
  return request({
    url: `/config/${id}`,
    method: 'delete'
  })
}

// 系统日志
export function getLogs(query) {
  return request({
    url: '/log/list',
    method: 'get',
    params: query
  })
}

export function clearLogs() {
  return request({
    url: '/log/clear',
    method: 'delete'
  })
} 