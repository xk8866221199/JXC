package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.PurchaseOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 采购订单服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class PurchaseOrderServiceTest {
    
    @Resource
    private PurchaseOrderService purchaseOrderService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testCreateOrder() {
        // 准备测试数据
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo("PO20250928001");
        order.setSupplierId(1L);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setDiscountAmount(new BigDecimal("0.00"));
        order.setTaxAmount(new BigDecimal("130.00"));
        order.setFinalAmount(new BigDecimal("1130.00"));
        order.setOrderStatus(1);
        order.setPaymentStatus(1);
        order.setRemark("测试采购订单");
        
        // 执行测试
        boolean result = purchaseOrderService.createOrder(order);
        
        // 验证结果
        assertTrue(result);
        assertNotNull(order.getId());
    }
    
    @Test
    void testGetOrdersWithPagination() {
        // 执行测试
        Page<PurchaseOrder> page = new Page<>(1, 10);
        IPage<PurchaseOrder> result = purchaseOrderService.getOrdersWithPagination(page, null, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
    
    @Test
    void testUpdateOrder() {
        // 准备测试数据
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo("PO20250928002");
        order.setSupplierId(1L);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setDiscountAmount(new BigDecimal("0.00"));
        order.setTaxAmount(new BigDecimal("130.00"));
        order.setFinalAmount(new BigDecimal("1130.00"));
        order.setOrderStatus(1);
        order.setPaymentStatus(1);
        order.setRemark("测试采购订单");
        purchaseOrderService.createOrder(order);
        
        // 修改数据
        order.setRemark("更新测试采购订单");
        boolean result = purchaseOrderService.updateOrder(order);
        
        // 验证结果
        assertTrue(result);
        
        // 验证更新是否成功
        PurchaseOrder updatedOrder = purchaseOrderService.getOrderById(order.getId());
        assertEquals("更新测试采购订单", updatedOrder.getRemark());
    }
    
    @Test
    void testDeleteOrder() {
        // 准备测试数据
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo("PO20250928003");
        order.setSupplierId(1L);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setDiscountAmount(new BigDecimal("0.00"));
        order.setTaxAmount(new BigDecimal("130.00"));
        order.setFinalAmount(new BigDecimal("1130.00"));
        order.setOrderStatus(1);
        order.setPaymentStatus(1);
        order.setRemark("测试采购订单");
        purchaseOrderService.createOrder(order);
        
        // 执行测试
        boolean result = purchaseOrderService.deleteOrder(order.getId());
        
        // 验证结果
        assertTrue(result);
        
        // 验证删除是否成功
        PurchaseOrder deletedOrder = purchaseOrderService.getOrderById(order.getId());
        assertEquals(1, deletedOrder.getDeleted().intValue());
    }
    
    @Test
    void testUpdateOrderStatus() {
        // 准备测试数据
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo("PO20250928004");
        order.setSupplierId(1L);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setDiscountAmount(new BigDecimal("0.00"));
        order.setTaxAmount(new BigDecimal("130.00"));
        order.setFinalAmount(new BigDecimal("1130.00"));
        order.setOrderStatus(1);
        order.setPaymentStatus(1);
        order.setRemark("测试采购订单");
        purchaseOrderService.createOrder(order);
        
        // 执行测试
        boolean result = purchaseOrderService.updateOrderStatus(order.getId(), 2);
        
        // 验证结果
        assertTrue(result);
        
        // 验证状态更新是否成功
        PurchaseOrder updatedOrder = purchaseOrderService.getOrderById(order.getId());
        assertEquals(2, updatedOrder.getOrderStatus().intValue());
    }
}