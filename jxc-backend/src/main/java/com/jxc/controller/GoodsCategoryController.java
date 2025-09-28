package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.GoodsCategory;
import com.jxc.service.GoodsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品分类管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "商品分类管理", description = "商品分类管理相关接口")
@RestController
@RequestMapping("/goods/categories")
@CrossOrigin(origins = "*")
public class GoodsCategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(GoodsCategoryController.class);
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    /**
     * 分页查询商品分类列表
     */
    @Operation(summary = "分页查询商品分类列表", description = "分页查询商品分类列表，支持搜索和状态筛选")
    @GetMapping
    public Result<IPage<GoodsCategory>> getCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        try {
            Page<GoodsCategory> pageRequest = new Page<>(page, size);
            IPage<GoodsCategory> result = goodsCategoryService.getCategoriesWithPagination(pageRequest, keyword, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品分类列表失败: {}", e.getMessage(), e);
            return Result.error("查询商品分类列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取商品分类详情
     */
    @Operation(summary = "根据ID获取商品分类详情", description = "根据ID获取商品分类详情")
    @GetMapping("/{id}")
    public Result<GoodsCategory> getCategoryById(@PathVariable Long id) {
        try {
            GoodsCategory category = goodsCategoryService.getCategoryById(id);
            if (category == null || category.getDeleted() == 1) {
                return Result.error("商品分类不存在");
            }
            return Result.success(category, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品分类详情失败: {}", e.getMessage(), e);
            return Result.error("查询商品分类详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建商品分类
     */
    @Operation(summary = "创建商品分类", description = "创建商品分类")
    @PostMapping
    public Result<Boolean> createCategory(@RequestBody GoodsCategory category) {
        try {
            boolean result = goodsCategoryService.createCategory(category);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建商品分类失败: {}", e.getMessage(), e);
            return Result.error("创建商品分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新商品分类
     */
    @Operation(summary = "更新商品分类", description = "更新商品分类")
    @PutMapping("/{id}")
    public Result<Boolean> updateCategory(@PathVariable Long id, @RequestBody GoodsCategory category) {
        try {
            category.setId(id);
            boolean result = goodsCategoryService.updateCategory(category);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新商品分类失败: {}", e.getMessage(), e);
            return Result.error("更新商品分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除商品分类
     */
    @Operation(summary = "删除商品分类", description = "删除商品分类")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        try {
            boolean result = goodsCategoryService.deleteCategory(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (RuntimeException e) {
            logger.error("删除商品分类失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            logger.error("删除商品分类失败: {}", e.getMessage(), e);
            return Result.error("删除商品分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新商品分类状态
     */
    @Operation(summary = "更新商品分类状态", description = "更新商品分类状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateCategoryStatus(@PathVariable Long id, @RequestBody GoodsCategory category) {
        try {
            boolean result = goodsCategoryService.updateCategoryStatus(id, category.getStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新商品分类状态失败: {}", e.getMessage(), e);
            return Result.error("更新商品分类状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有可用商品分类列表（不分页）
     */
    @Operation(summary = "获取所有可用商品分类列表", description = "获取所有可用商品分类列表（不分页）")
    @GetMapping("/all")
    public Result<IPage<GoodsCategory>> getAllCategories() {
        try {
            Page<GoodsCategory> page = new Page<>(1, 1000); // 设置较大页数以获取所有数据
            IPage<GoodsCategory> result = goodsCategoryService.getCategoriesWithPagination(page, null, 1); // 只查询启用状态的分类
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询所有商品分类失败: {}", e.getMessage(), e);
            return Result.error("查询所有商品分类失败：" + e.getMessage());
        }
    }
}