<template>
  <div class="dashboard">
    <!-- CSS工具类测试区域 -->
    <div class="mb-4 p-4 bg-light border rounded shadow-card">
      <div class="d-flex align-center justify-between mb-3">
        <h3 class="font-lg font-semibold text-primary m-0">✅ 新样式系统已启用</h3>
        <span class="font-sm text-secondary">Ant Design Vue + 原生CSS变量</span>
      </div>
      <div class="d-flex flex-wrap">
        <span class="mr-3 mb-2 p-2 bg-primary text-white rounded-sm font-sm">间距系统</span>
        <span class="mr-3 mb-2 p-2 bg-success text-white rounded-sm font-sm">颜色系统</span>
        <span class="mr-3 mb-2 p-2 bg-warning text-white rounded-sm font-sm">布局系统</span>
        <span class="mr-3 mb-2 p-2 bg-error text-white rounded-sm font-sm">字体系统</span>
      </div>
    </div>

    <!-- 欢迎横幕 -->
    <div class="welcome-banner">
      <a-card class="welcome-card">
        <div class="welcome-content">
          <div class="welcome-text">
            <h1>欢迎回来，{{ userStore.userInfo?.name || '管理员' }}！</h1>
            <p>今天是 {{ currentDate }}，祝您工作愉快</p>
          </div>
          <div class="welcome-actions">
            <a-space>
              <a-button type="primary" @click="handleQuickAdd"> <PlusOutlined />快速录入 </a-button>
              <a-button @click="handleDataExport"> <DownloadOutlined />数据导出 </a-button>
            </a-space>
          </div>
        </div>
      </a-card>
    </div>

    <!-- 数据统计卡片 -->
    <a-row :gutter="[24, 24]" class="stats-row">
      <a-col :xs="24" :sm="12" :md="6">
        <a-card class="stat-card" hoverable>
          <a-statistic
            title="今日销售额"
            :value="12345"
            :precision="2"
            prefix="¥"
            :value-style="{ color: '#3f8600' }"
          >
            <template #suffix>
              <div class="stat-trend">
                <ArrowUpOutlined class="trend-up" />
                <span class="trend-text">12%</span>
              </div>
            </template>
          </a-statistic>
          <div class="stat-desc">较昨日上升</div>
        </a-card>
      </a-col>

      <a-col :xs="24" :sm="12" :md="6">
        <a-card class="stat-card" hoverable>
          <a-statistic title="今日订单数" :value="89" :value-style="{ color: '#1890ff' }">
            <template #suffix>
              <div class="stat-trend">
                <ArrowUpOutlined class="trend-up" />
                <span class="trend-text">8%</span>
              </div>
            </template>
          </a-statistic>
          <div class="stat-desc">较昨日增长</div>
        </a-card>
      </a-col>

      <a-col :xs="24" :sm="12" :md="6">
        <a-card class="stat-card" hoverable>
          <a-statistic title="库存预警" :value="12" :value-style="{ color: '#cf1322' }">
            <template #suffix>
              <div class="stat-trend">
                <ArrowDownOutlined class="trend-down" />
                <span class="trend-text">3</span>
              </div>
            </template>
          </a-statistic>
          <div class="stat-desc">需要及时补货</div>
        </a-card>
      </a-col>

      <a-col :xs="24" :sm="12" :md="6">
        <a-card class="stat-card" hoverable>
          <a-statistic title="待处理采购" :value="5" :value-style="{ color: '#722ed1' }">
            <template #suffix>
              <div class="stat-trend">
                <ArrowUpOutlined class="trend-up" />
                <span class="trend-text">2</span>
              </div>
            </template>
          </a-statistic>
          <div class="stat-desc">较昨日增加</div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 数据表格和图表区域 -->
    <a-row :gutter="[24, 24]" class="content-row">
      <!-- 销售趋势图表 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" hoverable>
          <template #title>
            <div class="card-title">
              <LineChartOutlined class="title-icon" />
              <span>销售趋势</span>
            </div>
          </template>
          <SalesTrendChart :data="salesTrendData" height="300px" title="" />
        </a-card>
      </a-col>

      <!-- 商品分类销售分布 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" hoverable>
          <template #title>
            <div class="card-title">
              <PieChartOutlined class="title-icon" />
              <span>商品分类销售</span>
            </div>
          </template>
          <CategoryPieChart :data="categoryPieData" height="300px" title="" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 库存监控和财务指标 -->
    <a-row :gutter="[24, 24]" class="content-row">
      <!-- 库存预警图表 -->
      <a-col :xs="24" :lg="16">
        <a-card class="chart-card" hoverable>
          <template #title>
            <div class="card-title">
              <BarChartOutlined class="title-icon warning" />
              <span>库存预警监控</span>
            </div>
          </template>
          <InventoryBarChart :data="inventoryData" height="300px" title="" />
        </a-card>
      </a-col>

      <!-- 财务指标仪表盘 -->
      <a-col :xs="24" :lg="8">
        <a-card class="chart-card" hoverable>
          <template #title>
            <div class="card-title">
              <DashboardOutlined class="title-icon" />
              <span>本月回款率</span>
            </div>
          </template>
          <FinanceGaugeChart :data="financeGaugeData" height="300px" title="" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 数据列表区域 -->
    <a-row :gutter="[24, 24]" class="content-row">
      <!-- 最近订单 -->
      <a-col :xs="24" :lg="12">
        <a-card class="data-card" hoverable>
          <template #title>
            <div class="card-title">
              <FileTextOutlined class="title-icon" />
              <span>最近订单</span>
            </div>
          </template>
          <template #extra>
            <a-button type="link" size="small" @click="handleViewAllOrders"> 查看全部 </a-button>
          </template>

          <a-table :dataSource="recentOrders" :pagination="false" size="small" :scroll="{ y: 240 }">
            <a-table-column key="id" title="订单号" data-index="id" width="140" />
            <a-table-column key="customer" title="客户" data-index="customer" width="120" />
            <a-table-column key="amount" title="金额" data-index="amount" width="100" />
            <a-table-column key="status" title="状态" data-index="status" width="100">
              <template #default="{ text }">
                <a-tag :color="getStatusColor(text)">{{ text }}</a-tag>
              </template>
            </a-table-column>
          </a-table>
        </a-card>
      </a-col>

      <!-- 库存预警 -->
      <a-col :xs="24" :lg="12">
        <a-card class="data-card" hoverable>
          <template #title>
            <div class="card-title">
              <AlertOutlined class="title-icon warning" />
              <span>库存预警</span>
            </div>
          </template>
          <template #extra>
            <a-button type="link" size="small" @click="handleViewAllStock"> 查看全部 </a-button>
          </template>

          <a-table
            :dataSource="stockWarnings"
            :pagination="false"
            size="small"
            :scroll="{ y: 240 }"
          >
            <a-table-column key="name" title="商品名称" data-index="name" width="140" />
            <a-table-column key="stock" title="当前库存" data-index="stock" width="100">
              <template #default="{ text }">
                <span class="stock-low">{{ text }}</span>
              </template>
            </a-table-column>
            <a-table-column key="warning" title="预警值" data-index="warning" width="80" />
            <a-table-column key="action" title="操作" width="80">
              <template #default>
                <a-button size="small" type="link" @click="handleStockReplenish"> 补货 </a-button>
              </template>
            </a-table-column>
          </a-table>
        </a-card>
      </a-col>
    </a-row>

    <!-- 快捷操作区域 -->
    <a-row :gutter="[24, 24]" class="quick-actions-row">
      <a-col :span="24">
        <a-card class="quick-actions-card">
          <template #title>
            <div class="card-title">
              <ThunderboltOutlined class="title-icon" />
              <span>快捷操作</span>
            </div>
          </template>

          <div class="quick-actions">
            <div class="action-group">
              <h4>商品管理</h4>
              <a-space wrap>
                <a-button type="dashed" @click="navigateTo('/goods/list')">
                  <ShopOutlined />商品列表
                </a-button>
                <a-button type="dashed" @click="navigateTo('/goods/categories')">
                  <AppstoreOutlined />商品分类
                </a-button>
              </a-space>
            </div>

            <div class="action-group">
              <h4>库存管理</h4>
              <a-space wrap>
                <a-button type="dashed" @click="navigateTo('/inventory/stock')">
                  <DatabaseOutlined />库存查询
                </a-button>
                <a-button type="dashed" @click="navigateTo('/inventory/check')">
                  <AuditOutlined />库存盘点
                </a-button>
              </a-space>
            </div>

            <div class="action-group">
              <h4>销售管理</h4>
              <a-space wrap>
                <a-button type="dashed" @click="navigateTo('/sales/orders')">
                  <DollarOutlined />销售订单
                </a-button>
                <a-button type="dashed" @click="navigateTo('/sales/customers')">
                  <UserOutlined />客户管理
                </a-button>
              </a-space>
            </div>

            <div class="action-group">
              <h4>财务管理</h4>
              <a-space wrap>
                <a-button type="dashed" @click="navigateTo('/finance/receivables')">
                  <RiseOutlined />应收管理
                </a-button>
                <a-button type="dashed" @click="navigateTo('/finance/payables')">
                  <FallOutlined />应付管理
                </a-button>
              </a-space>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import { message } from 'ant-design-vue'
  import {
    PlusOutlined,
    DownloadOutlined,
    ArrowUpOutlined,
    ArrowDownOutlined,
    FileTextOutlined,
    AlertOutlined,
    ThunderboltOutlined,
    ShopOutlined,
    AppstoreOutlined,
    DatabaseOutlined,
    AuditOutlined,
    DollarOutlined,
    UserOutlined,
    RiseOutlined,
    FallOutlined,
    LineChartOutlined,
    PieChartOutlined,
    BarChartOutlined,
    DashboardOutlined
  } from '@ant-design/icons-vue'

  // 引入图表组件
  import {
    SalesTrendChart,
    CategoryPieChart,
    InventoryBarChart,
    FinanceGaugeChart,
    type SalesData,
    type CategoryData,
    type InventoryData,
    type GaugeData
  } from '@/components/charts'

  // 类型定义
  interface OrderData {
    key: string
    id: string
    customer: string
    amount: string
    status: string
  }

  interface StockWarning {
    key: string
    name: string
    stock: number
    warning: number
    status: string
  }

  const router = useRouter()
  const userStore = useUserStore()

  // 响应式数据
  const loading = ref(false)

  // 当前日期
  const currentDate = computed(() => {
    const date = new Date()
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      weekday: 'long'
    }
    return date.toLocaleDateString('zh-CN', options)
  })

  // 销售趋势数据
  const salesTrendData = ref<SalesData[]>([
    { date: '01-15', sales: 12000, profit: 3000 },
    { date: '01-16', sales: 15000, profit: 3800 },
    { date: '01-17', sales: 8000, profit: 2000 },
    { date: '01-18', sales: 22000, profit: 5500 },
    { date: '01-19', sales: 18000, profit: 4500 },
    { date: '01-20', sales: 25000, profit: 6200 },
    { date: '01-21', sales: 20000, profit: 5000 }
  ])

  // 商品分类销售数据
  const categoryPieData = ref<CategoryData[]>([
    { name: '饮料食品', value: 35000 },
    { name: '日用品', value: 28000 },
    { name: '电子产品', value: 22000 },
    { name: '服饰鞋帽', value: 18000 },
    { name: '家居用品', value: 15000 },
    { name: '其他', value: 12000 }
  ])

  // 库存预警数据
  const inventoryData = ref<InventoryData[]>([
    { name: '可口可乐', current: 10, min: 20, max: 100, status: 'danger' },
    { name: '康师傅方便面', current: 5, min: 15, max: 80, status: 'danger' },
    { name: '洗衣粉', current: 8, min: 10, max: 50, status: 'warning' },
    { name: '大米', current: 25, min: 12, max: 60, status: 'normal' },
    { name: '洗发水', current: 7, min: 18, max: 45, status: 'danger' },
    { name: '油菜', current: 15, min: 8, max: 30, status: 'normal' }
  ])

  // 财务指标数据
  const financeGaugeData = ref<GaugeData>({
    name: '本月回款率',
    value: 75,
    max: 100,
    unit: '%',
    color: '#52c41a'
  })

  // 最近订单数据
  const recentOrders = ref<OrderData[]>([
    {
      key: '1',
      id: 'SO20240301001',
      customer: '张三公司',
      amount: '¥ 2,345',
      status: '已完成'
    },
    {
      key: '2',
      id: 'SO20240301002',
      customer: '李四企业',
      amount: '¥ 1,234',
      status: '处理中'
    },
    {
      key: '3',
      id: 'SO20240301003',
      customer: '王五贸易',
      amount: '¥ 3,456',
      status: '待发货'
    },
    {
      key: '4',
      id: 'SO20240301004',
      customer: '赵六商贸',
      amount: '¥ 5,678',
      status: '已发货'
    },
    {
      key: '5',
      id: 'SO20240301005',
      customer: '孙七集团',
      amount: '¥ 1,890',
      status: '已完成'
    }
  ])

  // 库存预警数据
  const stockWarnings = ref<StockWarning[]>([
    {
      key: '1',
      name: '可口可乐 330ml',
      stock: 10,
      warning: 20,
      status: '库存不足'
    },
    {
      key: '2',
      name: '康师傅方便面',
      stock: 5,
      warning: 15,
      status: '库存不足'
    },
    {
      key: '3',
      name: '洗衣粉 2kg',
      stock: 8,
      warning: 10,
      status: '库存不足'
    },
    {
      key: '4',
      name: '大米 10kg',
      stock: 3,
      warning: 12,
      status: '库存不足'
    },
    {
      key: '5',
      name: '洗发水 500ml',
      stock: 7,
      warning: 18,
      status: '库存不足'
    }
  ])

  // 获取状态颜色
  const getStatusColor = (status: string): string => {
    const statusColors: Record<string, string> = {
      已完成: 'green',
      处理中: 'blue',
      待发货: 'orange',
      已发货: 'cyan',
      已取消: 'red'
    }
    return statusColors[status] || 'default'
  }

  // 导航到指定页面
  const navigateTo = (path: string) => {
    router.push(path)
  }

  // 快速录入
  const handleQuickAdd = () => {
    message.info('快速录入功能开发中')
  }

  // 数据导出
  const handleDataExport = () => {
    message.info('数据导出功能开发中')
  }

  // 查看所有订单
  const handleViewAllOrders = () => {
    navigateTo('/sales/orders')
  }

  // 查看所有库存
  const handleViewAllStock = () => {
    navigateTo('/inventory/stock')
  }

  // 库存补货
  const handleStockReplenish = () => {
    message.info('库存补货功能开发中')
  }

  // 初始化数据
  const initDashboard = async () => {
    loading.value = true
    try {
      // 模拟数据加载
      await new Promise(resolve => setTimeout(resolve, 800))

      // 这里可以加载真实数据
      console.log('仪表盘数据加载完成')
    } catch (error) {
      message.error('数据加载失败')
    } finally {
      loading.value = false
    }
  }

  // 组件挂载
  onMounted(() => {
    initDashboard()
  })
