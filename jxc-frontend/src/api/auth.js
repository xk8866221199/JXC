import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端API基础URL
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token并添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 处理响应数据
    const { code, data, message } = response.data
    if (code === 200) {
      return data
    } else {
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  error => {
    // 处理错误响应
    if (error.response) {
      const { data } = error.response
      const message = data?.message || '请求失败'
      
      // 处理401未授权错误
      if (error.response.status === 401) {
        // 清除本地存储的用户信息
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        // 跳转到登录页
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(message))
    } else {
      return Promise.reject(new Error('网络错误，请检查网络连接'))
    }
  }
)

/**
 * 用户登录API
 */
export function loginApi(data) {
  return request.post('/auth/login', data)
}

/**
 * 获取用户信息API
 */
export function getUserInfoApi() {
  return request.get('/auth/user')
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
