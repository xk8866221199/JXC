# JXC进销存管理系统前端

一个基于Vue 3 + TypeScript + Ant Design Vue的现代化进销存管理系统前端应用。

## 🚀 技术栈

- **框架**: Vue 3.5.13 + TypeScript 5.9.2
- **构建工具**: Vite 6.3.5
- **UI组件库**: Ant Design Vue 4.2.6
- **状态管理**: Pinia 3.0.3 + pinia-plugin-persistedstate 4.5.0
- **路由管理**: Vue Router 4.5.1
- **图表库**: ECharts 5.6.0 + Vue-ECharts 7.0.3
- **HTTP客户端**: Axios 1.9.0
- **日期处理**: Day.js 1.11.18
- **代码规范**: ESLint + Prettier
- **单元测试**: Vitest + @vue/test-utils
- **部署**: Nginx

## 📁 项目结构

```
jxc-frontend/
├── public/                 # 静态资源
├── src/
│   ├── assets/            # 资源文件
│   ├── components/        # 全局组件
│   │   └── charts/        # 图表组件
│   ├── layouts/           # 布局组件
│   ├── router/            # 路由配置
│   ├── stores/            # Pinia状态管理
│   ├── utils/             # 工具函数
│   ├── views/             # 页面组件
│   │   ├── goods/         # 商品管理
│   │   ├── inventory/     # 库存管理
│   │   ├── purchase/      # 采购管理
│   │   ├── sales/         # 销售管理
│   │   ├── finance/       # 财务管理
│   │   ├── system/        # 系统管理
│   │   └── reports/       # 数据报表
│   ├── App.vue            # 根组件
│   ├── main.ts            # 应用入口
│   └── style.css          # 全局样式
├── tests/                 # 测试文件
├── .env.development       # 开发环境配置
├── .env.production        # 生产环境配置
├── .eslintrc.js          # ESLint配置
├── .prettierrc           # Prettier配置
├── nginx.conf            # Nginx配置
├── package.json          # 项目依赖
├── tsconfig.json         # TypeScript配置
├── vite.config.ts        # Vite配置
└── vitest.config.ts      # 测试配置
```

## 🎯 功能特性

### 核心业务模块
- **仪表盘**: 数据概览、快捷操作、实时图表
- **商品管理**: 商品列表、商品分类
- **库存管理**: 库存查询、库存盘点
- **采购管理**: 采购订单、供应商管理
- **销售管理**: 销售订单、客户管理
- **财务管理**: 应收管理、应付管理
- **系统管理**: 用户管理、角色管理
- **数据报表**: 销售报表、库存报表

### 技术特性
- **响应式设计**: 支持PC、平板、手机多端适配
- **数据可视化**: 基于ECharts的专业图表展示
- **性能优化**: 代码分割、懒加载、缓存策略
- **类型安全**: 完整的TypeScript类型定义
- **代码规范**: ESLint + Prettier自动化代码检查
- **单元测试**: 完整的测试覆盖
- **部署便捷**: Nginx部署

## 🛠️ 开发环境搭建

### 环境要求
- Node.js >= 18.0.0
- npm >= 9.0.0

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

## 🧪 代码质量

### 代码检查
```bash
# 检查代码规范
npm run lint:check

# 自动修复代码规范问题
npm run lint

# 检查代码格式
npm run format:check

# 自动格式化代码
npm run format
```

### 单元测试
```bash
# 运行测试
npm run test

# 运行测试并生成覆盖率报告
npm run test:coverage

# 运行测试UI界面
npm run test:ui
```

### 类型检查
```bash
npm run type-check
```

## 🚀 部署指南

### 手动部署

1. **构建项目**
```bash
npm run build
```

2. **配置Nginx**
```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api/ {
        proxy_pass http://your-backend-api;
    }
}
```

3. **启动服务**
```bash
nginx -s reload
```

### 一键部署

项目提供了自动化部署脚本：

