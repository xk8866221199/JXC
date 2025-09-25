-- ======================================
-- 库存管理模块
-- ======================================

-- 库存表
DROP TABLE IF EXISTS inventory_stock;
CREATE TABLE inventory_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '库存ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    warehouse_id BIGINT DEFAULT 1 COMMENT '仓库ID',
    current_stock INT NOT NULL DEFAULT 0 COMMENT '当前库存',
    available_stock INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    frozen_stock INT NOT NULL DEFAULT 0 COMMENT '冻结库存',
    total_in_stock INT NOT NULL DEFAULT 0 COMMENT '累计入库',
    total_out_stock INT NOT NULL DEFAULT 0 COMMENT '累计出库',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_goods_warehouse (goods_id, warehouse_id)
) ENGINE=InnoDB COMMENT='库存表';

-- 库存记录表
DROP TABLE IF EXISTS inventory_record;
CREATE TABLE inventory_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    warehouse_id BIGINT DEFAULT 1 COMMENT '仓库ID',
    record_type TINYINT NOT NULL COMMENT '记录类型：1-入库 2-出库 3-调拨 4-盘点',
    quantity INT NOT NULL COMMENT '数量（正数入库，负数出库）',
    before_stock INT NOT NULL COMMENT '变动前库存',
    after_stock INT NOT NULL COMMENT '变动后库存',
    unit_price DECIMAL(10,2) COMMENT '单价',
    total_amount DECIMAL(12,2) COMMENT '总金额',
    remark VARCHAR(200) COMMENT '备注',
    operator_id BIGINT COMMENT '操作员ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='库存记录表';

-- ======================================
-- 采购管理模块
-- ======================================

-- 采购订单表
DROP TABLE IF EXISTS purchase_order;
CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '采购单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    order_date DATE NOT NULL COMMENT '采购日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1-待审核 2-已审核 3-部分到货 4-全部到货 5-已完成',
    total_quantity INT NOT NULL DEFAULT 0 COMMENT '总数量',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
    received_quantity INT DEFAULT 0 COMMENT '已收货数量',
    creator_id BIGINT COMMENT '创建人ID',
    remark VARCHAR(200) COMMENT '备注',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='采购订单表';

-- 采购订单明细表
DROP TABLE IF EXISTS purchase_order_detail;
CREATE TABLE purchase_order_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '采购订单ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购价格',
    order_quantity INT NOT NULL COMMENT '订购数量',
    received_quantity INT DEFAULT 0 COMMENT '已收货数量',
    amount DECIMAL(12,2) NOT NULL COMMENT '金额',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='采购订单明细表';

-- ======================================
-- 销售管理模块
-- ======================================

-- 客户信息表
DROP TABLE IF EXISTS customer_info;
CREATE TABLE customer_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_code VARCHAR(50) NOT NULL UNIQUE COMMENT '客户编码',
    customer_type TINYINT DEFAULT 1 COMMENT '客户类型：1-个人 2-企业',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '地址',
    total_consumption DECIMAL(15,2) DEFAULT 0.00 COMMENT '累计消费',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='客户信息表';

-- 销售订单表
DROP TABLE IF EXISTS sales_order;
CREATE TABLE sales_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '销售单号',
    customer_id BIGINT COMMENT '客户ID',
    order_date DATE NOT NULL COMMENT '销售日期',
    order_type TINYINT DEFAULT 1 COMMENT '订单类型：1-零售 2-批发',
    status TINYINT DEFAULT 1 COMMENT '状态：1-待确认 2-已确认 3-已完成 4-已取消',
    total_quantity INT NOT NULL DEFAULT 0 COMMENT '总数量',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
    payment_status TINYINT DEFAULT 1 COMMENT '付款状态：1-未付款 2-已付款',
    payment_method TINYINT COMMENT '付款方式：1-现金 2-银行卡 3-支付宝 4-微信',
    salesperson_id BIGINT COMMENT '销售员ID',
    remark VARCHAR(200) COMMENT '备注',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='销售订单表';

-- 销售订单明细表
DROP TABLE IF EXISTS sales_order_detail;
CREATE TABLE sales_order_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '销售订单ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sale_price DECIMAL(10,2) NOT NULL COMMENT '销售价格',
    order_quantity INT NOT NULL COMMENT '订购数量',
    amount DECIMAL(12,2) NOT NULL COMMENT '金额',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    profit_amount DECIMAL(10,2) COMMENT '利润金额',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='销售订单明细表';

-- ======================================
-- 财务管理模块
-- ======================================

-- 应收账款表
DROP TABLE IF EXISTS finance_receivable;
CREATE TABLE finance_receivable (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    receivable_no VARCHAR(50) NOT NULL UNIQUE COMMENT '应收单号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    receivable_amount DECIMAL(12,2) NOT NULL COMMENT '应收金额',
    received_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '已收金额',
    remaining_amount DECIMAL(12,2) NOT NULL COMMENT '剩余金额',
    due_date DATE COMMENT '到期日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1-未收款 2-部分收款 3-已收款',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='应收账款表';

-- 应付账款表
DROP TABLE IF EXISTS finance_payable;
CREATE TABLE finance_payable (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    payable_no VARCHAR(50) NOT NULL UNIQUE COMMENT '应付单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    payable_amount DECIMAL(12,2) NOT NULL COMMENT '应付金额',
    paid_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '已付金额',
    remaining_amount DECIMAL(12,2) NOT NULL COMMENT '剩余金额',
    due_date DATE COMMENT '到期日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1-未付款 2-部分付款 3-已付款',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='应付账款表';

-- 财务记录表
DROP TABLE IF EXISTS finance_record;
CREATE TABLE finance_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '记录单号',
    record_type TINYINT NOT NULL COMMENT '记录类型：1-收入 2-支出',
    category VARCHAR(50) NOT NULL COMMENT '收支分类',
    amount DECIMAL(12,2) NOT NULL COMMENT '金额',
    payment_method TINYINT COMMENT '付款方式：1-现金 2-银行卡 3-支付宝 4-微信',
    transaction_date DATE NOT NULL COMMENT '交易日期',
    description VARCHAR(200) COMMENT '描述',
    operator_id BIGINT COMMENT '操作员ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) ENGINE=InnoDB COMMENT='财务记录表';