<template>
  <div class="login-bg">
    <div class="login-container">
      <!-- 登录卡片 -->
      <a-card class="login-card" :loading="pageLoading">
        <template #title>
          <div class="login-header">
            <div class="login-logo">
              <img src="/vite.svg" alt="系统图标" />
            </div>
            <h1>进销存管理系统</h1>
            <p>专业的超市库存管理解决方案</p>
          </div>
        </template>

        <!-- 登录表单 -->
        <a-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @finish="handleLogin"
          @finishFailed="handleLoginFailed"
          layout="vertical"
        >
          <a-form-item name="username" label="用户名">
            <a-input
              v-model:value="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              allow-clear
              :maxlength="50"
              @pressEnter="handleLogin"
            >
              <template #prefix>
                <UserOutlined class="input-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="loginForm.password"
              placeholder="请输入密码"
              size="large"
              allow-clear
              :maxlength="50"
              @pressEnter="handleLogin"
            >
              <template #prefix>
                <LockOutlined class="input-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <!-- 验证码 -->
          <a-form-item v-if="showCaptcha" name="captcha" label="验证码">
            <a-row :gutter="8">
              <a-col :span="14">
                <a-input
                  v-model:value="loginForm.captcha"
                  placeholder="请输入验证码"
                  size="large"
                  allow-clear
                  :maxlength="6"
                  @pressEnter="handleLogin"
                >
                  <template #prefix>
                    <SafetyCertificateOutlined class="input-icon" />
                  </template>
                </a-input>
              </a-col>
              <a-col :span="10">
                <div class="captcha-container" @click="refreshCaptcha">
                  <span class="captcha-code">{{ captchaCode }}</span>
                  <ReloadOutlined class="refresh-icon" />
                </div>
              </a-col>
            </a-row>
          </a-form-item>

          <!-- 记住登录 -->
          <a-form-item>
            <div class="login-options">
              <a-checkbox v-model:checked="rememberMe"> 记住登录 </a-checkbox>
              <a class="forgot-password" @click="handleForgotPassword"> 忘记密码？ </a>
            </div>
          </a-form-item>

          <!-- 登录按钮 -->
          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              :loading="loginLoading"
              class="login-button"
              size="large"
              block
            >
              <template #icon>
                <LoginOutlined v-if="!loginLoading" />
              </template>
              {{ loginLoading ? '登录中...' : '登录' }}
            </a-button>
          </a-form-item>
        </a-form>

        <!-- 版权信息 -->
        <div class="copyright">
          <p>© 2024 JXC进销存管理系统. 版权所有</p>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, computed } from 'vue'
  import { useRouter } from 'vue-router'
  import { message } from 'ant-design-vue'
  import { useUserStore } from '@/stores/user'
  import {
    UserOutlined,
    LockOutlined,
    SafetyCertificateOutlined,
    ReloadOutlined,
    LoginOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 类型定义
  interface LoginForm {
    username: string
    password: string
    captcha?: string
  }

  const router = useRouter()
  const userStore = useUserStore()

  // 响应式数据
  const pageLoading = ref(false)
  const loginLoading = ref(false)
  const loginFormRef = ref<FormInstance>()
  const rememberMe = ref(false)
  const showCaptcha = ref(false)
  const failedAttempts = ref(0)
  const captchaCode = ref('')

  // 登录表单数据
  const loginForm = reactive<LoginForm>({
    username: '',
    password: '',
    captcha: ''
  })

  // 表单验证规则
  const loginRules = computed(() => ({
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 3, max: 50, message: '用户名长度为3-50个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 50, message: '密码长度为6-50个字符', trigger: 'blur' }
    ],
    captcha: showCaptcha.value ? [{ required: true, message: '请输入验证码', trigger: 'blur' }] : []
  }))

  // 生成验证码
  const generateCaptcha = (): string => {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
    let result = ''
    for (let i = 0; i < 4; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length))
    }
    return result
  }

  // 刷新验证码
  const refreshCaptcha = () => {
    captchaCode.value = generateCaptcha()
    loginForm.captcha = ''
  }

  // 初始化页面
  const initPage = async () => {
    pageLoading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 500))

      // 检查是否记住登录
      const savedUsername = localStorage.getItem('remember_username')
      if (savedUsername) {
        loginForm.username = savedUsername
        rememberMe.value = true
      }

      refreshCaptcha()
    } catch (error) {
      message.error('页面初始化失败')
    } finally {
      pageLoading.value = false
    }
  }

  // 登录提交
  const handleLogin = async () => {
    try {
      await loginFormRef.value?.validate()

      // 验证验证码
      if (showCaptcha.value && loginForm.captcha?.toUpperCase() !== captchaCode.value) {
        message.error('验证码错误')
        refreshCaptcha()
        return
      }

      loginLoading.value = true

      // 模拟登录请求
      await new Promise(resolve => setTimeout(resolve, 1500))

      // 模拟登录成功
      const userInfo = {
        id: 1,
        name: '管理员',
        username: loginForm.username,
        roles: ['admin']
      }

      // 保存用户信息
      userStore.setToken('mock-jwt-token-' + Date.now())
      userStore.setUserInfo(userInfo)
      userStore.setRoles(userInfo.roles)

      // 记住登录
      if (rememberMe.value) {
        localStorage.setItem('remember_username', loginForm.username)
      } else {
        localStorage.removeItem('remember_username')
      }

      // 重置失败次数
      failedAttempts.value = 0
      showCaptcha.value = false

      message.success('登录成功，欢迎回来！')
      router.push('/')
    } catch (error: any) {
      failedAttempts.value++

      // 3次失败后显示验证码
      if (failedAttempts.value >= 3) {
        showCaptcha.value = true
        refreshCaptcha()
      }

      if (error.errorFields) {
        message.error('请检查输入信息')
      } else {
        message.error(error.message || '登录失败，请稍后重试')
      }
    } finally {
      loginLoading.value = false
    }
  }

  // 登录失败处理
  const handleLoginFailed = () => {
    message.error('请填写完整的登录信息')
  }

  // 忘记密码
  const handleForgotPassword = () => {
    message.info('请联系管理员重置密码')
  }

  // 组件挂载
  onMounted(() => {
    initPage()
  })