```bash
# 完整部署流程
./deploy.sh

# 仅构建项目
./deploy.sh build

# 仅启动服务
./deploy.sh start

# 查看帮助
./deploy.sh --help
```

## 🔧 配置说明

### 环境变量

在 `.env.development` 和 `.env.production` 中配置：

```env
# API基础URL
VITE_API_BASE_URL=http://localhost:8080/api

# 应用标题
VITE_APP_TITLE=JXC进销存管理系统

# 环境标识
VITE_APP_ENV=development
```

### Vite配置

`vite.config.ts` 包含了以下优化配置：
- 代码分割策略
- 依赖预构建
- 压缩优化
- 开发服务器配置

### 代码规范配置

- **ESLint**: `.eslintrc.js` - 代码质量检查
- **Prettier**: `.prettierrc` - 代码格式化
- **TypeScript**: `tsconfig.json` - 类型检查配置

## 📊 性能监控

应用内置了性能监控工具：

```typescript
import { performance } from '@/utils/performance'

// 记录页面加载时间
performance.markPageLoad()

// 记录API响应时间
performance.markApiResponse('getUserList', responseTime)
```

## 🏗️ 开发指南

### 添加新页面

1. 在 `src/views/` 下创建页面组件
2. 在 `src/router/index.ts` 中添加路由配置
3. 在 `src/layouts/DefaultLayout.vue` 中添加菜单项

### 添加新图表

1. 在 `src/components/charts/` 下创建图表组件
2. 定义数据类型接口
3. 在 `index.ts` 中导出组件

### 状态管理

使用Pinia进行状态管理：

```typescript
// stores/example.ts
export const useExampleStore = defineStore('example', () => {
  const state = ref(initialState)
  
  const actions = {
    async fetchData() {
      // 异步操作
    }
  }
  
  return { state, ...actions }
})
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 更新日志

### v1.0.0 (2024-09-25)
#### ✨ 新增功能
- ✅ 完成基础框架搭建
- ✅ 集成Vue 3.5.13 + TypeScript 5.9.2
- ✅ 集成Ant Design Vue 4.2.6 UI组件库
- ✅ 集成Pinia 3.0.3状态管理和数据持久化
- ✅ 集成Vue Router 4.5.1路由管理
- ✅ 集成ECharts 5.6.0数据可视化
- ✅ 配置完整的代码规范和格式化
- ✅ 配置Vitest单元测试环境
- ✅ 完成Nginx部署配置
- ✅ 完成所有业务页面基础结构
- ✅ 完成登录页面和仪表盘功能
- ✅ 完成主布局和导航系统
- ✅ 完成路由守卫和权限控制

#### 🔧 技术优化
- ✅ 配置了完整的TypeScript类型系统
- ✅ 集成Husky + lint-staged代码质量控制
- ✅ 配置了多环境变量管理
- ✅ 使用Vite 6.3.5高性能构建工具
- ✅ 完成一键部署脚本配置

#### 📱 业务模块状态
- ✅ 系统管理：用户管理、角色管理页面
- ✅ 商品管理：商品列表、商品分类页面
- ✅ 采购管理：采购订单、供应商管理页面
- ✅ 库存管理：库存查询、库存盘点页面
- ✅ 销售管理：销售订单、客户管理页面
- ✅ 财务管理：应收管理、应付管理页面
- ✅ 数据报表：销售报表、库存报表页面

#### 🎯 当前状态
- ✅ 前端技术框架搭建完成
- ✅ 所有业务页面基础结构已完成
- 🔄 等待后端API接口完成进行功能联调
- 🔄 准备进行集成测试和用户体验优化

## 📞 技术支持

- **项目地址**: https://github.com/your-org/jxc-frontend
- **问题反馈**: https://github.com/your-org/jxc-frontend/issues
- **开发文档**: https://your-docs-site.com

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

**JXC进销存管理系统** - 让库存管理更简单、更高效！ 🎉