package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.InventoryTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 库存流水服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class InventoryTransactionServiceTest {
    
    @Resource
    private InventoryTransactionService inventoryTransactionService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testCreateTransaction() {
        // 准备测试数据
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionNo("TEST20250928001");
        transaction.setProductId(1L);
        transaction.setTransactionType(1);
        transaction.setSourceType(1);
        transaction.setQuantity(10);
        transaction.setUnitCost(new BigDecimal("50.00"));
        transaction.setTotalCost(new BigDecimal("500.00"));
        transaction.setBeforeQuantity(100);
        transaction.setAfterQuantity(110);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemark("测试入库");
        
        // 执行测试
        boolean result = inventoryTransactionService.createTransaction(transaction);
        
        // 验证结果
        assertTrue(result);
        assertNotNull(transaction.getId());
    }
    
    @Test
    void testGetTransactionsWithPagination() {
        // 执行测试
        Page<InventoryTransaction> page = new Page<>(1, 10);
        IPage<InventoryTransaction> result = inventoryTransactionService.getTransactionsWithPagination(page, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
    
    @Test
    void testUpdateTransaction() {
        // 准备测试数据
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionNo("TEST20250928002");
        transaction.setProductId(1L);
        transaction.setTransactionType(1);
        transaction.setSourceType(1);
        transaction.setQuantity(10);
        transaction.setUnitCost(new BigDecimal("50.00"));
        transaction.setTotalCost(new BigDecimal("500.00"));
        transaction.setBeforeQuantity(100);
        transaction.setAfterQuantity(110);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemark("测试入库");
        inventoryTransactionService.createTransaction(transaction);
        
        // 修改数据
        transaction.setRemark("更新测试入库");
        boolean result = inventoryTransactionService.updateTransaction(transaction);
        
        // 验证结果
        assertTrue(result);
        
        // 验证更新是否成功
        InventoryTransaction updatedTransaction = inventoryTransactionService.getTransactionById(transaction.getId());
        assertEquals("更新测试入库", updatedTransaction.getRemark());
    }
    
    @Test
    void testDeleteTransaction() {
        // 准备测试数据
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionNo("TEST20250928003");
        transaction.setProductId(1L);
        transaction.setTransactionType(1);
        transaction.setSourceType(1);
        transaction.setQuantity(10);
        transaction.setUnitCost(new BigDecimal("50.00"));
        transaction.setTotalCost(new BigDecimal("500.00"));
        transaction.setBeforeQuantity(100);
        transaction.setAfterQuantity(110);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemark("测试入库");
        inventoryTransactionService.createTransaction(transaction);
        
        // 执行测试
        boolean result = inventoryTransactionService.deleteTransaction(transaction.getId());
        
        // 验证结果
        assertTrue(result);
    }
}