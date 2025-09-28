package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.SupplierInfo;
import com.jxc.service.SupplierInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商信息管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "供应商信息管理", description = "供应商信息管理相关接口")
@RestController
@RequestMapping("/suppliers")
@CrossOrigin(origins = "*")
public class SupplierInfoController {
    
    private static final Logger logger = LoggerFactory.getLogger(SupplierInfoController.class);
    
    @Autowired
    private SupplierInfoService supplierInfoService;
    
    /**
     * 分页查询供应商信息列表
     */
    @Operation(summary = "分页查询供应商信息列表", description = "分页查询供应商信息列表，支持搜索和状态筛选")
    @GetMapping
    public Result<IPage<SupplierInfo>> getSuppliers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        try {
            Page<SupplierInfo> pageRequest = new Page<>(page, size);
            IPage<SupplierInfo> result = supplierInfoService.getSuppliersWithPagination(pageRequest, keyword, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询供应商信息列表失败: {}", e.getMessage(), e);
            return Result.error("查询供应商信息列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取供应商信息详情
     */
    @Operation(summary = "根据ID获取供应商信息详情", description = "根据ID获取供应商信息详情")
    @GetMapping("/{id}")
    public Result<SupplierInfo> getSupplierById(@PathVariable Long id) {
        try {
            SupplierInfo supplier = supplierInfoService.getSupplierById(id);
            if (supplier == null || supplier.getDeleted() == 1) {
                return Result.error("供应商信息不存在");
            }
            return Result.success(supplier, "查询成功");
        } catch (Exception e) {
            logger.error("查询供应商信息详情失败: {}", e.getMessage(), e);
            return Result.error("查询供应商信息详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建供应商信息
     */
    @Operation(summary = "创建供应商信息", description = "创建供应商信息")
    @PostMapping
    public Result<Boolean> createSupplier(@RequestBody SupplierInfo supplier) {
        try {
            boolean result = supplierInfoService.createSupplier(supplier);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建供应商信息失败: {}", e.getMessage(), e);
            return Result.error("创建供应商信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新供应商信息
     */
    @Operation(summary = "更新供应商信息", description = "更新供应商信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateSupplier(@PathVariable Long id, @RequestBody SupplierInfo supplier) {
        try {
            supplier.setId(id);
            boolean result = supplierInfoService.updateSupplier(supplier);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新供应商信息失败: {}", e.getMessage(), e);
            return Result.error("更新供应商信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除供应商信息
     */
    @Operation(summary = "删除供应商信息", description = "删除供应商信息")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSupplier(@PathVariable Long id) {
        try {
            boolean result = supplierInfoService.deleteSupplier(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除供应商信息失败: {}", e.getMessage(), e);
            return Result.error("删除供应商信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新供应商信息状态
     */
    @Operation(summary = "更新供应商信息状态", description = "更新供应商信息状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateSupplierStatus(@PathVariable Long id, @RequestBody SupplierInfo supplier) {
        try {
            boolean result = supplierInfoService.updateSupplierStatus(id, supplier.getStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新供应商信息状态失败: {}", e.getMessage(), e);
            return Result.error("更新供应商信息状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有可用供应商信息列表（不分页）
     */
    @Operation(summary = "获取所有可用供应商信息列表", description = "获取所有可用供应商信息列表（不分页）")
    @GetMapping("/all")
    public Result<IPage<SupplierInfo>> getAllSuppliers() {
        try {
            Page<SupplierInfo> page = new Page<>(1, 1000); // 设置较大页数以获取所有数据
            IPage<SupplierInfo> result = supplierInfoService.getSuppliersWithPagination(page, null, 1); // 只查询启用状态的供应商
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询所有供应商信息失败: {}", e.getMessage(), e);
            return Result.error("查询所有供应商信息失败：" + e.getMessage());
        }
    }
}