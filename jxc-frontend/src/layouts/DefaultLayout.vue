<template>
  <a-layout class="layout-container">
    <!-- 侧边栏 -->
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      width="240px"
      theme="dark"
      class="layout-sider"
    >
      <!-- Logo区域 -->
      <div class="logo-container">
        <div class="logo">
          <img src="/vite.svg" alt="系统Logo" />
          <span v-show="!collapsed" class="logo-text">JXC管理系统</span>
        </div>
      </div>

      <!-- 菜单导航 -->
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        theme="dark"
        :inline-collapsed="collapsed"
        @click="handleMenuClick"
        class="layout-menu"
      >
        <!-- 仪表盘 -->
        <a-menu-item key="/">
          <template #icon><DashboardOutlined /></template>
          <span>仪表盘</span>
        </a-menu-item>

        <!-- 商品管理 -->
        <a-sub-menu key="/goods">
          <template #icon><ShopOutlined /></template>
          <template #title>商品管理</template>
          <a-menu-item key="/goods/list">
            <template #icon><UnorderedListOutlined /></template>
            商品列表
          </a-menu-item>
          <a-menu-item key="/goods/categories">
            <template #icon><AppstoreOutlined /></template>
            商品分类
          </a-menu-item>
        </a-sub-menu>

        <!-- 库存管理 -->
        <a-sub-menu key="/inventory">
          <template #icon><InboxOutlined /></template>
          <template #title>库存管理</template>
          <a-menu-item key="/inventory/stock">
            <template #icon><DatabaseOutlined /></template>
            库存查询
          </a-menu-item>
          <a-menu-item key="/inventory/check">
            <template #icon><AuditOutlined /></template>
            库存盘点
          </a-menu-item>
        </a-sub-menu>

        <!-- 采购管理 -->
        <a-sub-menu key="/purchase">
          <template #icon><ShoppingCartOutlined /></template>
          <template #title>采购管理</template>
          <a-menu-item key="/purchase/orders">
            <template #icon><FileTextOutlined /></template>
            采购订单
          </a-menu-item>
          <a-menu-item key="/purchase/suppliers">
            <template #icon><TeamOutlined /></template>
            供应商管理
          </a-menu-item>
        </a-sub-menu>

        <!-- 销售管理 -->
        <a-sub-menu key="/sales">
          <template #icon><DollarOutlined /></template>
          <template #title>销售管理</template>
          <a-menu-item key="/sales/orders">
            <template #icon><FileDoneOutlined /></template>
            销售订单
          </a-menu-item>
          <a-menu-item key="/sales/customers">
            <template #icon><UserOutlined /></template>
            客户管理
          </a-menu-item>
        </a-sub-menu>

        <!-- 财务管理 -->
        <a-sub-menu key="/finance">
          <template #icon><BankOutlined /></template>
          <template #title>财务管理</template>
          <a-menu-item key="/finance/receivables">
            <template #icon><RiseOutlined /></template>
            应收管理
          </a-menu-item>
          <a-menu-item key="/finance/payables">
            <template #icon><FallOutlined /></template>
            应付管理
          </a-menu-item>
        </a-sub-menu>

        <!-- 系统管理 -->
        <a-sub-menu key="/system">
          <template #icon><SettingOutlined /></template>
          <template #title>系统管理</template>
          <a-menu-item key="/system/users">
            <template #icon><UsergroupAddOutlined /></template>
            用户管理
          </a-menu-item>
          <a-menu-item key="/system/roles">
            <template #icon><SafetyOutlined /></template>
            角色管理
          </a-menu-item>
        </a-sub-menu>

        <!-- 数据报表 -->
        <a-sub-menu key="/reports">
          <template #icon><BarChartOutlined /></template>
          <template #title>数据报表</template>
          <a-menu-item key="/reports/sales">
            <template #icon><LineChartOutlined /></template>
            销售报表
          </a-menu-item>
          <a-menu-item key="/reports/inventory">
            <template #icon><PieChartOutlined /></template>
            库存报表
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>

    <a-layout>
      <!-- 顶部导航栏 -->
      <a-layout-header class="layout-header">
        <div class="header-left">
          <!-- 收缩按钮 -->
          <a-button type="text" @click="toggleCollapsed" class="collapse-btn">
            <MenuUnfoldOutlined v-if="collapsed" />
            <MenuFoldOutlined v-else />
          </a-button>

          <!-- 面包屑导航 -->
          <a-breadcrumb class="breadcrumb">
            <a-breadcrumb-item v-for="item in breadcrumbItems" :key="item.path">
              <template v-if="item.icon" #icon>
                <component :is="item.icon" />
              </template>
              {{ item.title }}
            </a-breadcrumb-item>
          </a-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 全屏按钮 -->
          <a-tooltip :title="isFullscreen ? '退出全屏' : '全屏'">
            <a-button type="text" @click="toggleFullscreen" class="header-action-btn">
              <FullscreenExitOutlined v-if="isFullscreen" />
              <FullscreenOutlined v-else />
            </a-button>
          </a-tooltip>

          <!-- 通知中心 -->
          <a-badge :count="notificationCount" :offset="[10, 0]">
            <a-button type="text" @click="showNotifications" class="header-action-btn">
              <BellOutlined />
            </a-button>
          </a-badge>

          <!-- 用户信息下拉菜单 -->
          <a-dropdown placement="bottomRight">
            <div class="user-info">
              <a-avatar :size="32" :src="userStore.userInfo?.avatar" class="user-avatar">
                <template #icon><UserOutlined /></template>
              </a-avatar>
              <span v-if="!collapsed" class="user-name">
                {{ userStore.userInfo?.name || '未登录' }}
              </span>
              <DownOutlined class="dropdown-icon" />
            </div>
            <template #overlay>
              <a-menu @click="handleUserMenuClick">
                <a-menu-item key="profile">
                  <UserOutlined />
                  个人信息
                </a-menu-item>
                <a-menu-item key="settings">
                  <SettingOutlined />
                  系统设置
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>

      <!-- 主内容区域 -->
      <a-layout-content class="layout-content">
        <div class="content-wrapper">
          <router-view v-slot="{ Component }">
            <transition name="page-transition" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </a-layout-content>

      <!-- 底部版权信息 -->
      <a-layout-footer class="layout-footer">
        <div class="footer-content">
          <span>© 2024 JXC进销存管理系统 - 专业的超市库存管理解决方案</span>
          <span>技术支持：Vue 3 + TypeScript + Ant Design Vue</span>
        </div>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
  import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import { message } from 'ant-design-vue'
  import { logoutApi } from '@/api/auth'
  import {
    DashboardOutlined,
    SettingOutlined,
    ShopOutlined,
    ShoppingCartOutlined,
    InboxOutlined,
    DollarOutlined,
    BankOutlined,
    DownOutlined,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    FullscreenOutlined,
    FullscreenExitOutlined,
    BellOutlined,
    UserOutlined,
    LogoutOutlined,
    UnorderedListOutlined,
    AppstoreOutlined,
    DatabaseOutlined,
    AuditOutlined,
    FileTextOutlined,
    TeamOutlined,
    FileDoneOutlined,
    RiseOutlined,
    FallOutlined,
    UsergroupAddOutlined,
    SafetyOutlined,
    BarChartOutlined,
    LineChartOutlined,
    PieChartOutlined
  } from '@ant-design/icons-vue'

  // 类型定义
  interface BreadcrumbItem {
    path: string
    title: string
    icon?: any
  }

  const route = useRoute()
  const router = useRouter()
  const userStore = useUserStore()

  // 响应式数据
  const collapsed = ref(false)
  const selectedKeys = ref([route.path])
  const openKeys = ref<string[]>([])
  const isFullscreen = ref(false)
  const notificationCount = ref(3)

  // 监听路由变化更新选中的菜单
  watch(
    () => route.path,
    newPath => {
      selectedKeys.value = [newPath]
      updateOpenKeys(newPath)
    }
  )

  // 监听菜单收缩状态
  watch(collapsed, isCollapsed => {
    if (isCollapsed) {
      openKeys.value = []
    } else {
      updateOpenKeys(route.path)
    }
  })

  // 更新展开的菜单项
  const updateOpenKeys = (path: string) => {
    const pathSegments = path.split('/').filter(Boolean)
    if (pathSegments.length > 1) {
      openKeys.value = [`/${pathSegments[0]}`]
    }
  }

  // 面包屑导航数据
  const breadcrumbItems = computed<BreadcrumbItem[]>(() => {
    const pathSegments = route.path.split('/').filter(Boolean)
    const items: BreadcrumbItem[] = []

    // 根路径
    if (pathSegments.length === 0) {
      return [{ path: '/', title: '仪表盘', icon: DashboardOutlined }]
    }

    // 构建面包屑路径
    const breadcrumbMap: Record<string, { title: string; icon?: any }> = {
      goods: { title: '商品管理', icon: ShopOutlined },
      inventory: { title: '库存管理', icon: InboxOutlined },
      purchase: { title: '采购管理', icon: ShoppingCartOutlined },
      sales: { title: '销售管理', icon: DollarOutlined },
      finance: { title: '财务管理', icon: BankOutlined },
      system: { title: '系统管理', icon: SettingOutlined },
      reports: { title: '数据报表', icon: BarChartOutlined },
      list: { title: '列表' },
      categories: { title: '分类' },
      stock: { title: '库存' },
      check: { title: '盘点' },
      orders: { title: '订单' },
      suppliers: { title: '供应商' },
      customers: { title: '客户' },
      receivables: { title: '应收' },
      payables: { title: '应付' },
      users: { title: '用户' },
      roles: { title: '角色' }
    }

    // 特殊处理：根据路径上下文确定最后一级的标题
    const getSegmentTitle = (segment: string, pathSegments: string[], index: number) => {
      const baseConfig = breadcrumbMap[segment]
      if (!baseConfig) return null

      // 如果是最后一级且在reports下，特殊处理
      if (index === pathSegments.length - 1 && pathSegments[0] === 'reports') {
        if (segment === 'sales') return '销售报表'
        if (segment === 'inventory') return '库存报表'
      }

      return baseConfig.title
    }

    let currentPath = ''
    pathSegments.forEach((segment, index) => {
      currentPath += `/${segment}`
      const title = getSegmentTitle(segment, pathSegments, index)
      const config = breadcrumbMap[segment]

      if (title || config) {
        items.push({
          path: currentPath,
          title: title || config?.title || segment,
          icon: index === 0 ? config?.icon : undefined
        })
      }
    })

    return items
  })

  // 菜单点击处理
  const handleMenuClick = ({ key }: { key: string }) => {
    router.push(key)
  }

  // 收缩菜单
  const toggleCollapsed = () => {
    collapsed.value = !collapsed.value
  }

  // 全屏切换
  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen()
      isFullscreen.value = true
    } else {
      document.exitFullscreen()
      isFullscreen.value = false
    }
  }

  // 显示通知
  const showNotifications = () => {
    message.info('通知中心功能开发中')
  }

  // 用户菜单点击处理
  const handleUserMenuClick = async ({ key }: { key: string }) => {
    switch (key) {
      case 'logout':
        try {
          // 调用后端登出API
          await logoutApi()
          // 清除本地存储
          userStore.logout()
          localStorage.removeItem('token')
          message.success('已退出登录')
          router.push('/login')
        } catch (error) {
          // 即使后端调用失败，也要清除本地存储
          userStore.logout()
          localStorage.removeItem('token')
          message.success('已退出登录')
          router.push('/login')
        }
        break
      case 'profile':
        message.info('个人信息功能开发中')
        break
      case 'settings':
        message.info('系统设置功能开发中')
        break
      default:
        break
    }
  }

  // 监听全屏状态变化
  const handleFullscreenChange = () => {
    isFullscreen.value = !!document.fullscreenElement
  }

  // 组件挂载
  onMounted(() => {
    updateOpenKeys(route.path)
    document.addEventListener('fullscreenchange', handleFullscreenChange)
  })

  // 组件卸载
  onUnmounted(() => {
    document.removeEventListener('fullscreenchange', handleFullscreenChange)
  })
