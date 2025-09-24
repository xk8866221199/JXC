<template>
  <div class="sales-orders">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">销售订单</h2>
      <p class="text-secondary mt-1 mb-0">管理所有销售订单，包括订单创建、发货、收款等流程</p>
    </div>

    <!-- 统计卡片 -->
    <a-row :gutter="24" class="mb-4">
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="待发货"
            :value="statistics.pending"
            :value-style="{ color: '#faad14' }"
            suffix="单"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="已发货"
            :value="statistics.shipped"
            :value-style="{ color: '#52c41a' }"
            suffix="单"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="已完成"
            :value="statistics.completed"
            :value-style="{ color: '#1890ff' }"
            suffix="单"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="本月销售额"
            :value="statistics.monthAmount"
            :precision="2"
            :value-style="{ color: '#722ed1' }"
            prefix="¥"
          />
        </a-card>
      </a-col>
    </a-row>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="5">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索订单号或客户"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.customerId"
            placeholder="选择客户"
            allowClear
            style="width: 100%"
          >
            <a-select-option v-for="customer in customers" :key="customer.id" :value="customer.id">
              {{ customer.name }}
            </a-select-option>
          </a-select>
        </a-col>
        <a-col :span="3">
          <a-select
            v-model:value="searchForm.status"
            placeholder="订单状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="draft">草稿</a-select-option>
            <a-select-option value="confirmed">已确认</a-select-option>
            <a-select-option value="shipped">已发货</a-select-option>
            <a-select-option value="completed">已完成</a-select-option>
            <a-select-option value="cancelled">已取消</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="5">
          <a-range-picker
            v-model:value="searchForm.dateRange"
            placeholder="选择日期范围"
            style="width: 100%"
          />
        </a-col>
        <a-col :span="7" class="text-right">
          <a-space>
            <a-button @click="handleReset">重置</a-button>
            <a-button type="primary" @click="handleSearch">
              <SearchOutlined />
              搜索
            </a-button>
            <a-button type="primary" @click="handleAdd">
              <PlusOutlined />
              新建订单
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 订单表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="orderList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'orderNo'">
            <a @click="handleView(record)" class="text-primary font-medium">
              {{ record.orderNo }}
            </a>
          </template>

          <template v-else-if="column.key === 'totalAmount'">
            <span class="font-medium text-primary">¥{{ record.totalAmount.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'paymentStatus'">
            <a-tag :color="getPaymentStatusColor(record.paymentStatus)">
              {{ getPaymentStatusText(record.paymentStatus) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleView(record)">
                <EyeOutlined />
                查看
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleEdit(record)"
                v-if="record.status === 'draft'"
              >
                <EditOutlined />
                编辑
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleConfirm(record)"
                v-if="record.status === 'draft'"
              >
                <CheckOutlined />
                确认
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleShip(record)"
                v-if="record.status === 'confirmed'"
              >
                <SendOutlined />
                发货
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handlePayment(record)"
                v-if="record.paymentStatus === 'unpaid'"
              >
                <MoneyCollectOutlined />
                收款
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新建/编辑销售单弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑销售单' : '新建销售单'"
      width="1000px"
      @ok="handleSave"
      @cancel="handleModalCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="销售单号" name="orderNo">
              <a-input v-model:value="formData.orderNo" placeholder="系统自动生成" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="客户" name="customerId">
              <a-select v-model:value="formData.customerId" placeholder="请选择客户">
                <a-select-option
                  v-for="customer in customers"
                  :key="customer.id"
                  :value="customer.id"
                >
                  {{ customer.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="交货日期" name="deliveryDate">
              <a-date-picker
                v-model:value="formData.deliveryDate"
                style="width: 100%"
                placeholder="请选择交货日期"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注说明" name="remark">
          <a-textarea v-model:value="formData.remark" :rows="2" placeholder="请输入备注说明" />
        </a-form-item>

        <!-- 销售商品明细 -->
        <div class="mb-3">
          <div class="d-flex justify-between align-center mb-3">
            <h4 class="m-0">销售商品明细</h4>
            <a-button type="dashed" @click="handleAddGoods">
              <PlusOutlined />
              添加商品
            </a-button>
          </div>

          <a-table
            :columns="goodsColumns"
            :data-source="formData.goods"
            :pagination="false"
            size="small"
            :scroll="{ y: 300 }"
          >
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'goodsName'">
                <a-select
                  v-model:value="record.goodsId"
                  placeholder="请选择商品"
                  style="width: 200px"
                  show-search
                  @change="value => handleGoodsChange(value, index)"
                >
                  <a-select-option v-for="goods in goodsList" :key="goods.id" :value="goods.id">
                    {{ goods.name }} ({{ goods.code }})
                  </a-select-option>
                </a-select>
              </template>

              <template v-else-if="column.key === 'quantity'">
                <a-input-number
                  v-model:value="record.quantity"
                  :min="1"
                  style="width: 100px"
                  @change="() => calculateAmount(index)"
                />
              </template>

              <template v-else-if="column.key === 'price'">
                <a-input-number
                  v-model:value="record.price"
                  :min="0"
                  :precision="2"
                  style="width: 120px"
                  @change="() => calculateAmount(index)"
                />
              </template>

              <template v-else-if="column.key === 'amount'">
                <span class="font-medium">¥{{ record.amount.toFixed(2) }}</span>
              </template>

              <template v-else-if="column.key === 'action'">
                <a-button type="link" size="small" danger @click="handleRemoveGoods(index)">
                  <DeleteOutlined />
                  删除
                </a-button>
              </template>
            </template>

            <template #footer>
              <div class="text-right">
                <span class="font-lg font-semibold">
                  合计金额：¥{{ formData.totalAmount.toFixed(2) }}
                </span>
              </div>
            </template>
          </a-table>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import dayjs from 'dayjs'
  import {
    SearchOutlined,
    PlusOutlined,
    EditOutlined,
    EyeOutlined,
    DeleteOutlined,
    CheckOutlined,
    SendOutlined,
    MoneyCollectOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface SalesOrder {
    id: number
    orderNo: string
    customerId: number
    customerName: string
    totalAmount: number
    status: 'draft' | 'confirmed' | 'shipped' | 'completed' | 'cancelled'
    paymentStatus: 'unpaid' | 'partial' | 'paid'
    deliveryDate: string
    creator: string
    remark: string
    createTime: string
  }

  interface Customer {
    id: number
    name: string
    contact: string
    phone: string
  }

  interface Goods {
    id: number
    name: string
    code: string
    price: number
    stock: number
  }

  interface OrderGoods {
    goodsId: number
    goodsName: string
    goodsCode: string
    quantity: number
    price: number
    amount: number
  }

  interface Statistics {
    pending: number
    shipped: number
    completed: number
    monthAmount: number
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const orderList = ref<SalesOrder[]>([])
  const customers = ref<Customer[]>([])
  const goodsList = ref<Goods[]>([])
  const modalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    customerId: undefined,
    status: undefined,
    dateRange: null
  })

  // 统计数据
  const statistics = ref<Statistics>({
    pending: 0,
    shipped: 0,
    completed: 0,
    monthAmount: 0
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

  // 主表格列配置
  const columns = [
    {
      title: '销售单号',
      key: 'orderNo',
      width: 150
    },
    {
      title: '客户',
      dataIndex: 'customerName',
      key: 'customerName',
      width: 150
    },
    {
      title: '订单金额',
      key: 'totalAmount',
      width: 120,
      align: 'right'
    },
    {
      title: '订单状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '付款状态',
      key: 'paymentStatus',
      width: 100,
      align: 'center'
    },
    {
      title: '交货日期',
      dataIndex: 'deliveryDate',
      key: 'deliveryDate',
      width: 120
    },
    {
      title: '创建人',
      dataIndex: 'creator',
      key: 'creator',
      width: 100
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
      width: 280,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 商品明细表格列
  const goodsColumns = [
    {
      title: '商品名称',
      key: 'goodsName',
      width: 220
    },
    {
      title: '商品编码',
      dataIndex: 'goodsCode',
      key: 'goodsCode',
      width: 120
    },
    {
      title: '销售数量',
      key: 'quantity',
      width: 120,
      align: 'center'
    },
    {
      title: '销售单价',
      key: 'price',
      width: 140,
      align: 'center'
    },
    {
      title: '小计金额',
      key: 'amount',
      width: 120,
      align: 'right'
    },
    {
      title: '操作',
      key: 'action',
      width: 80,
      align: 'center'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined,
    orderNo: '',
    customerId: undefined,
    deliveryDate: null,
    remark: '',
    goods: [] as OrderGoods[],
    totalAmount: 0
  })

  // 表单验证规则
  const formRules = {
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    deliveryDate: [{ required: true, message: '请选择交货日期', trigger: 'change' }]
  }

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      draft: 'default',
      confirmed: 'processing',
      shipped: 'warning',
      completed: 'success',
      cancelled: 'error'
    }
    return colors[status] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = {
      draft: '草稿',
      confirmed: '已确认',
      shipped: '已发货',
      completed: '已完成',
      cancelled: '已取消'
    }
    return texts[status] || status
  }

  // 获取付款状态颜色
  const getPaymentStatusColor = (status: string) => {
    const colors = {
      unpaid: 'error',
      partial: 'warning',
      paid: 'success'
    }
    return colors[status] || 'default'
  }

  // 获取付款状态文本
  const getPaymentStatusText = (status: string) => {
    const texts = {
      unpaid: '未付款',
      partial: '部分付款',
      paid: '已付款'
    }
    return texts[status] || status
  }

  // 生成销售单号
  const generateOrderNo = () => {
    const now = dayjs()
    const timestamp = now.format('YYYYMMDDHHmmss')
    return `SO${timestamp}`
  }

  // 加载销售订单列表
  const loadOrderList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          orderNo: 'SO20240115001',
          customerId: 1,
          customerName: 'ABC电子商城',
          totalAmount: 59990.0,
          status: 'confirmed',
          paymentStatus: 'unpaid',
          deliveryDate: '2024-01-20',
          creator: '张三',
          remark: '紧急订单',
          createTime: '2024-01-15 09:00:00'
        },
        {
          id: 2,
          orderNo: 'SO20240116001',
          customerId: 2,
          customerName: 'XYZ数码城',
          totalAmount: 99980.0,
          status: 'shipped',
          paymentStatus: 'paid',
          deliveryDate: '2024-01-22',
          creator: '李四',
          remark: '月度订单',
          createTime: '2024-01-16 10:30:00'
        }
      ]

      orderList.value = mockData
      pagination.total = mockData.length

      // 计算统计数据
      statistics.value = {
        pending: mockData.filter(item => item.status === 'confirmed').length,
        shipped: mockData.filter(item => item.status === 'shipped').length,
        completed: mockData.filter(item => item.status === 'completed').length,
        monthAmount: mockData.reduce((sum, item) => sum + item.totalAmount, 0)
      }
    } catch (error) {
      message.error('加载销售订单失败')
    } finally {
      loading.value = false
    }
  }

  // 加载客户列表
  const loadCustomers = async () => {
    try {
      const mockCustomers = [
        { id: 1, name: 'ABC电子商城', contact: '王经理', phone: '13800138001' },
        { id: 2, name: 'XYZ数码城', contact: '李经理', phone: '13800138002' },
        { id: 3, name: '123手机专卖', contact: '张经理', phone: '13800138003' }
      ]
      customers.value = mockCustomers
    } catch (error) {
      message.error('加载客户列表失败')
    }
  }

  // 加载商品列表
  const loadGoodsList = async () => {
    try {
      const mockGoods = [
        { id: 1, name: '苹果iPhone 15', code: 'IP15001', price: 5999.0, stock: 25 },
        { id: 2, name: '华为Mate 60', code: 'HW60001', price: 4999.0, stock: 15 },
        { id: 3, name: '小米14', code: 'MI14001', price: 3999.0, stock: 30 }
      ]
      goodsList.value = mockGoods
    } catch (error) {
      message.error('加载商品列表失败')
    }
  }

  // 搜索、重置、表格变化等方法
  const handleSearch = () => {
    pagination.current = 1
    loadOrderList()
  }

  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      customerId: undefined,
      status: undefined,
      dateRange: null
    })
    handleSearch()
  }

  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadOrderList()
  }

  // 新增、编辑、查看等操作
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
    formData.orderNo = generateOrderNo()
  }

  const handleEdit = (record: SalesOrder) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, {
      ...record,
      deliveryDate: dayjs(record.deliveryDate),
      goods: [],
      totalAmount: record.totalAmount
    })
  }

  const handleView = (record: SalesOrder) => {
    message.info('查看详情功能开发中')
  }

  const handleConfirm = async (record: SalesOrder) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('订单确认成功')
      loadOrderList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleShip = (record: SalesOrder) => {
    message.info('发货功能开发中')
  }

  const handlePayment = (record: SalesOrder) => {
    message.info('收款功能开发中')
  }

  // 商品操作方法
  const handleAddGoods = () => {
    formData.goods.push({
      goodsId: undefined,
      goodsName: '',
      goodsCode: '',
      quantity: 1,
      price: 0,
      amount: 0
    })
  }

  const handleRemoveGoods = (index: number) => {
    formData.goods.splice(index, 1)
    calculateTotalAmount()
  }

  const handleGoodsChange = (goodsId: number, index: number) => {
    const goods = goodsList.value.find(item => item.id === goodsId)
    if (goods) {
      formData.goods[index].goodsName = goods.name
      formData.goods[index].goodsCode = goods.code
      formData.goods[index].price = goods.price
      calculateAmount(index)
    }
  }

  const calculateAmount = (index: number) => {
    const item = formData.goods[index]
    item.amount = (item.quantity || 0) * (item.price || 0)
    calculateTotalAmount()
  }

  const calculateTotalAmount = () => {
    formData.totalAmount = formData.goods.reduce((sum, item) => sum + item.amount, 0)
  }

  // 保存、取消等方法
  const handleSave = async () => {
    try {
      await formRef.value?.validate()

      if (formData.goods.length === 0) {
        message.error('请至少添加一个商品')
        return
      }

      saveLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success(isEdit.value ? '更新成功' : '创建成功')
      modalVisible.value = false
      loadOrderList()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      saveLoading.value = false
    }
  }

  const handleModalCancel = () => {
    modalVisible.value = false
    resetForm()
  }

  const resetForm = () => {
    Object.assign(formData, {
      id: undefined,
      orderNo: '',
      customerId: undefined,
      deliveryDate: null,
      remark: '',
      goods: [],
      totalAmount: 0
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadCustomers()
    loadGoodsList()
    loadOrderList()
  })
</script>

<style scoped>
  .sales-orders {
    padding: 0;
  }

  .stat-card {
    text-align: center;
  }

  :deep(.ant-table-cell) {
    padding: 8px;
  }

  :deep(.ant-statistic-content-value) {
    font-size: 24px;
    font-weight: 600;
  }
</style>
