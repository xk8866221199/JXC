package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.dto.FinanceStatisticsDTO;
import com.jxc.service.impl.FinanceStatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 财务统计服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class FinanceStatisticsServiceTest {
    
    @Resource
    private FinanceStatisticsService financeStatisticsService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testGetFinanceStatistics() {
        // 执行测试
        Page<FinanceStatisticsDTO> page = new Page<>(1, 10);
        IPage<FinanceStatisticsDTO> result = financeStatisticsService.getFinanceStatistics(page, LocalDate.now().minusDays(30), LocalDate.now());
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getRecords().size() <= 10);
    }
    
    @Test
    void testGetReceivableStatistics() {
        // 执行测试
        List<FinanceStatisticsDTO> result = financeStatisticsService.getReceivableStatistics(LocalDate.now().minusDays(7), LocalDate.now());
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testGetPayableStatistics() {
        // 执行测试
        List<FinanceStatisticsDTO> result = financeStatisticsService.getPayableStatistics(LocalDate.now().minusDays(7), LocalDate.now());
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}