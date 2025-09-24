<template>
  <div class="receivables">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">应收管理</h2>
      <p class="text-secondary mt-1 mb-0">管理客户应收账款，包括应收记录、收款登记、账龄分析等</p>
    </div>

    <!-- 统计卡片 -->
    <a-row :gutter="16" class="mb-4">
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="总应收金额"
            :value="statistics.totalAmount"
            suffix="元"
            :value-style="{ color: '#cf1322' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="逾期应收"
            :value="statistics.overdueAmount"
            suffix="元"
            :value-style="{ color: '#fa541c' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="本月回款"
            :value="statistics.monthlyCollection"
            suffix="元"
            :value-style="{ color: '#52c41a' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic title="应收笔数" :value="statistics.totalCount" suffix="笔" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索客户名称或单据号"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.status"
            placeholder="应收状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="pending">未收款</a-select-option>
            <a-select-option value="partial">部分收款</a-select-option>
            <a-select-option value="completed">已收款</a-select-option>
            <a-select-option value="overdue">逾期</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-date-picker
            v-model:value="searchForm.dateRange"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </a-col>
        <a-col :span="10" class="text-right">
          <a-space>
            <a-button @click="handleReset">重置</a-button>
            <a-button type="primary" @click="handleSearch">
              <SearchOutlined />
              搜索
            </a-button>
            <a-button type="primary" @click="handleAddCollection">
              <PlusOutlined />
              登记收款
            </a-button>
            <a-button @click="handleExport">
              <ExportOutlined />
              导出
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 应收列表 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="receivablesList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'customerName'">
            <div class="d-flex align-center">
              <UserOutlined class="text-primary mr-2" />
              <div>
                <div class="font-medium">{{ record.customerName }}</div>
                <div class="text-secondary font-sm">{{ record.customerPhone }}</div>
              </div>
            </div>
          </template>

          <template v-else-if="column.key === 'orderInfo'">
            <div>
              <div class="font-medium">{{ record.orderNo }}</div>
              <div class="text-secondary font-sm">{{ record.orderDate }}</div>
            </div>
          </template>

          <template v-else-if="column.key === 'amount'">
            <span class="font-medium text-error">¥{{ record.amount.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'paidAmount'">
            <span class="font-medium text-success">¥{{ record.paidAmount.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'remainingAmount'">
            <span
              class="font-medium"
              :class="record.remainingAmount > 0 ? 'text-warning' : 'text-success'"
            >
              ¥{{ record.remainingAmount.toFixed(2) }}
            </span>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'dueDate'">
            <div :class="{ 'text-error': isOverdue(record.dueDate) }">
              {{ record.dueDate }}
              <ClockCircleOutlined v-if="isOverdue(record.dueDate)" class="ml-1" />
            </div>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleViewDetails(record)">
                <EyeOutlined />
                详情
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleCollection(record)"
                :disabled="record.status === 'completed'"
              >
                <MoneyCollectOutlined />
                收款
              </a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 收款登记弹窗 -->
    <a-modal
      v-model:open="collectionModalVisible"
      title="收款登记"
      width="600px"
      @ok="handleSaveCollection"
      @cancel="handleCancelCollection"
      :confirmLoading="saveLoading"
    >
      <a-form
        ref="collectionFormRef"
        :model="collectionFormData"
        :rules="collectionFormRules"
        layout="vertical"
        class="mt-4"
      >
        <a-form-item label="应收单据" name="receivableId">
          <a-select v-model:value="collectionFormData.receivableId" placeholder="请选择应收单据">
            <a-select-option v-for="item in receivablesList" :key="item.id" :value="item.id">
              {{ item.orderNo }} - {{ item.customerName }} (应收¥{{
                item.remainingAmount.toFixed(2)
              }})
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="收款金额" name="amount">
              <a-input-number
                v-model:value="collectionFormData.amount"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="0.00"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="收款方式" name="paymentMethod">
              <a-select
                v-model:value="collectionFormData.paymentMethod"
                placeholder="请选择收款方式"
              >
                <a-select-option value="cash">现金</a-select-option>
                <a-select-option value="bank">银行转账</a-select-option>
                <a-select-option value="alipay">支付宝</a-select-option>
                <a-select-option value="wechat">微信支付</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="收款日期" name="collectionDate">
              <a-date-picker
                v-model:value="collectionFormData.collectionDate"
                style="width: 100%"
                placeholder="选择收款日期"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="收款人" name="collector">
              <a-input v-model:value="collectionFormData.collector" placeholder="请输入收款人" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="collectionFormData.remark"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 应收详情弹窗 -->
    <a-modal v-model:open="detailModalVisible" title="应收详情" width="800px" :footer="null">
      <div v-if="currentReceivable">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="单据号">{{ currentReceivable.orderNo }}</a-descriptions-item>
          <a-descriptions-item label="客户名称">{{
            currentReceivable.customerName
          }}</a-descriptions-item>
          <a-descriptions-item label="联系电话">{{
            currentReceivable.customerPhone
          }}</a-descriptions-item>
          <a-descriptions-item label="订单日期">{{
            currentReceivable.orderDate
          }}</a-descriptions-item>
          <a-descriptions-item label="应收金额">
            <span class="text-error font-medium">¥{{ currentReceivable.amount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="已收金额">
            <span class="text-success font-medium"
              >¥{{ currentReceivable.paidAmount.toFixed(2) }}</span
            >
          </a-descriptions-item>
          <a-descriptions-item label="未收金额">
            <span class="text-warning font-medium"
              >¥{{ currentReceivable.remainingAmount.toFixed(2) }}</span
            >
          </a-descriptions-item>
          <a-descriptions-item label="到期日期">{{
            currentReceivable.dueDate
          }}</a-descriptions-item>
          <a-descriptions-item label="状态" :span="2">
            <a-tag :color="getStatusColor(currentReceivable.status)">
              {{ getStatusText(currentReceivable.status) }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>

        <div class="mt-4">
          <h4>收款记录</h4>
          <a-table
            :columns="collectionColumns"
            :data-source="currentReceivable.collections || []"
            :pagination="false"
            size="small"
          />
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import {
    SearchOutlined,
    PlusOutlined,
    ExportOutlined,
    EyeOutlined,
    EditOutlined,
    MoneyCollectOutlined,
    UserOutlined,
    ClockCircleOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'
  import dayjs from 'dayjs'

  // 接口类型定义
  interface ReceivableItem {
    id: number
    orderNo: string
    orderDate: string
    customerName: string
    customerPhone: string
    amount: number
    paidAmount: number
    remainingAmount: number
    status: 'pending' | 'partial' | 'completed' | 'overdue'
    dueDate: string
    createTime: string
    collections?: CollectionRecord[]
  }

  interface CollectionRecord {
    id: number
    amount: number
    paymentMethod: string
    collectionDate: string
    collector: string
    remark: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const receivablesList = ref<ReceivableItem[]>([])
  const collectionModalVisible = ref(false)
  const detailModalVisible = ref(false)
  const currentReceivable = ref<ReceivableItem | null>(null)
  const collectionFormRef = ref<FormInstance>()

  // 统计数据
  const statistics = ref({
    totalAmount: 0,
    overdueAmount: 0,
    monthlyCollection: 0,
    totalCount: 0
  })

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    status: undefined,
    dateRange: undefined
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
      title: '客户信息',
      key: 'customerName',
      width: 180
    },
    {
      title: '订单信息',
      key: 'orderInfo',
      width: 150
    },
    {
      title: '应收金额',
      key: 'amount',
      width: 120,
      align: 'right'
    },
    {
      title: '已收金额',
      key: 'paidAmount',
      width: 120,
      align: 'right'
    },
    {
      title: '未收金额',
      key: 'remainingAmount',
      width: 120,
      align: 'right'
    },
    {
      title: '到期日期',
      key: 'dueDate',
      width: 120,
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width: 160
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 收款记录表格列
  const collectionColumns = [
    {
      title: '收款金额',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount: number) => `¥${amount.toFixed(2)}`
    },
    {
      title: '收款方式',
      dataIndex: 'paymentMethod',
      key: 'paymentMethod'
    },
    {
      title: '收款日期',
      dataIndex: 'collectionDate',
      key: 'collectionDate'
    },
    {
      title: '收款人',
      dataIndex: 'collector',
      key: 'collector'
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark'
    }
  ]

  // 收款表单数据
  const collectionFormData = reactive({
    receivableId: undefined as number | undefined,
    amount: undefined as number | undefined,
    paymentMethod: undefined as string | undefined,
    collectionDate: undefined,
    collector: '',
    remark: ''
  })

  // 收款表单验证规则
  const collectionFormRules = {
    receivableId: [{ required: true, message: '请选择应收单据', trigger: 'change' }],
    amount: [{ required: true, message: '请输入收款金额', trigger: 'blur' }],
    paymentMethod: [{ required: true, message: '请选择收款方式', trigger: 'change' }],
    collectionDate: [{ required: true, message: '请选择收款日期', trigger: 'change' }],
    collector: [{ required: true, message: '请输入收款人', trigger: 'blur' }]
  }

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      pending: 'orange',
      partial: 'blue',
      completed: 'green',
      overdue: 'red'
    }
    return colors[status as keyof typeof colors] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = {
      pending: '未收款',
      partial: '部分收款',
      completed: '已收款',
      overdue: '逾期'
    }
    return texts[status as keyof typeof texts] || '未知'
  }

  // 判断是否逾期
  const isOverdue = (dueDate: string) => {
    return dayjs().isAfter(dayjs(dueDate))
  }

  // 加载应收列表
  const loadReceivables = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData: ReceivableItem[] = [
        {
          id: 1,
          orderNo: 'SO20240115001',
          orderDate: '2024-01-15',
          customerName: '北京科技有限公司',
          customerPhone: '13800138001',
          amount: 15800.0,
          paidAmount: 8000.0,
          remainingAmount: 7800.0,
          status: 'partial',
          dueDate: '2024-02-15',
          createTime: '2024-01-15 10:30:00',
          collections: [
            {
              id: 1,
              amount: 8000.0,
              paymentMethod: '银行转账',
              collectionDate: '2024-01-20',
              collector: '张三',
              remark: '首付款'
            }
          ]
        },
        {
          id: 2,
          orderNo: 'SO20240110002',
          orderDate: '2024-01-10',
          customerName: '上海贸易公司',
          customerPhone: '13800138002',
          amount: 25600.0,
          paidAmount: 0,
          remainingAmount: 25600.0,
          status: 'overdue',
          dueDate: '2024-01-25',
          createTime: '2024-01-10 14:20:00'
        }
      ]

      receivablesList.value = mockData
      pagination.total = mockData.length

      // 更新统计数据
      statistics.value = {
        totalAmount: mockData.reduce((sum, item) => sum + item.amount, 0),
        overdueAmount: mockData
          .filter(item => item.status === 'overdue')
          .reduce((sum, item) => sum + item.remainingAmount, 0),
        monthlyCollection: 35000.0,
        totalCount: mockData.length
      }
    } catch (error) {
      message.error('加载应收列表失败')
    } finally {
      loading.value = false
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadReceivables()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      status: undefined,
      dateRange: undefined
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadReceivables()
  }

  // 查看详情
  const handleViewDetails = (record: ReceivableItem) => {
    currentReceivable.value = record
    detailModalVisible.value = true
  }

  // 收款登记
  const handleCollection = (record: ReceivableItem) => {
    collectionFormData.receivableId = record.id
    collectionFormData.amount = record.remainingAmount
    collectionModalVisible.value = true
  }

  // 新增收款
  const handleAddCollection = () => {
    resetCollectionForm()
    collectionModalVisible.value = true
  }

  // 编辑
  const handleEdit = (record: ReceivableItem) => {
    message.info('编辑功能开发中')
  }

  // 导出
  const handleExport = () => {
    message.info('导出功能开发中')
  }

  // 保存收款记录
  const handleSaveCollection = async () => {
    try {
      await collectionFormRef.value?.validate()
      saveLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success('收款登记成功')
      collectionModalVisible.value = false
      loadReceivables()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      saveLoading.value = false
    }
  }

  // 取消收款
  const handleCancelCollection = () => {
    collectionModalVisible.value = false
    resetCollectionForm()
  }

  // 重置收款表单
  const resetCollectionForm = () => {
    Object.assign(collectionFormData, {
      receivableId: undefined,
      amount: undefined,
      paymentMethod: undefined,
      collectionDate: undefined,
      collector: '',
      remark: ''
    })
    collectionFormRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadReceivables()
  })
</script>

<style scoped>
  .receivables {
    padding: 0;
  }

  .stat-card {
    text-align: center;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-statistic-content) {
    font-size: 24px;
    font-weight: bold;
  }

  :deep(.ant-statistic-title) {
    margin-bottom: 8px;
    color: #666;
    font-size: 14px;
  }
</style>
