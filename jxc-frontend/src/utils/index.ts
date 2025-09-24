// 工具函数统一导出
export * from './common'
export * from './performance'

// 常用类型定义
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  success: boolean
}

export interface PaginationParams {
  current: number
  pageSize: number
  total?: number
}

export interface TableColumn {
  title: string
  dataIndex?: string
  key: string
  width?: number | string
  align?: 'left' | 'center' | 'right'
  fixed?: 'left' | 'right'
  sorter?: boolean
  filters?: Array<{ text: string; value: any }>
}

export interface FormRule {
  required?: boolean
  message?: string
  trigger?: string | string[]
  validator?: (rule: any, value: any, callback: (error?: string) => void) => void
}
