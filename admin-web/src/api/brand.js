import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/brand/list',
    method: 'get',
    params: params
  })
}

export function createBrand(data) {
  return request({
    url: '/brand/create',
    method: 'post',
    data: data
  })
}

export function updateShowStatus(id, showStatus) {
  return request({
    url: '/brand/' + id + '/showStatus/' + showStatus,
    method: 'post',
  })
}

export function updateFactoryStatus(id, status) {
  return request({
    url: `/brand/${id}/factoryStatus/${status}`,
    method: 'post',
  })
}

export function deleteBrand(id) {
  return request({
    url: '/brand/delete/' + id,
    method: 'get',
  })
}

export function getBrand(id) {
  return request({
    url: '/brand/' + id,
    method: 'get',
  })
}

export function updateBrand(id, data) {
  return request({
    url: '/brand/update/' + id,
    method: 'post',
    data: data
  })
}