</script>

<style scoped>
  .layout-container {
    height: 100vh;
    overflow: hidden;
  }

  /* 侧边栏样式 */
  .layout-sider {
    background: #001529;
    transition: all 0.2s;
  }

  .logo-container {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.1);
    margin-bottom: 1px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 0 16px;
  }

  .logo img {
    width: 32px;
    height: 32px;
    border-radius: 6px;
  }

  .logo-text {
    color: #fff;
    font-size: 16px;
    font-weight: 600;
    white-space: nowrap;
  }

  .layout-menu {
    border-right: none;
    height: calc(100vh - 64px);
    overflow-y: auto;
  }

  /* 顶部导航栏样式 */
  .layout-header {
    background: #fff;
    padding: 0 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #f0f0f0;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    height: 64px;
  }

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .collapse-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    color: #666;
    transition: all 0.3s;
  }

  .collapse-btn:hover {
    color: #1890ff;
    background-color: #f0f8ff;
  }

  .breadcrumb {
    margin-left: 8px;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .header-action-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    color: #666;
    transition: all 0.3s;
  }

  .header-action-btn:hover {
    color: #1890ff;
    background-color: #f0f8ff;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 4px 12px;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s;
    min-width: 120px;
  }

  .user-info:hover {
    background-color: #f5f5f5;
  }

  .user-avatar {
    border: 2px solid #f0f0f0;
  }

  .user-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .dropdown-icon {
    color: #999;
    font-size: 12px;
    transition: transform 0.3s;
  }

  .user-info:hover .dropdown-icon {
    transform: rotate(180deg);
  }

  /* 主内容区域样式 */
  .layout-content {
    background: #f0f2f5;
    overflow: auto;
    height: calc(100vh - 64px - 48px); /* 减去 header 和 footer 高度 */
  }

  .content-wrapper {
    padding: 24px;
    min-height: 100%;
  }

  /* 底部样式 */
  .layout-footer {
    background: #fff;
    padding: 12px 24px;
    border-top: 1px solid #f0f0f0;
    text-align: center;
    height: 48px;
    line-height: 24px;
  }

  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 12px;
    color: #666;
  }

  /* 页面过渡动画 */
  .page-transition-enter-active,
  .page-transition-leave-active {
    transition: all 0.3s ease;
  }

  .page-transition-enter-from {
    opacity: 0;
    transform: translateX(30px);
  }

  .page-transition-leave-to {
    opacity: 0;
    transform: translateX(-30px);
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .layout-header {
      padding: 0 16px;
    }

    .header-left {
      gap: 8px;
    }

    .breadcrumb {
      display: none;
    }

    .user-name {
      display: none;
    }

    .footer-content {
      flex-direction: column;
      gap: 4px;
    }

    .content-wrapper {
      padding: 16px;
    }
  }

  /* 滚动条样式 */
  .layout-menu::-webkit-scrollbar {
    width: 4px;
  }

  .layout-menu::-webkit-scrollbar-track {
    background: transparent;
  }

  .layout-menu::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 2px;
  }

  .layout-menu::-webkit-scrollbar-thumb:hover {
    background: rgba(255, 255, 255, 0.3);
  }

  .layout-content::-webkit-scrollbar {
    width: 6px;
  }

  .layout-content::-webkit-scrollbar-track {
    background: #f1f1f1;
  }

  .layout-content::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
  }

  .layout-content::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
  }
</style>
