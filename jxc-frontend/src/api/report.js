import request from './auth'

/**
 * 销售统计API
 */

/**
 * 分页查询销售统计列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getSalesStatistics(params) {
  return request.get('/reports/sales/statistics', { params })
}

/**
 * 获取销售趋势数据
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getSalesTrend(params) {
  return request.get('/reports/sales/trend', { params })
}

/**
 * 获取商品销售排行
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 * @param {number} params.limit - 数量限制
 */
export function getProductSalesRanking(params) {
  return request.get('/reports/sales/ranking', { params })
}

/**
 * 库存报表API
 */

/**
 * 分页查询库存报表列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @param {number} params.categoryId - 分类ID
 * @param {string} params.keyword - 关键字
 */
export function getInventoryReport(params) {
  return request.get('/reports/inventory', { params })
}

/**
 * 获取库存预警列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 */
export function getInventoryWarningReport(params) {
  return request.get('/reports/inventory/warning', { params })
}

/**
 * 获取库存周转率统计
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getInventoryTurnoverRate(params) {
  return request.get('/reports/inventory/turnover', { params })
}

/**
 * 财务统计API
 */

/**
 * 分页查询财务统计列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getFinanceStatistics(params) {
  return request.get('/reports/finance', { params })
}

/**
 * 获取应收统计
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getReceivableStatistics(params) {
  return request.get('/reports/finance/receivable', { params })
}

/**
 * 获取应付统计
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getPayableStatistics(params) {
  return request.get('/reports/finance/payable', { params })
}