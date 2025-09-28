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
          @change="handleSearchChange"
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
            <a-form-item label="分类名称" name="categoryName">
              <a-input v-model:value="formData.categoryName" placeholder="请输入分类名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="分类编码" name="categoryCode">
              <a-input v-model:value="formData.categoryCode" placeholder="请输入分类编码" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="排序" name="sortOrder">
              <a-input-number
                v-model:value="formData.sortOrder"
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
  import {
    getGoodsCategoriesApi,
    getAllGoodsCategoriesApi,
    createGoodsCategoryApi,
    updateGoodsCategoryApi,
    deleteGoodsCategoryApi,
    updateGoodsCategoryStatusApi
  } from '@/api/goodsCategory'

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
    categoryName: '',
    categoryCode: '',
    parentId: undefined as number | undefined,
    sortOrder: 0,
    status: 'active' as 'active' | 'inactive',
    description: ''
  })

  // 表单验证规则
  const formRules = {
    categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
    categoryCode: [{ required: true, message: '请输入分类编码', trigger: 'blur' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }]
  }

  // 构建树形结构
  const buildTree = (list: CategoryItem[], parentId: number | null = null): CategoryItem[] => {
    console.log('构建树形结构，输入列表长度:', list.length, '父ID:', parentId)
    // 处理parentId为0的情况（表示顶级分类）
    const targetParentId = parentId === null ? 0 : parentId;
    const result = list
      .filter(item => item.parentId === targetParentId)
      .map(item => ({
        ...item,
        children: buildTree(list, item.id)
      }))
      .sort((a, b) => a.sort - b.sort)
    console.log('构建的子树，父ID:', targetParentId, '结果数量:', result.length)
    return result
  }

  // 过滤树形数据
  const filterTree = (tree: CategoryItem[], keyword: string): CategoryItem[] => {
    console.log('过滤树形数据，树:', tree, '关键词:', keyword)
    if (!keyword) {
      console.log('无关键词，返回原始树')
      return tree
    }

    const result = tree.reduce((acc: CategoryItem[], item) => {
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
    
    console.log('过滤后的结果:', result)
    return result
  }

  // 计算属性：分类树
  const categoryTree = computed(() => {
    console.log('计算categoryTree，categoryList:', categoryList.value)
    const tree = buildTree(categoryList.value)
    console.log('构建的树结构:', tree)
    const filteredTree = filterTree(tree, searchKeyword.value)
    console.log('过滤后的树结构:', filteredTree)
    return filteredTree
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
      loadCategories(searchKeyword.value)
    } else {
      // 如果搜索框为空，收起所有节点并重新加载数据
      collapseAll()
      loadCategories()
    }
  }

  // 处理搜索框内容变化
  const handleSearchChange = (e: any) => {
    // 如果搜索框变为空，重新加载所有数据
    if (!e.target.value) {
      collapseAll()
      loadCategories()
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
  const loadCategories = async (keyword?: string) => {
    console.log('开始加载分类数据，关键词:', keyword)
    loading.value = true
    try {
      const params: any = {
        page: 1,
        size: 1000 // 获取所有分类
      }
      
      // 如果提供了搜索关键词，则添加到参数中
      if (keyword) {
        params.keyword = keyword
      }
      
      console.log('发送请求参数:', params)
      const response = await getGoodsCategoriesApi(params)
      console.log('收到完整响应:', response)
      
      // 正确处理API响应数据结构（响应拦截器已经提取了data字段）
      const categories = response.records || []
      console.log('从响应中提取的分类数据:', categories)
      
      categoryList.value = categories.map((item: any) => ({
        ...item,
        name: item.categoryName, // 映射 categoryName 到 name
        code: item.categoryCode, // 映射 categoryCode 到 code
        status: item.status === 1 ? 'active' : 'inactive',
        createTime: item.createdAt || item.createTime
      }))
      
      console.log('处理后的分类数据:', categoryList.value)
      console.log('categoryList.value长度:', categoryList.value.length)
    } catch (error: any) {
      message.error('加载分类列表失败: ' + (error.message || '未知错误'))
      console.error('加载分类列表失败:', error)
    } finally {
      loading.value = false
      console.log('数据加载完成，loading状态:', loading.value)
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
      console.log('开始删除分类，ID:', id)
      const response = await deleteGoodsCategoryApi(id)
      console.log('删除响应:', response)
      if (response.code === 200) {
        if (response.data === true) {
          message.success('删除成功')
          loadCategories()
        } else {
          // 显示更详细的错误信息
          const errorMsg = response.message || '删除失败'
          message.error(`删除失败: ${errorMsg}`)
          console.error('删除失败详情:', response)
        }
      } else {
        const errorMsg = response.message || '未知错误'
        message.error(`删除失败: ${errorMsg}`)
        console.error('删除失败详情:', response)
      }
    } catch (error: any) {
      console.error('删除失败:', error)
      const errorMsg = error.message || error.toString() || '未知错误'
      message.error(`删除失败: ${errorMsg}`)
    }
  }

  // 保存
  const handleSave = async () => {
    try {
      await formRef.value?.validate()
      saveLoading.value = true

      // 转换状态值
      const submitData: any = {
        ...formData,
        status: formData.status === 'active' ? 1 : 0
      }

      // 如果是编辑模式但没有提供分类名称或编码，则使用原来的值
      if (isEdit.value) {
        submitData.categoryName = formData.categoryName || undefined;
        submitData.categoryCode = formData.categoryCode || undefined;
      }

      if (isEdit.value) {
        await updateGoodsCategoryApi(formData.id!, submitData)
        message.success('更新成功')
      } else {
        await createGoodsCategoryApi(submitData)
        message.success('添加成功')
      }

      modalVisible.value = false
      loadCategories()
    } catch (error: any) {
      if (error.message?.includes('分类编码已存在')) {
        message.error('分类编码已存在，请更换其他编码')
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
      parentId: undefined,
      sort: 0,
      status: 'active',
      description: ''
    })
    formRef.value?.resetFields()
  }

  // 组件挂载
  onMounted(() => {
    console.log('商品分类组件已挂载，开始加载数据...')
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
