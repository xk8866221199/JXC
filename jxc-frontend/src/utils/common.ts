/**
 * 通用工具函数
 */

// 深拷贝
export const deepClone = <T>(obj: T): T => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime()) as unknown as T
  if (obj instanceof Array) return obj.map(item => deepClone(item)) as unknown as T
  if (typeof obj === 'object') {
    const clonedObj = {} as { [key: string]: any }
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key])
      }
    }
    return clonedObj as T
  }
  return obj
}

// 格式化金额
export const formatCurrency = (amount: number, currency = '¥', decimals = 2): string => {
  return `${currency}${amount.toLocaleString('zh-CN', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals
  })}`
}

// 格式化数字
export const formatNumber = (num: number, decimals = 0): string => {
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals
  })
}

// 格式化百分比
export const formatPercent = (value: number, decimals = 1): string => {
  return `${(value * 100).toFixed(decimals)}%`
}

// 格式化文件大小
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 生成随机字符串
export const generateRandomString = (length = 8): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 获取文件扩展名
export const getFileExtension = (filename: string): string => {
  return filename.slice(((filename.lastIndexOf('.') - 1) >>> 0) + 2)
}

// 验证邮箱
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// 验证手机号
export const isValidPhone = (phone: string): boolean => {
  const phoneRegex = /^1[3-9]\d{9}$/
  return phoneRegex.test(phone)
}

// 验证身份证号
export const isValidIdCard = (idCard: string): boolean => {
  const idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return idCardRegex.test(idCard)
}

// 获取URL参数
export const getUrlParams = (url: string = window.location.href): Record<string, string> => {
  const params: Record<string, string> = {}
  const urlParams = new URLSearchParams(url.split('?')[1])
  urlParams.forEach((value, key) => {
    params[key] = value
  })
  return params
}

// 下载文件
export const downloadFile = (blob: Blob, filename: string): void => {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}

// 复制到剪贴板
export const copyToClipboard = async (text: string): Promise<boolean> => {
  try {
    if (navigator.clipboard) {
      await navigator.clipboard.writeText(text)
      return true
    } else {
      // 兼容旧浏览器
      const textArea = document.createElement('textarea')
      textArea.value = text
      document.body.appendChild(textArea)
      textArea.select()
      const successful = document.execCommand('copy')
      document.body.removeChild(textArea)
      return successful
    }
  } catch (error) {
    console.error('复制失败:', error)
    return false
  }
}

// 获取设备信息
export const getDeviceInfo = () => {
  const ua = navigator.userAgent
  return {
    isMobile: /Mobile|Android|iPhone|iPad/.test(ua),
    isIOS: /iPhone|iPad|iPod/.test(ua),
    isAndroid: /Android/.test(ua),
    isChrome: /Chrome/.test(ua),
    isSafari: /Safari/.test(ua) && !/Chrome/.test(ua),
    isFirefox: /Firefox/.test(ua),
    isEdge: /Edg/.test(ua)
  }
}

// 日期时间工具
export const dateUtils = {
  // 格式化日期
  format: (date: Date | string | number, format = 'YYYY-MM-DD HH:mm:ss'): string => {
    const d = new Date(date)
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    const hours = String(d.getHours()).padStart(2, '0')
    const minutes = String(d.getMinutes()).padStart(2, '0')
    const seconds = String(d.getSeconds()).padStart(2, '0')

    return format
      .replace('YYYY', year.toString())
      .replace('MM', month)
      .replace('DD', day)
      .replace('HH', hours)
      .replace('mm', minutes)
      .replace('ss', seconds)
  },

  // 获取相对时间
  getRelativeTime: (date: Date | string | number): string => {
    const now = new Date()
    const target = new Date(date)
    const diff = now.getTime() - target.getTime()

    const minute = 60 * 1000
    const hour = 60 * minute
    const day = 24 * hour
    const month = 30 * day
    const year = 365 * day

    if (diff < minute) return '刚刚'
    if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
    if (diff < day) return `${Math.floor(diff / hour)}小时前`
    if (diff < month) return `${Math.floor(diff / day)}天前`
    if (diff < year) return `${Math.floor(diff / month)}个月前`
    return `${Math.floor(diff / year)}年前`
  }
}

// 存储工具
export const storage = {
  // localStorage
  local: {
    set: (key: string, value: any): void => {
      try {
        localStorage.setItem(key, JSON.stringify(value))
      } catch (error) {
        console.error('localStorage设置失败:', error)
      }
    },
    get: <T>(key: string, defaultValue: T | null = null): T | null => {
      try {
        const item = localStorage.getItem(key)
        return item ? JSON.parse(item) : defaultValue
      } catch (error) {
        console.error('localStorage获取失败:', error)
        return defaultValue
      }
    },
    remove: (key: string): void => {
      try {
        localStorage.removeItem(key)
      } catch (error) {
        console.error('localStorage删除失败:', error)
      }
    },
    clear: (): void => {
      try {
        localStorage.clear()
      } catch (error) {
        console.error('localStorage清空失败:', error)
      }
    }
  },

  // sessionStorage
  session: {
    set: (key: string, value: any): void => {
      try {
        sessionStorage.setItem(key, JSON.stringify(value))
      } catch (error) {
        console.error('sessionStorage设置失败:', error)
      }
    },
    get: <T>(key: string, defaultValue: T | null = null): T | null => {
      try {
        const item = sessionStorage.getItem(key)
        return item ? JSON.parse(item) : defaultValue
      } catch (error) {
        console.error('sessionStorage获取失败:', error)
        return defaultValue
      }
    },
    remove: (key: string): void => {
      try {
        sessionStorage.removeItem(key)
      } catch (error) {
        console.error('sessionStorage删除失败:', error)
      }
    },
    clear: (): void => {
      try {
        sessionStorage.clear()
      } catch (error) {
        console.error('sessionStorage清空失败:', error)
      }
    }
  }
}
