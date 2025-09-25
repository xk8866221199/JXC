-- MySQL dump 10.13  Distrib 8.0.43, for macos26.0 (arm64)
--
-- Host: localhost    Database: jxc_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户编码',
  `customer_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `contact_person` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系邮箱',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `credit_level` tinyint NOT NULL DEFAULT '1' COMMENT '信用等级(1:A级,2:B级,3:C级)',
  `customer_type` tinyint NOT NULL DEFAULT '1' COMMENT '客户类型(1:个人,2:企业)',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_code` (`customer_code`) USING BTREE,
  KEY `idx_customer_name` (`customer_name`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'C001','阿里巴巴集团','陈经理','13900001001','chen@alibaba.com','杭州市余杭区文一西路',1,2,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'C002','腾讯科技有限公司','刘经理','13900001002','liu@tencent.com','深圳市南山区科技园',1,2,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,'C003','个人客户张三','张三','13900001003','zhangsan@163.com','北京市海淀区中关村',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transaction`
--

DROP TABLE IF EXISTS `inventory_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `transaction_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '流水单号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `transaction_type` tinyint NOT NULL COMMENT '流水类型(1:入库,2:出库,3:调拨,4:盘点)',
  `source_type` tinyint NOT NULL COMMENT '来源类型(1:采购入库,2:销售出库,3:调拨,4:盘点,5:其他)',
  `source_id` bigint DEFAULT NULL COMMENT '来源单据ID',
  `quantity` int NOT NULL COMMENT '数量(正数为入库,负数为出库)',
  `unit_cost` decimal(10,2) DEFAULT NULL COMMENT '单位成本',
  `total_cost` decimal(12,2) DEFAULT NULL COMMENT '总成本',
  `before_quantity` int NOT NULL COMMENT '变动前库存',
  `after_quantity` int NOT NULL COMMENT '变动后库存',
  `transaction_date` datetime NOT NULL COMMENT '交易时间',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_transaction_type` (`transaction_type`) USING BTREE,
  KEY `idx_transaction_date` (`transaction_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transaction`
--

LOCK TABLES `inventory_transaction` WRITE;
/*!40000 ALTER TABLE `inventory_transaction` DISABLE KEYS */;
INSERT INTO `inventory_transaction` VALUES (1,'IT202509240001',1,1,1,1,20,7999.00,159980.00,30,50,'2025-09-24 09:00:00','采购入库',1,'2025-09-24 21:44:38'),(2,'IT202509240002',2,1,1,NULL,30,8999.00,269970.00,0,30,'2025-09-24 10:00:00','期初入库',1,'2025-09-24 21:44:38'),(3,'IT202509240003',3,1,1,NULL,80,1999.00,159920.00,0,80,'2025-09-24 11:00:00','期初入库',1,'2025-09-24 21:44:38'),(4,'IT202509240004',4,1,1,NULL,1000,1.50,1500.00,0,1000,'2025-09-24 12:00:00','期初入库',1,'2025-09-24 21:44:38'),(5,'IT202509240005',5,1,1,NULL,500,25.00,12500.00,0,500,'2025-09-24 13:00:00','期初入库',1,'2025-09-24 21:44:38');
/*!40000 ALTER TABLE `inventory_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品编码',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '品牌',
  `unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位',
  `specification` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规格型号',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `image_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品图片',
  `purchase_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '采购价格',
  `sale_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '销售价格',
  `stock_quantity` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `min_stock` int NOT NULL DEFAULT '0' COMMENT '最小库存',
  `max_stock` int NOT NULL DEFAULT '0' COMMENT '最大库存',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_stock_quantity` (`stock_quantity`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'P001','iPhone 15 Pro',11,'Apple','台','256GB 钛原色','Apple iPhone 15 Pro 智能手机',NULL,7999.00,8999.00,50,10,200,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'P002','MacBook Air',12,'Apple','台','M2芯片 8GB 256GB','Apple MacBook Air 笔记本电脑',NULL,8999.00,9999.00,30,5,100,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,'P003','戴尔显示器',12,'Dell','台','27英寸 4K','Dell 27英寸 4K显示器',NULL,1999.00,2499.00,80,20,200,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(4,'P004','中性笔',21,'晨光','支','0.5mm 黑色','晨光中性笔 黑色',NULL,1.50,2.00,1000,100,5000,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(5,'P005','A4复印纸',21,'双A','包','80g 500张/包','双A A4复印纸',NULL,25.00,35.00,500,50,2000,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父分类ID',
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类编码',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类图标',
  `sort_order` int NOT NULL DEFAULT '1' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_sort_order` (`sort_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1,0,'电子产品','electronics','各类电子产品',NULL,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,0,'办公用品','office','办公用品用具',NULL,2,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,0,'服装鞋帽','clothing','服装鞋帽类商品',NULL,3,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(11,1,'手机数码','mobile','手机及数码产品',NULL,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(12,1,'电脑配件','computer','电脑及配件',NULL,2,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(13,1,'家用电器','appliance','家用电器',NULL,3,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(21,2,'文具用品','stationery','各类文具',NULL,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(22,2,'办公设备','equipment','办公设备',NULL,2,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `order_date` date NOT NULL COMMENT '订单日期',
  `delivery_date` date DEFAULT NULL COMMENT '交货日期',
  `total_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `tax_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `final_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '最终金额',
  `order_status` tinyint NOT NULL DEFAULT '1' COMMENT '订单状态(1:待审核,2:已审核,3:采购中,4:部分到货,5:全部到货,6:已取消)',
  `payment_status` tinyint NOT NULL DEFAULT '1' COMMENT '付款状态(1:未付款,2:部分付款,3:已付款)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE,
  KEY `idx_order_date` (`order_date`) USING BTREE,
  KEY `idx_order_status` (`order_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
INSERT INTO `purchase_order` VALUES (1,'PO202509240001',1,'2025-09-24','2025-09-30',159980.00,0.00,20797.40,180777.40,2,1,'采购iPhone设备',1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'PO202509240002',3,'2025-09-24','2025-09-26',2000.00,100.00,247.00,2147.00,1,1,'采购办公文具',1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_item`
--

DROP TABLE IF EXISTS `purchase_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '采购数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(12,2) NOT NULL COMMENT '总价',
  `received_quantity` int NOT NULL DEFAULT '0' COMMENT '已收货数量',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_item`
--

LOCK TABLES `purchase_order_item` WRITE;
/*!40000 ALTER TABLE `purchase_order_item` DISABLE KEYS */;
INSERT INTO `purchase_order_item` VALUES (1,1,1,20,7999.00,159980.00,20,NULL,'2025-09-24 21:44:38','2025-09-24 21:44:38'),(2,2,4,1000,1.50,1500.00,0,NULL,'2025-09-24 21:44:38','2025-09-24 21:44:38'),(3,2,5,20,25.00,500.00,0,NULL,'2025-09-24 21:44:38','2025-09-24 21:44:38');
/*!40000 ALTER TABLE `purchase_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order`
--

DROP TABLE IF EXISTS `sales_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售订单ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `order_date` date NOT NULL COMMENT '订单日期',
  `delivery_date` date DEFAULT NULL COMMENT '交货日期',
  `total_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `tax_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `final_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '最终金额',
  `order_status` tinyint NOT NULL DEFAULT '1' COMMENT '订单状态(1:待审核,2:已审核,3:生产中,4:部分发货,5:全部发货,6:已取消)',
  `payment_status` tinyint NOT NULL DEFAULT '1' COMMENT '收款状态(1:未收款,2:部分收款,3:已收款)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_order_date` (`order_date`) USING BTREE,
  KEY `idx_order_status` (`order_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
INSERT INTO `sales_order` VALUES (1,'SO202509240001',1,'2025-09-24','2025-09-28',89990.00,1000.00,11628.70,100618.70,2,1,'阿里巴巴采购设备',1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `sales_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order_item`
--

DROP TABLE IF EXISTS `sales_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '销售数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(12,2) NOT NULL COMMENT '总价',
  `delivered_quantity` int NOT NULL DEFAULT '0' COMMENT '已发货数量',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order_item`
--

LOCK TABLES `sales_order_item` WRITE;
/*!40000 ALTER TABLE `sales_order_item` DISABLE KEYS */;
INSERT INTO `sales_order_item` VALUES (1,1,1,10,8999.00,89990.00,0,NULL,'2025-09-24 21:44:38','2025-09-24 21:44:38');
/*!40000 ALTER TABLE `sales_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系邮箱',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `credit_level` tinyint NOT NULL DEFAULT '1' COMMENT '信用等级(1:A级,2:B级,3:C级)',
  `payment_terms` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款条件',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`) USING BTREE,
  KEY `idx_supplier_name` (`supplier_name`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'S001','苹果授权经销商','张经理','13800001001','zhang@apple-dealer.com','深圳市南山区科技园',1,'月结30天',1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'S002','戴尔中国有限公司','李经理','13800001002','li@dell.com.cn','北京市朝阳区建国门外大街',1,'月结45天',1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,'S003','晨光文具集团','王经理','13800001003','wang@mg-pen.com','上海市青浦区城中西路',2,'月结15天',1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父权限ID',
  `permission_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限编码',
  `permission_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `permission_type` tinyint NOT NULL DEFAULT '1' COMMENT '权限类型(1:菜单,2:按钮,3:接口)',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由路径',
  `component` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标',
  `sort_order` int NOT NULL DEFAULT '1' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `visible` tinyint NOT NULL DEFAULT '1' COMMENT '是否可见(0:隐藏,1:显示)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_permission_type` (`permission_type`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_sort_order` (`sort_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,0,'system','系统管理',1,'/system','Layout','el-icon-setting',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,0,'product','商品管理',1,'/product','Layout','el-icon-goods',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,0,'purchase','采购管理',1,'/purchase','Layout','el-icon-shopping-cart-2',3,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(4,0,'sales','销售管理',1,'/sales','Layout','el-icon-sell',4,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(5,0,'inventory','库存管理',1,'/inventory','Layout','el-icon-box',5,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(6,0,'report','报表管理',1,'/report','Layout','el-icon-data-analysis',6,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(11,1,'system:user','用户管理',1,'/system/user','system/user/index','el-icon-user',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(12,1,'system:role','角色管理',1,'/system/role','system/role/index','el-icon-user-solid',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(13,1,'system:permission','权限管理',1,'/system/permission','system/permission/index','el-icon-lock',3,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(21,2,'product:category','商品分类',1,'/product/category','product/category/index','el-icon-menu',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(22,2,'product:list','商品信息',1,'/product/list','product/list/index','el-icon-goods',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(31,3,'purchase:supplier','供应商管理',1,'/purchase/supplier','purchase/supplier/index','el-icon-office-building',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(32,3,'purchase:order','采购订单',1,'/purchase/order','purchase/order/index','el-icon-document',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(41,4,'sales:customer','客户管理',1,'/sales/customer','sales/customer/index','el-icon-user-solid',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(42,4,'sales:order','销售订单',1,'/sales/order','sales/order/index','el-icon-document',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(51,5,'inventory:stock','库存查询',1,'/inventory/stock','inventory/stock/index','el-icon-box',1,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(52,5,'inventory:transaction','库存流水',1,'/inventory/transaction','inventory/transaction/index','el-icon-tickets',2,1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(111,11,'system:user:add','添加用户',2,'','','',1,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(112,11,'system:user:edit','编辑用户',2,'','','',2,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(113,11,'system:user:delete','删除用户',2,'','','',3,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(114,11,'system:user:reset','重置密码',2,'','','',4,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(121,12,'system:role:add','添加角色',2,'','','',1,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(122,12,'system:role:edit','编辑角色',2,'','','',2,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(123,12,'system:role:delete','删除角色',2,'','','',3,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(221,22,'product:list:add','添加商品',2,'','','',1,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(222,22,'product:list:edit','编辑商品',2,'','','',2,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(223,22,'product:list:delete','删除商品',2,'','','',3,1,0,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `sort_order` int NOT NULL DEFAULT '1' COMMENT '排序',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_sort_order` (`sort_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'ADMIN','系统管理员','拥有系统所有权限',1,1,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'MANAGER','部门经理','拥有部门管理权限',1,2,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,'CLERK','业务员','拥有基本业务操作权限',1,3,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (1,1,1,1,'2025-09-24 21:44:38'),(2,1,2,1,'2025-09-24 21:44:38'),(3,1,3,1,'2025-09-24 21:44:38'),(4,1,4,1,'2025-09-24 21:44:38'),(5,1,5,1,'2025-09-24 21:44:38'),(6,1,6,1,'2025-09-24 21:44:38'),(7,1,11,1,'2025-09-24 21:44:38'),(8,1,12,1,'2025-09-24 21:44:38'),(9,1,13,1,'2025-09-24 21:44:38'),(10,1,21,1,'2025-09-24 21:44:38'),(11,1,22,1,'2025-09-24 21:44:38'),(12,1,31,1,'2025-09-24 21:44:38'),(13,1,32,1,'2025-09-24 21:44:38'),(14,1,41,1,'2025-09-24 21:44:38'),(15,1,42,1,'2025-09-24 21:44:38'),(16,1,51,1,'2025-09-24 21:44:38'),(17,1,52,1,'2025-09-24 21:44:38'),(18,1,111,1,'2025-09-24 21:44:38'),(19,1,112,1,'2025-09-24 21:44:38'),(20,1,113,1,'2025-09-24 21:44:38'),(21,1,114,1,'2025-09-24 21:44:38'),(22,1,121,1,'2025-09-24 21:44:38'),(23,1,122,1,'2025-09-24 21:44:38'),(24,1,123,1,'2025-09-24 21:44:38'),(25,1,221,1,'2025-09-24 21:44:38'),(26,1,222,1,'2025-09-24 21:44:38'),(27,1,223,1,'2025-09-24 21:44:38'),(28,2,2,1,'2025-09-24 21:44:38'),(29,2,3,1,'2025-09-24 21:44:38'),(30,2,4,1,'2025-09-24 21:44:38'),(31,2,5,1,'2025-09-24 21:44:38'),(32,2,6,1,'2025-09-24 21:44:38'),(33,2,21,1,'2025-09-24 21:44:38'),(34,2,22,1,'2025-09-24 21:44:38'),(35,2,31,1,'2025-09-24 21:44:38'),(36,2,32,1,'2025-09-24 21:44:38'),(37,2,41,1,'2025-09-24 21:44:38'),(38,2,42,1,'2025-09-24 21:44:38'),(39,2,51,1,'2025-09-24 21:44:38'),(40,2,52,1,'2025-09-24 21:44:38'),(41,2,221,1,'2025-09-24 21:44:38'),(42,2,222,1,'2025-09-24 21:44:38'),(43,3,2,1,'2025-09-24 21:44:38'),(44,3,3,1,'2025-09-24 21:44:38'),(45,3,4,1,'2025-09-24 21:44:38'),(46,3,5,1,'2025-09-24 21:44:38'),(47,3,21,1,'2025-09-24 21:44:38'),(48,3,22,1,'2025-09-24 21:44:38'),(49,3,31,1,'2025-09-24 21:44:38'),(50,3,32,1,'2025-09-24 21:44:38'),(51,3,41,1,'2025-09-24 21:44:38'),(52,3,42,1,'2025-09-24 21:44:38'),(53,3,51,1,'2025-09-24 21:44:38'),(54,3,52,1,'2025-09-24 21:44:38');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后登录IP',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  UNIQUE KEY `uk_phone` (`phone`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC','系统管理员','13800138000','admin@jxc.com',NULL,NULL,1,'系统默认管理员账户',NULL,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(2,'manager','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC','部门经理','13800138001','manager@jxc.com',NULL,NULL,1,'部门经理账户',NULL,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0),(3,'clerk','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8igtLk6z6UcIGJJQ5NVx2Jn1lOwPC','业务员','13800138002','clerk@jxc.com',NULL,NULL,1,'普通业务员账户',NULL,NULL,1,1,'2025-09-24 21:44:38','2025-09-24 21:44:38',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,1,1,'2025-09-24 21:44:38'),(2,2,2,1,'2025-09-24 21:44:38'),(3,3,3,1,'2025-09-24 21:44:38');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-24 21:53:12
