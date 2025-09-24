import { mount, VueWrapper } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router'
import type { Component } from 'vue'

// 创建测试用的 Pinia 实例
export const createTestPinia = () => {
  return createPinia()
}

// 创建测试用的路由实例
export const createTestRouter = (routes: any[] = []) => {
  return createRouter({
    history: createWebHistory(),
    routes: [{ path: '/', component: { template: '<div>Home</div>' } }, ...routes]
  })
}

// 组件挂载助手
export const mountComponent = (
  component: Component,
  options: {
    props?: Record<string, any>
    global?: {
      plugins?: any[]
      mocks?: Record<string, any>
      stubs?: Record<string, any>
    }
    attachTo?: HTMLElement
  } = {}
): VueWrapper => {
  const pinia = createTestPinia()
  const router = createTestRouter()

  const defaultGlobal = {
    plugins: [pinia, router],
    mocks: {},
    stubs: {}
  }

  const mergedOptions = {
    ...options,
    global: {
      ...defaultGlobal,
      ...options.global,
      plugins: [...defaultGlobal.plugins, ...(options.global?.plugins || [])]
    }
  }

  return mount(component, mergedOptions)
}

// 等待DOM更新
export const waitForUpdate = async (wrapper: VueWrapper) => {
  await wrapper.vm.$nextTick()
}

// 触发事件助手
export const triggerEvent = async (
  wrapper: VueWrapper,
  selector: string,
  event: string,
  payload?: any
) => {
  const element = wrapper.find(selector)
  await element.trigger(event, payload)
  await waitForUpdate(wrapper)
}

// 输入文本助手
export const typeText = async (wrapper: VueWrapper, selector: string, text: string) => {
  const input = wrapper.find(selector)
  await input.setValue(text)
  await waitForUpdate(wrapper)
}

// 模拟API响应
export const mockApiResponse = <T>(data: T, delay = 0) => {
  return new Promise<T>(resolve => {
    setTimeout(() => resolve(data), delay)
  })
}

// 模拟API错误
export const mockApiError = (message = 'API Error', delay = 0) => {
  return new Promise((_, reject) => {
    setTimeout(() => reject(new Error(message)), delay)
  })
}

// 表单验证助手
export const validateForm = async (wrapper: VueWrapper, formSelector = 'form') => {
  const form = wrapper.find(formSelector)
  const validateFn = form.vm?.validate || form.vm?.$refs?.form?.validate

  if (validateFn) {
    return await validateFn()
  }

  throw new Error('Form validation method not found')
}

// 创建测试数据
export const createMockData = {
  user: (overrides = {}) => ({
    id: 1,
    name: '测试用户',
    email: 'test@example.com',
    phone: '13800138000',
    role: 'admin',
    ...overrides
  }),

  goods: (overrides = {}) => ({
    id: 1,
    name: '测试商品',
    code: 'TEST001',
    price: 100.0,
    stock: 50,
    category: '测试分类',
    ...overrides
  }),

  order: (overrides = {}) => ({
    id: 1,
    orderNo: 'SO20240101001',
    customer: '测试客户',
    amount: 1000.0,
    status: 'pending',
    createTime: '2024-01-01 10:00:00',
    ...overrides
  })
}
