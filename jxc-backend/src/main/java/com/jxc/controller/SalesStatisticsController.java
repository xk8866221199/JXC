package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.dto.SalesStatisticsDTO;
import com.jxc.service.SalesStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 销售统计控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "销售统计", description = "销售统计相关接口")
@RestController
@RequestMapping("/reports/sales")
@CrossOrigin(origins = "*")
public class SalesStatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(SalesStatisticsController.class);
    
    @Autowired
    private SalesStatisticsService salesStatisticsService;
    
    /**
     * 分页查询销售统计列表
     */
    @Operation(summary = "分页查询销售统计列表", description = "分页查询销售统计列表，支持日期范围筛选")
    @GetMapping("/statistics")
    public Result<IPage<SalesStatisticsDTO>> getSalesStatistics(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Page<SalesStatisticsDTO> pageRequest = new Page<>(page, size);
            IPage<SalesStatisticsDTO> result = salesStatisticsService.getSalesStatistics(pageRequest, startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询销售统计列表失败: {}", e.getMessage(), e);
            return Result.error("查询销售统计列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取销售趋势数据
     */
    @Operation(summary = "获取销售趋势数据", description = "获取销售趋势数据，支持日期范围筛选")
    @GetMapping("/trend")
    public Result<List<SalesStatisticsDTO>> getSalesTrend(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            List<SalesStatisticsDTO> result = salesStatisticsService.getSalesTrend(startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询销售趋势数据失败: {}", e.getMessage(), e);
            return Result.error("查询销售趋势数据失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取商品销售排行
     */
    @Operation(summary = "获取商品销售排行", description = "获取商品销售排行，支持日期范围筛选")
    @GetMapping("/ranking")
    public Result<List<SalesStatisticsDTO>> getProductSalesRanking(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<SalesStatisticsDTO> result = salesStatisticsService.getProductSalesRanking(startDate, endDate, limit);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品销售排行失败: {}", e.getMessage(), e);
            return Result.error("查询商品销售排行失败：" + e.getMessage());
        }
    }
}