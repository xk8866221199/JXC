package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.PurchaseOrderItem;
import com.jxc.service.PurchaseOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购订单明细管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "采购订单明细管理", description = "采购订单明细管理相关接口")
@RestController
@RequestMapping("/purchase/order-items")
@CrossOrigin(origins = "*")
public class PurchaseOrderItemController {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderItemController.class);
    
    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;
    
    /**
     * 分页查询采购订单明细列表
     */
    @Operation(summary = "分页查询采购订单明细列表", description = "分页查询采购订单明细列表，支持订单ID和商品ID筛选")
    @GetMapping
    public Result<IPage<PurchaseOrderItem>> getItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long productId) {
        try {
            Page<PurchaseOrderItem> pageRequest = new Page<>(page, size);
            IPage<PurchaseOrderItem> result = purchaseOrderItemService.getItemsWithPagination(pageRequest, orderId, productId);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询采购订单明细列表失败: {}", e.getMessage(), e);
            return Result.error("查询采购订单明细列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据订单ID获取所有采购订单明细
     */
    @Operation(summary = "根据订单ID获取所有采购订单明细", description = "根据订单ID获取所有采购订单明细")
    @GetMapping("/order/{orderId}")
    public Result<List<PurchaseOrderItem>> getItemsByOrderId(@PathVariable Long orderId) {
        try {
            List<PurchaseOrderItem> items = purchaseOrderItemService.getItemsByOrderId(orderId);
            return Result.success(items, "查询成功");
        } catch (Exception e) {
            logger.error("根据订单ID查询采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("根据订单ID查询采购订单明细失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取采购订单明细详情
     */
    @Operation(summary = "根据ID获取采购订单明细详情", description = "根据ID获取采购订单明细详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrderItem> getItemById(@PathVariable Long id) {
        try {
            PurchaseOrderItem item = purchaseOrderItemService.getItemById(id);
            if (item == null) {
                return Result.error("采购订单明细不存在");
            }
            return Result.success(item, "查询成功");
        } catch (Exception e) {
            logger.error("查询采购订单明细详情失败: {}", e.getMessage(), e);
            return Result.error("查询采购订单明细详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建采购订单明细
     */
    @Operation(summary = "创建采购订单明细", description = "创建采购订单明细")
    @PostMapping
    public Result<Boolean> createItem(@RequestBody PurchaseOrderItem item) {
        try {
            boolean result = purchaseOrderItemService.createItem(item);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("创建采购订单明细失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量创建采购订单明细
     */
    @Operation(summary = "批量创建采购订单明细", description = "批量创建采购订单明细")
    @PostMapping("/batch")
    public Result<Boolean> createItems(@RequestBody List<PurchaseOrderItem> items) {
        try {
            boolean result = purchaseOrderItemService.createItems(items);
            return Result.success(result, result ? "批量创建成功" : "批量创建失败");
        } catch (Exception e) {
            logger.error("批量创建采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("批量创建采购订单明细失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新采购订单明细
     */
    @Operation(summary = "更新采购订单明细", description = "更新采购订单明细")
    @PutMapping("/{id}")
    public Result<Boolean> updateItem(@PathVariable Long id, @RequestBody PurchaseOrderItem item) {
        try {
            item.setId(id);
            boolean result = purchaseOrderItemService.updateItem(item);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("更新采购订单明细失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除采购订单明细
     */
    @Operation(summary = "删除采购订单明细", description = "删除采购订单明细")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteItem(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderItemService.deleteItem(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("删除采购订单明细失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据订单ID删除所有采购订单明细
     */
    @Operation(summary = "根据订单ID删除所有采购订单明细", description = "根据订单ID删除所有采购订单明细")
    @DeleteMapping("/order/{orderId}")
    public Result<Boolean> deleteItemsByOrderId(@PathVariable Long orderId) {
        try {
            boolean result = purchaseOrderItemService.deleteItemsByOrderId(orderId);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("根据订单ID删除采购订单明细失败: {}", e.getMessage(), e);
            return Result.error("根据订单ID删除采购订单明细失败：" + e.getMessage());
        }
    }
}