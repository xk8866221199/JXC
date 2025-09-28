package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.SalesOrder;
import com.jxc.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 销售订单管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "销售订单管理", description = "销售订单管理相关接口")
@RestController
@RequestMapping("/sales/orders")
@CrossOrigin(origins = "*")
public class SalesOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderController.class);
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    /**
     * 分页查询销售订单列表
     */
    @Operation(summary = "分页查询销售订单列表", description = "分页查询销售订单列表，支持订单编号、客户ID、订单状态和日期范围筛选")
    @GetMapping
    public Result<IPage<SalesOrder>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Page<SalesOrder> pageRequest = new Page<>(page, size);
            IPage<SalesOrder> result = salesOrderService.getOrdersWithPagination(pageRequest, orderNo, customerId, orderStatus, startDate, endDate);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询销售订单列表失败: {}", e.getMessage(), e);
            return Result.error("查询销售订单列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取销售订单详情
     */
    @Operation(summary = "根据ID获取销售订单详情", description = "根据ID获取销售订单详情")
    @GetMapping("/{id}")
    public Result<SalesOrder> getOrderById(@PathVariable Long id) {
        try {
            SalesOrder order = salesOrderService.getOrderById(id);
            if (order == null || order.getDeleted() == 1) {
                return Result.error("销售订单不存在");
            }
            return Result.success(order, "查询成功");
        } catch (Exception e) {
            logger.error("查询销售订单详情失败: {}", e.getMessage(), e);
            return Result.error("查询销售订单详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建销售订单
     */
    @Operation(summary = "创建销售订单", description = "创建销售订单")
    @PostMapping
    public Result<Boolean> createOrder(@RequestBody SalesOrder order) {
        try {
            boolean result = salesOrderService.createOrder(order);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建销售订单失败: {}", e.getMessage(), e);
            return Result.error("创建销售订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新销售订单
     */
    @Operation(summary = "更新销售订单", description = "更新销售订单")
    @PutMapping("/{id}")
    public Result<Boolean> updateOrder(@PathVariable Long id, @RequestBody SalesOrder order) {
        try {
            order.setId(id);
            boolean result = salesOrderService.updateOrder(order);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新销售订单失败: {}", e.getMessage(), e);
            return Result.error("更新销售订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除销售订单
     */
    @Operation(summary = "删除销售订单", description = "删除销售订单")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrder(@PathVariable Long id) {
        try {
            boolean result = salesOrderService.deleteOrder(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除销售订单失败: {}", e.getMessage(), e);
            return Result.error("删除销售订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新销售订单状态
     */
    @Operation(summary = "更新销售订单状态", description = "更新销售订单状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long id, @RequestBody SalesOrder order) {
        try {
            boolean result = salesOrderService.updateOrderStatus(id, order.getOrderStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新销售订单状态失败: {}", e.getMessage(), e);
            return Result.error("更新销售订单状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新销售订单收款状态
     */
    @Operation(summary = "更新销售订单收款状态", description = "更新销售订单收款状态")
    @PutMapping("/{id}/payment")
    public Result<Boolean> updatePaymentStatus(@PathVariable Long id, @RequestBody SalesOrder order) {
        try {
            boolean result = salesOrderService.updatePaymentStatus(id, order.getPaymentStatus());
            return Result.success(result, result ? "收款状态更新成功" : "收款状态更新失败");
        } catch (Exception e) {
            logger.error("更新销售订单收款状态失败: {}", e.getMessage(), e);
            return Result.error("更新销售订单收款状态失败：" + e.getMessage());
        }
    }
}