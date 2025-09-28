package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.GoodsInfo;
import com.jxc.service.GoodsInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品信息管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "商品信息管理", description = "商品信息管理相关接口")
@RestController
@RequestMapping("/goods/info")
@CrossOrigin(origins = "*")
public class GoodsInfoController {
    
    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoController.class);
    
    @Autowired
    private GoodsInfoService goodsInfoService;
    
    /**
     * 分页查询商品信息列表
     */
    @Operation(summary = "分页查询商品信息列表", description = "分页查询商品信息列表，支持搜索、分类和状态筛选")
    @GetMapping
    public Result<IPage<GoodsInfo>> getGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        try {
            Page<GoodsInfo> pageRequest = new Page<>(page, size);
            IPage<GoodsInfo> result = goodsInfoService.getGoodsWithPagination(pageRequest, keyword, categoryId, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品信息列表失败: {}", e.getMessage(), e);
            return Result.error("查询商品信息列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取商品信息详情
     */
    @Operation(summary = "根据ID获取商品信息详情", description = "根据ID获取商品信息详情")
    @GetMapping("/{id}")
    public Result<GoodsInfo> getGoodsById(@PathVariable Long id) {
        try {
            GoodsInfo goods = goodsInfoService.getGoodsById(id);
            if (goods == null || goods.getDeleted() == 1) {
                return Result.error("商品信息不存在");
            }
            return Result.success(goods, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品信息详情失败: {}", e.getMessage(), e);
            return Result.error("查询商品信息详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据条码获取商品信息
     */
    @Operation(summary = "根据条码获取商品信息", description = "根据条码获取商品信息")
    @GetMapping("/barcode/{barcode}")
    public Result<GoodsInfo> getGoodsByBarcode(@PathVariable String barcode) {
        try {
            GoodsInfo goods = goodsInfoService.getGoodsByBarcode(barcode);
            if (goods == null) {
                return Result.error("商品信息不存在或已下架");
            }
            return Result.success(goods, "查询成功");
        } catch (Exception e) {
            logger.error("查询商品信息失败: {}", e.getMessage(), e);
            return Result.error("查询商品信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建商品信息
     */
    @Operation(summary = "创建商品信息", description = "创建商品信息")
    @PostMapping
    public Result<Boolean> createGoods(@RequestBody GoodsInfo goods) {
        try {
            boolean result = goodsInfoService.createGoods(goods);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建商品信息失败: {}", e.getMessage(), e);
            return Result.error("创建商品信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新商品信息
     */
    @Operation(summary = "更新商品信息", description = "更新商品信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateGoods(@PathVariable Long id, @RequestBody GoodsInfo goods) {
        try {
            goods.setId(id);
            boolean result = goodsInfoService.updateGoods(goods);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新商品信息失败: {}", e.getMessage(), e);
            return Result.error("更新商品信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除商品信息
     */
    @Operation(summary = "删除商品信息", description = "删除商品信息")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteGoods(@PathVariable Long id) {
        try {
            boolean result = goodsInfoService.deleteGoods(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除商品信息失败: {}", e.getMessage(), e);
            return Result.error("删除商品信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新商品信息状态
     */
    @Operation(summary = "更新商品信息状态", description = "更新商品信息状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateGoodsStatus(@PathVariable Long id, @RequestBody GoodsInfo goods) {
        try {
            boolean result = goodsInfoService.updateGoodsStatus(id, goods.getStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新商品信息状态失败: {}", e.getMessage(), e);
            return Result.error("更新商品信息状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有可用商品信息列表（不分页）
     */
    @Operation(summary = "获取所有可用商品信息列表", description = "获取所有可用商品信息列表（不分页）")
    @GetMapping("/all")
    public Result<IPage<GoodsInfo>> getAllGoods() {
        try {
            Page<GoodsInfo> page = new Page<>(1, 1000); // 设置较大页数以获取所有数据
            IPage<GoodsInfo> result = goodsInfoService.getGoodsWithPagination(page, null, null, 1); // 只查询上架的商品
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询所有商品信息失败: {}", e.getMessage(), e);
            return Result.error("查询所有商品信息失败：" + e.getMessage());
        }
    }
}