</script>

<style scoped>
  .dashboard {
    padding: 0;
  }

  /* 欢迎横幅样式 */
  .welcome-banner {
    margin-bottom: 24px;
  }

  .welcome-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 12px;
    overflow: hidden;
  }

  .welcome-card :deep(.ant-card-body) {
    padding: 32px;
  }

  .welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .welcome-text h1 {
    color: #fff;
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }

  .welcome-text p {
    color: rgba(255, 255, 255, 0.9);
    font-size: 16px;
    margin: 0;
  }

  .welcome-actions .ant-btn {
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: #fff;
    backdrop-filter: blur(10px);
  }

  .welcome-actions .ant-btn:hover {
    background: rgba(255, 255, 255, 0.3);
    border-color: rgba(255, 255, 255, 0.5);
    transform: translateY(-2px);
  }

  /* 数据统计卡片样式 */
  .stats-row {
    margin-bottom: 24px;
  }

  .stat-card {
    border-radius: 12px;
    transition: all 0.3s ease;
    border: 1px solid #f0f0f0;
  }

  .stat-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }

  .stat-card :deep(.ant-card-body) {
    padding: 24px;
  }

  .stat-trend {
    display: flex;
    align-items: center;
    gap: 4px;
    margin-top: 8px;
  }

  .trend-up {
    color: #52c41a;
  }

  .trend-down {
    color: #ff4d4f;
  }

  .trend-text {
    font-size: 14px;
    font-weight: 500;
  }

  .stat-desc {
    color: #666;
    font-size: 12px;
    margin-top: 8px;
  }

  /* 内容区域样式 */
  .content-row {
    margin-bottom: 24px;
  }

  .data-card,
  .chart-card {
    border-radius: 12px;
    transition: all 0.3s ease;
    height: 380px;
  }

  .data-card:hover,
  .chart-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }

  .chart-card {
    overflow: hidden;
  }

  .chart-card :deep(.ant-card-body) {
    padding: 16px;
  }

  .card-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
  }

  .title-icon {
    font-size: 16px;
    color: #1890ff;
  }

  .title-icon.warning {
    color: #fa8c16;
  }

  .stock-low {
    color: #ff4d4f;
    font-weight: 600;
  }

  /* 快捷操作区域样式 */
  .quick-actions-row {
    margin-bottom: 24px;
  }

  .quick-actions-card {
    border-radius: 12px;
  }

  .quick-actions {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 24px;
  }

  .action-group h4 {
    color: #333;
    font-size: 16px;
    font-weight: 600;
    margin: 0 0 12px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #f0f0f0;
  }

  .action-group .ant-btn {
    border-radius: 8px;
    transition: all 0.3s ease;
  }

  .action-group .ant-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .welcome-content {
      flex-direction: column;
      gap: 16px;
      text-align: center;
    }

    .welcome-text h1 {
      font-size: 24px;
    }

    .welcome-text p {
      font-size: 14px;
    }

    .stats-row .ant-col {
      margin-bottom: 16px;
    }

    .data-card {
      height: auto;
      margin-bottom: 16px;
    }

    .quick-actions {
      grid-template-columns: 1fr;
      gap: 16px;
    }
  }

  @media (max-width: 576px) {
    .welcome-card :deep(.ant-card-body) {
      padding: 20px;
    }

    .stats-row,
    .content-row,
    .quick-actions-row {
      margin-left: -8px;
      margin-right: -8px;
    }

    .stats-row .ant-col,
    .content-row .ant-col,
    .quick-actions-row .ant-col {
      padding-left: 8px;
      padding-right: 8px;
    }
  }

  /* 动画效果 */
  .stat-card,
  .data-card,
  .quick-actions-card {
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

  /* 滚动条样式 */
  .data-card :deep(.ant-table-body) {
    scrollbar-width: thin;
    scrollbar-color: #d9d9d9 transparent;
  }

  .data-card :deep(.ant-table-body)::-webkit-scrollbar {
    width: 6px;
  }

  .data-card :deep(.ant-table-body)::-webkit-scrollbar-track {
    background: transparent;
  }

  .data-card :deep(.ant-table-body)::-webkit-scrollbar-thumb {
    background-color: #d9d9d9;
    border-radius: 3px;
  }

  .data-card :deep(.ant-table-body)::-webkit-scrollbar-thumb:hover {
    background-color: #bfbfbf;
  }
</style>
