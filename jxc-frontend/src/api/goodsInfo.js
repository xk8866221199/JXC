import request from '@/api/auth'

/**
 * 商品信息管理API
 */

/**
 * 分页查询商品信息列表
 */
export function getGoodsListApi(params) {
  return request.get('/goods/info', { params })
}

/**
 * 获取所有可用商品信息列表
 */
export function getAllGoodsListApi() {
  return request.get('/goods/info/all')
}

/**
 * 根据ID获取商品信息详情
 */
export function getGoodsByIdApi(id) {
  return request.get(`/goods/info/${id}`)
}

/**
 * 创建商品信息
 */
export function createGoodsApi(data) {
  return request.post('/goods/info', data)
}

/**
 * 更新商品信息
 */
export function updateGoodsApi(id, data) {
  return request.put(`/goods/info/${id}`, data)
}

/**
 * 删除商品信息
 */
export function deleteGoodsApi(id) {
  return request.delete(`/goods/info/${id}`)
}

/**
 * 更新商品信息状态
 */
export function updateGoodsStatusApi(id, data) {
  return request.put(`/goods/info/${id}/status`, data)
}