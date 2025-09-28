package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.CustomerInfo;
import com.jxc.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户信息管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "客户信息管理", description = "客户信息管理相关接口")
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerInfoController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);
    
    @Autowired
    private CustomerInfoService customerInfoService;
    
    /**
     * 分页查询客户信息列表
     */
    @Operation(summary = "分页查询客户信息列表", description = "分页查询客户信息列表，支持客户编码、客户名称、客户类型和状态筛选")
    @GetMapping
    public Result<IPage<CustomerInfo>> getCustomers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String customerCode,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Integer customerType,
            @RequestParam(required = false) Integer status) {
        try {
            Page<CustomerInfo> pageRequest = new Page<>(page, size);
            IPage<CustomerInfo> result = customerInfoService.getCustomersWithPagination(pageRequest, customerCode, customerName, customerType, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询客户信息列表失败: {}", e.getMessage(), e);
            return Result.error("查询客户信息列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取客户信息详情
     */
    @Operation(summary = "根据ID获取客户信息详情", description = "根据ID获取客户信息详情")
    @GetMapping("/{id}")
    public Result<CustomerInfo> getCustomerById(@PathVariable Long id) {
        try {
            CustomerInfo customer = customerInfoService.getCustomerById(id);
            if (customer == null || customer.getDeleted() == 1) {
                return Result.error("客户信息不存在");
            }
            return Result.success(customer, "查询成功");
        } catch (Exception e) {
            logger.error("查询客户信息详情失败: {}", e.getMessage(), e);
            return Result.error("查询客户信息详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建客户信息
     */
    @Operation(summary = "创建客户信息", description = "创建客户信息")
    @PostMapping
    public Result<Boolean> createCustomer(@RequestBody CustomerInfo customer) {
        try {
            boolean result = customerInfoService.createCustomer(customer);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建客户信息失败: {}", e.getMessage(), e);
            return Result.error("创建客户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新客户信息
     */
    @Operation(summary = "更新客户信息", description = "更新客户信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateCustomer(@PathVariable Long id, @RequestBody CustomerInfo customer) {
        try {
            customer.setId(id);
            boolean result = customerInfoService.updateCustomer(customer);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新客户信息失败: {}", e.getMessage(), e);
            return Result.error("更新客户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除客户信息
     */
    @Operation(summary = "删除客户信息", description = "删除客户信息")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCustomer(@PathVariable Long id) {
        try {
            boolean result = customerInfoService.deleteCustomer(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除客户信息失败: {}", e.getMessage(), e);
            return Result.error("删除客户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新客户信息状态
     */
    @Operation(summary = "更新客户信息状态", description = "更新客户信息状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateCustomerStatus(@PathVariable Long id, @RequestBody CustomerInfo customer) {
        try {
            boolean result = customerInfoService.updateCustomerStatus(id, customer.getStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新客户信息状态失败: {}", e.getMessage(), e);
            return Result.error("更新客户信息状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有可用客户信息列表（不分页）
     */
    @Operation(summary = "获取所有可用客户信息列表", description = "获取所有可用客户信息列表（不分页）")
    @GetMapping("/all")
    public Result<IPage<CustomerInfo>> getAllCustomers() {
        try {
            Page<CustomerInfo> page = new Page<>(1, 1000); // 设置较大页数以获取所有数据
            IPage<CustomerInfo> result = customerInfoService.getCustomersWithPagination(page, null, null, null, 1); // 只查询启用状态的客户
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询所有客户信息失败: {}", e.getMessage(), e);
            return Result.error("查询所有客户信息失败：" + e.getMessage());
        }
    }
}