<template>
  <div class="sales-report">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">销售报表</h2>
      <p class="text-secondary mt-1 mb-0">分析销售数据，掌握业务趋势，优化经营决策</p>
    </div>

    <!-- 筛选条件 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-date-picker
            v-model:value="filters.dateRange"
            type="daterange"
            placeholder="选择日期范围"
            style="width: 100%"
            @change="handleFilterChange"
          />
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
            v-model:value="filters.customer"
            placeholder="客户类型"
            allowClear
            style="width: 100%"
            @change="handleFilterChange"
          >
            <a-select-option value="all">全部客户</a-select-option>
            <a-select-option value="vip">VIP客户</a-select-option>
            <a-select-option value="regular">普通客户</a-select-option>
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

    <!-- 统计概览 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="总销售额"
            :value="statistics.totalSales"
            prefix="¥"
            :precision="2"
            :value-style="{ color: '#3f8600' }"
          />
          <div class="stat-trend">
            <ArrowUpOutlined style="color: #3f8600" />
            <span style="color: #3f8600">12.5%</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="总订单数"
            :value="statistics.totalOrders"
            :value-style="{ color: '#1890ff' }"
          />
          <div class="stat-trend">
            <ArrowUpOutlined style="color: #3f8600" />
            <span style="color: #3f8600">8.3%</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="平均客单价"
            :value="statistics.avgOrderValue"
            prefix="¥"
            :precision="2"
            :value-style="{ color: '#722ed1' }"
          />
          <div class="stat-trend">
            <ArrowUpOutlined style="color: #3f8600" />
            <span style="color: #3f8600">5.2%</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card class="stat-card">
          <a-statistic
            title="毛利率"
            :value="statistics.grossMarginRate"
            suffix="%"
            :precision="1"
            :value-style="{ color: '#52c41a' }"
          />
          <div class="stat-trend">
            <ArrowDownOutlined style="color: #cf1322" />
            <span style="color: #cf1322">-1.2%</span>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表分析 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <!-- 销售趋势图 -->
      <a-col :xs="24" :lg="16">
        <a-card title="销售趋势分析" class="chart-card">
          <template #extra>
            <a-radio-group v-model:value="trendPeriod" size="small" @change="handlePeriodChange">
              <a-radio-button value="day">日</a-radio-button>
              <a-radio-button value="week">周</a-radio-button>
              <a-radio-button value="month">月</a-radio-button>
            </a-radio-group>
          </template>
          <SalesTrendChart :data="salesTrendData" height="400px" title="" />
        </a-card>
      </a-col>

      <!-- 销售分布饼图 -->
      <a-col :xs="24" :lg="8">
        <a-card title="销售分布" class="chart-card">
          <CategoryPieChart :data="salesDistributionData" height="400px" title="" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 商品销售排行 -->
    <a-row :gutter="[24, 24]" class="mb-4">
      <a-col :xs="24" :lg="12">
        <a-card title="商品销售排行TOP10" class="table-card">
          <a-table
            :columns="productRankColumns"
            :data-source="productRankData"
            :pagination="false"
            size="small"
            :scroll="{ y: 350 }"
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
              <template v-else-if="column.key === 'sales'">
                <span class="font-medium text-success">¥{{ record.sales.toLocaleString() }}</span>
              </template>
              <template v-else-if="column.key === 'quantity'">
                <span class="font-medium">{{ record.quantity }}</span>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <!-- 客户销售排行 -->
      <a-col :xs="24" :lg="12">
        <a-card title="客户销售排行TOP10" class="table-card">
          <a-table
            :columns="customerRankColumns"
            :data-source="customerRankData"
            :pagination="false"
            size="small"
            :scroll="{ y: 350 }"
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
              <template v-else-if="column.key === 'sales'">
                <span class="font-medium text-success">¥{{ record.sales.toLocaleString() }}</span>
              </template>
              <template v-else-if="column.key === 'orders'">
                <span class="font-medium">{{ record.orders }}</span>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>

    <!-- 时段分析 -->
    <a-row :gutter="[24, 24]">
      <a-col :span="24">
        <a-card title="销售时段分析" class="chart-card">
          <template #extra>
            <a-space>
              <a-select
                v-model:value="timeAnalysisType"
                style="width: 120px"
                @change="handleTimeAnalysisChange"
              >
                <a-select-option value="hour">按小时</a-select-option>
                <a-select-option value="weekday">按星期</a-select-option>
                <a-select-option value="month">按月份</a-select-option>
              </a-select>
            </a-space>
          </template>
          <InventoryBarChart :data="timeAnalysisData" height="300px" title="" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import {
    SearchOutlined,
    ExportOutlined,
    ArrowUpOutlined,
    ArrowDownOutlined
  } from '@ant-design/icons-vue'
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
    dateRange: null,
    category: undefined,
    customer: undefined
  })

  // 趋势周期
  const trendPeriod = ref('day')
  const timeAnalysisType = ref('hour')

  // 统计数据
  const statistics = ref({
    totalSales: 256789.5,
    totalOrders: 1234,
    avgOrderValue: 208.12,
    grossMarginRate: 25.8
  })

  // 销售趋势数据
  const salesTrendData = ref<SalesData[]>([
    { date: '01-15', sales: 12000, profit: 3000 },
    { date: '01-16', sales: 15000, profit: 3800 },
    { date: '01-17', sales: 8000, profit: 2000 },
    { date: '01-18', sales: 22000, profit: 5500 },
    { date: '01-19', sales: 18000, profit: 4500 },
    { date: '01-20', sales: 25000, profit: 6200 },
    { date: '01-21', sales: 20000, profit: 5000 },
    { date: '01-22', sales: 28000, profit: 7000 },
    { date: '01-23', sales: 15000, profit: 3750 },
    { date: '01-24', sales: 32000, profit: 8000 }
  ])

  // 销售分布数据
  const salesDistributionData = ref<CategoryData[]>([
    { name: '饮料食品', value: 89000 },
    { name: '日用品', value: 67000 },
    { name: '电子产品', value: 45000 },
    { name: '服装鞋帽', value: 32000 },
    { name: '家居用品', value: 23000 }
  ])

  // 时段分析数据
  const timeAnalysisData = ref<InventoryData[]>([
    { name: '9-10点', current: 1200, min: 0, max: 5000, status: 'normal' },
    { name: '10-11点', current: 2800, min: 0, max: 5000, status: 'normal' },
    { name: '11-12点', current: 4200, min: 0, max: 5000, status: 'normal' },
    { name: '14-15点', current: 3600, min: 0, max: 5000, status: 'normal' },
    { name: '15-16点', current: 3100, min: 0, max: 5000, status: 'normal' },
    { name: '16-17点', current: 2900, min: 0, max: 5000, status: 'normal' }
  ])

  // 商品排行表格列
  const productRankColumns = [
    { title: '排名', key: 'rank', width: 80, align: 'center' },
    { title: '商品名称', dataIndex: 'name', key: 'name', width: 200 },
    { title: '销售额', key: 'sales', width: 120, align: 'right' },
    { title: '销量', key: 'quantity', width: 100, align: 'center' },
    { title: '分类', dataIndex: 'category', key: 'category', width: 120 }
  ]

  // 商品排行数据
  const productRankData = ref([
    { name: '可口可乐 330ml', sales: 45600, quantity: 1520, category: '饮料食品' },
    { name: '康师傅方便面', sales: 38900, quantity: 1297, category: '饮料食品' },
    { name: '洗衣粉 2kg', sales: 32100, quantity: 642, category: '日用品' },
    { name: '大米 10kg', sales: 28700, quantity: 574, category: '饮料食品' },
    { name: '洗发水 500ml', sales: 25300, quantity: 506, category: '日用品' }
  ])

  // 客户排行表格列
  const customerRankColumns = [
    { title: '排名', key: 'rank', width: 80, align: 'center' },
    { title: '客户名称', dataIndex: 'name', key: 'name', width: 200 },
    { title: '销售额', key: 'sales', width: 120, align: 'right' },
    { title: '订单数', key: 'orders', width: 100, align: 'center' },
    { title: '客户类型', dataIndex: 'type', key: 'type', width: 120 }
  ]

  // 客户排行数据
  const customerRankData = ref([
    { name: '张三公司', sales: 156000, orders: 45, type: 'VIP客户' },
    { name: '李四企业', sales: 132000, orders: 38, type: 'VIP客户' },
    { name: '王五贸易', sales: 98000, orders: 29, type: '普通客户' },
    { name: '赵六商贸', sales: 87000, orders: 25, type: '普通客户' },
    { name: '孙七集团', sales: 76000, orders: 22, type: 'VIP客户' }
  ])

  // 筛选变化处理
  const handleFilterChange = () => {
    loadReportData()
  }

  // 重置筛选
  const handleReset = () => {
    Object.assign(filters, {
      dateRange: null,
      category: undefined,
      customer: undefined
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

  // 周期变化处理
  const handlePeriodChange = () => {
    loadSalesTrendData(trendPeriod.value)
  }

  // 时段分析类型变化
  const handleTimeAnalysisChange = () => {
    loadTimeAnalysisData(timeAnalysisType.value)
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

  // 加载销售趋势数据
  const loadSalesTrendData = async (period: string) => {
    // 根据周期加载不同数据
    console.log('加载销售趋势数据:', period)
  }

  // 加载时段分析数据
  const loadTimeAnalysisData = async (type: string) => {
    // 根据类型加载不同数据
    console.log('加载时段分析数据:', type)
  }

  // 组件挂载
  onMounted(() => {
    loadReportData()
  })
</script>

<style scoped>
  .sales-report {
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

  .stat-trend {
    margin-top: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    font-size: 12px;
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
