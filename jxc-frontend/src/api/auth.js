import axios from 'axios'

export function loginApi(data) {
  // 实际开发中应替换为后端接口
  return new Promise((resolve, reject) => {
    if (data.username === 'admin' && data.password === '123456') {
      resolve({
        token: 'mock-token',
        user: { username: 'admin', name: '管理员', roles: ['admin'] }
      })
    } else {
      reject(new Error('用户名或密码错误'))
    }
  })
}

export function getUserInfoApi(token) {
  // 模拟接口
  return Promise.resolve({
    username: 'admin',
    name: '管理员',
    roles: ['admin']
  })
}

export function logoutApi() {
  return Promise.resolve()
}
