package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.PurchaseOrder;
import com.jxc.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 采购订单管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "采购订单管理", description = "采购订单管理相关接口")
@RestController
@RequestMapping("/purchase/orders")
@CrossOrigin(origins = "*")
public class PurchaseOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    
    /**
     * 分页查询采购订单列表
     */
    @Operation(summary = "分页查询采购订单列表", description = "分页查询采购订单列表，支持订单编号、供应商ID、订单状态和日期范围筛选")
    @GetMapping
    public Result<IPage<PurchaseOrder>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Page<PurchaseOrder> pageRequest = new Page<>(page, size);
            IPage<PurchaseOrder> result = purchaseOrderService.getOrdersWithPagination(pageRequest, orderNo, supplierId, orderStatus, startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询采购订单列表失败: {}", e.getMessage(), e);
            return Result.error("查询采购订单列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取采购订单详情
     */
    @Operation(summary = "根据ID获取采购订单详情", description = "根据ID获取采购订单详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrder> getOrderById(@PathVariable Long id) {
        try {
            PurchaseOrder order = purchaseOrderService.getOrderById(id);
            if (order == null || order.getDeleted() == 1) {
                return Result.error("采购订单不存在");
            }
            return Result.success(order, "查询成功");
        } catch (Exception e) {
            logger.error("查询采购订单详情失败: {}", e.getMessage(), e);
            return Result.error("查询采购订单详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建采购订单
     */
    @Operation(summary = "创建采购订单", description = "创建采购订单")
    @PostMapping
    public Result<Boolean> createOrder(@RequestBody PurchaseOrder order) {
        try {
            boolean result = purchaseOrderService.createOrder(order);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建采购订单失败: {}", e.getMessage(), e);
            return Result.error("创建采购订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新采购订单
     */
    @Operation(summary = "更新采购订单", description = "更新采购订单")
    @PutMapping("/{id}")
    public Result<Boolean> updateOrder(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        try {
            order.setId(id);
            boolean result = purchaseOrderService.updateOrder(order);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新采购订单失败: {}", e.getMessage(), e);
            return Result.error("更新采购订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除采购订单
     */
    @Operation(summary = "删除采购订单", description = "删除采购订单")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrder(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderService.deleteOrder(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除采购订单失败: {}", e.getMessage(), e);
            return Result.error("删除采购订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新采购订单状态
     */
    @Operation(summary = "更新采购订单状态", description = "更新采购订单状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        try {
            boolean result = purchaseOrderService.updateOrderStatus(id, order.getOrderStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新采购订单状态失败: {}", e.getMessage(), e);
            return Result.error("更新采购订单状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新采购订单付款状态
     */
    @Operation(summary = "更新采购订单付款状态", description = "更新采购订单付款状态")
    @PutMapping("/{id}/payment")
    public Result<Boolean> updatePaymentStatus(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        try {
            boolean result = purchaseOrderService.updatePaymentStatus(id, order.getPaymentStatus());
            return Result.success(result, result ? "付款状态更新成功" : "付款状态更新失败");
        } catch (Exception e) {
            logger.error("更新采购订单付款状态失败: {}", e.getMessage(), e);
            return Result.error("更新采购订单付款状态失败：" + e.getMessage());
        }
    }
}