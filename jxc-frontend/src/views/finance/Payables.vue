<template>
  <div class="payables">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">应付管理</h2>
      <p class="text-secondary mt-1 mb-0">管理供应商应付账款，包括应付记录、付款登记、账龄分析等</p>
    </div>

    <!-- 统计卡片 -->
    <a-row :gutter="16" class="mb-4">
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="总应付金额"
            :value="statistics.totalAmount"
            suffix="元"
            :value-style="{ color: '#1890ff' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="逾期应付"
            :value="statistics.overdueAmount"
            suffix="元"
            :value-style="{ color: '#fa541c' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic
            title="本月付款"
            :value="statistics.monthlyPayment"
            suffix="元"
            :value-style="{ color: '#52c41a' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card">
          <a-statistic title="应付笔数" :value="statistics.totalCount" suffix="笔" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索供应商名称或单据号"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.status"
            placeholder="应付状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="pending">未付款</a-select-option>
            <a-select-option value="partial">部分付款</a-select-option>
            <a-select-option value="completed">已付款</a-select-option>
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
            <a-button type="primary" @click="handleAddPayment">
              <PlusOutlined />
              登记付款
            </a-button>
            <a-button @click="handleExport">
              <ExportOutlined />
              导出
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 应付列表 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="payablesList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'supplierName'">
            <div class="d-flex align-center">
              <ShopOutlined class="text-primary mr-2" />
              <div>
                <div class="font-medium">{{ record.supplierName }}</div>
                <div class="text-secondary font-sm">{{ record.supplierPhone }}</div>
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
            <span class="font-medium text-primary">¥{{ record.amount.toFixed(2) }}</span>
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
                @click="handlePayment(record)"
                :disabled="record.status === 'completed'"
              >
                <CreditCardOutlined />
                付款
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

    <!-- 付款登记弹窗 -->
    <a-modal
      v-model:open="paymentModalVisible"
      title="付款登记"
      width="600px"
      @ok="handleSavePayment"
      @cancel="handleCancelPayment"
      :confirmLoading="saveLoading"
    >
      <a-form
        ref="paymentFormRef"
        :model="paymentFormData"
        :rules="paymentFormRules"
        layout="vertical"
        class="mt-4"
      >
        <a-form-item label="应付单据" name="payableId">
          <a-select v-model:value="paymentFormData.payableId" placeholder="请选择应付单据">
            <a-select-option v-for="item in payablesList" :key="item.id" :value="item.id">
              {{ item.orderNo }} - {{ item.supplierName }} (应付¥{{
                item.remainingAmount.toFixed(2)
              }})
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="付款金额" name="amount">
              <a-input-number
                v-model:value="paymentFormData.amount"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="0.00"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="付款方式" name="paymentMethod">
              <a-select v-model:value="paymentFormData.paymentMethod" placeholder="请选择付款方式">
                <a-select-option value="cash">现金</a-select-option>
                <a-select-option value="bank">银行转账</a-select-option>
                <a-select-option value="check">支票</a-select-option>
                <a-select-option value="draft">汇票</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="付款日期" name="paymentDate">
              <a-date-picker
                v-model:value="paymentFormData.paymentDate"
                style="width: 100%"
                placeholder="选择付款日期"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="付款人" name="payer">
              <a-input v-model:value="paymentFormData.payer" placeholder="请输入付款人" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="paymentFormData.remark"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 应付详情弹窗 -->
    <a-modal v-model:open="detailModalVisible" title="应付详情" width="800px" :footer="null">
      <div v-if="currentPayable">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="单据号">{{ currentPayable.orderNo }}</a-descriptions-item>
          <a-descriptions-item label="供应商名称">{{
            currentPayable.supplierName
          }}</a-descriptions-item>
          <a-descriptions-item label="联系电话">{{
            currentPayable.supplierPhone
          }}</a-descriptions-item>
          <a-descriptions-item label="订单日期">{{ currentPayable.orderDate }}</a-descriptions-item>
          <a-descriptions-item label="应付金额">
            <span class="text-primary font-medium">¥{{ currentPayable.amount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="已付金额">
            <span class="text-success font-medium"
              >¥{{ currentPayable.paidAmount.toFixed(2) }}</span
            >
          </a-descriptions-item>
          <a-descriptions-item label="未付金额">
            <span class="text-warning font-medium"
              >¥{{ currentPayable.remainingAmount.toFixed(2) }}</span
            >
          </a-descriptions-item>
          <a-descriptions-item label="到期日期">{{ currentPayable.dueDate }}</a-descriptions-item>
          <a-descriptions-item label="状态" :span="2">
            <a-tag :color="getStatusColor(currentPayable.status)">
              {{ getStatusText(currentPayable.status) }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>

        <div class="mt-4">
          <h4>付款记录</h4>
          <a-table
            :columns="paymentColumns"
            :data-source="currentPayable.payments || []"
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
    CreditCardOutlined,
    ShopOutlined,
    ClockCircleOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'
  import dayjs from 'dayjs'

  // 接口类型定义
  interface PayableItem {
    id: number
    orderNo: string
    orderDate: string
    supplierName: string
    supplierPhone: string
    amount: number
    paidAmount: number
    remainingAmount: number
    status: 'pending' | 'partial' | 'completed' | 'overdue'
    dueDate: string
    createTime: string
    payments?: PaymentRecord[]
  }

  interface PaymentRecord {
    id: number
    amount: number
    paymentMethod: string
    paymentDate: string
    payer: string
    remark: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const payablesList = ref<PayableItem[]>([])
  const paymentModalVisible = ref(false)
  const detailModalVisible = ref(false)
  const currentPayable = ref<PayableItem | null>(null)
  const paymentFormRef = ref<FormInstance>()

  // 统计数据
  const statistics = ref({
    totalAmount: 0,
    overdueAmount: 0,
    monthlyPayment: 0,
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
    { title: '供应商信息', key: 'supplierName', width: 180 },
    { title: '订单信息', key: 'orderInfo', width: 150 },
    { title: '应付金额', key: 'amount', width: 120, align: 'right' },
    { title: '已付金额', key: 'paidAmount', width: 120, align: 'right' },
    { title: '未付金额', key: 'remainingAmount', width: 120, align: 'right' },
    { title: '到期日期', key: 'dueDate', width: 120, align: 'center' },
    { title: '状态', key: 'status', width: 100, align: 'center' },
    { title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 160 },
    { title: '操作', key: 'action', width: 200, align: 'center', fixed: 'right' }
  ]

  // 付款记录表格列
  const paymentColumns = [
    {
      title: '付款金额',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount: number) => `¥${amount.toFixed(2)}`
    },
    { title: '付款方式', dataIndex: 'paymentMethod', key: 'paymentMethod' },
    { title: '付款日期', dataIndex: 'paymentDate', key: 'paymentDate' },
    { title: '付款人', dataIndex: 'payer', key: 'payer' },
    { title: '备注', dataIndex: 'remark', key: 'remark' }
  ]

  // 付款表单数据
  const paymentFormData = reactive({
    payableId: undefined as number | undefined,
    amount: undefined as number | undefined,
    paymentMethod: undefined as string | undefined,
    paymentDate: undefined,
    payer: '',
    remark: ''
  })

  // 付款表单验证规则
  const paymentFormRules = {
    payableId: [{ required: true, message: '请选择应付单据', trigger: 'change' }],
    amount: [{ required: true, message: '请输入付款金额', trigger: 'blur' }],
    paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }],
    paymentDate: [{ required: true, message: '请选择付款日期', trigger: 'change' }],
    payer: [{ required: true, message: '请输入付款人', trigger: 'blur' }]
  }

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = { pending: 'orange', partial: 'blue', completed: 'green', overdue: 'red' }
    return colors[status as keyof typeof colors] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = { pending: '未付款', partial: '部分付款', completed: '已付款', overdue: '逾期' }
    return texts[status as keyof typeof texts] || '未知'
  }

  // 判断是否逾期
  const isOverdue = (dueDate: string) => dayjs().isAfter(dayjs(dueDate))

  // 加载应付列表
  const loadPayables = async () => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      const mockData: PayableItem[] = [
        {
          id: 1,
          orderNo: 'PO20240115001',
          orderDate: '2024-01-15',
          supplierName: '东联电子供应商',
          supplierPhone: '13900139001',
          amount: 12800.0,
          paidAmount: 5000.0,
          remainingAmount: 7800.0,
          status: 'partial',
          dueDate: '2024-02-15',
          createTime: '2024-01-15 10:30:00',
          payments: [
            {
              id: 1,
              amount: 5000.0,
              paymentMethod: '银行转账',
              paymentDate: '2024-01-20',
              payer: '李四',
              remark: '首付款'
            }
          ]
        },
        {
          id: 2,
          orderNo: 'PO20240110002',
          orderDate: '2024-01-10',
          supplierName: '龙达科技有限公司',
          supplierPhone: '13900139002',
          amount: 18600.0,
          paidAmount: 0,
          remainingAmount: 18600.0,
          status: 'overdue',
          dueDate: '2024-01-25',
          createTime: '2024-01-10 14:20:00'
        }
      ]
      payablesList.value = mockData
      pagination.total = mockData.length
      statistics.value = {
        totalAmount: mockData.reduce((sum, item) => sum + item.amount, 0),
        overdueAmount: mockData
          .filter(item => item.status === 'overdue')
          .reduce((sum, item) => sum + item.remainingAmount, 0),
        monthlyPayment: 28000.0,
        totalCount: mockData.length
      }
    } catch (error) {
      message.error('加载应付列表失败')
    } finally {
      loading.value = false
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadPayables()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, { keyword: '', status: undefined, dateRange: undefined })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadPayables()
  }

  // 查看详情
  const handleViewDetails = (record: PayableItem) => {
    currentPayable.value = record
    detailModalVisible.value = true
  }

  // 付款登记
  const handlePayment = (record: PayableItem) => {
    paymentFormData.payableId = record.id
    paymentFormData.amount = record.remainingAmount
    paymentModalVisible.value = true
  }

  // 新增付款
  const handleAddPayment = () => {
    resetPaymentForm()
    paymentModalVisible.value = true
  }

  // 编辑
  const handleEdit = (record: PayableItem) => message.info('编辑功能开发中')

  // 导出
  const handleExport = () => message.info('导出功能开发中')

  // 保存付款记录
  const handleSavePayment = async () => {
    try {
      await paymentFormRef.value?.validate()
      saveLoading.value = true
      await new Promise(resolve => setTimeout(resolve, 800))
      message.success('付款登记成功')
      paymentModalVisible.value = false
      loadPayables()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      saveLoading.value = false
    }
  }

  // 取消付款
  const handleCancelPayment = () => {
    paymentModalVisible.value = false
    resetPaymentForm()
  }

  // 重置付款表单
  const resetPaymentForm = () => {
    Object.assign(paymentFormData, {
      payableId: undefined,
      amount: undefined,
      paymentMethod: undefined,
      paymentDate: undefined,
      payer: '',
      remark: ''
    })
    paymentFormRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadPayables()
  })
</script>

<style scoped>
  .payables {
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
