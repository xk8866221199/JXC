<template>
  <div class="goods-list">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">商品列表</h2>
      <p class="text-secondary mt-1 mb-0">管理所有商品信息，包括添加、编辑、删除操作</p>
    </div>

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
            v-model:value="searchForm.status"
            placeholder="商品状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="active">正常</a-select-option>
            <a-select-option value="inactive">停用</a-select-option>
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
              新增商品
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 商品表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="goodsList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'image'">
            <a-image
              :width="60"
              :height="60"
              :src="record.image"
              :preview="true"
              fallback="/placeholder.png"
              class="rounded"
            />
          </template>

          <template v-else-if="column.key === 'price'">
            <span class="font-medium text-primary">¥{{ record.price.toFixed(2) }}</span>
          </template>

          <template v-else-if="column.key === 'stock'">
            <a-tag :color="getStockColor(record.stock, record.minStock)">
              {{ record.stock }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 'active' ? 'success' : 'default'">
              {{ record.status === 'active' ? '正常' : '停用' }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handleView(record)">
                <EyeOutlined />
                详情
              </a-button>
              <a-popconfirm
                title="确定要删除这个商品吗？"
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

    <!-- 商品编辑/新增弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑商品' : '新增商品'"
      width="800px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="商品名称" name="name">
              <a-input v-model:value="formData.name" placeholder="请输入商品名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="商品编码" name="code">
              <a-input v-model:value="formData.code" placeholder="请输入商品编码" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="商品分类" name="categoryId">
              <a-select v-model:value="formData.categoryId" placeholder="请选择商品分类">
                <a-select-option
                  v-for="category in categories"
                  :key="category.id"
                  :value="category.id"
                >
                  {{ category.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="商品状态" name="status">
              <a-select v-model:value="formData.status" placeholder="请选择商品状态">
                <a-select-option value="active">正常</a-select-option>
                <a-select-option value="inactive">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="售价" name="price">
              <a-input-number
                v-model:value="formData.price"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="0.00"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="成本价" name="costPrice">
              <a-input-number
                v-model:value="formData.costPrice"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="0.00"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="最低库存" name="minStock">
              <a-input-number
                v-model:value="formData.minStock"
                :min="0"
                style="width: 100%"
                placeholder="0"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="商品描述" name="description">
          <a-textarea v-model:value="formData.description" :rows="3" placeholder="请输入商品描述" />
        </a-form-item>
      </a-form>
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
    DeleteOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'
  import {
    getGoodsListApi,
    createGoodsApi,
    updateGoodsApi,
    deleteGoodsApi,
    updateGoodsStatusApi,
    getGoodsByIdApi
  } from '@/api/goodsInfo'
  import { getAllGoodsCategoriesApi } from '@/api/goodsCategory'

  // 接口类型定义
  interface GoodsItem {
    id: number
    name: string
    code: string
    categoryId: number
    categoryName: string
    price: number
    costPrice: number
    stock: number
    minStock: number
    status: 'active' | 'inactive'
    description: string
    image: string
    createTime: string
  }

  interface Category {
    id: number
    name: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const goodsList = ref<GoodsItem[]>([])
  const categories = ref<Category[]>([])
  const modalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    categoryId: undefined,
    status: undefined
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
      width: 100,
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
      title: '售价',
      key: 'price',
      width: 100,
      align: 'right'
    },
    {
      title: '库存',
      key: 'stock',
      width: 100,
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      width: 80,
      align: 'center'
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
      width: 200,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined,
    name: '',
    code: '',
    categoryId: undefined,
    price: undefined,
    costPrice: undefined,
    minStock: undefined,
    status: 'active',
    description: ''
  })

  // 表单验证规则
  const formRules = {
    name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
    categoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
    price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
    status: [{ required: true, message: '请选择商品状态', trigger: 'change' }]
  }

  // 获取库存状态颜色
  const getStockColor = (stock: number, minStock: number) => {
    if (stock <= minStock) return 'error'
    if (stock <= minStock * 2) return 'warning'
    return 'success'
  }

  // 加载商品列表
  const loadGoodsList = async () => {
    loading.value = true
    try {
      const params = {
        page: pagination.current,
        size: pagination.pageSize,
        keyword: searchForm.keyword || undefined,
        categoryId: searchForm.categoryId,
        status: searchForm.status === 'active' ? 1 : (searchForm.status === 'inactive' ? 0 : undefined)
      }

      const response = await getGoodsListApi(params)
      
      const goods = response.data?.records || response.records || []
      goodsList.value = goods.map((item: any) => ({
        ...item,
        status: item.status === 1 ? 'active' : 'inactive',
        categoryName: item.categoryName || '',
        price: item.salePrice || 0,
        costPrice: item.purchasePrice || 0,
        stock: item.stockQuantity || 0,
        minStock: item.minStock || 0,
        createTime: item.createdAt || item.createTime
      }))
      
      pagination.total = response.data?.total || response.total || 0
    } catch (error: any) {
      message.error('加载商品列表失败: ' + (error.message || '未知错误'))
    } finally {
      loading.value = false
    }
  }

  // 加载分类列表
  const loadCategories = async () => {
    try {
      const response = await getAllGoodsCategoriesApi()
      const categories = response.data?.records || response.records || []
      categories.value = categories.map((item: any) => ({
        id: item.id,
        name: item.categoryName || item.name
      }))
    } catch (error: any) {
      message.error('加载分类列表失败: ' + (error.message || '未知错误'))
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadGoodsList()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      categoryId: undefined,
      status: undefined
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadGoodsList()
  }

  // 新增
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
  }

  // 编辑
  const handleEdit = (record: GoodsItem) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, record)
  }

  // 查看详情
  const handleView = (record: GoodsItem) => {
    message.info('查看详情功能开发中')
  }

  // 删除
  const handleDelete = async (id: number) => {
    try {
      await deleteGoodsApi(id)
      message.success('删除成功')
      loadGoodsList()
    } catch (error: any) {
      message.error('删除失败: ' + (error.message || '未知错误'))
    }
  }

  // 保存
  const handleSave = async () => {
    try {
      await formRef.value?.validate()
      saveLoading.value = true

      // 转换数据格式
      const submitData = {
        ...formData,
        status: formData.status === 'active' ? 1 : 0,
        salePrice: formData.price,
        purchasePrice: formData.costPrice,
        stockQuantity: formData.minStock
      }
      // 移除前端使用的字段
      delete submitData.price
      delete submitData.costPrice
      delete submitData.minStock

      if (isEdit.value) {
        await updateGoodsApi(formData.id!, submitData)
        message.success('更新成功')
      } else {
        await createGoodsApi(submitData)
        message.success('添加成功')
      }

      modalVisible.value = false
      loadGoodsList()
    } catch (error: any) {
      if (error.message?.includes('商品编码已存在')) {
        message.error('商品编码已存在，请更换其他编码')
      } else {
        message.error('保存失败: ' + (error.message || '未知错误'))
      }
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
      categoryId: undefined,
      price: undefined,
      costPrice: undefined,
      minStock: undefined,
      status: 'active',
      description: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadCategories()
    loadGoodsList()
  })
</script>

<style scoped>
  .goods-list {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-image) {
    border-radius: 4px;
  }
</style>
