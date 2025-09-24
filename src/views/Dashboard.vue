<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>今日销售额</span>
            </div>
          </template>
          <div class="card-content">
            <h2>¥ 12,345</h2>
            <div class="trend">
              <span class="up">↑ 12%</span>
              较昨日
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>今日订单数</span>
            </div>
          </template>
          <div class="card-content">
            <h2>89</h2>
            <div class="trend">
              <span class="up">↑ 8%</span>
              较昨日
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>库存预警</span>
            </div>
          </template>
          <div class="card-content">
            <h2>12</h2>
            <div class="trend">
              <span class="down">↓ 3</span>
              较昨日
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>待处理采购</span>
            </div>
          </template>
          <div class="card-content">
            <h2>5</h2>
            <div class="trend">
              <span class="up">↑ 2</span>
              较昨日
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近订单</span>
            </div>
          </template>
          <el-table :data="recentOrders" style="width: 100%">
            <el-table-column prop="id" label="订单号" width="180" />
            <el-table-column prop="customer" label="客户" width="180" />
            <el-table-column prop="amount" label="金额" />
            <el-table-column prop="status" label="状态" />
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>库存预警</span>
            </div>
          </template>
          <el-table :data="stockWarnings" style="width: 100%">
            <el-table-column prop="name" label="商品名称" width="180" />
            <el-table-column prop="stock" label="当前库存" width="180" />
            <el-table-column prop="warning" label="预警值" />
            <el-table-column prop="status" label="状态" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const recentOrders = ref([
  {
    id: 'SO20240301001',
    customer: '张三公司',
    amount: '¥ 2,345',
    status: '已完成'
  },
  {
    id: 'SO20240301002',
    customer: '李四企业',
    amount: '¥ 1,234',
    status: '处理中'
  },
  {
    id: 'SO20240301003',
    customer: '王五贸易',
    amount: '¥ 3,456',
    status: '待发货'
  }
])

const stockWarnings = ref([
  {
    name: '商品A',
    stock: 10,
    warning: 20,
    status: '库存不足'
  },
  {
    name: '商品B',
    stock: 5,
    warning: 15,
    status: '库存不足'
  },
  {
    name: '商品C',
    stock: 8,
    warning: 10,
    status: '库存不足'
  }
])
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.trend {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.up {
  color: #67C23A;
}

.down {
  color: #F56C6C;
}
</style> 