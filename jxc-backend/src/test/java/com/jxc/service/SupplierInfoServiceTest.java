package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.SupplierInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 供应商信息服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class SupplierInfoServiceTest {
    
    @Resource
    private SupplierInfoService supplierInfoService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testCreateSupplier() {
        // 准备测试数据
        SupplierInfo supplier = new SupplierInfo();
        supplier.setSupplierName("测试供应商");
        supplier.setSupplierCode("SUPPLIER001");
        supplier.setContactPerson("联系人");
        supplier.setContactPhone("13800138000");
        supplier.setAddress("测试地址");
        supplier.setStatus(1);
        
        // 执行测试
        boolean result = supplierInfoService.createSupplier(supplier);
        
        // 验证结果
        assertTrue(result);
        assertNotNull(supplier.getId());
    }
    
    @Test
    void testGetSuppliersWithPagination() {
        // 执行测试
        Page<SupplierInfo> page = new Page<>(1, 10);
        IPage<SupplierInfo> result = supplierInfoService.getSuppliersWithPagination(page, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
    
    @Test
    void testUpdateSupplier() {
        // 准备测试数据
        SupplierInfo supplier = new SupplierInfo();
        supplier.setSupplierName("测试供应商");
        supplier.setSupplierCode("SUPPLIER002");
        supplier.setContactPerson("联系人");
        supplier.setContactPhone("13800138000");
        supplier.setAddress("测试地址");
        supplier.setStatus(1);
        supplierInfoService.createSupplier(supplier);
        
        // 修改数据
        supplier.setSupplierName("更新测试供应商");
        boolean result = supplierInfoService.updateSupplier(supplier);
        
        // 验证结果
        assertTrue(result);
        
        // 验证更新是否成功
        SupplierInfo updatedSupplier = supplierInfoService.getSupplierById(supplier.getId());
        assertEquals("更新测试供应商", updatedSupplier.getSupplierName());
    }
    
    @Test
    void testDeleteSupplier() {
        // 准备测试数据
        SupplierInfo supplier = new SupplierInfo();
        supplier.setSupplierName("测试供应商");
        supplier.setSupplierCode("SUPPLIER003");
        supplier.setContactPerson("联系人");
        supplier.setContactPhone("13800138000");
        supplier.setAddress("测试地址");
        supplier.setStatus(1);
        supplierInfoService.createSupplier(supplier);
        
        // 执行测试
        boolean result = supplierInfoService.deleteSupplier(supplier.getId());
        
        // 验证结果
        assertTrue(result);
        
        // 验证删除是否成功
        SupplierInfo deletedSupplier = supplierInfoService.getSupplierById(supplier.getId());
        assertEquals(1, deletedSupplier.getDeleted().intValue());
    }
    
    @Test
    void testCheckSupplierCodeExists() {
        // 准备测试数据
        SupplierInfo supplier = new SupplierInfo();
        supplier.setSupplierName("测试供应商");
        supplier.setSupplierCode("SUPPLIER004");
        supplier.setContactPerson("联系人");
        supplier.setContactPhone("13800138000");
        supplier.setAddress("测试地址");
        supplier.setStatus(1);
        supplierInfoService.createSupplier(supplier);
        
        // 执行测试
        boolean exists = supplierInfoService.checkSupplierCodeExists("SUPPLIER004", null);
        
        // 验证结果
        assertTrue(exists);
    }
}