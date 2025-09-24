<template>
  <div class="stock">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">库存查询</h2>
      <p class="text-secondary mt-1 mb-0">查看所有商品的库存情况，支持实时库存监控和预警</p>
    </div>

    <!-- 统计卡片 -->
    <a-row :gutter="24" class="mb-4">
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="总商品数"
            :value="statistics.totalGoods"
            :value-style="{ color: '#1890ff' }"
            suffix="个"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="库存预警"
            :value="statistics.lowStock"
            :value-style="{ color: '#ff4d4f' }"
            suffix="个"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="零库存"
            :value="statistics.zeroStock"
            :value-style="{ color: '#faad14' }"
            suffix="个"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="库存总值"
            :value="statistics.totalValue"
            :precision="2"
            :value-style="{ color: '#52c41a' }"
            prefix="¥"
          />
        </a-card>
      </a-col>
    </a-row>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索商品名称或编码"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.categoryId"
            placeholder="选择分类"
            allowClear
            style="width: 100%"
          >
            <a-select-option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.stockStatus"
            placeholder="库存状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="normal">正常</a-select-option>
            <a-select-option value="low">预警</a-select-option>
            <a-select-option value="zero">零库存</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="10" class="text-right">
          <a-space>
            <a-button @click="handleReset">重置</a-button>
            <a-button type="primary" @click="handleSearch">
              <SearchOutlined />
              搜索
            </a-button>
            <a-button type="primary" @click="handleExport">
              <DownloadOutlined />
              导出库存
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 库存表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="stockList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'image'">
            <a-image
              :width="50"
              :height="50"
              :src="record.image"
              :preview="true"
              fallback="/placeholder.png"
              class="rounded"
            />
          </template>

          <template v-else-if="column.key === 'price'">
            <span class="font-medium">¥{{ record.price.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'stock'">
            <div class="d-flex align-center">
              <a-tag :color="getStockColor(record.stock, record.minStock)" class="mr-2">
                {{ record.stock }}
              </a-tag>
              <span v-if="record.stock <= record.minStock" class="text-error font-sm">
                (预警)
              </span>
            </div>
          </template>

          <template v-else-if="column.key === 'stockValue'">
            <span class="font-medium text-primary"
              >¥{{ (record.stock * record.price).toFixed(2) }}</span
            >
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleStockAdjust(record)">
                <EditOutlined />
                库存调整
              </a-button>
              <a-button type="link" size="small" @click="handleStockHistory(record)">
                <HistoryOutlined />
                出入库记录
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 库存调整弹窗 -->
    <a-modal
      v-model:open="adjustModalVisible"
      title="库存调整"
      width="600px"
      @ok="handleAdjustSave"
      @cancel="handleAdjustCancel"
      :confirmLoading="adjustLoading"
    >
      <div v-if="currentGoods" class="mb-4">
        <a-descriptions :column="2" bordered size="small">
          <a-descriptions-item label="商品名称">{{ currentGoods.name }}</a-descriptions-item>
          <a-descriptions-item label="商品编码">{{ currentGoods.code }}</a-descriptions-item>
          <a-descriptions-item label="当前库存">
            <a-tag :color="getStockColor(currentGoods.stock, currentGoods.minStock)">
              {{ currentGoods.stock }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="最低库存">{{ currentGoods.minStock }}</a-descriptions-item>
        </a-descriptions>
      </div>

      <a-form ref="adjustFormRef" :model="adjustForm" :rules="adjustRules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="调整类型" name="type">
              <a-select v-model:value="adjustForm.type" placeholder="请选择调整类型">
                <a-select-option value="in">入库</a-select-option>
                <a-select-option value="out">出库</a-select-option>
                <a-select-option value="set">设置</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="调整数量" name="quantity">
              <a-input-number
                v-model:value="adjustForm.quantity"
                :min="adjustForm.type === 'out' ? -currentGoods?.stock : undefined"
                style="width: 100%"
                placeholder="请输入数量"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="调整原因" name="reason">
          <a-textarea v-model:value="adjustForm.reason" :rows="3" placeholder="请输入调整原因" />
        </a-form-item>

        <div
          v-if="adjustForm.type && adjustForm.quantity && currentGoods"
          class="bg-light p-3 rounded"
        >
          <div class="d-flex justify-between align-center">
            <span>调整后库存：</span>
            <span class="font-medium text-primary">
              {{ getAdjustedStock() }}
            </span>
          </div>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import {
    SearchOutlined,
    DownloadOutlined,
    EditOutlined,
    HistoryOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface StockItem {
    id: number
    name: string
    code: string
    categoryId: number
    categoryName: string
    price: number
    stock: number
    minStock: number
    image: string
    createTime: string
  }

  interface Category {
    id: number
    name: string
  }

  interface Statistics {
    totalGoods: number
    lowStock: number
    zeroStock: number
    totalValue: number
  }

  // 响应式数据
  const loading = ref(false)
  const adjustLoading = ref(false)
  const stockList = ref<StockItem[]>([])
  const categories = ref<Category[]>([])
  const adjustModalVisible = ref(false)
  const currentGoods = ref<StockItem | null>(null)
  const adjustFormRef = ref<FormInstance>()

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    categoryId: undefined,
    stockStatus: undefined
  })

  // 库存调整表单
  const adjustForm = reactive({
    type: '',
    quantity: undefined,
    reason: ''
  })

  // 统计数据
  const statistics = ref<Statistics>({
    totalGoods: 0,
    lowStock: 0,
    zeroStock: 0,
    totalValue: 0
  })

  // 分页配置
  const pagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total: number) => `共 ${total} 条记录`
  })

  // 表格列配置
  const columns = [
    {
      title: '商品图片',
      key: 'image',
      width: 80,
      align: 'center'
    },
    {
      title: '商品名称',
      dataIndex: 'name',
      key: 'name',
      width: 200
    },
    {
      title: '商品编码',
      dataIndex: 'code',
      key: 'code',
      width: 120
    },
    {
      title: '分类',
      dataIndex: 'categoryName',
      key: 'categoryName',
      width: 120
    },
    {
      title: '单价',
      key: 'price',
      width: 100,
      align: 'right'
    },
    {
      title: '当前库存',
      key: 'stock',
      width: 120,
      align: 'center'
    },
    {
      title: '最低库存',
      dataIndex: 'minStock',
      key: 'minStock',
      width: 100,
      align: 'center'
    },
    {
      title: '库存金额',
      key: 'stockValue',
      width: 120,
      align: 'right'
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 表单验证规则
  const adjustRules = {
    type: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
    quantity: [{ required: true, message: '请输入调整数量', trigger: 'blur' }],
    reason: [{ required: true, message: '请输入调整原因', trigger: 'blur' }]
  }

  // 获取库存状态颜色
  const getStockColor = (stock: number, minStock: number) => {
    if (stock === 0) return 'error'
    if (stock <= minStock) return 'warning'
    return 'success'
  }

  // 计算调整后库存
  const getAdjustedStock = () => {
    if (!currentGoods.value || !adjustForm.quantity) return currentGoods.value?.stock || 0

    const currentStock = currentGoods.value.stock
    const quantity = adjustForm.quantity

    switch (adjustForm.type) {
      case 'in':
        return currentStock + quantity
      case 'out':
        return Math.max(0, currentStock - quantity)
      case 'set':
        return quantity
      default:
        return currentStock
    }
  }

  // 加载库存数据
  const loadStockList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          name: '苹果iPhone 15',
          code: 'IP15001',
          categoryId: 1,
          categoryName: '电子产品',
          price: 5999.0,
          stock: 8,
          minStock: 10,
          image: '/api/placeholder/50/50',
          createTime: '2024-01-15 10:30:00'
        },
        {
          id: 2,
          name: '华为Mate 60',
          code: 'HW60001',
          categoryId: 1,
          categoryName: '电子产品',
          price: 4999.0,
          stock: 0,
          minStock: 5,
          image: '/api/placeholder/50/50',
          createTime: '2024-01-15 11:00:00'
        }
      ]

      stockList.value = mockData
      pagination.total = mockData.length

      // 计算统计数据
      statistics.value = {
        totalGoods: mockData.length,
        lowStock: mockData.filter(item => item.stock <= item.minStock && item.stock > 0).length,
        zeroStock: mockData.filter(item => item.stock === 0).length,
        totalValue: mockData.reduce((sum, item) => sum + item.stock * item.price, 0)
      }
    } catch (error) {
      message.error('加载库存数据失败')
    } finally {
      loading.value = false
    }
  }

  // 加载分类列表
  const loadCategories = async () => {
    try {
      const mockCategories = [
        { id: 1, name: '电子产品' },
        { id: 2, name: '服装鞋帽' },
        { id: 3, name: '食品饮料' }
      ]
      categories.value = mockCategories
    } catch (error) {
      message.error('加载分类列表失败')
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadStockList()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      categoryId: undefined,
      stockStatus: undefined
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadStockList()
  }

  // 导出库存
  const handleExport = () => {
    message.success('导出功能开发中')
  }

  // 库存调整
  const handleStockAdjust = (record: StockItem) => {
    currentGoods.value = record
    adjustModalVisible.value = true
    resetAdjustForm()
  }

  // 出入库记录
  const handleStockHistory = (record: StockItem) => {
    message.info('出入库记录功能开发中')
  }

  // 保存库存调整
  const handleAdjustSave = async () => {
    try {
      await adjustFormRef.value?.validate()
      adjustLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success('库存调整成功')
      adjustModalVisible.value = false
      loadStockList()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      adjustLoading.value = false
    }
  }

  // 取消库存调整
  const handleAdjustCancel = () => {
    adjustModalVisible.value = false
    resetAdjustForm()
  }

  // 重置调整表单
  const resetAdjustForm = () => {
    Object.assign(adjustForm, {
      type: '',
      quantity: undefined,
      reason: ''
    })
    adjustFormRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadCategories()
    loadStockList()
  })
</script>

<style scoped>
  .stock {
    padding: 0;
  }

  .stat-card {
    text-align: center;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-statistic-content-value) {
    font-size: 24px;
    font-weight: 600;
  }
</style>
