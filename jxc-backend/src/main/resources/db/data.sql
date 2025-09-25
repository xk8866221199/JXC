-- ======================================
-- JXC进销存管理系统初始化数据
-- 作者: JXC Development Team
-- 版本: 1.0.0
-- ======================================

USE jxc_db;

-- ======================================
-- 系统管理初始化数据
-- ======================================

-- 初始化权限数据
INSERT INTO sys_permission (permission_name, permission_code, permission_type, parent_id, path, component, icon, sort_order) VALUES
-- 一级菜单
('系统管理', 'system', 1, 0, '/system', 'Layout', 'system', 1),
('商品管理', 'goods', 1, 0, '/goods', 'Layout', 'goods', 2),
('库存管理', 'inventory', 1, 0, '/inventory', 'Layout', 'inventory', 3),
('采购管理', 'purchase', 1, 0, '/purchase', 'Layout', 'purchase', 4),
('销售管理', 'sales', 1, 0, '/sales', 'Layout', 'sales', 5),
('财务管理', 'finance', 1, 0, '/finance', 'Layout', 'finance', 6),
('数据报表', 'reports', 1, 0, '/reports', 'Layout', 'reports', 7),

-- 系统管理子菜单
('用户管理', 'system:user', 1, 1, '/system/users', 'system/Users', 'user', 101),
('角色管理', 'system:role', 1, 1, '/system/roles', 'system/Roles', 'role', 102),

-- 商品管理子菜单
('商品列表', 'goods:list', 1, 2, '/goods/list', 'goods/GoodsList', 'goods-list', 201),
('商品分类', 'goods:category', 1, 2, '/goods/categories', 'goods/Categories', 'category', 202),
('供应商管理', 'goods:supplier', 1, 2, '/goods/suppliers', 'goods/Suppliers', 'supplier', 203),

-- 库存管理子菜单
('库存查询', 'inventory:stock', 1, 3, '/inventory/stock', 'inventory/Stock', 'stock', 301),
('库存记录', 'inventory:record', 1, 3, '/inventory/records', 'inventory/Records', 'record', 302),

-- 采购管理子菜单
('采购订单', 'purchase:order', 1, 4, '/purchase/orders', 'purchase/Orders', 'order', 401),

-- 销售管理子菜单
('销售订单', 'sales:order', 1, 5, '/sales/orders', 'sales/Orders', 'order', 501),
('客户管理', 'sales:customer', 1, 5, '/sales/customers', 'sales/Customers', 'customer', 502),

-- 财务管理子菜单
('应收账款', 'finance:receivable', 1, 6, '/finance/receivables', 'finance/Receivables', 'receivable', 601),
('应付账款', 'finance:payable', 1, 6, '/finance/payables', 'finance/Payables', 'payable', 602);

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限'),
('系统管理员', 'SYSTEM_ADMIN', '系统管理员，负责用户和权限管理'),
('采购员', 'PURCHASER', '采购员，负责采购管理'),
('销售员', 'SALESPERSON', '销售员，负责销售管理'),
('仓库管理员', 'WAREHOUSE_MANAGER', '仓库管理员，负责库存管理'),
('财务员', 'ACCOUNTANT', '财务员，负责财务管理');

