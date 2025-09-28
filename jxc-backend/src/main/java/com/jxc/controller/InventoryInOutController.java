package com.jxc.controller;

import com.jxc.common.Result;
import com.jxc.entity.InventoryTransaction;
import com.jxc.service.InventoryInOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出入库管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "出入库管理", description = "出入库管理相关接口")
@RestController
@RequestMapping("/inventory/inout")
@CrossOrigin(origins = "*")
public class InventoryInOutController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryInOutController.class);
    
    @Autowired
    private InventoryInOutService inventoryInOutService;
    
    /**
     * 入库操作
     */
    @Operation(summary = "入库操作", description = "入库操作")
    @PostMapping("/in")
    public Result<Boolean> stockIn(@RequestBody InventoryTransaction transaction) {
        try {
            boolean result = inventoryInOutService.stockIn(transaction);
            return Result.success(result, result ? "入库成功" : "入库失败");
        } catch (Exception e) {
            logger.error("入库操作失败: {}", e.getMessage(), e);
            return Result.error("入库操作失败：" + e.getMessage());
        }
    }
    
    /**
     * 出库操作
     */
    @Operation(summary = "出库操作", description = "出库操作")
    @PostMapping("/out")
    public Result<Boolean> stockOut(@RequestBody InventoryTransaction transaction) {
        try {
            boolean result = inventoryInOutService.stockOut(transaction);
            return Result.success(result, result ? "出库成功" : "出库失败");
        } catch (Exception e) {
            logger.error("出库操作失败: {}", e.getMessage(), e);
            return Result.error("出库操作失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量入库操作
     */
    @Operation(summary = "批量入库操作", description = "批量入库操作")
    @PostMapping("/batch/in")
    public Result<Boolean> batchStockIn(@RequestBody List<InventoryTransaction> transactions) {
        try {
            boolean result = inventoryInOutService.batchStockIn(transactions);
            return Result.success(result, result ? "批量入库成功" : "批量入库失败");
        } catch (Exception e) {
            logger.error("批量入库操作失败: {}", e.getMessage(), e);
            return Result.error("批量入库操作失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量出库操作
     */
    @Operation(summary = "批量出库操作", description = "批量出库操作")
    @PostMapping("/batch/out")
    public Result<Boolean> batchStockOut(@RequestBody List<InventoryTransaction> transactions) {
        try {
            boolean result = inventoryInOutService.batchStockOut(transactions);
            return Result.success(result, result ? "批量出库成功" : "批量出库失败");
        } catch (Exception e) {
            logger.error("批量出库操作失败: {}", e.getMessage(), e);
            return Result.error("批量出库操作失败：" + e.getMessage());
        }
    }
}