package com.jxc.controller;

import com.jxc.common.Result;
import com.jxc.entity.InventoryTransaction;
import com.jxc.service.InventoryCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存盘点控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "库存盘点", description = "库存盘点相关接口")
@RestController
@RequestMapping("/inventory/check")
@CrossOrigin(origins = "*")
public class InventoryCheckController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryCheckController.class);
    
    @Autowired
    private InventoryCheckService inventoryCheckService;
    
    /**
     * 开始盘点
     */
    @Operation(summary = "开始盘点", description = "开始盘点")
    @PostMapping("/start")
    public Result<Boolean> startCheck(@RequestBody List<InventoryTransaction> checkTransactions) {
        try {
            boolean result = inventoryCheckService.startCheck(checkTransactions);
            return Result.success(result, result ? "开始盘点成功" : "开始盘点失败");
        } catch (Exception e) {
            logger.error("开始盘点失败: {}", e.getMessage(), e);
            return Result.error("开始盘点失败：" + e.getMessage());
        }
    }
    
    /**
     * 完成盘点
     */
    @Operation(summary = "完成盘点", description = "完成盘点")
    @PostMapping("/complete")
    public Result<Boolean> completeCheck(@RequestBody List<InventoryTransaction> checkTransactions) {
        try {
            boolean result = inventoryCheckService.completeCheck(checkTransactions);
            return Result.success(result, result ? "完成盘点成功" : "完成盘点失败");
        } catch (Exception e) {
            logger.error("完成盘点失败: {}", e.getMessage(), e);
            return Result.error("完成盘点失败：" + e.getMessage());
        }
    }
    
    /**
     * 取消盘点
     */
    @Operation(summary = "取消盘点", description = "取消盘点")
    @DeleteMapping("/{id}")
    public Result<Boolean> cancelCheck(@PathVariable Long id) {
        try {
            boolean result = inventoryCheckService.cancelCheck(id);
            return Result.success(result, result ? "取消盘点成功" : "取消盘点失败");
        } catch (Exception e) {
            logger.error("取消盘点失败: {}", e.getMessage(), e);
            return Result.error("取消盘点失败：" + e.getMessage());
        }
    }
}