-- 初始化用户数据（密码: 123456，使用BCrypt加密）
INSERT INTO sys_user (username, password, real_name, phone, email, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfR5sF2PzinGoHsqt/jWK2K6', '系统管理员', '13800138000', 'admin@jxc.com', 1),
('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfR5sF2PzinGoHsqt/jWK2K6', '总经理', '13800138001', 'manager@jxc.com', 1),
('purchaser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfR5sF2PzinGoHsqt/jWK2K6', '采购员', '13800138002', 'purchaser@jxc.com', 1),
('salesperson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfR5sF2PzinGoHsqt/jWK2K6', '销售员', '13800138003', 'sales@jxc.com', 1);

-- 分配用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- admin -> 超级管理员
(2, 2), -- manager -> 系统管理员
(3, 3), -- purchaser -> 采购员
(4, 4); -- salesperson -> 销售员

-- 分配角色权限（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE deleted = 0;

-- ======================================
-- 商品管理初始化数据
-- ======================================

-- 初始化商品分类
INSERT INTO goods_category (category_name, category_code, parent_id, level, sort_order) VALUES
-- 一级分类
('食品饮料', 'FOOD_DRINK', 0, 1, 1),
('生活用品', 'DAILY_GOODS', 0, 1, 2),
('电子产品', 'ELECTRONICS', 0, 1, 3),
('服装鞋帽', 'CLOTHING', 0, 1, 4),

-- 二级分类 - 食品饮料
('休闲食品', 'SNACK_FOOD', 1, 2, 101),
('饮料', 'BEVERAGES', 1, 2, 102),
('酒类', 'ALCOHOL', 1, 2, 103),
('乳制品', 'DAIRY', 1, 2, 104),

-- 二级分类 - 生活用品
('洗护用品', 'PERSONAL_CARE', 2, 2, 201),
('清洁用品', 'CLEANING', 2, 2, 202),
('厨房用品', 'KITCHEN', 2, 2, 203),

-- 二级分类 - 电子产品
('数码产品', 'DIGITAL', 3, 2, 301),
('家用电器', 'APPLIANCES', 3, 2, 302);

-- 初始化供应商数据
INSERT INTO supplier_info (supplier_name, supplier_code, contact_person, contact_phone, address) VALUES
('康师傅控股有限公司', 'SUPPLIER_001', '张经理', '021-12345678', '上海市浦东新区XXX路XXX号'),
('可口可乐公司', 'SUPPLIER_002', '李经理', '010-87654321', '北京市朝阳区XXX路XXX号'),
('宝洁公司', 'SUPPLIER_003', '王经理', '020-11111111', '广州市天河区XXX路XXX号'),
('联想集团', 'SUPPLIER_004', '赵经理', '010-22222222', '北京市海淀区XXX路XXX号'),
('美的集团', 'SUPPLIER_005', '陈经理', '0757-33333333', '佛山市顺德区XXX路XXX号');

-- 初始化商品数据
INSERT INTO goods_info (goods_name, goods_code, barcode, category_id, supplier_id, brand, unit, purchase_price, sale_price, min_stock, description) VALUES
-- 食品饮料
('康师傅红烧牛肉面', 'GOODS_001', '6901028180105', 5, 1, '康师傅', '袋', 2.50, 3.50, 100, '经典红烧牛肉味方便面'),
('可口可乐330ml', 'GOODS_002', '6925303711113', 6, 2, '可口可乐', '瓶', 1.80, 2.50, 200, '经典可乐饮料'),
('康师傅冰红茶500ml', 'GOODS_003', '6901028180112', 6, 1, '康师傅', '瓶', 2.00, 3.00, 150, '清香冰红茶饮料'),
('伊利纯牛奶250ml', 'GOODS_004', '6901028001234', 8, NULL, '伊利', '盒', 2.20, 3.20, 80, '优质纯牛奶'),

-- 生活用品
('海飞丝洗发水400ml', 'GOODS_005', '6901028005678', 9, 3, '海飞丝', '瓶', 15.00, 25.00, 50, '去屑洗发水'),
('舒肤佳香皂', 'GOODS_006', '6901028009012', 9, 3, '舒肤佳', '块', 3.50, 5.00, 100, '抗菌香皂'),
('威露士消毒液1L', 'GOODS_007', '6901028001111', 10, NULL, '威露士', '瓶', 12.00, 18.00, 30, '家用消毒液'),

-- 电子产品
('联想笔记本电脑', 'GOODS_008', '6901028012345', 11, 4, '联想', '台', 3500.00, 4200.00, 5, 'ThinkPad系列商务笔记本'),
('美的电饭煲3L', 'GOODS_009', '6901028023456', 12, 5, '美的', '台', 180.00, 250.00, 10, '智能电饭煲');

-- ======================================
-- 库存管理初始化数据
-- ======================================

-- 初始化库存数据
INSERT INTO inventory_stock (goods_id, current_stock, available_stock, total_in_stock) VALUES
(1, 150, 150, 150),
(2, 300, 300, 300),
(3, 200, 200, 200),
(4, 120, 120, 120),
(5, 80, 80, 80),
(6, 150, 150, 150),
(7, 50, 50, 50),
(8, 8, 8, 8),
(9, 15, 15, 15);

-- 初始化库存记录（期初入库）
INSERT INTO inventory_record (goods_id, record_type, quantity, before_stock, after_stock, unit_price, total_amount, remark, operator_id) VALUES
(1, 1, 150, 0, 150, 2.50, 375.00, '期初入库', 1),
(2, 1, 300, 0, 300, 1.80, 540.00, '期初入库', 1),
(3, 1, 200, 0, 200, 2.00, 400.00, '期初入库', 1),
(4, 1, 120, 0, 120, 2.20, 264.00, '期初入库', 1),
(5, 1, 80, 0, 80, 15.00, 1200.00, '期初入库', 1),
(6, 1, 150, 0, 150, 3.50, 525.00, '期初入库', 1),
(7, 1, 50, 0, 50, 12.00, 600.00, '期初入库', 1),
(8, 1, 8, 0, 8, 3500.00, 28000.00, '期初入库', 1),
(9, 1, 15, 0, 15, 180.00, 2700.00, '期初入库', 1);

-- ======================================
-- 客户管理初始化数据
-- ======================================

-- 初始化客户数据
INSERT INTO customer_info (customer_name, customer_code, customer_type, contact_person, contact_phone, address) VALUES
('张三', 'CUSTOMER_001', 1, '张三', '13912345678', '北京市朝阳区XXX小区'),
('李四', 'CUSTOMER_002', 1, '李四', '13987654321', '上海市浦东新区XXX路XXX号'),
('北京ABC超市', 'CUSTOMER_003', 2, '王经理', '010-12345678', '北京市海淀区XXX商场'),
('上海XYZ便利店', 'CUSTOMER_004', 2, '刘经理', '021-87654321', '上海市徐汇区XXX街XXX号'),
('深圳DEF连锁店', 'CUSTOMER_005', 2, '陈经理', '0755-11111111', '深圳市南山区XXX路XXX号');

-- ======================================
-- 创建索引
-- ======================================

-- 为主要查询字段创建索引
CREATE INDEX idx_goods_category_id ON goods_info(category_id);
CREATE INDEX idx_goods_supplier_id ON goods_info(supplier_id);
CREATE INDEX idx_inventory_goods_id ON inventory_stock(goods_id);
CREATE INDEX idx_inventory_record_goods_id ON inventory_record(goods_id);
CREATE INDEX idx_inventory_record_type ON inventory_record(record_type);
CREATE INDEX idx_purchase_order_supplier_id ON purchase_order(supplier_id);
CREATE INDEX idx_purchase_order_date ON purchase_order(order_date);
CREATE INDEX idx_sales_order_customer_id ON sales_order(customer_id);
CREATE INDEX idx_sales_order_date ON sales_order(order_date);

-- ======================================
-- 插入完成提示
-- ======================================

SELECT '数据库初始化完成！' AS message,
       (SELECT COUNT(*) FROM sys_user) AS user_count,
       (SELECT COUNT(*) FROM sys_role) AS role_count,
       (SELECT COUNT(*) FROM sys_permission) AS permission_count,
       (SELECT COUNT(*) FROM goods_category) AS category_count,
       (SELECT COUNT(*) FROM goods_info) AS goods_count,
       (SELECT COUNT(*) FROM supplier_info) AS supplier_count,
       (SELECT COUNT(*) FROM customer_info) AS customer_count,
       (SELECT COUNT(*) FROM inventory_stock) AS stock_count;