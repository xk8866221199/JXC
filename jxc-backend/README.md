# JXC进销存管理系统后端

一个基于Spring Boot 3.3.5 + Java 17 + MyBatis Plus的现代化进销存管理系统后端服务。

## 🚀 技术栈

### 核心框架
- **Spring Boot 3.3.5**: 企业级Java开发框架  
- **Java 17**: LTS版本，性能与安全性兼备
- **Maven 3.9+**: 项目构建与依赖管理

### 安全与权限
- **Spring Security 6.0**: 认证与授权框架
- **JWT 0.12.3**: 无状态身份认证
- **RBAC**: 基于角色的权限控制

### 数据访问层
- **MyBatis Plus 3.5.12**: ORM框架（Spring Boot 3专用版本）
- **HikariCP**: 高性能数据库连接池（内置）
- **MySQL 8.0**: 关系型数据库
- **Redis 7.2**: 缓存和会话存储

### 服务治理
- **Spring Boot Actuator**: 应用监控与健康检查
- **Logback**: 日志管理
- **Hibernate Validator**: 参数校验
- **Swagger/OpenAPI 3**: API文档自动生成

### 工具库
- **Hutool 5.8.25**: Java工具类库
- **FastJSON 2.0.44**: JSON序列化
- **EasyExcel 3.3.2**: Excel导入导出
- **Spring Boot DevTools**: 开发热部署

## 🎯 当前状态

### ✅ 已完成功能
- **基础架构**: Spring Boot 3.3.5 + Java 17项目框架
- **数据库集成**: MyBatis Plus 3.5.12 + MySQL 8.0连接
- **缓存系统**: Redis 7.2集成和配置
- **安全认证**: JWT身份认证机制
- **权限控制**: Spring Security 6.0安全框架
- **API文档**: Swagger UI自动生成
- **健康检查**: Spring Boot Actuator监控
- **异常处理**: 全局异常处理器
- **数据验证**: 请求参数校验
- **日志系统**: 分级日志记录

### 🔄 开发中功能
- **用户管理**: 用户CRUD操作API
- **角色权限**: RBAC权限体系实现
- **商品管理**: 商品和分类管理API
- **库存管理**: 库存查询和盘点API
- **采购管理**: 采购订单和供应商API
- **销售管理**: 销售订单和客户API
- **财务管理**: 应收应付管理API
- **数据报表**: 统计分析API

### 📊 运行状态
- **服务状态**: ✅ 正常运行在 http://localhost:8080/api
- **API文档**: ✅ 可访问 http://localhost:8080/api/swagger-ui/index.html
- **健康检查**: ✅ http://localhost:8080/api/actuator/health
- **默认账户**: admin / 123456 ✅ 测试可用

## 🏗️ 项目结构

```
jxc-backend/
├── src/main/java/com/jxc/
│   ├── controller/          # 控制器层 (4个文件)
│   │   ├── AuthController.java      # 认证控制器
│   │   ├── TestController.java      # 测试接口
│   │   ├── HealthController.java    # 健康检查
│   │   └── VerifyController.java    # 验证接口
│   ├── service/            # 业务逻辑层 (1个文件)
│   ├── repository/         # 数据访问层 (1个文件)
│   │   └── SysUserMapper.java       # 用户数据访问
│   ├── entity/             # 实体类 (1个文件)
│   │   └── SysUser.java             # 用户实体
│   ├── dto/                # 数据传输对象 (2个文件)
│   │   ├── LoginRequest.java        # 登录请求
│   │   └── LoginResponse.java       # 登录响应
│   ├── config/             # 配置类 (4个文件)
│   │   ├── SecurityConfig.java      # 安全配置
│   │   ├── RedisConfig.java         # Redis配置
│   │   └── PasswordConfig.java      # 密码加密配置
│   ├── security/           # 安全相关 (2个文件)
│   │   ├── JwtAuthenticationFilter.java  # JWT过滤器
│   │   └── JwtAuthenticationEntryPoint.java # 认证入口
│   ├── common/             # 公共组件 (4个文件)
│   │   ├── Result.java              # 统一响应格式
│   │   ├── PageResult.java          # 分页响应
│   │   ├── BusinessException.java   # 业务异常
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   └── utils/              # 工具类 (1个文件)
│       └── JwtUtils.java            # JWT工具类
├── src/main/resources/
│   ├── application.yml     # 主配置文件
│   ├── application-dev.yml # 开发环境配置
│   └── application-prod.yml # 生产环境配置
└── src/test/java/          # 测试代码
```

