package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.SalesOrderItem;
import com.jxc.repository.SalesOrderItemMapper;
import com.jxc.service.SalesOrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 销售订单明细服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class SalesOrderItemServiceImpl extends ServiceImpl<SalesOrderItemMapper, SalesOrderItem> implements SalesOrderItemService {
    
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderItemServiceImpl.class);
    
    @Autowired
    private SalesOrderItemMapper salesOrderItemMapper;
    
    @Override
    public IPage<SalesOrderItem> getItemsWithPagination(Page<SalesOrderItem> page, Long orderId, Long productId) {
        try {
            QueryWrapper<SalesOrderItem> queryWrapper = new QueryWrapper<>();
            
            if (orderId != null) {
                queryWrapper.eq("order_id", orderId);
            }
            
            if (productId != null) {
                queryWrapper.eq("product_id", productId);
            }
            
            queryWrapper.orderByDesc("created_at");
            return salesOrderItemMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询销售订单明细列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询销售订单明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<SalesOrderItem> getItemsByOrderId(Long orderId) {
        QueryWrapper<SalesOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return salesOrderItemMapper.selectList(queryWrapper);
    }
    
    @Override
    public SalesOrderItem getItemById(Long id) {
        return salesOrderItemMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createItem(SalesOrderItem item) {
        try {
            // 保存销售订单明细
            int result = salesOrderItemMapper.insert(item);
            logger.info("创建销售订单明细成功: 订单ID={}, 商品ID={}", item.getOrderId(), item.getProductId());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建销售订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建销售订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createItems(List<SalesOrderItem> items) {
        try {
            // 批量保存销售订单明细
            for (SalesOrderItem item : items) {
                salesOrderItemMapper.insert(item);
            }
            logger.info("批量创建销售订单明细成功，共{}条记录", items.size());
            return true;
        } catch (Exception e) {
            logger.error("批量创建销售订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量创建销售订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateItem(SalesOrderItem item) {
        try {
            // 更新销售订单明细
            int result = salesOrderItemMapper.updateById(item);
            logger.info("更新销售订单明细成功: ID={}", item.getId());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新销售订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新销售订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteItem(Long id) {
        try {
            // 删除销售订单明细
            int result = salesOrderItemMapper.deleteById(id);
            logger.info("删除销售订单明细成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除销售订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除销售订单明细失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteItemsByOrderId(Long orderId) {
        try {
            // 根据订单ID删除所有销售订单明细
            QueryWrapper<SalesOrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            int result = salesOrderItemMapper.delete(queryWrapper);
            logger.info("根据订单ID删除销售订单明细成功: 订单ID={}, 删除数量={}", orderId, result);
            return true;
        } catch (Exception e) {
            logger.error("根据订单ID删除销售订单明细失败: {}", e.getMessage(), e);
            throw new RuntimeException("根据订单ID删除销售订单明细失败: " + e.getMessage());
        }
    }
}