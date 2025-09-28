package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.GoodsInfo;
import com.jxc.service.InventoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存查询控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "库存查询", description = "库存查询相关接口")
@RestController
@RequestMapping("/inventory/query")
@CrossOrigin(origins = "*")
public class InventoryQueryController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryQueryController.class);
    
    @Autowired
    private InventoryQueryService inventoryQueryService;
    
    /**
     * 分页查询库存列表
     */
    @Operation(summary = "分页查询库存列表", description = "分页查询库存列表，支持商品编码、商品名称、分类ID、库存范围和状态筛选")
    @GetMapping
    public Result<IPage<GoodsInfo>> getInventory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Integer status) {
        try {
            Page<GoodsInfo> pageRequest = new Page<>(page, size);
            IPage<GoodsInfo> result = inventoryQueryService.getInventoryWithPagination(pageRequest, productCode, productName, categoryId, minStock, maxStock, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存列表失败: {}", e.getMessage(), e);
            return Result.error("查询库存列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取库存详情
     */
    @Operation(summary = "根据ID获取库存详情", description = "根据ID获取库存详情")
    @GetMapping("/{id}")
    public Result<GoodsInfo> getInventoryById(@PathVariable Long id) {
        try {
            GoodsInfo inventory = inventoryQueryService.getInventoryById(id);
            if (inventory == null || inventory.getDeleted() == 1) {
                return Result.error("库存信息不存在");
            }
            return Result.success(inventory, "查询成功");
        } catch (Exception e) {
            logger.error("查询库存详情失败: {}", e.getMessage(), e);
            return Result.error("查询库存详情失败：" + e.getMessage());
        }
    }
}