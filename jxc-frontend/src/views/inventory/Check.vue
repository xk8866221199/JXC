<template>
  <div class="inventory-check">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">库存盘点</h2>
      <p class="text-secondary mt-1 mb-0">定期进行库存盘点，确保账实一致</p>
    </div>

    <!-- 操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <div class="d-flex justify-between align-center">
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索盘点单号或创建人"
          style="width: 300px"
          @search="handleSearch"
          allowClear
        />
        <a-space>
          <a-select
            v-model:value="statusFilter"
            placeholder="盘点状态"
            style="width: 120px"
            allowClear
          >
            <a-select-option value="draft">草稿</a-select-option>
            <a-select-option value="checking">盘点中</a-select-option>
            <a-select-option value="completed">已完成</a-select-option>
          </a-select>
          <a-button type="primary" @click="handleCreate">
            <PlusOutlined />
            新建盘点
          </a-button>
        </a-space>
      </div>
    </a-card>

    <!-- 盘点单列表 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="checkList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'progress'">
            <a-progress
              :percent="record.progress"
              :size="['small', 14]"
              :stroke-color="record.progress === 100 ? '#52c41a' : '#1890ff'"
            />
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button
                type="link"
                size="small"
                @click="handleView(record)"
                v-if="record.status !== 'draft'"
              >
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
                @click="handleStart(record)"
                v-if="record.status === 'draft'"
              >
                <PlayCircleOutlined />
                开始盘点
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleComplete(record)"
                v-if="record.status === 'checking' && record.progress === 100"
              >
                <CheckCircleOutlined />
                完成盘点
              </a-button>
              <a-popconfirm
                title="确定要删除这个盘点单吗？"
                @confirm="handleDelete(record.id)"
                ok-text="确定"
                cancel-text="取消"
                v-if="record.status === 'draft'"
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

    <!-- 新建/编辑盘点单弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑盘点单' : '新建盘点单'"
      width="800px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="盘点单号" name="checkNo">
              <a-input v-model:value="formData.checkNo" placeholder="系统自动生成" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点类型" name="type">
              <a-select v-model:value="formData.type" placeholder="请选择盘点类型">
                <a-select-option value="full">全盘</a-select-option>
                <a-select-option value="category">分类盘点</a-select-option>
                <a-select-option value="spot">抽盘</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="盘点日期" name="checkDate">
              <a-date-picker
                v-model:value="formData.checkDate"
                style="width: 100%"
                placeholder="请选择盘点日期"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点人员" name="checkers">
              <a-select
                v-model:value="formData.checkers"
                mode="multiple"
                placeholder="请选择盘点人员"
                style="width: 100%"
              >
                <a-select-option value="user1">张三</a-select-option>
                <a-select-option value="user2">李四</a-select-option>
                <a-select-option value="user3">王五</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="盘点范围" name="categoryIds" v-if="formData.type === 'category'">
          <a-select
            v-model:value="formData.categoryIds"
            mode="multiple"
            placeholder="请选择盘点分类"
            style="width: 100%"
          >
            <a-select-option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="备注说明" name="remark">
          <a-textarea v-model:value="formData.remark" :rows="3" placeholder="请输入备注说明" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 盘点详情弹窗 -->
    <a-modal v-model:open="detailModalVisible" title="盘点详情" width="1200px" :footer="null">
      <div v-if="currentCheck">
        <!-- 盘点单基本信息 -->
        <a-descriptions :column="3" bordered class="mb-4">
          <a-descriptions-item label="盘点单号">{{ currentCheck.checkNo }}</a-descriptions-item>
          <a-descriptions-item label="盘点类型">{{
            getTypeText(currentCheck.type)
          }}</a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="getStatusColor(currentCheck.status)">
              {{ getStatusText(currentCheck.status) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="盘点日期">{{ currentCheck.checkDate }}</a-descriptions-item>
          <a-descriptions-item label="创建人">{{ currentCheck.creator }}</a-descriptions-item>
          <a-descriptions-item label="盘点进度">
            <a-progress
              :percent="currentCheck.progress"
              :size="['small', 16]"
              :stroke-color="currentCheck.progress === 100 ? '#52c41a' : '#1890ff'"
            />
          </a-descriptions-item>
        </a-descriptions>

        <!-- 盘点商品列表 -->
        <h4 class="mb-3">盘点商品明细</h4>
        <a-table
          :columns="detailColumns"
          :data-source="checkDetails"
          :pagination="false"
          size="small"
          :scroll="{ y: 400 }"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'bookStock'">
              <span class="font-medium">{{ record.bookStock }}</span>
            </template>

            <template v-else-if="column.key === 'actualStock'">
              <a-input-number
                v-model:value="record.actualStock"
                :min="0"
                size="small"
                style="width: 80px"
                :disabled="currentCheck.status === 'completed'"
                @change="updateProgress"
              />
            </template>

            <template v-else-if="column.key === 'difference'">
              <span
                :class="{
                  'text-success': record.difference > 0,
                  'text-error': record.difference < 0,
                  'text-secondary': record.difference === 0
                }"
                class="font-medium"
              >
                {{ record.difference > 0 ? '+' : '' }}{{ record.difference }}
              </span>
            </template>

            <template v-else-if="column.key === 'status'">
              <a-tag :color="record.actualStock !== null ? 'success' : 'default'">
                {{ record.actualStock !== null ? '已盘点' : '未盘点' }}
              </a-tag>
            </template>
          </template>
        </a-table>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import dayjs from 'dayjs'
  import {
    PlusOutlined,
    EditOutlined,
    EyeOutlined,
    DeleteOutlined,
    PlayCircleOutlined,
    CheckCircleOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface CheckItem {
    id: number
    checkNo: string
    type: 'full' | 'category' | 'spot'
    status: 'draft' | 'checking' | 'completed'
    checkDate: string
    creator: string
    checkers: string[]
    progress: number
    totalItems: number
    checkedItems: number
    remark: string
    createTime: string
  }

  interface CheckDetail {
    id: number
    goodsId: number
    goodsName: string
    goodsCode: string
    bookStock: number
    actualStock: number | null
    difference: number
  }

  interface Category {
    id: number
    name: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const searchKeyword = ref('')
  const statusFilter = ref('')
  const checkList = ref<CheckItem[]>([])
  const categories = ref<Category[]>([])
  const modalVisible = ref(false)
  const detailModalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()
  const currentCheck = ref<CheckItem | null>(null)
  const checkDetails = ref<CheckDetail[]>([])

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
      title: '盘点单号',
      dataIndex: 'checkNo',
      key: 'checkNo',
      width: 150
    },
    {
      title: '盘点类型',
      dataIndex: 'type',
      key: 'type',
      width: 100,
      customRender: ({ text }: any) => getTypeText(text)
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '盘点进度',
      key: 'progress',
      width: 150,
      align: 'center'
    },
    {
      title: '盘点日期',
      dataIndex: 'checkDate',
      key: 'checkDate',
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
      width: 180
    },
    {
      title: '操作',
      key: 'action',
      width: 250,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 盘点详情表格列
  const detailColumns = [
    {
      title: '商品名称',
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 200
    },
    {
      title: '商品编码',
      dataIndex: 'goodsCode',
      key: 'goodsCode',
      width: 120
    },
    {
      title: '账面库存',
      key: 'bookStock',
      width: 100,
      align: 'center'
    },
    {
      title: '实际库存',
      key: 'actualStock',
      width: 120,
      align: 'center'
    },
    {
      title: '差异',
      key: 'difference',
      width: 100,
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined,
    checkNo: '',
    type: '',
    checkDate: null,
    checkers: [],
    categoryIds: [],
    remark: ''
  })

  // 表单验证规则
  const formRules = {
    type: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
    checkDate: [{ required: true, message: '请选择盘点日期', trigger: 'change' }],
    checkers: [{ required: true, message: '请选择盘点人员', trigger: 'change' }]
  }

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      draft: 'default',
      checking: 'processing',
      completed: 'success'
    }
    return colors[status] || 'default'
  }

  // 获取状态文本
  const getStatusText = (status: string) => {
    const texts = {
      draft: '草稿',
      checking: '盘点中',
      completed: '已完成'
    }
    return texts[status] || status
  }

  // 获取类型文本
  const getTypeText = (type: string) => {
    const texts = {
      full: '全盘',
      category: '分类盘点',
      spot: '抽盘'
    }
    return texts[type] || type
  }

  // 生成盘点单号
  const generateCheckNo = () => {
    const now = dayjs()
    const timestamp = now.format('YYYYMMDDHHmmss')
    return `PD${timestamp}`
  }

  // 加载盘点列表
  const loadCheckList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          checkNo: 'PD20240115001',
          type: 'full',
          status: 'completed',
          checkDate: '2024-01-15',
          creator: '张三',
          checkers: ['张三', '李四'],
          progress: 100,
          totalItems: 150,
          checkedItems: 150,
          remark: '月度全盘',
          createTime: '2024-01-15 09:00:00'
        },
        {
          id: 2,
          checkNo: 'PD20240120001',
          type: 'category',
          status: 'checking',
          checkDate: '2024-01-20',
          creator: '李四',
          checkers: ['李四', '王五'],
          progress: 65,
          totalItems: 80,
          checkedItems: 52,
          remark: '电子产品盘点',
          createTime: '2024-01-20 10:00:00'
        }
      ]

      checkList.value = mockData
      pagination.total = mockData.length
    } catch (error) {
      message.error('加载盘点列表失败')
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
    loadCheckList()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadCheckList()
  }

  // 新建盘点
  const handleCreate = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
    formData.checkNo = generateCheckNo()
  }

  // 编辑盘点
  const handleEdit = (record: CheckItem) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, {
      ...record,
      checkDate: dayjs(record.checkDate)
    })
  }

  // 查看详情
  const handleView = async (record: CheckItem) => {
    currentCheck.value = record
    detailModalVisible.value = true

    // 加载盘点详情
    const mockDetails = [
      {
        id: 1,
        goodsId: 1,
        goodsName: '苹果iPhone 15',
        goodsCode: 'IP15001',
        bookStock: 25,
        actualStock: 23,
        difference: -2
      },
      {
        id: 2,
        goodsId: 2,
        goodsName: '华为Mate 60',
        goodsCode: 'HW60001',
        bookStock: 15,
        actualStock: null,
        difference: 0
      }
    ]

    checkDetails.value = mockDetails.map(item => ({
      ...item,
      difference: item.actualStock !== null ? item.actualStock - item.bookStock : 0
    }))
  }

  // 开始盘点
  const handleStart = async (record: CheckItem) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('盘点已开始')
      loadCheckList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  // 完成盘点
  const handleComplete = async (record: CheckItem) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('盘点已完成')
      loadCheckList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  // 删除盘点
  const handleDelete = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('删除成功')
      loadCheckList()
    } catch (error) {
      message.error('删除失败')
    }
  }

  // 保存盘点单
  const handleSave = async () => {
    try {
      await formRef.value?.validate()
      saveLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success(isEdit.value ? '更新成功' : '创建成功')
      modalVisible.value = false
      loadCheckList()
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
      checkNo: '',
      type: '',
      checkDate: null,
      checkers: [],
      categoryIds: [],
      remark: ''
    })
    formRef.value?.resetFields()
  }

  // 更新进度
  const updateProgress = () => {
    if (currentCheck.value) {
      const checkedCount = checkDetails.value.filter(item => item.actualStock !== null).length
      currentCheck.value.progress = Math.round((checkedCount / checkDetails.value.length) * 100)

      // 更新差异
      checkDetails.value.forEach(item => {
        if (item.actualStock !== null) {
          item.difference = item.actualStock - item.bookStock
        }
      })
    }
  }

  // 组件挂载
  onMounted(() => {
    loadCategories()
    loadCheckList()
  })
</script>

<style scoped>
  .inventory-check {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-progress-line) {
    margin: 0;
  }
</style>
