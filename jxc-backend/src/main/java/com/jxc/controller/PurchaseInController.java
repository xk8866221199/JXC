package com.jxc.controller;

import com.jxc.common.Result;
import com.jxc.entity.InventoryTransaction;
import com.jxc.entity.PurchaseOrderItem;
import com.jxc.service.PurchaseInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购入库管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "采购入库管理", description = "采购入库管理相关接口")
@RestController
@RequestMapping("/purchase/in")
@CrossOrigin(origins = "*")
public class PurchaseInController {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseInController.class);
    
    @Autowired
    private PurchaseInService purchaseInService;
    
    /**
     * 采购入库
     */
    @Operation(summary = "采购入库", description = "采购入库")
    @PostMapping("/{orderId}")
    public Result<Boolean> purchaseIn(@PathVariable Long orderId, @RequestBody List<PurchaseOrderItem> items) {
        try {
            boolean result = purchaseInService.purchaseIn(orderId, items);
            return Result.success(result, result ? "入库成功" : "入库失败");
        } catch (Exception e) {
            logger.error("采购入库失败: {}", e.getMessage(), e);
            return Result.error("采购入库失败：" + e.getMessage());
        }
    }
    
    /**
     * 部分采购入库
     */
    @Operation(summary = "部分采购入库", description = "部分采购入库")
    @PostMapping("/partial/{orderId}")
    public Result<Boolean> partialPurchaseIn(@PathVariable Long orderId, @RequestBody List<PurchaseOrderItem> items) {
        try {
            boolean result = purchaseInService.partialPurchaseIn(orderId, items);
            return Result.success(result, result ? "部分入库成功" : "部分入库失败");
        } catch (Exception e) {
            logger.error("部分采购入库失败: {}", e.getMessage(), e);
            return Result.error("部分采购入库失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建入库流水记录
     */
    @Operation(summary = "创建入库流水记录", description = "创建入库流水记录")
    @PostMapping("/transaction")
    public Result<Boolean> createInTransaction(@RequestBody InventoryTransaction transaction) {
        try {
            boolean result = purchaseInService.createInTransaction(transaction);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建入库流水记录失败: {}", e.getMessage(), e);
            return Result.error("创建入库流水记录失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量创建入库流水记录
     */
    @Operation(summary = "批量创建入库流水记录", description = "批量创建入库流水记录")
    @PostMapping("/transactions")
    public Result<Boolean> createInTransactions(@RequestBody List<InventoryTransaction> transactions) {
        try {
            boolean result = purchaseInService.createInTransactions(transactions);
            return Result.success(result, result ? "批量创建成功" : "批量创建失败");
        } catch (Exception e) {
            logger.error("批量创建入库流水记录失败: {}", e.getMessage(), e);
            return Result.error("批量创建入库流水记录失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新采购订单状态
     */
    @Operation(summary = "更新采购订单状态", description = "更新采购订单状态")
    @PutMapping("/{orderId}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long orderId, @RequestBody Integer orderStatus) {
        try {
            boolean result = purchaseInService.updateOrderStatus(orderId, orderStatus);
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新采购订单状态失败: {}", e.getMessage(), e);
            return Result.error("更新采购订单状态失败：" + e.getMessage());
        }
    }
}