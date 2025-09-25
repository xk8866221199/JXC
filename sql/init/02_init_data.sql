-- JXC进销存管理系统测试数据初始化脚本
-- 创建时间: 2025-09-24
-- 作者: JXC Development Team
-- 版本: 1.0.0

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 初始化系统用户数据
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `status`, `remark`, `created_by`, `updated_by`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC', '系统管理员', '13800138000', 'admin@jxc.com', 1, '系统默认管理员账户', 1, 1),
(2, 'manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC', '部门经理', '13800138001', 'manager@jxc.com', 1, '部门经理账户', 1, 1),
(3, 'clerk', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC', '业务员', '13800138002', 'clerk@jxc.com', 1, '普通业务员账户', 1, 1);

-- ----------------------------
-- 初始化系统角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `sort_order`, `created_by`, `updated_by`) VALUES
(1, 'ADMIN', '系统管理员', '拥有系统所有权限', 1, 1, 1, 1),
(2, 'MANAGER', '部门经理', '拥有部门管理权限', 1, 2, 1, 1),
(3, 'CLERK', '业务员', '拥有基本业务操作权限', 1, 3, 1, 1);

-- ----------------------------
-- 初始化用户角色关联数据
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `created_by`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1);

-- ----------------------------
-- 初始化系统权限数据
-- ----------------------------
INSERT INTO `sys_permission` (`id`, `parent_id`, `permission_code`, `permission_name`, `permission_type`, `path`, `component`, `icon`, `sort_order`, `status`, `visible`, `created_by`, `updated_by`) VALUES
-- 主菜单
(1, 0, 'system', '系统管理', 1, '/system', 'Layout', 'el-icon-setting', 1, 1, 1, 1, 1),
(2, 0, 'product', '商品管理', 1, '/product', 'Layout', 'el-icon-goods', 2, 1, 1, 1, 1),
(3, 0, 'purchase', '采购管理', 1, '/purchase', 'Layout', 'el-icon-shopping-cart-2', 3, 1, 1, 1, 1),
(4, 0, 'sales', '销售管理', 1, '/sales', 'Layout', 'el-icon-sell', 4, 1, 1, 1, 1),
(5, 0, 'inventory', '库存管理', 1, '/inventory', 'Layout', 'el-icon-box', 5, 1, 1, 1, 1),
(6, 0, 'report', '报表管理', 1, '/report', 'Layout', 'el-icon-data-analysis', 6, 1, 1, 1, 1),

-- 系统管理子菜单
(11, 1, 'system:user', '用户管理', 1, '/system/user', 'system/user/index', 'el-icon-user', 1, 1, 1, 1, 1),
(12, 1, 'system:role', '角色管理', 1, '/system/role', 'system/role/index', 'el-icon-user-solid', 2, 1, 1, 1, 1),
(13, 1, 'system:permission', '权限管理', 1, '/system/permission', 'system/permission/index', 'el-icon-lock', 3, 1, 1, 1, 1),

-- 商品管理子菜单
(21, 2, 'product:category', '商品分类', 1, '/product/category', 'product/category/index', 'el-icon-menu', 1, 1, 1, 1, 1),
(22, 2, 'product:list', '商品信息', 1, '/product/list', 'product/list/index', 'el-icon-goods', 2, 1, 1, 1, 1),

-- 采购管理子菜单
(31, 3, 'purchase:supplier', '供应商管理', 1, '/purchase/supplier', 'purchase/supplier/index', 'el-icon-office-building', 1, 1, 1, 1, 1),
(32, 3, 'purchase:order', '采购订单', 1, '/purchase/order', 'purchase/order/index', 'el-icon-document', 2, 1, 1, 1, 1),

-- 销售管理子菜单
(41, 4, 'sales:customer', '客户管理', 1, '/sales/customer', 'sales/customer/index', 'el-icon-user-solid', 1, 1, 1, 1, 1),
(42, 4, 'sales:order', '销售订单', 1, '/sales/order', 'sales/order/index', 'el-icon-document', 2, 1, 1, 1, 1),

-- 库存管理子菜单
(51, 5, 'inventory:stock', '库存查询', 1, '/inventory/stock', 'inventory/stock/index', 'el-icon-box', 1, 1, 1, 1, 1),
(52, 5, 'inventory:transaction', '库存流水', 1, '/inventory/transaction', 'inventory/transaction/index', 'el-icon-tickets', 2, 1, 1, 1, 1),

-- 按钮权限
(111, 11, 'system:user:add', '添加用户', 2, '', '', '', 1, 1, 0, 1, 1),
(112, 11, 'system:user:edit', '编辑用户', 2, '', '', '', 2, 1, 0, 1, 1),
(113, 11, 'system:user:delete', '删除用户', 2, '', '', '', 3, 1, 0, 1, 1),
(114, 11, 'system:user:reset', '重置密码', 2, '', '', '', 4, 1, 0, 1, 1),

