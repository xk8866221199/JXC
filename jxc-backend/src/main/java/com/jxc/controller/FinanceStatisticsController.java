package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.dto.FinanceStatisticsDTO;
import com.jxc.service.FinanceStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 财务统计控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "财务统计", description = "财务统计相关接口")
@RestController
@RequestMapping("/reports/finance")
@CrossOrigin(origins = "*")
public class FinanceStatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(FinanceStatisticsController.class);
    
    @Autowired
    private FinanceStatisticsService financeStatisticsService;
    
    /**
     * 分页查询财务统计列表
     */
    @Operation(summary = "分页查询财务统计列表", description = "分页查询财务统计列表，支持日期范围筛选")
    @GetMapping
    public Result<IPage<FinanceStatisticsDTO>> getFinanceStatistics(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Page<FinanceStatisticsDTO> pageRequest = new Page<>(page, size);
            IPage<FinanceStatisticsDTO> result = financeStatisticsService.getFinanceStatistics(pageRequest, startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询财务统计列表失败: {}", e.getMessage(), e);
            return Result.error("查询财务统计列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取应收统计
     */
    @Operation(summary = "获取应收统计", description = "获取应收统计，支持日期范围筛选")
    @GetMapping("/receivable")
    public Result<List<FinanceStatisticsDTO>> getReceivableStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            List<FinanceStatisticsDTO> result = financeStatisticsService.getReceivableStatistics(startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询应收统计失败: {}", e.getMessage(), e);
            return Result.error("查询应收统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取应付统计
     */
    @Operation(summary = "获取应付统计", description = "获取应付统计，支持日期范围筛选")
    @GetMapping("/payable")
    public Result<List<FinanceStatisticsDTO>> getPayableStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            List<FinanceStatisticsDTO> result = financeStatisticsService.getPayableStatistics(startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询应付统计失败: {}", e.getMessage(), e);
            return Result.error("查询应付统计失败：" + e.getMessage());
        }
    }
}