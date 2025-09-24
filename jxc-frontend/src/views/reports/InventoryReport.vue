<template>
  <div class="inventory-report">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">库存报表</h2>
      <p class="text-secondary mt-1 mb-0">实时监控库存状况，优化仓储管理，降低运营成本</p>
    </div>

    <!-- 筛选条件 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-select
            v-model:value="filters.warehouse"
            placeholder="选择仓库"
            allowClear
            style="width: 100%"
            @change="handleFilterChange"
          >
            <a-select-option value="all">全部仓库</a-select-option>
            <a-select-option value="main">主仓库</a-select-option>
            <a-select-option value="backup">备用仓库</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="filters.category"
            placeholder="商品分类"
            allowClear
            style="width: 100%"
            @change="handleFilterChange"
          >
            <a-select-option value="all">全部分类</a-select-option>
            <a-select-option value="drink">饮料食品</a-select-option>
            <a-select-option value="daily">日用品</a-select-option>
            <a-select-option value="electronics">电子产品</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="filters.status"
            placeholder="库存状态"
            allowClear
            style="width: 100%"
            @change="handleFilterChange"
          >
            <a-select-option value="all">全部状态</a-select-option>
            <a-select-option value="normal">正常</a-select-option>
            <a-select-option value="warning">预警</a-select-option>
            <a-select-option value="danger">缺货</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="10" class="text-right">
          <a-space>
            <a-button @click="handleReset">重置</a-button>
            <a-button type="primary" @click="handleSearch">
              <SearchOutlined />
              查询
            </a-button>
            <a-button @click="handleExport">
              <ExportOutlined />
              导出报表
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 库存概览 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="商品总数"
            :value="statistics.totalProducts"
            :value-style="{ color: '#1890ff' }"
          />
          <div class="stat-desc">种类数量</div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="库存总值"
            :value="statistics.totalValue"
            prefix="¥"
            :precision="2"
            :value-style="{ color: '#52c41a' }"
          />
          <div class="stat-desc">按成本价计算</div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="预警商品"
            :value="statistics.warningProducts"
            :value-style="{ color: '#faad14' }"
          />
          <div class="stat-desc">需要补货</div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="缺货商品"
            :value="statistics.outOfStock"
            :value-style="{ color: '#f5222d' }"
          />
          <div class="stat-desc">急需处理</div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表分析 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <!-- 库存预警监控 -->
      <a-col :xs="24" :lg="14">
        <a-card title="库存预警监控" class="chart-card">
          <InventoryBarChart :data="inventoryWarningData" height="400px" title="" />
        </a-card>
      </a-col>

      <!-- 库存分布 -->
      <a-col :xs="24" :lg="10">
        <a-card title="库存分布" class="chart-card">
          <CategoryPieChart :data="inventoryDistributionData" height="400px" title="" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 周转率分析 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <a-col :span="24">
        <a-card title="库存周转率分析" class="chart-card">
          <template #extra>
            <a-radio-group
              v-model:value="turnoverPeriod"
              size="small"
              @change="handleTurnoverPeriodChange"
            >
              <a-radio-button value="month">月度</a-radio-button>
              <a-radio-button value="quarter">季度</a-radio-button>
              <a-radio-button value="year">年度</a-radio-button>
            </a-radio-group>
          </template>
          <SalesTrendChart :data="turnoverData" height="350px" title="" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 库存明细表格 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <a-col :xs="24" :lg="12">
        <a-card title="库存预警明细" class="table-card">
          <a-table
            :columns="warningColumns"
            :data-source="warningTableData"
            :pagination="{ pageSize: 8, simple: true }"
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="getStatusColor(record.status)">
                  {{ getStatusText(record.status) }}
                </a-tag>
              </template>
              <template v-else-if="column.key === 'stock'">
                <span
                  :class="{
                    'text-warning': record.status === 'warning',
                    'text-error': record.status === 'danger'
                  }"
                >
                  {{ record.stock }}
                </span>
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space>
                  <a-button type="link" size="small" @click="handleStockAdjust(record)">
                    调整
                  </a-button>
                  <a-button type="link" size="small" @click="handlePurchase(record)">
                    采购
                  </a-button>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col :xs="24" :lg="12">
        <a-card title="周转率排行" class="table-card">
          <a-table
            :columns="turnoverColumns"
            :data-source="turnoverTableData"
            :pagination="{ pageSize: 8, simple: true }"
            size="small"
          >
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'rank'">
                <a-tag
                  :color="index < 3 ? ['gold', 'silver', '#cd7f32'][index] : 'default'"
                  class="rank-tag"
                >
                  {{ index + 1 }}
                </a-tag>
              </template>
              <template v-else-if="column.key === 'turnover'">
                <span class="font-medium" :class="getTurnoverClass(record.turnover)">
                  {{ record.turnover.toFixed(2) }}
                </span>
              </template>
              <template v-else-if="column.key === 'value'">
                <span class="font-medium">¥{{ record.value.toLocaleString() }}</span>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>

    <!-- 库存变动趋势 -->
    <a-row :gutter="[24, 24]">
      <a-col :span="24">
        <a-card title="库存变动趋势" class="chart-card">
          <template #extra>
            <a-space>
              <a-select
                v-model:value="trendType"
                style="width: 120px"
                @change="handleTrendTypeChange"
              >
                <a-select-option value="in">入库趋势</a-select-option>
                <a-select-option value="out">出库趋势</a-select-option>
                <a-select-option value="both">进出对比</a-select-option>
              </a-select>
            </a-space>
          </template>
          <SalesTrendChart :data="stockTrendData" height="300px" title="" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import { SearchOutlined, ExportOutlined } from '@ant-design/icons-vue'
  import {
    SalesTrendChart,
    CategoryPieChart,
    InventoryBarChart,
    type SalesData,
    type CategoryData,
    type InventoryData
  } from '@/components/charts'

  // 筛选条件
  const filters = reactive({
    warehouse: undefined,
    category: undefined,
    status: undefined
  })

  // 控制变量
  const turnoverPeriod = ref('month')
  const trendType = ref('both')

  // 统计数据
  const statistics = ref({
    totalProducts: 1245,
    totalValue: 1285600.5,
    warningProducts: 23,
    outOfStock: 8
  })

  // 库存预警数据
  const inventoryWarningData = ref<InventoryData[]>([
    { name: '可口可乐 330ml', current: 10, min: 20, max: 100, status: 'danger' },
    { name: '康师傅方便面', current: 5, min: 15, max: 80, status: 'danger' },
    { name: '洗衣粉 2kg', current: 8, min: 10, max: 50, status: 'warning' },
    { name: '大米 10kg', current: 25, min: 12, max: 60, status: 'normal' },
    { name: '洗发水 500ml', current: 7, min: 18, max: 45, status: 'danger' },
    { name: '面包', current: 15, min: 8, max: 30, status: 'normal' }
  ])

  // 库存分布数据
  const inventoryDistributionData = ref<CategoryData[]>([
    { name: '饮料食品', value: 456000 },
    { name: '日用品', value: 298000 },
    { name: '电子产品', value: 235000 },
    { name: '服装鞋帽', value: 156000 },
    { name: '家居用品', value: 140600 }
  ])

  // 周转率数据
  const turnoverData = ref<SalesData[]>([
    { date: '1月', sales: 12.5, profit: 8.2 },
    { date: '2月', sales: 15.3, profit: 9.8 },
    { date: '3月', sales: 8.7, profit: 6.1 },
    { date: '4月', sales: 22.1, profit: 15.6 },
    { date: '5月', sales: 18.9, profit: 12.3 },
    { date: '6月', sales: 25.6, profit: 18.9 }
  ])

  // 库存变动趋势数据
  const stockTrendData = ref<SalesData[]>([
    { date: '01-15', sales: 2800, profit: 2200 },
    { date: '01-16', sales: 3200, profit: 2800 },
    { date: '01-17', sales: 2100, profit: 1900 },
    { date: '01-18', sales: 3800, profit: 3200 },
    { date: '01-19', sales: 3100, profit: 2600 },
    { date: '01-20', sales: 4200, profit: 3800 },
    { date: '01-21', sales: 2900, profit: 2400 }
  ])

  // 预警表格列
  const warningColumns = [
    { title: '商品名称', dataIndex: 'name', key: 'name', width: 140 },
    { title: '当前库存', key: 'stock', width: 100, align: 'center' },
    { title: '预警值', dataIndex: 'warning', key: 'warning', width: 80, align: 'center' },
    { title: '状态', key: 'status', width: 80, align: 'center' },
    { title: '操作', key: 'action', width: 120, align: 'center' }
  ]

  // 预警表格数据
  const warningTableData = ref([
    { name: '可口可乐 330ml', stock: 10, warning: 20, status: 'danger' },
    { name: '康师傅方便面', stock: 5, warning: 15, status: 'danger' },
    { name: '洗衣粉 2kg', stock: 8, warning: 10, status: 'warning' },
    { name: '大米 10kg', stock: 25, warning: 12, status: 'normal' },
    { name: '洗发水 500ml', stock: 7, warning: 18, status: 'danger' }
  ])

  // 周转率表格列
  const turnoverColumns = [
    { title: '排名', key: 'rank', width: 80, align: 'center' },
    { title: '商品名称', dataIndex: 'name', key: 'name', width: 140 },
    { title: '周转率', key: 'turnover', width: 100, align: 'center' },
    { title: '库存价值', key: 'value', width: 120, align: 'right' }
  ]

  // 周转率表格数据
  const turnoverTableData = ref([
    { name: '可口可乐 330ml', turnover: 8.5, value: 12600 },
    { name: '康师傅方便面', turnover: 7.2, value: 8900 },
    { name: '洗衣粉 2kg', turnover: 6.8, value: 15600 },
    { name: '大米 10kg', turnover: 5.9, value: 23400 },
    { name: '洗发水 500ml', turnover: 4.3, value: 18700 }
  ])

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      normal: 'green',
      warning: 'orange',
      danger: 'red'
    }
    return colors[status as keyof typeof colors] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = {
      normal: '正常',
      warning: '预警',
      danger: '缺货'
    }
    return texts[status as keyof typeof texts] || status
  }

  // 获取周转率样式类
  const getTurnoverClass = (turnover: number) => {
    if (turnover > 6) return 'text-success'
    if (turnover > 3) return 'text-warning'
    return 'text-error'
  }

  // 筛选变化处理
  const handleFilterChange = () => {
    loadReportData()
  }

  // 重置筛选
  const handleReset = () => {
    Object.assign(filters, {
      warehouse: undefined,
      category: undefined,
      status: undefined
    })
    loadReportData()
  }

  // 查询数据
  const handleSearch = () => {
    loadReportData()
  }

  // 导出报表
  const handleExport = () => {
    message.info('报表导出功能开发中')
  }

  // 周转率周期变化
  const handleTurnoverPeriodChange = () => {
    loadTurnoverData(turnoverPeriod.value)
  }

  // 趋势类型变化
  const handleTrendTypeChange = () => {
    loadStockTrendData(trendType.value)
  }

  // 库存调整
  const handleStockAdjust = (record: any) => {
    message.info(`调整 ${record.name} 的库存`)
  }

  // 采购处理
  const handlePurchase = (record: any) => {
    message.info(`为 ${record.name} 创建采购订单`)
  }

  // 加载报表数据
  const loadReportData = async () => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('数据加载完成')
    } catch (error) {
      message.error('数据加载失败')
    }
  }

  // 加载周转率数据
  const loadTurnoverData = async (period: string) => {
    console.log('加载周转率数据:', period)
  }

  // 加载库存趋势数据
  const loadStockTrendData = async (type: string) => {
    console.log('加载库存趋势数据:', type)
  }

  // 组件挂载
  onMounted(() => {
    loadReportData()
  })
</script>

<style scoped>
  .inventory-report {
    padding: 0;
  }

  .stat-card {
    text-align: center;
    border-radius: 8px;
    transition: all 0.3s ease;
  }

  .stat-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .stat-desc {
    color: #666;
    font-size: 12px;
    margin-top: 8px;
  }

  .chart-card,
  .table-card {
    border-radius: 8px;
    transition: all 0.3s ease;
  }

  .chart-card:hover,
  .table-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .rank-tag {
    font-weight: bold;
    min-width: 32px;
    text-align: center;
  }

  :deep(.ant-table-cell) {
    padding: 8px 12px;
  }

  :deep(.ant-card-head) {
    border-bottom: 1px solid #f0f0f0;
  }

  :deep(.ant-card-head-title) {
    font-weight: 600;
    color: #262626;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .stat-card {
      margin-bottom: 16px;
    }

    .chart-card,
    .table-card {
      margin-bottom: 16px;
    }
  }
</style>
