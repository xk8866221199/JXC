// 图表组件统一导出
export { default as SalesTrendChart } from './SalesTrendChart.vue'
export { default as CategoryPieChart } from './CategoryPieChart.vue'
export { default as InventoryBarChart } from './InventoryBarChart.vue'
export { default as FinanceGaugeChart } from './FinanceGaugeChart.vue'

// 图表组件类型定义
export interface SalesData {
  date: string
  sales: number
  profit: number
}

export interface CategoryData {
  name: string
  value: number
  percentage?: number
}

export interface InventoryData {
  name: string
  current: number
  min: number
  max: number
  status: 'normal' | 'warning' | 'danger'
}

export interface GaugeData {
  name: string
  value: number
  max: number
  unit: string
  color?: string
}
