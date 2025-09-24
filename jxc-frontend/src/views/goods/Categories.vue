<template>
  <div class="categories">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="font-xl font-semibold m-0">商品分类</h2>
      <p class="text-secondary mt-1 mb-0">管理商品分类信息，支持多级分类结构</p>
    </div>

    <!-- 操作区域 -->
    <a-card class="mb-4" :bordered="false">
      <div class="d-flex justify-between align-center">
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索分类名称"
          style="width: 300px"
          @search="handleSearch"
          allowClear
        />
        <a-space>
          <a-button @click="expandAll">
            <NodeExpandOutlined />
            展开全部
          </a-button>
          <a-button @click="collapseAll">
            <NodeCollapseOutlined />
            收起全部
          </a-button>
          <a-button type="primary" @click="handleAdd()">
            <PlusOutlined />
            新增分类
          </a-button>
        </a-space>
      </div>
    </a-card>

    <!-- 分类树表格 -->
    <a-card :bordered="false">
      <a-table
        :columns="columns"
        :data-source="categoryTree"
        :loading="loading"
        :pagination="false"
        row-key="id"
        :default-expand-all-rows="false"
        :expanded-row-keys="expandedKeys"
        @expand="handleExpand"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'name'">
            <div class="d-flex align-center">
              <FolderOutlined class="text-warning mr-2" />
              <span class="font-medium">{{ record.name }}</span>
            </div>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 'active' ? 'success' : 'default'">
              {{ record.status === 'active' ? '启用' : '禁用' }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'goodsCount'">
            <span class="text-primary font-medium">{{ record.goodsCount }}</span>
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleAdd(record)">
                <PlusOutlined />
                添加子分类
              </a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined />
                编辑
              </a-button>
              <a-popconfirm
                title="确定要删除这个分类吗？删除后该分类下的商品将移至其他分类。"
                @confirm="handleDelete(record.id)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button
                  type="link"
                  size="small"
                  danger
                  :disabled="record.children && record.children.length > 0"
                >
                  <DeleteOutlined />
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 分类编辑/新增弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑分类' : '新增分类'"
      width="600px"
      @ok="handleSave"
      @cancel="handleCancel"
      :confirmLoading="saveLoading"
    >
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical" class="mt-4">
        <a-form-item label="上级分类" name="parentId">
          <a-tree-select
            v-model:value="formData.parentId"
            :tree-data="categorySelectTree"
            placeholder="请选择上级分类，不选择则为顶级分类"
            tree-default-expand-all
            allowClear
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
          />
        </a-form-item>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="分类名称" name="name">
              <a-input v-model:value="formData.name" placeholder="请输入分类名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="分类编码" name="code">
              <a-input v-model:value="formData.code" placeholder="请输入分类编码" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
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
          <a-col :span="12">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="formData.status" placeholder="请选择状态">
                <a-select-option value="active">启用</a-select-option>
                <a-select-option value="inactive">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="分类描述" name="description">
          <a-textarea v-model:value="formData.description" :rows="3" placeholder="请输入分类描述" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { message } from 'ant-design-vue'
  import {
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
    FolderOutlined,
    NodeExpandOutlined,
    NodeCollapseOutlined
  } from '@ant-design/icons-vue'
  import type { FormInstance } from 'ant-design-vue'

  // 接口类型定义
  interface CategoryItem {
    id: number
    name: string
    code: string
    parentId: number | null
    level: number
    sort: number
    status: 'active' | 'inactive'
    description: string
    goodsCount: number
    children?: CategoryItem[]
    createTime: string
  }

  // 响应式数据
  const loading = ref(false)
  const saveLoading = ref(false)
  const searchKeyword = ref('')
  const modalVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref<FormInstance>()
  const expandedKeys = ref<number[]>([])

  // 原始分类数据
  const categoryList = ref<CategoryItem[]>([])

  // 表格列配置
  const columns = [
    {
      title: '分类名称',
      key: 'name',
      width: 300
    },
    {
      title: '分类编码',
      dataIndex: 'code',
      key: 'code',
      width: 150
    },
    {
      title: '商品数量',
      key: 'goodsCount',
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
      title: '状态',
      key: 'status',
      width: 100,
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
      width: 280,
      align: 'center',
      fixed: 'right'
    }
  ]

  // 表单数据
  const formData = reactive({
    id: undefined as number | undefined,
    name: '',
    code: '',
    parentId: undefined as number | undefined,
    sort: 0,
    status: 'active' as 'active' | 'inactive',
    description: ''
  })

  // 表单验证规则
  const formRules = {
    name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入分类编码', trigger: 'blur' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }]
  }

  // 构建树形结构
  const buildTree = (list: CategoryItem[], parentId: number | null = null): CategoryItem[] => {
    return list
      .filter(item => item.parentId === parentId)
      .map(item => ({
        ...item,
        children: buildTree(list, item.id)
      }))
      .sort((a, b) => a.sort - b.sort)
  }

  // 过滤树形数据
  const filterTree = (tree: CategoryItem[], keyword: string): CategoryItem[] => {
    if (!keyword) return tree

    return tree.reduce((acc: CategoryItem[], item) => {
      const matchesName = item.name.toLowerCase().includes(keyword.toLowerCase())
      const filteredChildren = filterTree(item.children || [], keyword)

      if (matchesName || filteredChildren.length > 0) {
        acc.push({
          ...item,
          children: filteredChildren
        })
      }

      return acc
    }, [])
  }

  // 计算属性：分类树
  const categoryTree = computed(() => {
    const tree = buildTree(categoryList.value)
    return filterTree(tree, searchKeyword.value)
  })

  // 计算属性：分类选择树（用于表单中的上级分类选择）
  const categorySelectTree = computed(() => {
    const filterCurrentCategory = (tree: CategoryItem[], currentId?: number): CategoryItem[] => {
      return tree
        .filter(item => item.id !== currentId)
        .map(item => ({
          ...item,
          children: filterCurrentCategory(item.children || [], currentId)
        }))
    }

    return filterCurrentCategory(buildTree(categoryList.value), formData.id)
  })

  // 搜索
  const handleSearch = () => {
    // 搜索时展开所有匹配的节点
    if (searchKeyword.value) {
      expandAllMatched()
    }
  }

  // 展开所有节点
  const expandAll = () => {
    const getAllIds = (tree: CategoryItem[]): number[] => {
      let ids: number[] = []
      tree.forEach(item => {
        ids.push(item.id)
        if (item.children && item.children.length > 0) {
          ids = [...ids, ...getAllIds(item.children)]
        }
      })
      return ids
    }
    expandedKeys.value = getAllIds(categoryTree.value)
  }

  // 收起所有节点
  const collapseAll = () => {
    expandedKeys.value = []
  }

  // 展开匹配的节点
  const expandAllMatched = () => {
    const getMatchedIds = (tree: CategoryItem[], keyword: string): number[] => {
      let ids: number[] = []
      tree.forEach(item => {
        if (item.name.toLowerCase().includes(keyword.toLowerCase())) {
          ids.push(item.id)
        }
        if (item.children && item.children.length > 0) {
          ids = [...ids, ...getMatchedIds(item.children, keyword)]
        }
      })
      return ids
    }
    expandedKeys.value = getMatchedIds(categoryTree.value, searchKeyword.value)
  }

  // 处理展开/收起
  const handleExpand = (expanded: boolean, record: CategoryItem) => {
    if (expanded) {
      expandedKeys.value.push(record.id)
    } else {
      expandedKeys.value = expandedKeys.value.filter(key => key !== record.id)
    }
  }

  // 加载分类列表
  const loadCategories = async () => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))

      const mockData: CategoryItem[] = [
        {
          id: 1,
          name: '电子产品',
          code: 'ELECTRONICS',
          parentId: null,
          level: 1,
          sort: 1,
          status: 'active',
          description: '各类电子产品',
          goodsCount: 15,
          createTime: '2024-01-10 09:00:00'
        },
        {
          id: 2,
          name: '手机',
          code: 'PHONE',
          parentId: 1,
          level: 2,
          sort: 1,
          status: 'active',
          description: '智能手机',
          goodsCount: 8,
          createTime: '2024-01-10 09:15:00'
        },
        {
          id: 3,
          name: '电脑',
          code: 'COMPUTER',
          parentId: 1,
          level: 2,
          sort: 2,
          status: 'active',
          description: '台式机和笔记本',
          goodsCount: 7,
          createTime: '2024-01-10 09:30:00'
        },
        {
          id: 4,
          name: '服装鞋帽',
          code: 'CLOTHING',
          parentId: null,
          level: 1,
          sort: 2,
          status: 'active',
          description: '各类服装鞋帽',
          goodsCount: 25,
          createTime: '2024-01-10 10:00:00'
        },
        {
          id: 5,
          name: '男装',
          code: 'MEN_CLOTHING',
          parentId: 4,
          level: 2,
          sort: 1,
          status: 'active',
          description: '男士服装',
          goodsCount: 12,
          createTime: '2024-01-10 10:15:00'
        }
      ]

      categoryList.value = mockData
    } catch (error) {
      message.error('加载分类列表失败')
    } finally {
      loading.value = false
    }
  }

  // 新增
  const handleAdd = (parent?: CategoryItem) => {
    isEdit.value = false
    modalVisible.value = true
    resetForm()
    if (parent) {
      formData.parentId = parent.id
    }
  }

  // 编辑
  const handleEdit = (record: CategoryItem) => {
    isEdit.value = true
    modalVisible.value = true
    Object.assign(formData, {
      id: record.id,
      name: record.name,
      code: record.code,
      parentId: record.parentId,
      sort: record.sort,
      status: record.status,
      description: record.description
    })
  }

  // 删除
  const handleDelete = async (id: number) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      message.success('删除成功')
      loadCategories()
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
      loadCategories()
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
      parentId: undefined,
      sort: 0,
      status: 'active',
      description: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    loadCategories()
  })
</script>

<style scoped>
  .categories {
    padding: 0;
  }

  :deep(.ant-table-cell) {
    padding: 12px 8px;
  }

  :deep(.ant-table-row-expand-icon) {
    margin-right: 8px;
  }
</style>
