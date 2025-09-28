package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.PurchaseOrderItem;
import com.jxc.repository.PurchaseOrderItemMapper;
import com.jxc.service.PurchaseOrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购订单明细服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItem> implements PurchaseOrderItemService {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderItemServiceImpl.class);
    
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;
    
    @Override
    public IPage<PurchaseOrderItem> getItemsWithPagination(Page<PurchaseOrderItem> page, Long orderId, Long productId) {
        try {
            QueryWrapper<PurchaseOrderItem> queryWrapper = new QueryWrapper<>();
            
            if (orderId != null) {
                queryWrapper.eq("order_id", orderId);
            }
            
            if (productId != null) {
                queryWrapper.eq("product_id", productId);
            }
            
            queryWrapper.orderByDesc("created_at");
            return purchaseOrderItemMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询采购订单明细列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询采购订单明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<PurchaseOrderItem> getItemsByOrderId(Long orderId) {
        QueryWrapper<PurchaseOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return purchaseOrderItemMapper.selectList(queryWrapper);
    }
    
    @Override
    public PurchaseOrderItem getItemById(Long id) {
        return purchaseOrderItemMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createItem(PurchaseOrderItem item) {
        try {
            // 保存采购订单明细
            int result = purchaseOrderItemMapper.insert(item);
            logger.info("创建采购订单明细成功: 订单ID={}, 商品ID={}", item.getOrderId(), item.getProductId());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建采购订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建采购订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createItems(List<PurchaseOrderItem> items) {
        try {
            // 批量保存采购订单明细
            for (PurchaseOrderItem item : items) {
                purchaseOrderItemMapper.insert(item);
            }
            logger.info("批量创建采购订单明细成功，共{}条记录", items.size());
            return true;
        } catch (Exception e) {
            logger.error("批量创建采购订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量创建采购订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateItem(PurchaseOrderItem item) {
        try {
            // 更新采购订单明细
            int result = purchaseOrderItemMapper.updateById(item);
            logger.info("更新采购订单明细成功: ID={}", item.getId());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新采购订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新采购订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteItem(Long id) {
        try {
            // 删除采购订单明细
            int result = purchaseOrderItemMapper.deleteById(id);
            logger.info("删除采购订单明细成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除采购订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除采购订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteItemsByOrderId(Long orderId) {
        try {
            // 根据订单ID删除所有采购订单明细
            QueryWrapper<PurchaseOrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            int result = purchaseOrderItemMapper.delete(queryWrapper);
            logger.info("根据订单ID删除采购订单明细成功: 订单ID={}, 删除数量={}", orderId, result);
            return true;
        } catch (Exception e) {
            logger.error("根据订单ID删除采购订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("根据订单ID删除采购订单明细失败: " + e.getMessage());
        }
    }
}