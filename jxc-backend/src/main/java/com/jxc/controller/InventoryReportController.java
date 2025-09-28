package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.dto.InventoryReportDTO;
import com.jxc.service.InventoryReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存报表控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "库存报表", description = "库存报表相关接口")
@RestController
@RequestMapping("/reports/inventory")
@CrossOrigin(origins = "*")
public class InventoryReportController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryReportController.class);
    
    @Autowired
    private InventoryReportService inventoryReportService;
    
    /**
     * 分页查询库存报表列表
     */
    @Operation(summary = "分页查询库存报表列表", description = "分页查询库存报表列表，支持分类和关键字筛选")
    @GetMapping
    public Result<IPage<InventoryReportDTO>> getInventoryReport(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        try {
            Page<InventoryReportDTO> pageRequest = new Page<>(page, size);
            IPage<InventoryReportDTO> result = inventoryReportService.getInventoryReport(pageRequest, categoryId, keyword);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存报表列表失败: {}", e.getMessage(), e);
            return Result.error("查询库存报表列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取库存预警列表
     */
    @Operation(summary = "获取库存预警列表", description = "获取库存预警列表")
    @GetMapping("/warning")
    public Result<IPage<InventoryReportDTO>> getInventoryWarningReport(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<InventoryReportDTO> pageRequest = new Page<>(page, size);
            IPage<InventoryReportDTO> result = inventoryReportService.getInventoryWarningReport(pageRequest);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存预警列表失败: {}", e.getMessage(), e);
            return Result.error("查询库存预警列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取库存周转率统计
     */
    @Operation(summary = "获取库存周转率统计", description = "获取库存周转率统计，支持日期范围筛选")
    @GetMapping("/turnover")
    public Result<List<InventoryReportDTO>> getInventoryTurnoverRate(
            @RequestParam(required = false) java.time.LocalDate startDate,
            @RequestParam(required = false) java.time.LocalDate endDate) {
        try {
            List<InventoryReportDTO> result = inventoryReportService.getInventoryTurnoverRate(startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存周转率统计失败: {}", e.getMessage(), e);
            return Result.error("查询库存周转率统计失败：" + e.getMessage());
        }
    }
}