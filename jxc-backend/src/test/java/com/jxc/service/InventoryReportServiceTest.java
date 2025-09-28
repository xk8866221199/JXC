package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.dto.InventoryReportDTO;
import com.jxc.service.impl.InventoryReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 库存报表服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class InventoryReportServiceTest {
    
    @Resource
    private InventoryReportService inventoryReportService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testGetInventoryReport() {
        // 执行测试
        Page<InventoryReportDTO> page = new Page<>(1, 10);
        IPage<InventoryReportDTO> result = inventoryReportService.getInventoryReport(page, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getRecords().size() <= 10);
    }
    
    @Test
    void testGetInventoryWarningReport() {
        // 执行测试
        Page<InventoryReportDTO> page = new Page<>(1, 10);
        IPage<InventoryReportDTO> result = inventoryReportService.getInventoryWarningReport(page);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getRecords().size() <= 10);
    }
    
    @Test
    void testGetInventoryTurnoverRate() {
        // 执行测试
        List<InventoryReportDTO> result = inventoryReportService.getInventoryTurnoverRate(null, null);
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}