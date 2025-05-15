import request from '@/utils/request'

export function fetchMessageList(query) {
  return request({
    url: '/message/list',
    method: 'get',
    params: query
  })
}

export function replyMessage(data) {
  return request({
    url: '/message/reply',
    method: 'put',
    data
  })
}

export function deleteMessage(id) {
  return request({
    url: `/message/${id}`,
    method: 'delete'
  })
} 