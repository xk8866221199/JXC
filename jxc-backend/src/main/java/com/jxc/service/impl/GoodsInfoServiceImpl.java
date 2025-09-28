package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.GoodsInfo;
import com.jxc.repository.GoodsInfoMapper;
import com.jxc.service.GoodsInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 商品信息服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class GoodsInfoServiceImpl extends ServiceImpl<GoodsInfoMapper, GoodsInfo> implements GoodsInfoService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoServiceImpl.class);
    
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    
    @Override
    public IPage<GoodsInfo> getGoodsWithPagination(Page<GoodsInfo> page, String keyword, Long categoryId, Integer status) {
        try {
            QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("product_name", keyword)
                    .or()
                    .like("product_code", keyword));
            }
            
            if (categoryId != null) {
                queryWrapper.eq("category_id", categoryId);
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return goodsInfoMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询商品信息列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询商品信息列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public GoodsInfo getGoodsById(Long id) {
        return goodsInfoMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createGoods(GoodsInfo goods) {
        try {
            // 检查商品编码是否已存在
            if (checkGoodsCodeExists(goods.getProductCode(), null)) {
                throw new RuntimeException("商品编码已存在");
            }
            
            // 设置默认状态
            if (goods.getStatus() == null) {
                goods.setStatus(1); // 默认启用
            }
            
            // 设置默认单位
            if (goods.getUnit() == null || goods.getUnit().isEmpty()) {
                goods.setUnit("个");
            }
            
            // 设置默认价格
            if (goods.getPurchasePrice() == null) {
                goods.setPurchasePrice(BigDecimal.ZERO);
            }
            
            if (goods.getSalePrice() == null) {
                goods.setSalePrice(BigDecimal.ZERO);
            }
            
            // 设置默认库存
            if (goods.getStockQuantity() == null) {
                goods.setStockQuantity(0);
            }
            
            if (goods.getMinStock() == null) {
                goods.setMinStock(0);
            }
            
            if (goods.getMaxStock() == null) {
                goods.setMaxStock(0);
            }
            
            // 保存商品
            int result = goodsInfoMapper.insert(goods);
            logger.info("创建商品信息成功: {}", goods.getProductName());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建商品信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建商品信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGoods(GoodsInfo goods) {
        try {
            // 检查商品编码是否已存在
            if (checkGoodsCodeExists(goods.getProductCode(), goods.getId())) {
                throw new RuntimeException("商品编码已存在");
            }
            
            // 更新商品
            int result = goodsInfoMapper.updateById(goods);
            logger.info("更新商品信息成功: {}", goods.getProductName());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新商品信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新商品信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteGoods(Long id) {
        try {
            // 逻辑删除商品
            GoodsInfo goods = new GoodsInfo();
            goods.setId(id);
            goods.setDeleted(1);
            int result = goodsInfoMapper.updateById(goods);
            logger.info("删除商品信息成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除商品信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除商品信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGoodsStatus(Long id, Integer status) {
        try {
            GoodsInfo goods = new GoodsInfo();
            goods.setId(id);
            goods.setStatus(status);
            int result = goodsInfoMapper.updateById(goods);
            logger.info("更新商品信息状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新商品信息状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新商品信息状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkGoodsCodeExists(String productCode, Long excludeId) {
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_code", productCode);
        queryWrapper.eq("deleted", 0);
        
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        return goodsInfoMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public GoodsInfo getGoodsByBarcode(String barcode) {
        // 注意：数据库中没有barcode字段，这里返回null
        return null;
    }
}