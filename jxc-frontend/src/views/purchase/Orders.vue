<template>
  <div class="purchase-orders">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">采购订单</h2>
      <p class="text-secondary mt-1 mb-0">管理所有采购订单，包括订单创建、审核、入库等流程</p>
    </div>

    <!-- 统计卡片 -->
    <a-row :gutter="24" class="mb-4">
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="待审核"
            :value="statistics.pending"
            :value-style="{ color: '#faad14' }"
            suffix="单"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card class="stat-card">
          <a-statistic
            title="已审核"
            :value="statistics.approved"
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
            title="本月采购额"
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
            placeholder="搜索订单号或供应商"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.supplierId"
            placeholder="选择供应商"
            allowClear
            style="width: 100%"
          >
            <a-select-option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
              {{ supplier.name }}
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
            <a-select-option value="pending">待审核</a-select-option>
            <a-select-option value="approved">已审核</a-select-option>
            <a-select-option value="receiving">收货中</a-select-option>
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
              新建采购单
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

          <template v-else-if="column.key === 'progress'">
            <a-progress
              :percent="record.receiveProgress"
              :size="['small', 14]"
              :stroke-color="record.receiveProgress === 100 ? '#52c41a' : '#1890ff'"
            />
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
                @click="handleApprove(record)"
                v-if="record.status === 'pending'"
              >
                <CheckOutlined />
                审核
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleReceive(record)"
                v-if="record.status === 'approved'"
              >
                <InboxOutlined />
                收货
              </a-button>
              <a-popconfirm
                title="确定要取消这个采购单吗？"
                @confirm="handleCancel(record.id)"
                ok-text="确定"
                cancel-text="取消"
                v-if="['draft', 'pending'].includes(record.status)"
              >
                <a-button type="link" size="small" danger>
                  <CloseOutlined />
                  取消
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新建/编辑采购单弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑采购单' : '新建采购单'"
      width="1000px"
      @ok="handleSave"
      @cancel="handleModalCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="采购单号" name="orderNo">
              <a-input v-model:value="formData.orderNo" placeholder="系统自动生成" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="供应商" name="supplierId">
              <a-select v-model:value="formData.supplierId" placeholder="请选择供应商">
                <a-select-option
                  v-for="supplier in suppliers"
                  :key="supplier.id"
                  :value="supplier.id"
                >
                  {{ supplier.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="预计到货日期" name="expectedDate">
              <a-date-picker
                v-model:value="formData.expectedDate"
                style="width: 100%"
                placeholder="请选择预计到货日期"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注说明" name="remark">
          <a-textarea v-model:value="formData.remark" :rows="2" placeholder="请输入备注说明" />
        </a-form-item>

        <!-- 采购商品明细 -->
        <div class="mb-3">
          <div class="d-flex justify-between align-center mb-3">
            <h4 class="m-0">采购商品明细</h4>
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
                  :filter-option="filterGoods"
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
  import { ref, reactive, computed, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import dayjs from 'dayjs'
  import {
    SearchOutlined,
    PlusOutlined,
    EditOutlined,
    EyeOutlined,
    DeleteOutlined,
    CheckOutlined,
    InboxOutlined,
    CloseOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface PurchaseOrder {
    id: number
    orderNo: string
    supplierId: number
    supplierName: string
    totalAmount: number
    status: 'draft' | 'pending' | 'approved' | 'receiving' | 'completed' | 'cancelled'
    receiveProgress: number
    expectedDate: string
    creator: string
    remark: string
    createTime: string
  }

  interface Supplier {
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
    approved: number
    completed: number
    monthAmount: number
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const orderList = ref<PurchaseOrder[]>([])
  const suppliers = ref<Supplier[]>([])
  const goodsList = ref<Goods[]>([])
  const modalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    supplierId: undefined,
    status: undefined,
    dateRange: null
  })

  // 统计数据
  const statistics = ref<Statistics>({
    pending: 0,
    approved: 0,
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
      title: '采购单号',
      key: 'orderNo',
      width: 150
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
      width: 150
    },
    {
      title: '订单金额',
      key: 'totalAmount',
      width: 120,
      align: 'right'
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '收货进度',
      key: 'progress',
      width: 120,
      align: 'center'
    },
    {
      title: '预计到货',
      dataIndex: 'expectedDate',
      key: 'expectedDate',
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
      width: 250,
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
      title: '采购数量',
      key: 'quantity',
      width: 120,
      align: 'center'
    },
    {
      title: '采购单价',
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
    supplierId: undefined,
    expectedDate: null,
    remark: '',
    goods: [] as OrderGoods[],
    totalAmount: 0
  })

  // 表单验证规则
  const formRules = {
    supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
    expectedDate: [{ required: true, message: '请选择预计到货日期', trigger: 'change' }]
  }

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      draft: 'default',
      pending: 'processing',
      approved: 'success',
      receiving: 'warning',
      completed: 'success',
      cancelled: 'error'
    }
    return colors[status] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = {
      draft: '草稿',
      pending: '待审核',
      approved: '已审核',
      receiving: '收货中',
      completed: '已完成',
      cancelled: '已取消'
    }
    return texts[status] || status
  }

  // 生成采购单号
  const generateOrderNo = () => {
    const now = dayjs()
    const timestamp = now.format('YYYYMMDDHHmmss')
    return `PO${timestamp}`
  }

  // 商品筛选
  const filterGoods = (input: string, option: any) => {
    return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
  }

  // 加载采购订单列表
  const loadOrderList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          orderNo: 'PO20240115001',
          supplierId: 1,
          supplierName: '苹果供应商',
          totalAmount: 149975.0,
          status: 'approved',
          receiveProgress: 0,
          expectedDate: '2024-01-20',
          creator: '张三',
          remark: '紧急采购',
          createTime: '2024-01-15 09:00:00'
        },
        {
          id: 2,
          orderNo: 'PO20240116001',
          supplierId: 2,
          supplierName: '华为供应商',
          totalAmount: 99980.0,
          status: 'receiving',
          receiveProgress: 50,
          expectedDate: '2024-01-22',
          creator: '李四',
          remark: '月度补货',
          createTime: '2024-01-16 10:30:00'
        }
      ]

      orderList.value = mockData
      pagination.total = mockData.length

      // 计算统计数据
      statistics.value = {
        pending: mockData.filter(item => item.status === 'pending').length,
        approved: mockData.filter(item => item.status === 'approved').length,
        completed: mockData.filter(item => item.status === 'completed').length,
        monthAmount: mockData.reduce((sum, item) => sum + item.totalAmount, 0)
      }
    } catch (error) {
      message.error('加载采购订单失败')
    } finally {
      loading.value = false
    }
  }

  // 加载供应商列表
  const loadSuppliers = async () => {
    try {
      const mockSuppliers = [
        { id: 1, name: '苹果供应商', contact: '张经理', phone: '13800138001' },
        { id: 2, name: '华为供应商', contact: '李经理', phone: '13800138002' },
        { id: 3, name: '小米供应商', contact: '王经理', phone: '13800138003' }
      ]
      suppliers.value = mockSuppliers
    } catch (error) {
      message.error('加载供应商列表失败')
    }
  }

  // 加载商品列表
  const loadGoodsList = async () => {
    try {
      const mockGoods = [
        { id: 1, name: '苹果iPhone 15', code: 'IP15001', price: 5999.0 },
        { id: 2, name: '华为Mate 60', code: 'HW60001', price: 4999.0 },
        { id: 3, name: '小米14', code: 'MI14001', price: 3999.0 }
      ]
      goodsList.value = mockGoods
    } catch (error) {
      message.error('加载商品列表失败')
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadOrderList()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      supplierId: undefined,
      status: undefined,
      dateRange: null
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadOrderList()
  }

  // 新增
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
    formData.orderNo = generateOrderNo()
  }

  // 编辑
  const handleEdit = (record: PurchaseOrder) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, {
      ...record,
      expectedDate: dayjs(record.expectedDate),
      goods: [],
      totalAmount: record.totalAmount
    })
  }

  // 查看详情
  const handleView = (record: PurchaseOrder) => {
    message.info('查看详情功能开发中')
  }

  // 审核
  const handleApprove = async (record: PurchaseOrder) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('审核通过')
      loadOrderList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  // 收货
  const handleReceive = (record: PurchaseOrder) => {
    message.info('收货功能开发中')
  }

  // 取消订单
  const handleCancel = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('订单已取消')
      loadOrderList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  // 添加商品
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

  // 移除商品
  const handleRemoveGoods = (index: number) => {
    formData.goods.splice(index, 1)
    calculateTotalAmount()
  }

  // 商品变化处理
  const handleGoodsChange = (goodsId: number, index: number) => {
    const goods = goodsList.value.find(item => item.id === goodsId)
    if (goods) {
      formData.goods[index].goodsName = goods.name
      formData.goods[index].goodsCode = goods.code
      formData.goods[index].price = goods.price
      calculateAmount(index)
    }
  }

  // 计算单项金额
  const calculateAmount = (index: number) => {
    const item = formData.goods[index]
    item.amount = (item.quantity || 0) * (item.price || 0)
    calculateTotalAmount()
  }

  // 计算总金额
  const calculateTotalAmount = () => {
    formData.totalAmount = formData.goods.reduce((sum, item) => sum + item.amount, 0)
  }

  // 保存
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

  // 取消
  const handleModalCancel = () => {
    modalVisible.value = false
    resetForm()
  }

  // 重置表单
  const resetForm = () => {
    Object.assign(formData, {
      id: undefined,
      orderNo: '',
      supplierId: undefined,
      expectedDate: null,
      remark: '',
      goods: [],
      totalAmount: 0
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadSuppliers()
    loadGoodsList()
    loadOrderList()
  })
</script>

<style scoped>
  .purchase-orders {
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
