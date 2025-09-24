<template>
  <div class="users">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">用户管理</h2>
      <p class="text-secondary mt-1 mb-0">管理系统用户账号，包括权限分配和状态控制</p>
    </div>

    <!-- 搜索和操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <a-row :gutter="16" class="mb-3">
        <a-col :span="6">
          <a-input-search
            v-model:value="searchForm.keyword"
            placeholder="搜索用户名或真实姓名"
            enter-button="搜索"
            @search="handleSearch"
            allowClear
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.roleId"
            placeholder="选择角色"
            allowClear
            style="width: 100%"
          >
            <a-select-option v-for="role in roles" :key="role.id" :value="role.id">
              {{ role.name }}
            </a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="searchForm.status"
            placeholder="用户状态"
            allowClear
            style="width: 100%"
          >
            <a-select-option value="active">正常</a-select-option>
            <a-select-option value="disabled">禁用</a-select-option>
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
              新增用户
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>

    <!-- 用户表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'user'">
            <div class="d-flex align-center">
              <a-avatar class="mr-2" :size="40" :src="record.avatar">
                <template #icon><UserOutlined /></template>
              </a-avatar>
              <div>
                <div class="font-medium">{{ record.realName }}</div>
                <div class="font-sm text-secondary">{{ record.username }}</div>
              </div>
            </div>
          </template>

          <template v-else-if="column.key === 'roles'">
            <a-space>
              <a-tag v-for="role in record.roles" :key="role.id" :color="getRoleColor(role.code)">
                {{ role.name }}
              </a-tag>
            </a-space>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 'active' ? 'success' : 'error'">
              {{ record.status === 'active' ? '正常' : '禁用' }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handleResetPassword(record)">
                <KeyOutlined />
                重置密码
              </a-button>
              <a-button
                type="link"
                size="small"
                @click="handleToggleStatus(record)"
                :class="record.status === 'active' ? 'text-warning' : 'text-success'"
              >
                <StopOutlined v-if="record.status === 'active'" />
                <PlayCircleOutlined v-else />
                {{ record.status === 'active' ? '禁用' : '启用' }}
              </a-button>
              <a-popconfirm
                title="确定要删除这个用户吗？"
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

    <!-- 新增/编辑用户弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="600px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="用户名" name="username">
              <a-input
                v-model:value="formData.username"
                placeholder="请输入用户名"
                :disabled="isEdit"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="真实姓名" name="realName">
              <a-input v-model:value="formData.realName" placeholder="请输入真实姓名" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16" v-if="!isEdit">
          <a-col :span="12">
            <a-form-item label="登录密码" name="password">
              <a-input-password v-model:value="formData.password" placeholder="请输入登录密码" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="确认密码" name="confirmPassword">
              <a-input-password v-model:value="formData.confirmPassword" placeholder="请确认密码" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="手机号码" name="phone">
              <a-input v-model:value="formData.phone" placeholder="请输入手机号码" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电子邮箱" name="email">
              <a-input v-model:value="formData.email" placeholder="请输入电子邮箱" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="用户角色" name="roleIds">
              <a-select
                v-model:value="formData.roleIds"
                mode="multiple"
                placeholder="请选择用户角色"
                style="width: 100%"
              >
                <a-select-option v-for="role in roles" :key="role.id" :value="role.id">
                  {{ role.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="用户状态" name="status">
              <a-select v-model:value="formData.status" placeholder="请选择用户状态">
                <a-select-option value="active">正常</a-select-option>
                <a-select-option value="disabled">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注说明" name="remark">
          <a-textarea v-model:value="formData.remark" :rows="3" placeholder="请输入备注说明" />
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
    DeleteOutlined,
    KeyOutlined,
    StopOutlined,
    PlayCircleOutlined,
    UserOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface User {
    id: number
    username: string
    realName: string
    phone: string
    email: string
    status: 'active' | 'disabled'
    roles: Role[]
    remark: string
    avatar?: string
    lastLoginTime: string
    createTime: string
  }

  interface Role {
    id: number
    name: string
    code: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const userList = ref<User[]>([])
  const roles = ref<Role[]>([])
  const modalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()

  // 搜索表单
  const searchForm = reactive({
    keyword: '',
    roleId: undefined,
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
      title: '用户信息',
      key: 'user',
      width: 200
    },
    {
      title: '手机号码',
      dataIndex: 'phone',
      key: 'phone',
      width: 120
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
      width: 200
    },
    {
      title: '角色',
      key: 'roles',
      width: 200
    },
    {
      title: '状态',
      key: 'status',
      width: 100,
      align: 'center'
    },
    {
      title: '最后登录',
      dataIndex: 'lastLoginTime',
      key: 'lastLoginTime',
      width: 160
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

  // 表单数据
  const formData = reactive({
    id: undefined,
    username: '',
    realName: '',
    password: '',
    confirmPassword: '',
    phone: '',
    email: '',
    roleIds: [],
    status: 'active',
    remark: ''
  })

  // 表单验证规则
  const formRules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
    password: isEdit.value ? [] : [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
    confirmPassword: isEdit.value
      ? []
      : [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule: any, value: string) => {
              if (value !== formData.password) {
                return Promise.reject('两次输入密码不一致')
              }
              return Promise.resolve()
            },
            trigger: 'blur'
          }
        ],
    phone: [
      { required: true, message: '请输入手机号码', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入电子邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    roleIds: [{ required: true, message: '请选择用户角色', trigger: 'change' }],
    status: [{ required: true, message: '请选择用户状态', trigger: 'change' }]
  }

  // 获取角色颜色
  const getRoleColor = (code: string) => {
    const colors = {
      admin: 'red',
      manager: 'orange',
      employee: 'blue',
      guest: 'default'
    }
    return colors[code] || 'default'
  }

  // 加载用户列表
  const loadUserList = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData = [
        {
          id: 1,
          username: 'admin',
          realName: '系统管理员',
          phone: '13800138001',
          email: 'admin@jxc.com',
          status: 'active',
          roles: [{ id: 1, name: '超级管理员', code: 'admin' }],
          remark: '系统超级管理员',
          avatar: '/api/placeholder/40/40',
          lastLoginTime: '2024-01-24 09:30:00',
          createTime: '2024-01-01 00:00:00'
        },
        {
          id: 2,
          username: 'manager',
          realName: '店长',
          phone: '13800138002',
          email: 'manager@jxc.com',
          status: 'active',
          roles: [{ id: 2, name: '店长', code: 'manager' }],
          remark: '门店管理员',
          avatar: '/api/placeholder/40/40',
          lastLoginTime: '2024-01-24 08:45:00',
          createTime: '2024-01-05 10:00:00'
        }
      ]

      userList.value = mockData
      pagination.total = mockData.length
    } catch (error) {
      message.error('加载用户列表失败')
    } finally {
      loading.value = false
    }
  }

  // 加载角色列表
  const loadRoles = async () => {
    try {
      const mockRoles = [
        { id: 1, name: '超级管理员', code: 'admin' },
        { id: 2, name: '店长', code: 'manager' },
        { id: 3, name: '员工', code: 'employee' },
        { id: 4, name: '访客', code: 'guest' }
      ]
      roles.value = mockRoles
    } catch (error) {
      message.error('加载角色列表失败')
    }
  }

  // 搜索
  const handleSearch = () => {
    pagination.current = 1
    loadUserList()
  }

  // 重置搜索
  const handleReset = () => {
    Object.assign(searchForm, {
      keyword: '',
      roleId: undefined,
      status: undefined
    })
    handleSearch()
  }

  // 表格变化处理
  const handleTableChange = (pag: any) => {
    pagination.current = pag.current
    pagination.pageSize = pag.pageSize
    loadUserList()
  }

  // 新增
  const handleAdd = () => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
  }

  // 编辑
  const handleEdit = (record: User) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, {
      ...record,
      roleIds: record.roles.map(role => role.id)
    })
  }

  // 重置密码
  const handleResetPassword = async (record: User) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success(`已重置用户 ${record.realName} 的密码`)
    } catch (error) {
      message.error('重置密码失败')
    }
  }

  // 切换状态
  const handleToggleStatus = async (record: User) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      const action = record.status === 'active' ? '禁用' : '启用'
      message.success(`已${action}用户 ${record.realName}`)
      loadUserList()
    } catch (error) {
      message.error('操作失败')
    }
  }

  // 删除
  const handleDelete = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('删除成功')
      loadUserList()
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
      loadUserList()
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
      username: '',
      realName: '',
      password: '',
      confirmPassword: '',
      phone: '',
      email: '',
      roleIds: [],
      status: 'active',
      remark: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadRoles()
    loadUserList()
  })
</script>

<style scoped>
  .users {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }
</style>