</script>

<style scoped>
  .login-bg {
    min-height: 100vh;
    width: 100vw;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    overflow: auto;
    padding: 20px;
  }

  .login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    max-width: 420px;
    z-index: 1;
  }

  .login-card {
    width: 100%;
    box-shadow: 0 20px 40px 0 rgba(0, 0, 0, 0.15);
    border-radius: 16px;
    overflow: hidden;
    backdrop-filter: blur(10px);
    background: rgba(255, 255, 255, 0.95);
  }

  .login-header {
    text-align: center;
    margin-bottom: 8px;
  }

  .login-logo {
    margin-bottom: 16px;
  }

  .login-logo img {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(103, 126, 234, 0.3);
  }

  .login-header h1 {
    color: #1a202c;
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }

  .login-header p {
    color: #718096;
    font-size: 14px;
    margin: 0;
  }

  .login-form {
    padding: 8px 0;
  }

  .input-icon {
    color: #a0aec0;
  }

  .captcha-container {
    height: 40px;
    background: linear-gradient(45deg, #667eea, #764ba2);
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;
    position: relative;
    user-select: none;
  }

  .captcha-container:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(103, 126, 234, 0.4);
  }

  .captcha-code {
    color: white;
    font-weight: bold;
    font-size: 16px;
    letter-spacing: 2px;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
  }

  .refresh-icon {
    position: absolute;
    top: 2px;
    right: 4px;
    color: rgba(255, 255, 255, 0.8);
    font-size: 12px;
  }

  .login-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 8px 0;
  }

  .forgot-password {
    color: #667eea;
    font-size: 14px;
    transition: color 0.3s;
  }

  .forgot-password:hover {
    color: #764ba2;
  }

  .login-button {
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    box-shadow: 0 4px 12px rgba(103, 126, 234, 0.3);
    transition: all 0.3s;
  }

  .login-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(103, 126, 234, 0.4);
  }

  .copyright {
    text-align: center;
    margin-top: 20px;
    color: #718096;
  }

  .copyright p {
    margin: 4px 0;
    font-size: 12px;
    line-height: 1.5;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .login-bg {
      padding: 16px;
    }

    .login-container {
      max-width: 100%;
    }

    .login-header h1 {
      font-size: 20px;
    }

    .login-button {
      height: 44px;
      font-size: 15px;
    }
  }

  /* 动画效果 */
  .login-card {
    animation: fadeInUp 0.6s ease-out;
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
</style>
