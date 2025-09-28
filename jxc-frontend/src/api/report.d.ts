declare module '@/api/report.js' {
  // 销售统计API
  export function getSalesStatistics(params: any): Promise<any>
  export function getSalesTrend(params: any): Promise<any>
  export function getProductSalesRanking(params: any): Promise<any>
  
  // 库存报表API
  export function getInventoryReport(params: any): Promise<any>
  export function getInventoryWarningReport(params: any): Promise<any>
  export function getInventoryTurnoverRate(params: any): Promise<any>
  
  // 财务统计API
  export function getFinanceStatistics(params: any): Promise<any>
  export function getReceivableStatistics(params: any): Promise<any>
  export function getPayableStatistics(params: any): Promise<any>
}