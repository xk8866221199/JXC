package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.GoodsInfo;
import com.jxc.repository.GoodsInfoMapper;
import com.jxc.service.InventoryQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存查询服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class InventoryQueryServiceImpl implements InventoryQueryService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryQueryServiceImpl.class);
    
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    
    @Override
    public IPage<GoodsInfo> getInventoryWithPagination(Page<GoodsInfo> page, String productCode, String productName, Long categoryId, Integer minStock, Integer maxStock, Integer status) {
        try {
            QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (productCode != null && !productCode.isEmpty()) {
                queryWrapper.like("product_code", productCode);
            }
            
            if (productName != null && !productName.isEmpty()) {
                queryWrapper.like("product_name", productName);
            }
            
            if (categoryId != null) {
                queryWrapper.eq("category_id", categoryId);
            }
            
            if (minStock != null) {
                queryWrapper.ge("stock_quantity", minStock);
            }
            
            if (maxStock != null) {
                queryWrapper.le("stock_quantity", maxStock);
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return goodsInfoMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询库存列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询库存列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public GoodsInfo getInventoryById(Long productId) {
        return goodsInfoMapper.selectById(productId);
    }
}