import request from '@/api/auth'

/**
 * 用户管理API
 */

/**
 * 分页查询用户列表
 */
export function getUsersApi(params) {
  return request.get('/users', { params })
}

/**
 * 根据ID获取用户详情
 */
export function getUserByIdApi(id) {
  return request.get(`/users/${id}`)
}

/**
 * 创建用户
 */
export function createUserApi(data) {
  return request.post('/users', data)
}

/**
 * 更新用户
 */
export function updateUserApi(id, data) {
  return request.put(`/users/${id}`, data)
}

/**
 * 删除用户
 */
export function deleteUserApi(id) {
  return request.delete(`/users/${id}`)
}

/**
 * 重置用户密码
 */
export function resetUserPasswordApi(id, data) {
  return request.put(`/users/${id}/password`, data)
}

/**
 * 更新用户状态
 */
export function updateUserStatusApi(id, data) {
  return request.put(`/users/${id}/status`, data)
}

/**
 * 检查用户名是否已存在
 */
export function checkUsernameApi(username, excludeId) {
  return request.get('/users/check-username', {
    params: {
      username,
      excludeId
    }
  })
}