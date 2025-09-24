<template>
  <div class="customers">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">客户管理</h2>
      <p class="text-secondary mt-1 mb-0">管理客户信息，维护客户关系，提升销售业绩</p>
    </div>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索客户名称或联系人"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.status"
            placeholder="客户状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="active">正常</a-select-option>
            <a-select-option value="inactive">停用</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.level"
            placeholder="客户等级"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="VIP">VIP客户</a-select-option>
            <a-select-option value="Gold">金牌客户</a-select-option>
            <a-select-option value="Silver">银牌客户</a-select-option>
            <a-select-option value="Bronze">铜牌客户</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="10" class="text-right">
          <a-space>
            <a-button @click="handleReset">重置</a-button>
            <a-button type="primary" @click="handleSearch">
              <SearchOutlined />
              搜索
            </a-button>
            <a-button type="primary" @click="handleAdd">
              <PlusOutlined />
              新增客户
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 客户表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="customerList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'name'">
            <div class="d-flex align-center">
              <a-avatar class="mr-2" :size="40" :src="record.avatar">
                <template #icon><UserOutlined /></template>
              </a-avatar>
              <div>
                <div class="font-medium">{{ record.name }}</div>
                <div class="font-sm text-secondary">{{ record.code }}</div>
              </div>
            </div>
          </template>

          <template v-else-if="column.key === 'contact'">
            <div>
              <div class="font-medium">{{ record.contactPerson }}</div>
              <div class="font-sm text-secondary">{{ record.contactPhone }}</div>
            </div>
          </template>

          <template v-else-if="column.key === 'level'">
            <a-tag :color="getLevelColor(record.level)">
              {{ getLevelText(record.level) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 'active' ? 'success' : 'default'">
              {{ record.status === 'active' ? '正常' : '停用' }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'totalAmount'">
            <span class="font-medium text-primary">¥{{ record.totalAmount.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleView(record)">
                <EyeOutlined />
                详情
              </a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handleOrders(record)">
                <FileTextOutlined />
                销售记录
              </a-button>
              <a-popconfirm
                title="确定要删除这个客户吗？"
                @confirm="handleDelete(record.id)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button type="link" size="small" danger>
                  <DeleteOutlined />
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑客户弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑客户' : '新增客户'"
      width="800px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="客户名称" name="name">
              <a-input v-model:value="formData.name" placeholder="请输入客户名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户编码" name="code">
              <a-input v-model:value="formData.code" placeholder="请输入客户编码" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="联系人" name="contactPerson">
              <a-input v-model:value="formData.contactPerson" placeholder="请输入联系人" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话" name="contactPhone">
              <a-input v-model:value="formData.contactPhone" placeholder="请输入联系电话" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="电子邮箱" name="email">
              <a-input v-model:value="formData.email" placeholder="请输入电子邮箱" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户等级" name="level">
              <a-select v-model:value="formData.level" placeholder="请选择客户等级">
                <a-select-option value="VIP">VIP客户</a-select-option>
                <a-select-option value="Gold">金牌客户</a-select-option>
                <a-select-option value="Silver">银牌客户</a-select-option>
                <a-select-option value="Bronze">铜牌客户</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="客户状态" name="status">
              <a-select v-model:value="formData.status" placeholder="请选择客户状态">
                <a-select-option value="active">正常</a-select-option>
                <a-select-option value="inactive">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="信用额度" name="creditLimit">
              <a-input-number
                v-model:value="formData.creditLimit"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="0.00"
                addonAfter="元"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="详细地址" name="address">
          <a-textarea v-model:value="formData.address" :rows="2" placeholder="请输入详细地址" />
        </a-form-item>

        <a-form-item label="备注说明" name="remark">
          <a-textarea v-model:value="formData.remark" :rows="3" placeholder="请输入备注说明" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 客户详情弹窗 -->
    <a-modal v-model:open="detailModalVisible" title="客户详情" width="800px" :footer="null">
      <div v-if="currentCustomer">
        <a-descriptions title="基本信息" :column="2" bordered>
          <a-descriptions-item label="客户名称">{{ currentCustomer.name }}</a-descriptions-item>
          <a-descriptions-item label="客户编码">{{ currentCustomer.code }}</a-descriptions-item>
          <a-descriptions-item label="联系人">{{
            currentCustomer.contactPerson
          }}</a-descriptions-item>
          <a-descriptions-item label="联系电话">{{
            currentCustomer.contactPhone
          }}</a-descriptions-item>
          <a-descriptions-item label="电子邮箱">{{ currentCustomer.email }}</a-descriptions-item>
          <a-descriptions-item label="客户等级">
            <a-tag :color="getLevelColor(currentCustomer.level)">
              {{ getLevelText(currentCustomer.level) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="客户状态">
            <a-tag :color="currentCustomer.status === 'active' ? 'success' : 'default'">
              {{ currentCustomer.status === 'active' ? '正常' : '停用' }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="信用额度"
            >¥{{ currentCustomer.creditLimit.toFixed(2) }}</a-descriptions-item
          >
          <a-descriptions-item label="详细地址" :span="2">{{
            currentCustomer.address
          }}</a-descriptions-item>
          <a-descriptions-item label="备注说明" :span="2">{{
            currentCustomer.remark || '无'
          }}</a-descriptions-item>
        </a-descriptions>

        <a-descriptions title="交易统计" :column="3" bordered class="mt-4">
          <a-descriptions-item label="合作时长"
            >{{ currentCustomer.cooperationDays }}天</a-descriptions-item
          >
          <a-descriptions-item label="销售订单数"
            >{{ currentCustomer.orderCount }}单</a-descriptions-item
          >
          <a-descriptions-item label="累计销售额"
            >¥{{ currentCustomer.totalAmount.toFixed(2) }}</a-descriptions-item
          >
        </a-descriptions>
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
    EditOutlined,
    EyeOutlined,
    DeleteOutlined,
    FileTextOutlined,
    UserOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface Customer {
    id: number
    name: string
    code: string
    contactPerson: string
    contactPhone: string
    email: string
    level: 'VIP' | 'Gold' | 'Silver' | 'Bronze'
    status: 'active' | 'inactive'
    creditLimit: number
    address: string
    remark: string
    totalAmount: number
    orderCount: number
    cooperationDays: number
    avatar?: string
    createTime: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const customerList = ref<Customer[]>([])
  const modalVisible = ref(false)
  const detailModalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()
  const currentCustomer = ref<Customer | null>(null)

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    status: undefined,
    level: undefined
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
      key: 'name',
      width: 250
    },
    {
      title: '联系方式',
      key: 'contact',
      width: 180
    },
    {
      title: '电子邮箱',
      dataIndex: 'email',
      key: 'email',
      width: 200
    },
    {
      title: '等级',
      key: 'level',
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
      title: '累计销售额',
      key: 'totalAmount',
      width: 150,
      align: 'right'
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width: 180
    },
    {
      title: '操作',
      key: 'action',
      width: 280,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined,
    name: '',
    code: '',
    contactPerson: '',
    contactPhone: '',
    email: '',
    level: 'Bronze',
    status: 'active',
    creditLimit: 0,
    address: '',
    remark: ''
  })

  // 表单验证规则
  const formRules = {
    name: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入客户编码', trigger: 'blur' }],
    contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
    contactPhone: [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入电子邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    level: [{ required: true, message: '请选择客户等级', trigger: 'change' }],
    status: [{ required: true, message: '请选择客户状态', trigger: 'change' }]
  }

  // 获取等级颜色
  const getLevelColor = (level: string) => {
    const colors = {
      VIP: 'purple',
      Gold: 'gold',
      Silver: 'default',
      Bronze: 'orange'
    }
    return colors[level] || 'default'
  }

  // 获取等级文本
  const getLevelText = (level: string) => {
    const texts = {
      VIP: 'VIP客户',
      Gold: '金牌客户',
      Silver: '银牌客户',
      Bronze: '铜牌客户'
    }
    return texts[level] || level
  }

  // 加载客户列表
  const loadCustomerList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          name: 'ABC电子商城',
          code: 'CUS001',
          contactPerson: '王经理',
          contactPhone: '13800138001',
          email: 'wang@abc-electronics.com',
          level: 'VIP',
          status: 'active',
          creditLimit: 500000.0,
          address: '北京市朝阳区电子商城大厦12楼',
          remark: 'VIP客户，长期合作伙伴',
          totalAmount: 1580000.0,
          orderCount: 45,
          cooperationDays: 365,
          avatar: '/api/placeholder/40/40',
          createTime: '2024-01-10 09:00:00'
        },
        {
          id: 2,
          name: 'XYZ数码城',
          code: 'CUS002',
          contactPerson: '李经理',
          contactPhone: '13800138002',
          email: 'li@xyz-digital.com',
          level: 'Gold',
          status: 'active',
          creditLimit: 300000.0,
          address: '上海市浦东新区数码广场8楼',
          remark: '金牌客户，信誉良好',
          totalAmount: 980000.0,
          orderCount: 28,
          cooperationDays: 280,
          avatar: '/api/placeholder/40/40',
          createTime: '2024-01-12 10:30:00'
        },
        {
          id: 3,
          name: '123手机专卖',
          code: 'CUS003',
          contactPerson: '张经理',
          contactPhone: '13800138003',
          email: 'zhang@123mobile.com',
          level: 'Silver',
          status: 'active',
          creditLimit: 150000.0,
          address: '广州市天河区手机市场3楼',
          remark: '银牌客户，潜力客户',
          totalAmount: 560000.0,
          orderCount: 18,
          cooperationDays: 180,
          avatar: '/api/placeholder/40/40',
          createTime: '2024-01-15 14:20:00'
        }
      ]

      customerList.value = mockData
      pagination.total = mockData.length
    } catch (error) {
      message.error('加载客户列表失败')
    } finally {
      loading.value = false
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadCustomerList()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      status: undefined,
      level: undefined
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadCustomerList()
  }

  // 新增
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
  }

  // 编辑
  const handleEdit = (record: Customer) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, record)
  }

  // 查看详情
  const handleView = (record: Customer) => {
    currentCustomer.value = record
    detailModalVisible.value = true
  }

  // 销售记录
  const handleOrders = (record: Customer) => {
    message.info('销售记录功能开发中')
  }

  // 删除
  const handleDelete = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('删除成功')
      loadCustomerList()
    } catch (error) {
      message.error('删除失败')
    }
  }

  // 保存
  const handleSave = async () => {
    try {
      await formRef.value?.validate()
      saveLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success(isEdit.value ? '更新成功' : '添加成功')
      modalVisible.value = false
      loadCustomerList()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      saveLoading.value = false
    }
  }

  // 取消
  const handleCancel = () => {
    modalVisible.value = false
    resetForm()
  }

  // 重置表单
  const resetForm = () => {
    Object.assign(formData, {
      id: undefined,
      name: '',
      code: '',
      contactPerson: '',
      contactPhone: '',
      email: '',
      level: 'Bronze',
      status: 'active',
      creditLimit: 0,
      address: '',
      remark: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadCustomerList()
  })
</script>

<style scoped>
  .customers {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-descriptions-item-label) {
    font-weight: 600;
  }
</style>
