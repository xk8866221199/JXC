import request from '@/api/auth'

/**
 * 商品分类管理API
 */

/**
 * 分页查询商品分类列表
 */
export function getGoodsCategoriesApi(params) {
  return request.get('/goods/categories', { params })
}

/**
 * 获取所有可用商品分类列表
 */
export function getAllGoodsCategoriesApi() {
  return request.get('/goods/categories/all')
}

/**
 * 根据ID获取商品分类详情
 */
export function getGoodsCategoryByIdApi(id) {
  return request.get(`/goods/categories/${id}`)
}

/**
 * 创建商品分类
 */
export function createGoodsCategoryApi(data) {
  return request.post('/goods/categories', data)
}

/**
 * 更新商品分类
 */
export function updateGoodsCategoryApi(id, data) {
  return request.put(`/goods/categories/${id}`, data)
}

/**
 * 删除商品分类
 */
export function deleteGoodsCategoryApi(id) {
  return request.delete(`/goods/categories/${id}`)
}

/**
 * 更新商品分类状态
 */
export function updateGoodsCategoryStatusApi(id, data) {
  return request.put(`/goods/categories/${id}/status`, data)
}