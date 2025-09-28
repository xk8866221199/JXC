# 数据报表模块API文档

## 1. 销售统计API

### 1.1 分页查询销售统计列表
- **URL**: `/reports/sales/statistics`
- **Method**: GET
- **Description**: 分页查询销售统计列表，支持日期范围筛选
- **Parameters**:
  - `page` (Integer, optional): 页码，默认为1
  - `size` (Integer, optional): 每页大小，默认为10
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "records": [
        {
          "statDate": "2024-01-01",
          "totalSalesAmount": 1000.00,
          "totalOrderCount": 10,
          "totalProductCount": 50,
          "averageOrderAmount": 100.00,
          "growthRate": 5.0
        }
      ],
      "total": 100,
      "size": 10,
      "current": 1
    }
  }
  ```

### 1.2 获取销售趋势数据
- **URL**: `/reports/sales/trend`
- **Method**: GET
- **Description**: 获取销售趋势数据，支持日期范围筛选
- **Parameters**:
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "statDate": "2024-01-01",
        "totalSalesAmount": 1000.00,
        "totalOrderCount": 10,
        "totalProductCount": 50,
        "averageOrderAmount": 100.00,
        "growthRate": 5.0
      }
    ]
  }
  ```

### 1.3 获取商品销售排行
- **URL**: `/reports/sales/ranking`
- **Method**: GET
- **Description**: 获取商品销售排行，支持日期范围筛选
- **Parameters**:
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
  - `limit` (Integer, optional): 数量限制，默认为10
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "statDate": "2024-01-01",
        "totalSalesAmount": 5000.00,
        "totalOrderCount": 50,
        "totalProductCount": 200,
        "averageOrderAmount": 100.00,
        "growthRate": 5.0
      }
    ]
  }
  ```

## 2. 库存报表API

### 2.1 分页查询库存报表列表
- **URL**: `/reports/inventory`
- **Method**: GET
- **Description**: 分页查询库存报表列表，支持分类和关键字筛选
- **Parameters**:
  - `page` (Integer, optional): 页码，默认为1
  - `size` (Integer, optional): 每页大小，默认为10
  - `categoryId` (Long, optional): 分类ID
  - `keyword` (String, optional): 关键字
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "records": [
        {
          "productId": 1,
          "productCode": "P0001",
          "productName": "商品名称1",
          "categoryName": "分类1",
          "currentQuantity": 100,
          "inventoryCost": 50.00,
          "warningStatus": 1,
          "turnoverRate": 2.5,
          "salesQuantity": 50,
          "purchaseQuantity": 80
        }
      ],
      "total": 100,
      "size": 10,
      "current": 1
    }
  }
  ```

### 2.2 获取库存预警列表
- **URL**: `/reports/inventory/warning`
- **Method**: GET
- **Description**: 获取库存预警列表
- **Parameters**:
  - `page` (Integer, optional): 页码，默认为1
  - `size` (Integer, optional): 每页大小，默认为10
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "records": [
        {
          "productId": 1,
          "productCode": "W0001",
          "productName": "预警商品1",
          "categoryName": "预警分类1",
          "currentQuantity": 5,
          "inventoryCost": 30.00,
          "warningStatus": 3,
          "turnoverRate": 2.5,
          "salesQuantity": 50,
          "purchaseQuantity": 80
        }
      ],
      "total": 50,
      "size": 10,
      "current": 1
    }
  }
  ```

### 2.3 获取库存周转率统计
- **URL**: `/reports/inventory/turnover`
- **Method**: GET
- **Description**: 获取库存周转率统计，支持日期范围筛选
- **Parameters**:
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "productId": 1,
        "productCode": "T0001",
        "productName": "周转商品1",
        "categoryName": "周转分类1",
        "currentQuantity": 200,
        "inventoryCost": 50.00,
        "warningStatus": 1,
        "turnoverRate": 3.0,
        "salesQuantity": 100,
        "purchaseQuantity": 120
      }
    ]
  }
  ```

## 3. 财务统计API

### 3.1 分页查询财务统计列表
- **URL**: `/reports/finance`
- **Method**: GET
- **Description**: 分页查询财务统计列表，支持日期范围筛选
- **Parameters**:
  - `page` (Integer, optional): 页码，默认为1
  - `size` (Integer, optional): 每页大小，默认为10
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "records": [
        {
          "statDate": "2024-01-01",
          "totalReceivableAmount": 5000.00,
          "receivedAmount": 4000.00,
          "unreceivedAmount": 1000.00,
          "totalPayableAmount": 3000.00,
          "paidAmount": 2500.00,
          "unpaidAmount": 500.00,
          "netProfit": 1500.00,
          "grossProfitMargin": 25.0,
          "receivableTurnoverRate": 1.2,
          "payableTurnoverRate": 1.5
        }
      ],
      "total": 100,
      "size": 10,
      "current": 1
    }
  }
  ```

### 3.2 获取应收统计
- **URL**: `/reports/finance/receivable`
- **Method**: GET
- **Description**: 获取应收统计，支持日期范围筛选
- **Parameters**:
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "statDate": "2024-01-01",
        "totalReceivableAmount": 5000.00,
        "receivedAmount": 4000.00,
        "unreceivedAmount": 1000.00,
        "totalPayableAmount": 3000.00,
        "paidAmount": 2500.00,
        "unpaidAmount": 500.00,
        "netProfit": 1500.00,
        "grossProfitMargin": 25.0,
        "receivableTurnoverRate": 1.2,
        "payableTurnoverRate": 1.5
      }
    ]
  }
  ```

### 3.3 获取应付统计
- **URL**: `/reports/finance/payable`
- **Method**: GET
- **Description**: 获取应付统计，支持日期范围筛选
- **Parameters**:
  - `startDate` (LocalDate, optional): 开始日期
  - `endDate` (LocalDate, optional): 结束日期
- **Response**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "statDate": "2024-01-01",
        "totalReceivableAmount": 5000.00,
        "receivedAmount": 4000.00,
        "unreceivedAmount": 1000.00,
        "totalPayableAmount": 3000.00,
        "paidAmount": 2500.00,
        "unpaidAmount": 500.00,
        "netProfit": 1500.00,
        "grossProfitMargin": 25.0,
        "receivableTurnoverRate": 1.2,
        "payableTurnoverRate": 1.5
      }
    ]
  }
  ```

## 4. 联调准备

### 4.1 接口测试
- 所有API接口均已实现基础框架，可进行前后端联调测试
- 接口返回数据格式统一，符合项目规范
- 支持跨域访问，便于前端开发调试

### 4.2 注意事项
- 当前API返回的数据为模拟数据，实际开发中需要连接数据库获取真实数据
- 各服务接口中的TODO标记部分需要根据实际业务逻辑进行实现
- 建议在联调过程中逐步替换模拟数据为真实数据