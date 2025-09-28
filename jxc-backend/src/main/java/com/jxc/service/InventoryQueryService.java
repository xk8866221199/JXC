package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.GoodsInfo;

/**
 * 库存查询服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface InventoryQueryService {
    
    /**
     * 分页查询库存列表
     * 
     * @param page 分页对象
     * @param productCode 商品编码
     * @param productName 商品名称
     * @param categoryId 分类ID
     * @param minStock 最小库存
     * @param maxStock 最大库存
     * @param status 状态
     * @return 库存分页结果
     */
    IPage<GoodsInfo> getInventoryWithPagination(Page<GoodsInfo> page, String productCode, String productName, Long categoryId, Integer minStock, Integer maxStock, Integer status);
    
    /**
     * 根据商品ID获取库存详情
     * 
     * @param productId 商品ID
     * @return 库存信息
     */
    GoodsInfo getInventoryById(Long productId);
}