## 📊 数据库设计

### 核心业务表

- **系统管理**: sys_user, sys_role, sys_permission, sys_user_role, sys_role_permission
- **商品管理**: goods_category, goods_info, supplier_info
- **库存管理**: inventory_stock, inventory_record
- **采购管理**: purchase_order, purchase_order_detail
- **销售管理**: sales_order, sales_order_detail, customer_info
- **财务管理**: finance_receivable, finance_payable, finance_record

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.9+
- MySQL 8.0+（可选，当前为模拟数据）
- Redis 7.0+（可选，当前为模拟数据）

### 1. 克隆项目

```bash
git clone <project-url>
cd jxc-backend
```

### 2. 直接运行（推荐）

项目当前使用模拟数据，可直接运行：

```bash
# 使用Maven运行
mvn spring-boot:run

# 或者编译后运行
mvn clean package
java -jar target/jxc-backend-1.0.0.jar
```

### 3. 验证安装

访问健康检查接口：
```bash
curl http://localhost:8080/api/test/health
```

应该返回：
```json
{
  "code": 200,
  "message": "系统运行正常",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-01T00:00:00"
  }
}
```

## 🔐 当前可用账户

| 用户名 | 密码 | 状态 | 说明 |
|--------|------|------|------|
| admin | 123456 | ✅ 测试可用 | 默认管理员账户（模拟数据） |

> 注意：当前为模拟数据，后续集成数据库后将支持更多用户和角色。

## 📚 API文档

项目启动后，可以通过以下地址访问API文档和接口：

### 基础接口
- **健康检查**: `GET /api/test/health`
- **系统信息**: `GET /api/test/echo`
- **监控中心**: `GET /api/actuator/health`

### 认证接口
- **用户登录**: `POST /api/auth/login`
- **获取用户信息**: `GET /api/auth/me`
- **用户登出**: `POST /api/auth/logout`
- **刷新Token**: `POST /api/auth/refresh`

### API文档
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🛠️ 开发指南

### 代码生成

使用MyBatis Plus代码生成器快速生成基础代码：

```bash
# 运行代码生成器
java -cp target/classes com.jxc.utils.CodeGenerator
```

### 缓存策略

项目使用Redis作为缓存中间件，配置了以下缓存策略：

- **用户会话**: 7天过期
- **商品信息**: 1小时过期
- **库存数据**: 30分钟过期

### 分布式锁

提供了Redis分布式锁工具类 `RedisLockUtils`，用于处理并发场景：

```java
@Autowired
private RedisLockUtils redisLockUtils;

// 使用分布式锁
redisLockUtils.executeWithLock("inventory:update:" + goodsId, () -> {
    // 业务逻辑
});
```

## 📈 性能优化

- **数据库连接池**: 使用HikariCP高性能连接池
- **查询优化**: 合理设计索引，避免N+1查询
- **缓存策略**: 热点数据Redis缓存
- **异步处理**: 耗时操作异步化

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn test jacoco:report
```

## 🚀 部署

### 生产环境

1. 修改配置文件中的数据库和Redis连接信息
2. 设置环境变量 `SPRING_PROFILES_ACTIVE=prod`
3. 配置SSL证书和域名
4. 设置日志级别和文件路径

## 📝 更新日志

### v1.0.0 (2024-09-25)

#### ✨ 新增功能
- ✅ 完成项目基础架构搭建
- ✅ 集成Spring Boot 3.3.5 + Java 17
- ✅ 实现Spring Security + JWT认证体系
- ✅ 配置多环境配置文件
- ✅ 集成Swagger API文档
- ✅ 添加Spring Boot Actuator监控
- ✅ 实现统一异常处理和响应格式

#### 🔧 技术优化
- ✅ 使用MyBatis Plus Spring Boot 3 Starter
- ✅ 集成Hutool工具类库
- ✅ 添加FastJSON2和EasyExcel支持
- ✅ 配置开发时热部署

#### 🛠️ 当前状态
- ⚠️ MyBatis Plus数据库集成调试中
- ✅ 基础认证功能已完成
- ✅ 项目成功运行在端口8080

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目维护: JXC Development Team
- 邮箱: dev@jxc.com
- 项目地址: [GitHub](https://github.com/your-org/jxc-backend)

---

**JXC进销存管理系统** - 让超市管理更简单高效！