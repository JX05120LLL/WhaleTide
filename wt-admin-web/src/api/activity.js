import request from '@/utils/request'

/**
 * 获取活动列表
 * @param {Object} query 查询参数
 */
export function fetchList(query) {
  console.log('获取活动列表，参数:', query);
  return request({
    url: '/api/activity/list',
    method: 'get',
    params: query
  })
  .then(response => {
    console.log('活动列表获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取活动列表失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 获取活动详情
 * @param {Number} id 活动ID
 */
export function fetchActivity(id) {
  console.log('获取活动详情，ID:', id);
  return request({
    url: `/api/activity/detail/${id}`,
    method: 'get'
  })
  .then(response => {
    console.log('活动详情获取成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取活动详情失败:', error);
    return Promise.reject(error);
  });
}

/**
 * 创建活动
 * @param {Object} data 活动数据
 */
export function createActivity(data) {
  console.log('创建活动，数据:', data);
  
  // 确保description字段使用content提交
  const submitData = { ...data };
  if (submitData.description && !submitData.content) {
    submitData.content = submitData.description;
    delete submitData.description;
  }
  
  // 格式化日期时间
  if (submitData.startTime) {
    if (submitData.startTime instanceof Date) {
      // 格式化为"yyyy-MM-dd HH:mm:ss"格式
      submitData.startTime = formatDateTime(submitData.startTime);
    }
    console.log('格式化后的开始时间:', submitData.startTime);
  }
  
  if (submitData.endTime) {
    if (submitData.endTime instanceof Date) {
      // 格式化为"yyyy-MM-dd HH:mm:ss"格式
      submitData.endTime = formatDateTime(submitData.endTime);
    }
    console.log('格式化后的结束时间:', submitData.endTime);
  }
  
  // 将数字状态转换为布尔值
  // 数据库中status是布尔类型，前端使用的是数字(0已结束,1进行中,2未开始)
  if (submitData.status !== undefined) {
    console.log('提交前status值:', submitData.status);
    // 0表示已结束，对应false
    // 1和2都表示活动有效，对应true
    submitData.status = submitData.status !== 0;
    console.log('提交后status值(转为布尔):', submitData.status);
  }
  
  return request({
    url: '/api/activity/add',
    method: 'post',
    data: submitData
  })
  .then(response => {
    console.log('活动创建成功:', response);
    return response;
  })
  .catch(error => {
    console.error('创建活动失败:', error);
    return Promise.reject(error);
  });
}

// 日期时间格式化函数
function formatDateTime(date) {
  if (!date) return '';
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 更新活动
 * @param {Object} data 活动数据
 */
export function updateActivity(data) {
  console.log('更新活动，数据:', data);
  
  // 确保description字段使用content提交
  const submitData = { ...data };
  if (submitData.description && !submitData.content) {
    submitData.content = submitData.description;
    delete submitData.description;
  }
  
  // 格式化日期时间
  if (submitData.startTime) {
    if (submitData.startTime instanceof Date) {
      // 格式化为"yyyy-MM-dd HH:mm:ss"格式
      submitData.startTime = formatDateTime(submitData.startTime);
    }
    console.log('格式化后的开始时间:', submitData.startTime);
  }
  
  if (submitData.endTime) {
    if (submitData.endTime instanceof Date) {
      // 格式化为"yyyy-MM-dd HH:mm:ss"格式
      submitData.endTime = formatDateTime(submitData.endTime);
    }
    console.log('格式化后的结束时间:', submitData.endTime);
  }
  
  // 将数字状态转换为布尔值
  // 数据库中status是布尔类型，前端使用的是数字(0已结束,1进行中,2未开始)
  if (submitData.status !== undefined) {
    console.log('提交前status值:', submitData.status, typeof submitData.status);
    // 0表示已结束，对应false
    // 1和2都表示活动有效，对应true
    submitData.status = submitData.status !== 0;
    console.log('提交后status值(转为布尔):', submitData.status, typeof submitData.status);
  }
  
  return request({
    url: '/api/activity/update',
    method: 'put',
    data: submitData
  })
  .then(response => {
    console.log('活动更新成功:', response);
    return response;
  })
  .catch(error => {
    console.error('更新活动失败:', error);
    return Promise.reject(error);
  });
}

// 其他API保持一致的格式
export function deleteActivity(id) {
  console.log('删除活动，ID:', id);
  return request({
    url: `/api/activity/delete/${id}`,
    method: 'delete'
  })
  .then(response => {
    console.log('活动删除成功:', response);
    return response;
  })
  .catch(error => {
    console.error('删除活动失败:', error);
    return Promise.reject(error);
  });
}

export function getActivityParticipants(id) {
  console.log('获取活动参与者，活动ID:', id);
  return request({
    url: `/api/activity/participants/${id}`,
    method: 'get'
  })
  .then(response => {
    console.log('获取活动参与者成功:', response);
    return response;
  })
  .catch(error => {
    console.error('获取活动参与者失败:', error);
    return Promise.reject(error);
  });
}

export function removeParticipant(data) {
  console.log('移除活动参与者，数据:', data);
  return request({
    url: `/api/activity/cancel/${data.activityId}`,
    method: 'put',
    data
  })
  .then(response => {
    console.log('移除活动参与者成功:', response);
    return response;
  })
  .catch(error => {
    console.error('移除活动参与者失败:', error);
    return Promise.reject(error);
  });
}

export function joinActivity(id) {
  console.log('参与活动，ID:', id);
  return request({
    url: `/api/activity/join/${id}`,
    method: 'post'
  })
  .then(response => {
    console.log('参与活动成功:', response);
    return response;
  })
  .catch(error => {
    console.error('参与活动失败:', error);
    return Promise.reject(error);
  });
}

export function changeActivityStatus(id, status) {
  console.log('更改活动状态，ID:', id, '状态:', status);
  return request({
    url: '/api/activity/status',
    method: 'put',
    data: { id, status }
  })
  .then(response => {
    console.log('更改活动状态成功:', response);
    return response;
  })
  .catch(error => {
    console.error('更改活动状态失败:', error);
    return Promise.reject(error);
  });
} 