(121, 12, 'system:role:add', '添加角色', 2, '', '', '', 1, 1, 0, 1, 1),
(122, 12, 'system:role:edit', '编辑角色', 2, '', '', '', 2, 1, 0, 1, 1),
(123, 12, 'system:role:delete', '删除角色', 2, '', '', '', 3, 1, 0, 1, 1),

(221, 22, 'product:list:add', '添加商品', 2, '', '', '', 1, 1, 0, 1, 1),
(222, 22, 'product:list:edit', '编辑商品', 2, '', '', '', 2, 1, 0, 1, 1),
(223, 22, 'product:list:delete', '删除商品', 2, '', '', '', 3, 1, 0, 1, 1);

-- ----------------------------
-- 初始化角色权限关联数据
-- ----------------------------
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`) VALUES
-- 管理员拥有所有权限
(1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), (1, 5, 1), (1, 6, 1),
(1, 11, 1), (1, 12, 1), (1, 13, 1),
(1, 21, 1), (1, 22, 1),
(1, 31, 1), (1, 32, 1),
(1, 41, 1), (1, 42, 1),
(1, 51, 1), (1, 52, 1),
(1, 111, 1), (1, 112, 1), (1, 113, 1), (1, 114, 1),
(1, 121, 1), (1, 122, 1), (1, 123, 1),
(1, 221, 1), (1, 222, 1), (1, 223, 1),

-- 经理拥有业务权限
(2, 2, 1), (2, 3, 1), (2, 4, 1), (2, 5, 1), (2, 6, 1),
(2, 21, 1), (2, 22, 1),
(2, 31, 1), (2, 32, 1),
(2, 41, 1), (2, 42, 1),
(2, 51, 1), (2, 52, 1),
(2, 221, 1), (2, 222, 1),

-- 业务员拥有基本操作权限
(3, 2, 1), (3, 3, 1), (3, 4, 1), (3, 5, 1),
(3, 21, 1), (3, 22, 1),
(3, 31, 1), (3, 32, 1),
(3, 41, 1), (3, 42, 1),
(3, 51, 1), (3, 52, 1);

-- ----------------------------
-- 初始化商品分类数据
-- ----------------------------
INSERT INTO `product_category` (`id`, `parent_id`, `category_name`, `category_code`, `description`, `sort_order`, `status`, `created_by`, `updated_by`) VALUES
(1, 0, '电子产品', 'electronics', '各类电子产品', 1, 1, 1, 1),
(2, 0, '办公用品', 'office', '办公用品用具', 2, 1, 1, 1),
(3, 0, '服装鞋帽', 'clothing', '服装鞋帽类商品', 3, 1, 1, 1),

(11, 1, '手机数码', 'mobile', '手机及数码产品', 1, 1, 1, 1),
(12, 1, '电脑配件', 'computer', '电脑及配件', 2, 1, 1, 1),
(13, 1, '家用电器', 'appliance', '家用电器', 3, 1, 1, 1),

(21, 2, '文具用品', 'stationery', '各类文具', 1, 1, 1, 1),
(22, 2, '办公设备', 'equipment', '办公设备', 2, 1, 1, 1);

-- ----------------------------
-- 初始化商品数据
-- ----------------------------
INSERT INTO `product` (`id`, `product_code`, `product_name`, `category_id`, `brand`, `unit`, `specification`, `description`, `purchase_price`, `sale_price`, `stock_quantity`, `min_stock`, `max_stock`, `status`, `created_by`, `updated_by`) VALUES
(1, 'P001', 'iPhone 15 Pro', 11, 'Apple', '台', '256GB 钛原色', 'Apple iPhone 15 Pro 智能手机', 7999.00, 8999.00, 50, 10, 200, 1, 1, 1),
(2, 'P002', 'MacBook Air', 12, 'Apple', '台', 'M2芯片 8GB 256GB', 'Apple MacBook Air 笔记本电脑', 8999.00, 9999.00, 30, 5, 100, 1, 1, 1),
(3, 'P003', '戴尔显示器', 12, 'Dell', '台', '27英寸 4K', 'Dell 27英寸 4K显示器', 1999.00, 2499.00, 80, 20, 200, 1, 1, 1),
(4, 'P004', '中性笔', 21, '晨光', '支', '0.5mm 黑色', '晨光中性笔 黑色', 1.50, 2.00, 1000, 100, 5000, 1, 1, 1),
(5, 'P005', 'A4复印纸', 21, '双A', '包', '80g 500张/包', '双A A4复印纸', 25.00, 35.00, 500, 50, 2000, 1, 1, 1);

-- ----------------------------
-- 初始化供应商数据
-- ----------------------------
INSERT INTO `supplier` (`id`, `supplier_code`, `supplier_name`, `contact_person`, `contact_phone`, `contact_email`, `address`, `credit_level`, `payment_terms`, `status`, `created_by`, `updated_by`) VALUES
(1, 'S001', '苹果授权经销商', '张经理', '13800001001', 'zhang@apple-dealer.com', '深圳市南山区科技园', 1, '月结30天', 1, 1, 1),
(2, 'S002', '戴尔中国有限公司', '李经理', '13800001002', 'li@dell.com.cn', '北京市朝阳区建国门外大街', 1, '月结45天', 1, 1, 1),
(3, 'S003', '晨光文具集团', '王经理', '13800001003', 'wang@mg-pen.com', '上海市青浦区城中西路', 2, '月结15天', 1, 1, 1);

-- ----------------------------
-- 初始化客户数据
-- ----------------------------
INSERT INTO `customer` (`id`, `customer_code`, `customer_name`, `contact_person`, `contact_phone`, `contact_email`, `address`, `credit_level`, `customer_type`, `status`, `created_by`, `updated_by`) VALUES
(1, 'C001', '阿里巴巴集团', '陈经理', '13900001001', 'chen@alibaba.com', '杭州市余杭区文一西路', 1, 2, 1, 1, 1),
(2, 'C002', '腾讯科技有限公司', '刘经理', '13900001002', 'liu@tencent.com', '深圳市南山区科技园', 1, 2, 1, 1, 1),
(3, 'C003', '个人客户张三', '张三', '13900001003', 'zhangsan@163.com', '北京市海淀区中关村', 2, 1, 1, 1, 1);

-- ----------------------------
-- 初始化采购订单数据
-- ----------------------------
INSERT INTO `purchase_order` (`id`, `order_no`, `supplier_id`, `order_date`, `delivery_date`, `total_amount`, `discount_amount`, `tax_amount`, `final_amount`, `order_status`, `payment_status`, `remark`, `created_by`, `updated_by`) VALUES
(1, 'PO202509240001', 1, '2025-09-24', '2025-09-30', 159980.00, 0.00, 20797.40, 180777.40, 2, 1, '采购iPhone设备', 1, 1),
(2, 'PO202509240002', 3, '2025-09-24', '2025-09-26', 2000.00, 100.00, 247.00, 2147.00, 1, 1, '采购办公文具', 1, 1);

-- ----------------------------
-- 初始化采购订单明细数据
-- ----------------------------
INSERT INTO `purchase_order_item` (`id`, `order_id`, `product_id`, `quantity`, `unit_price`, `total_price`, `received_quantity`) VALUES
(1, 1, 1, 20, 7999.00, 159980.00, 20),
(2, 2, 4, 1000, 1.50, 1500.00, 0),
(3, 2, 5, 20, 25.00, 500.00, 0);

-- ----------------------------
-- 初始化销售订单数据
-- ----------------------------
INSERT INTO `sales_order` (`id`, `order_no`, `customer_id`, `order_date`, `delivery_date`, `total_amount`, `discount_amount`, `tax_amount`, `final_amount`, `order_status`, `payment_status`, `remark`, `created_by`, `updated_by`) VALUES
(1, 'SO202509240001', 1, '2025-09-24', '2025-09-28', 89990.00, 1000.00, 11628.70, 100618.70, 2, 1, '阿里巴巴采购设备', 1, 1);

-- ----------------------------
-- 初始化销售订单明细数据
-- ----------------------------
INSERT INTO `sales_order_item` (`id`, `order_id`, `product_id`, `quantity`, `unit_price`, `total_price`, `delivered_quantity`) VALUES
(1, 1, 1, 10, 8999.00, 89990.00, 0);

-- ----------------------------
-- 初始化库存流水数据
-- ----------------------------
INSERT INTO `inventory_transaction` (`id`, `transaction_no`, `product_id`, `transaction_type`, `source_type`, `source_id`, `quantity`, `unit_cost`, `total_cost`, `before_quantity`, `after_quantity`, `transaction_date`, `remark`, `created_by`) VALUES
(1, 'IT202509240001', 1, 1, 1, 1, 20, 7999.00, 159980.00, 30, 50, '2025-09-24 09:00:00', '采购入库', 1),
(2, 'IT202509240002', 2, 1, 1, NULL, 30, 8999.00, 269970.00, 0, 30, '2025-09-24 10:00:00', '期初入库', 1),
(3, 'IT202509240003', 3, 1, 1, NULL, 80, 1999.00, 159920.00, 0, 80, '2025-09-24 11:00:00', '期初入库', 1),
(4, 'IT202509240004', 4, 1, 1, NULL, 1000, 1.50, 1500.00, 0, 1000, '2025-09-24 12:00:00', '期初入库', 1),
(5, 'IT202509240005', 5, 1, 1, NULL, 500, 25.00, 12500.00, 0, 500, '2025-09-24 13:00:00', '期初入库', 1);

SET FOREIGN_KEY_CHECKS = 1;