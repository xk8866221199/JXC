import { describe, it, expect } from 'vitest'
import {
  formatCurrency,
  formatNumber,
  formatPercent,
  formatFileSize,
  generateRandomString,
  isValidEmail,
  isValidPhone,
  deepClone,
  dateUtils
} from '@/utils/common'

describe('Common Utils', () => {
  describe('formatCurrency', () => {
    it('should format currency correctly', () => {
      expect(formatCurrency(1234.56)).toBe('¥1,234.56')
      expect(formatCurrency(1234.56, '$')).toBe('$1,234.56')
      expect(formatCurrency(1234.567, '¥', 1)).toBe('¥1,234.6')
    })
  })

  describe('formatNumber', () => {
    it('should format number correctly', () => {
      expect(formatNumber(1234.56)).toBe('1,235')
      expect(formatNumber(1234.56, 2)).toBe('1,234.56')
    })
  })

  describe('formatPercent', () => {
    it('should format percentage correctly', () => {
      expect(formatPercent(0.1234)).toBe('12.3%')
      expect(formatPercent(0.1234, 2)).toBe('12.34%')
    })
  })

  describe('formatFileSize', () => {
    it('should format file size correctly', () => {
      expect(formatFileSize(0)).toBe('0 B')
      expect(formatFileSize(1024)).toBe('1 KB')
      expect(formatFileSize(1024 * 1024)).toBe('1 MB')
      expect(formatFileSize(1024 * 1024 * 1024)).toBe('1 GB')
    })
  })

  describe('generateRandomString', () => {
    it('should generate random string with correct length', () => {
      expect(generateRandomString().length).toBe(8)
      expect(generateRandomString(10).length).toBe(10)
    })

    it('should generate different strings', () => {
      const str1 = generateRandomString()
      const str2 = generateRandomString()
      expect(str1).not.toBe(str2)
    })
  })

  describe('isValidEmail', () => {
    it('should validate email correctly', () => {
      expect(isValidEmail('test@example.com')).toBe(true)
      expect(isValidEmail('user.name+tag@domain.co.uk')).toBe(true)
      expect(isValidEmail('invalid-email')).toBe(false)
      expect(isValidEmail('test@')).toBe(false)
      expect(isValidEmail('@example.com')).toBe(false)
    })
  })

  describe('isValidPhone', () => {
    it('should validate Chinese phone number correctly', () => {
      expect(isValidPhone('13800138000')).toBe(true)
      expect(isValidPhone('15912345678')).toBe(true)
      expect(isValidPhone('12345678901')).toBe(false)
      expect(isValidPhone('1380013800')).toBe(false)
      expect(isValidPhone('138001380000')).toBe(false)
    })
  })

  describe('deepClone', () => {
    it('should clone primitive values', () => {
      expect(deepClone(42)).toBe(42)
      expect(deepClone('hello')).toBe('hello')
      expect(deepClone(true)).toBe(true)
      expect(deepClone(null)).toBe(null)
    })

    it('should clone arrays', () => {
      const arr = [1, 2, { a: 3 }]
      const cloned = deepClone(arr)
      expect(cloned).toEqual(arr)
      expect(cloned).not.toBe(arr)
      expect(cloned[2]).not.toBe(arr[2])
    })

    it('should clone objects', () => {
      const obj = { a: 1, b: { c: 2 } }
      const cloned = deepClone(obj)
      expect(cloned).toEqual(obj)
      expect(cloned).not.toBe(obj)
      expect(cloned.b).not.toBe(obj.b)
    })

    it('should clone dates', () => {
      const date = new Date('2024-01-01')
      const cloned = deepClone(date)
      expect(cloned).toEqual(date)
      expect(cloned).not.toBe(date)
    })
  })

  describe('dateUtils', () => {
    describe('format', () => {
      it('should format date correctly', () => {
        const date = new Date('2024-01-01 12:34:56')
        expect(dateUtils.format(date, 'YYYY-MM-DD')).toBe('2024-01-01')
        expect(dateUtils.format(date, 'YYYY-MM-DD HH:mm:ss')).toBe('2024-01-01 12:34:56')
      })
    })

    describe('getRelativeTime', () => {
      it('should return relative time correctly', () => {
        const now = new Date()
        const minute = 60 * 1000
        const hour = 60 * minute
        const day = 24 * hour

        expect(dateUtils.getRelativeTime(new Date(now.getTime() - 30 * 1000))).toBe('刚刚')
        expect(dateUtils.getRelativeTime(new Date(now.getTime() - 5 * minute))).toBe('5分钟前')
        expect(dateUtils.getRelativeTime(new Date(now.getTime() - 2 * hour))).toBe('2小时前')
        expect(dateUtils.getRelativeTime(new Date(now.getTime() - 3 * day))).toBe('3天前')
      })
    })
  })
})
