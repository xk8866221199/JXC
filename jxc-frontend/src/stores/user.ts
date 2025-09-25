import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 用户信息接口
interface UserInfo {
  id?: string | number
  username?: string
  name?: string
  email?: string
  phone?: string
  avatar?: string
}

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref<string>('')
    const userInfo = ref<UserInfo | null>(null)
    const roles = ref<string[]>([])
    const permissions = ref<string[]>([])

    // 计算属性：是否已登录
    const isLoggedIn = computed(() => !!token.value)

    function setToken(val: string) {
      token.value = val
    }

    function setUserInfo(val: UserInfo | null) {
      userInfo.value = val
    }

    function setRoles(val: string[]) {
      roles.value = val
    }

    function setPermissions(val: string[]) {
      permissions.value = val
    }

    function hasPermission(permission: string): boolean {
      return permissions.value.includes(permission)
    }

    function hasRole(role: string): boolean {
      return roles.value.includes(role)
    }

    function logout() {
      token.value = ''
      userInfo.value = null
      roles.value = []
      permissions.value = []
    }

    return {
      token,
      userInfo,
      roles,
      permissions,
      isLoggedIn,
      setToken,
      setUserInfo,
      setRoles,
      setPermissions,
      hasPermission,
      hasRole,
      logout
    }
  },
  {
    persist: true
  }
)
