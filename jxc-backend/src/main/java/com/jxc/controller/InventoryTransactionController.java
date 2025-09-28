package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.InventoryTransaction;
import com.jxc.service.InventoryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存流水管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "库存流水管理", description = "库存流水管理相关接口")
@RestController
@RequestMapping("/inventory/transactions")
@CrossOrigin(origins = "*")
public class InventoryTransactionController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryTransactionController.class);
    
    @Autowired
    private InventoryTransactionService inventoryTransactionService;
    
    /**
     * 分页查询库存流水列表
     */
    @Operation(summary = "分页查询库存流水列表", description = "分页查询库存流水列表，支持商品ID、流水类型和日期范围筛选")
    @GetMapping
    public Result<IPage<InventoryTransaction>> getTransactions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer transactionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Page<InventoryTransaction> pageRequest = new Page<>(page, size);
            IPage<InventoryTransaction> result = inventoryTransactionService.getTransactionsWithPagination(pageRequest, productId, transactionType, startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存流水列表失败: {}", e.getMessage(), e);
            return Result.error("查询库存流水列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取库存流水详情
     */
    @Operation(summary = "根据ID获取库存流水详情", description = "根据ID获取库存流水详情")
    @GetMapping("/{id}")
    public Result<InventoryTransaction> getTransactionById(@PathVariable Long id) {
        try {
            InventoryTransaction transaction = inventoryTransactionService.getTransactionById(id);
            if (transaction == null) {
                return Result.error("库存流水不存在");
            }
            return Result.success(transaction, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存流水详情失败: {}", e.getMessage(), e);
            return Result.error("查询库存流水详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建库存流水
     */
    @Operation(summary = "创建库存流水", description = "创建库存流水")
    @PostMapping
    public Result<Boolean> createTransaction(@RequestBody InventoryTransaction transaction) {
        try {
            boolean result = inventoryTransactionService.createTransaction(transaction);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建库存流水失败: {}", e.getMessage(), e);
            return Result.error("创建库存流水失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新库存流水
     */
    @Operation(summary = "更新库存流水", description = "更新库存流水")
    @PutMapping("/{id}")
    public Result<Boolean> updateTransaction(@PathVariable Long id, @RequestBody InventoryTransaction transaction) {
        try {
            transaction.setId(id);
            boolean result = inventoryTransactionService.updateTransaction(transaction);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新库存流水失败: {}", e.getMessage(), e);
            return Result.error("更新库存流水失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除库存流水
     */
    @Operation(summary = "删除库存流水", description = "删除库存流水")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTransaction(@PathVariable Long id) {
        try {
            boolean result = inventoryTransactionService.deleteTransaction(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除库存流水失败: {}", e.getMessage(), e);
            return Result.error("删除库存流水失败：" + e.getMessage());
        }
    }
}