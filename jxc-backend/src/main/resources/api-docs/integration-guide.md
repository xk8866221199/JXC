# 前后端对接集成指南

## 1. 环境准备

### 1.1 后端环境
- JDK 17+
- Maven 3.9+
- MySQL 8.0+
- Redis 7.0+
- Node.js 18+ (仅用于测试)

### 1.2 前端环境
- Node.js 18+
- npm 9+ 或 yarn 1.22+
- Vue 3.4+
- TypeScript 5.0+

### 1.3 开发工具
- IntelliJ IDEA / VS Code
- Postman (API测试)
- 浏览器开发者工具

## 2. 项目启动

### 2.1 后端启动
```bash
# 进入后端项目目录
cd jxc-backend

# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/jxc-backend-1.0.0.jar
```

后端服务默认运行在 `http://localhost:8080`

### 2.2 前端启动
```bash
# 进入前端项目目录
cd jxc-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 或构建生产版本
npm run build
```

前端服务默认运行在 `http://localhost:5173`

## 3. API对接说明

### 3.1 认证流程
1. 前端调用 `/auth/login` 接口进行登录
2. 后端返回JWT Token
3. 前端在后续请求的Authorization头中携带Token

### 3.2 请求格式
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

### 3.3 响应格式
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "userId": 1,
    "realName": "系统管理员",
    "expiresIn": 86400000
  }
}
```

### 3.4 错误响应格式
```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

## 4. 接口文档

### 4.1 访问地址
- Swagger UI: `http://localhost:8080/api/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`

### 4.2 主要模块接口
1. **认证管理**: `/auth/**`
2. **用户管理**: `/users/**`
3. **商品管理**: `/goods/**`
4. **库存管理**: `/inventory/**`
5. **采购管理**: `/purchase/**`
6. **销售管理**: `/sales/**`
7. **财务管理**: `/finance/**`
8. **数据报表**: `/reports/**`

## 5. 前端API集成

### 5.1 Axios拦截器配置
```javascript
// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { code, data, message } = response.data
    if (code === 200) {
      return data
    } else {
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  error => {
    if (error.response) {
      const { data } = error.response
      const message = data?.message || '请求失败'
      
      // 处理401未授权错误
      if (error.response.status === 401) {
        const userStore = useUserStore()
        userStore.logout()
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(message))
    } else {
      return Promise.reject(new Error('网络错误，请检查网络连接'))
    }
  }
)
```

### 5.2 API调用示例
```javascript
// 登录
export function loginApi(data) {
  return request.post('/auth/login', data)
}

// 获取商品列表
export function getGoodsList(params) {
  return request.get('/goods', { params })
}

// 创建采购订单
export function createPurchaseOrder(data) {
  return request.post('/purchase/orders', data)
}
```

## 6. 联调测试

### 6.1 测试账号
- 用户名: `admin`
- 密码: `123456`

### 6.2 核心功能测试
1. **用户登录**: 验证认证流程
2. **数据查询**: 验证各模块数据查询功能
3. **数据操作**: 验证增删改查功能
4. **权限控制**: 验证RBAC权限控制
5. **报表展示**: 验证数据报表功能

### 6.3 常见问题排查
1. **跨域问题**: 确认后端Cors配置正确
2. **认证失败**: 检查Token是否正确传递
3. **数据不一致**: 检查前后端数据格式是否匹配
4. **性能问题**: 检查是否有大量重复请求

## 7. 部署说明

### 7.1 后端部署
```bash
# 打包
mvn clean package

# 运行
java -jar target/jxc-backend-1.0.0.jar --spring.profiles.active=prod
```

### 7.2 前端部署
```bash
# 构建
npm run build

# 部署到Nginx
# 将dist目录内容复制到Nginx站点目录
```

### 7.3 Nginx配置示例
```nginx
server {
    listen 80;
    server_name jxc.example.com;
    
    location / {
        root /var/www/jxc-frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 8. 性能优化建议

### 8.1 前端优化
1. 合理使用缓存策略
2. 图片压缩和懒加载
3. 组件懒加载
4. 减少不必要的重渲染

### 8.2 后端优化
1. 数据库索引优化
2. Redis缓存策略
3. 接口分页处理
4. 异步处理耗时操作

## 9. 监控和日志

### 9.1 后端监控
- Actuator端点: `http://localhost:8080/actuator`
- 健康检查: `http://localhost:8080/actuator/health`

### 9.2 日志查看
```bash
# 查看后端日志
tail -f jxc-backend/logs/application.log

# 查看前端日志
# 在浏览器开发者工具中查看Console和Network
```

## 10. 技术支持

如在对接过程中遇到问题，请联系：
- 技术负责人: JXC开发团队
- 邮箱: support@jxc.com
- GitHub: https://github.com/jxc-system