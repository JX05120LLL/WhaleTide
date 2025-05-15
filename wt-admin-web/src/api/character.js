import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/character/list',
    method: 'get',
    params: query
  })
}

export function fetchCharacter(id) {
  return request({
    url: `/api/character/detail/${id}`,
    method: 'get'
  })
}

export function createCharacter(data) {
  return request({
    url: '/api/character/add',
    method: 'post',
    data
  })
}

export function updateCharacter(data) {
  return request({
    url: '/api/character/update',
    method: 'put',
    data
  })
}

export function deleteCharacter(id) {
  return request({
    url: `/api/character/delete/${id}`,
    method: 'delete'
  })
}

export function fetchCharacterList(params) {
  return request({
    url: '/api/character/list',
    method: 'get',
    params
  })
} 