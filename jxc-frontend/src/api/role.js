import request from '@/api/auth'

/**
 * 角色管理API
 */

/**
 * 分页查询角色列表
 */
export function getRolesApi(params) {
  return request.get('/roles', { params })
}

/**
 * 根据ID获取角色详情
 */
export function getRoleByIdApi(id) {
  return request.get(`/roles/${id}`)
}

/**
 * 创建角色
 */
export function createRoleApi(data) {
  return request.post('/roles', data)
}

/**
 * 更新角色
 */
export function updateRoleApi(id, data) {
  return request.put(`/roles/${id}`, data)
}

/**
 * 删除角色
 */
export function deleteRoleApi(id) {
  return request.delete(`/roles/${id}`)
}

/**
 * 更新角色状态
 */
export function updateRoleStatusApi(id, data) {
  return request.put(`/roles/${id}/status`, data)
}

/**
 * 获取角色权限列表
 */
export function getRolePermissionsApi(id) {
  return request.get(`/roles/${id}/permissions`)
}

/**
 * 更新角色权限
 */
export function updateRolePermissionsApi(id, data) {
  return request.put(`/roles/${id}/permissions`, data)
}

/**
 * 获取所有可用角色列表（不分页）
 */
export function getAllRolesApi() {
  return request.get('/roles/all')
}