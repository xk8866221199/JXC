import axios from 'axios'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端API基础URL
  timeout: 15000, // 增加请求超时时间
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: false // 确保不发送凭据
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    try {
      console.log('发送请求:', config)
      // 从 Pinia store 获取 token
      const userStore = useUserStore()
      if (userStore.token) {
        config.headers.Authorization = `Bearer ${userStore.token}`
      }
    } catch (error) {
      console.error('请求拦截器错误:', error)
    }
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    try {
      console.log('收到响应:', response)
      // 处理响应数据
      const { code, data, message } = response.data
      if (code === 200) {
        return data
      } else {
        return Promise.reject(new Error(message || '请求失败'))
      }
    } catch (error) {
      console.error('响应拦截器错误:', error)
      return Promise.reject(new Error('响应数据解析失败'))
    }
  },
  error => {
    try {
      console.error('响应错误:', error)
      // 处理错误响应
      if (error.response) {
        console.error('错误响应:', error.response)
        const { data } = error.response
        const message = data?.message || '请求失败'
        
        // 处理401未授权错误
        if (error.response.status === 401) {
          try {
            // 使用 Pinia store 清除用户信息
            const userStore = useUserStore()
            userStore.logout()
            // 跳转到登录页
            if (typeof window !== 'undefined') {
              window.location.href = '/login'
            }
          } catch (storeError) {
            console.error('登出处理错误:', storeError)
          }
        }
        
        return Promise.reject(new Error(message))
      } else if (error.request) {
        console.error('网络错误:', error.request)
        return Promise.reject(new Error('网络错误，请检查网络连接'))
      } else {
        console.error('请求错误:', error.message)
        return Promise.reject(new Error('请求配置错误'))
      }
    } catch (interceptorError) {
      console.error('响应拦截器错误处理失败:', interceptorError)
      return Promise.reject(new Error('网络错误，请检查网络连接'))
    }
  }
)

// 导出请求实例
export default request

/**
 * 用户登录API
 */
export function loginApi(data) {
  console.log('调用登录API:', data)
  return request.post('/auth/login', data).catch(error => {
    console.error('登录API调用失败:', error)
    throw error
  })
}

/**
 * 获取用户信息API
 */
export function getUserInfoApi() {
  return request.get('/auth/me')
}

/**
 * 用户登出API
 */
export function logoutApi() {
  return request.post('/auth/logout')
}

/**
 * 刷新Token API
 */
export function refreshTokenApi() {
  return request.post('/auth/refresh')
}