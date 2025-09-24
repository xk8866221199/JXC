import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      // 系统管理
      {
        path: 'system/users',
        name: 'Users',
        component: () => import('@/views/system/Users.vue')
      },
      {
        path: 'system/roles',
        name: 'Roles',
        component: () => import('@/views/system/Roles.vue')
      },
      // 商品管理
      {
        path: 'goods/list',
        name: 'GoodsList',
        component: () => import('@/views/goods/GoodsList.vue')
      },
      {
        path: 'goods/categories',
        name: 'GoodsCategories',
        component: () => import('@/views/goods/Categories.vue')
      },
      // 采购管理
      {
        path: 'purchase/orders',
        name: 'PurchaseOrders',
        component: () => import('@/views/purchase/Orders.vue')
      },
      {
        path: 'purchase/suppliers',
        name: 'Suppliers',
        component: () => import('@/views/purchase/Suppliers.vue')
      },
      // 库存管理
      {
        path: 'inventory/stock',
        name: 'Stock',
        component: () => import('@/views/inventory/Stock.vue')
      },
      {
        path: 'inventory/check',
        name: 'InventoryCheck',
        component: () => import('@/views/inventory/Check.vue')
      },
      // 销售管理
      {
        path: 'sales/orders',
        name: 'SalesOrders',
        component: () => import('@/views/sales/Orders.vue')
      },
      {
        path: 'sales/customers',
        name: 'Customers',
        component: () => import('@/views/sales/Customers.vue')
      },
      // 财务管理
      {
        path: 'finance/receivables',
        name: 'Receivables',
        component: () => import('@/views/finance/Receivables.vue')
      },
      {
        path: 'finance/payables',
        name: 'Payables',
        component: () => import('@/views/finance/Payables.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 