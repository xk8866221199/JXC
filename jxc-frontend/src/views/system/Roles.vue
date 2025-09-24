<template>
  <div class="roles">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">角色管理</h2>
      <p class="text-secondary mt-1 mb-0">管理系统角色和权限配置，控制用户访问权限</p>
    </div>

    <!-- 操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <div class="d-flex justify-between align-center">
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索角色名称或描述"
          style="width: 300px"
          @search="handleSearch"
          allowClear
        />
        <a-button type="primary" @click="handleAdd">
          <PlusOutlined />
          新增角色
        </a-button>
      </div>
    </a-card>

    <!-- 角色表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="roleList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'name'">
            <div class="d-flex align-center">
              <a-avatar class="mr-2" :size="32" style="background-color: #1890ff">
                <template #icon><SafetyOutlined /></template>
              </a-avatar>
              <div>
                <div class="font-medium">{{ record.name }}</div>
                <div class="font-sm text-secondary">{{ record.code }}</div>
              </div>
            </div>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 'active' ? 'success' : 'default'">
              {{ record.status === 'active' ? '启用' : '禁用' }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'userCount'">
            <span class="text-primary font-medium">{{ record.userCount }}</span>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handlePermission(record)">
                <KeyOutlined />
                权限配置
              </a-button>
              <a-popconfirm
                title="确定要删除这个角色吗？"
                @confirm="handleDelete(record.id)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button type="link" size="small" danger :disabled="record.code === 'admin'">
                  <DeleteOutlined />
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑角色弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑角色' : '新增角色'"
      width="600px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="角色名称" name="name">
              <a-input v-model:value="formData.name" placeholder="请输入角色名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="角色编码" name="code">
              <a-input
                v-model:value="formData.code"
                placeholder="请输入角色编码"
                :disabled="isEdit && formData.code === 'admin'"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="角色状态" name="status">
              <a-select v-model:value="formData.status" placeholder="请选择角色状态">
                <a-select-option value="active">启用</a-select-option>
                <a-select-option value="inactive">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="排序" name="sort">
              <a-input-number
                v-model:value="formData.sort"
                :min="0"
                style="width: 100%"
                placeholder="数字越小排序越靠前"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="角色描述" name="description">
          <a-textarea v-model:value="formData.description" :rows="3" placeholder="请输入角色描述" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 权限配置弹窗 -->
    <a-modal
      v-model:open="permissionModalVisible"
      title="权限配置"
      width="800px"
      @ok="handlePermissionSave"
      @cancel="handlePermissionCancel"
      :confirmLoading="permissionSaveLoading"
    >
      <div v-if="currentRole">
        <a-alert
          :message="`正在为角色「${currentRole.name}」配置权限`"
          type="info"
          show-icon
          class="mb-4"
        />

        <a-tree
          v-model:checkedKeys="selectedPermissions"
          :tree-data="permissionTree"
          checkable
          :defaultExpandAll="true"
          :fieldNames="{ title: 'name', key: 'id', children: 'children' }"
        >
          <template #title="{ name, description }">
            <span>{{ name }}</span>
            <span v-if="description" class="text-secondary font-sm ml-2">({{ description }})</span>
          </template>
        </a-tree>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import {
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
    KeyOutlined,
    SafetyOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface Role {
    id: number
    name: string
    code: string
    status: 'active' | 'inactive'
    sort: number
    description: string
    userCount: number
    createTime: string
  }

  interface Permission {
    id: number
    name: string
    code: string
    description?: string
    children?: Permission[]
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const permissionSaveLoading = ref(false)
  const searchKeyword = ref('')
  const roleList = ref<Role[]>([])
  const modalVisible = ref(false)
  const permissionModalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()
  const currentRole = ref<Role | null>(null)
  const selectedPermissions = ref<number[]>([])

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
      title: '角色信息',
      key: 'name',
      width: 200
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '用户数量',
      key: 'userCount',
      width: 120,
      align: 'center'
    },
    {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort',
      width: 100,
      align: 'center'
    },
    {
      title: '描述',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true
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
      width: 220,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined,
    name: '',
    code: '',
    status: 'active',
    sort: 0,
    description: ''
  })

  // 表单验证规则
  const formRules = {
    name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
    status: [{ required: true, message: '请选择角色状态', trigger: 'change' }]
  }

  // 权限树数据
  const permissionTree = ref<Permission[]>([
    {
      id: 1,
      name: '系统管理',
      code: 'system',
      description: '系统相关功能',
      children: [
        { id: 11, name: '用户管理', code: 'system:user', description: '用户增删改查' },
        { id: 12, name: '角色管理', code: 'system:role', description: '角色增删改查' }
      ]
    },
    {
      id: 2,
      name: '商品管理',
      code: 'goods',
      description: '商品相关功能',
      children: [
        { id: 21, name: '商品列表', code: 'goods:list', description: '商品查看' },
        { id: 22, name: '商品编辑', code: 'goods:edit', description: '商品增删改' },
        { id: 23, name: '商品分类', code: 'goods:category', description: '分类管理' }
      ]
    },
    {
      id: 3,
      name: '库存管理',
      code: 'inventory',
      description: '库存相关功能',
      children: [
        { id: 31, name: '库存查询', code: 'inventory:query', description: '库存查看' },
        { id: 32, name: '库存调整', code: 'inventory:adjust', description: '库存调整' },
        { id: 33, name: '库存盘点', code: 'inventory:check', description: '盘点功能' }
      ]
    },
    {
      id: 4,
      name: '采购管理',
      code: 'purchase',
      description: '采购相关功能',
      children: [
        { id: 41, name: '采购订单', code: 'purchase:order', description: '采购订单管理' },
        { id: 42, name: '供应商管理', code: 'purchase:supplier', description: '供应商管理' }
      ]
    },
    {
      id: 5,
      name: '销售管理',
      code: 'sales',
      description: '销售相关功能',
      children: [
        { id: 51, name: '销售订单', code: 'sales:order', description: '销售订单管理' },
        { id: 52, name: '客户管理', code: 'sales:customer', description: '客户管理' }
      ]
    },
    {
      id: 6,
      name: '财务管理',
      code: 'finance',
      description: '财务相关功能',
      children: [
        { id: 61, name: '应收管理', code: 'finance:receivable', description: '应收账款管理' },
        { id: 62, name: '应付管理', code: 'finance:payable', description: '应付账款管理' }
      ]
    }
  ])

  // 加载角色列表
  const loadRoleList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          name: '超级管理员',
          code: 'admin',
          status: 'active',
          sort: 1,
          description: '系统超级管理员，拥有所有权限',
          userCount: 1,
          createTime: '2024-01-01 00:00:00'
        },
        {
          id: 2,
          name: '店长',
          code: 'manager',
          status: 'active',
          sort: 2,
          description: '门店管理员，拥有大部分管理权限',
          userCount: 3,
          createTime: '2024-01-05 10:00:00'
        },
        {
          id: 3,
          name: '员工',
          code: 'employee',
          status: 'active',
          sort: 3,
          description: '普通员工，拥有基本操作权限',
          userCount: 8,
          createTime: '2024-01-10 14:30:00'
        },
        {
          id: 4,
          name: '访客',
          code: 'guest',
          status: 'inactive',
          sort: 4,
          description: '访客角色，只能查看部分信息',
          userCount: 0,
          createTime: '2024-01-15 16:00:00'
        }
      ]

      roleList.value = mockData
      pagination.total = mockData.length
    } catch (error) {
      message.error('加载角色列表失败')
    } finally {
      loading.value = false
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadRoleList()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadRoleList()
  }

  // 新增
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
  }

  // 编辑
  const handleEdit = (record: Role) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, record)
  }

  // 权限配置
  const handlePermission = (record: Role) => {
    currentRole.value = record
    permissionModalVisible.value = true

    // 模拟加载角色权限
    if (record.code === 'admin') {
      selectedPermissions.value = [
        1, 11, 12, 2, 21, 22, 23, 3, 31, 32, 33, 4, 41, 42, 5, 51, 52, 6, 61, 62
      ]
    } else if (record.code === 'manager') {
      selectedPermissions.value = [2, 21, 22, 23, 3, 31, 32, 4, 41, 42, 5, 51, 52]
    } else {
      selectedPermissions.value = [2, 21, 3, 31, 5, 51]
    }
  }

  // 删除
  const handleDelete = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('删除成功')
      loadRoleList()
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
      loadRoleList()
    } catch (error) {
      console.log('表单验证失败:', error)
    } finally {
      saveLoading.value = false
    }
  }

  // 权限保存
  const handlePermissionSave = async () => {
    try {
      permissionSaveLoading.value = true

      await new Promise(resolve => setTimeout(resolve, 800))

      message.success('权限配置成功')
      permissionModalVisible.value = false
    } catch (error) {
      message.error('权限配置失败')
    } finally {
      permissionSaveLoading.value = false
    }
  }

  // 取消
  const handleCancel = () => {
    modalVisible.value = false
    resetForm()
  }

  // 权限取消
  const handlePermissionCancel = () => {
    permissionModalVisible.value = false
    selectedPermissions.value = []
    currentRole.value = null
  }

  // 重置表单
  const resetForm = () => {
    Object.assign(formData, {
      id: undefined,
      name: '',
      code: '',
      status: 'active',
      sort: 0,
      description: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadRoleList()
  })
</script>

<style scoped>
  .roles {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }
</style>
