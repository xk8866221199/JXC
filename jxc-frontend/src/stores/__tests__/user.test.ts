import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/stores/user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('should have initial state', () => {
    const userStore = useUserStore()

    expect(userStore.token).toBe('')
    expect(userStore.userInfo).toBeNull()
    expect(userStore.permissions).toEqual([])
  })

  it('should set user info correctly', () => {
    const userStore = useUserStore()
    const mockUser = {
      id: 1,
      name: '测试用户',
      email: 'test@example.com',
      avatar: '/avatar.png'
    }

    userStore.setUserInfo(mockUser)

    expect(userStore.userInfo).toEqual(mockUser)
  })

  it('should set token correctly', () => {
    const userStore = useUserStore()
    const mockToken = 'mock-jwt-token'

    userStore.setToken(mockToken)

    expect(userStore.token).toBe(mockToken)
  })

  it('should set permissions correctly', () => {
    const userStore = useUserStore()
    const mockPermissions = ['user:read', 'user:write', 'admin:all']

    userStore.setPermissions(mockPermissions)

    expect(userStore.permissions).toEqual(mockPermissions)
  })

  it('should check if user is logged in', () => {
    const userStore = useUserStore()

    expect(userStore.isLoggedIn).toBe(false)

    userStore.setToken('mock-token')
    expect(userStore.isLoggedIn).toBe(true)
  })

  it('should check permissions correctly', () => {
    const userStore = useUserStore()
    const permissions = ['user:read', 'user:write']

    userStore.setPermissions(permissions)

    expect(userStore.hasPermission('user:read')).toBe(true)
    expect(userStore.hasPermission('user:write')).toBe(true)
    expect(userStore.hasPermission('admin:all')).toBe(false)
  })

  it('should logout correctly', () => {
    const userStore = useUserStore()
    const mockUser = { id: 1, name: '测试用户' }
    const mockToken = 'mock-token'
    const mockPermissions = ['user:read']

    // 设置用户信息
    userStore.setUserInfo(mockUser)
    userStore.setToken(mockToken)
    userStore.setPermissions(mockPermissions)

    // 验证设置成功
    expect(userStore.userInfo).toEqual(mockUser)
    expect(userStore.token).toBe(mockToken)
    expect(userStore.permissions).toEqual(mockPermissions)

    // 执行登出
    userStore.logout()

    // 验证清空状态
    expect(userStore.userInfo).toBeNull()
    expect(userStore.token).toBe('')
    expect(userStore.permissions).toEqual([])
    expect(userStore.isLoggedIn).toBe(false)
  })

  it('should handle login flow', () => {
    const userStore = useUserStore()
    const mockLoginData = {
      token: 'mock-jwt-token',
      user: {
        id: 1,
        name: '测试用户',
        email: 'test@example.com'
      },
      permissions: ['user:read', 'user:write']
    }

    // 模拟登录成功
    userStore.setToken(mockLoginData.token)
    userStore.setUserInfo(mockLoginData.user)
    userStore.setPermissions(mockLoginData.permissions)

    expect(userStore.isLoggedIn).toBe(true)
    expect(userStore.token).toBe(mockLoginData.token)
    expect(userStore.userInfo).toEqual(mockLoginData.user)
    expect(userStore.permissions).toEqual(mockLoginData.permissions)
  })
})
