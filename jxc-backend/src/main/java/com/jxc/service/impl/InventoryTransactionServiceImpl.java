package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.InventoryTransaction;
import com.jxc.repository.InventoryTransactionMapper;
import com.jxc.service.InventoryTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 库存流水服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class InventoryTransactionServiceImpl extends ServiceImpl<InventoryTransactionMapper, InventoryTransaction> implements InventoryTransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryTransactionServiceImpl.class);
    
    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;
    
    @Override
    public IPage<InventoryTransaction> getTransactionsWithPagination(Page<InventoryTransaction> page, Long productId, Integer transactionType, String startDate, String endDate) {
        try {
            QueryWrapper<InventoryTransaction> queryWrapper = new QueryWrapper<>();
            
            if (productId != null) {
                queryWrapper.eq("product_id", productId);
            }
            
            if (transactionType != null) {
                queryWrapper.eq("transaction_type", transactionType);
            }
            
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge("transaction_date", startDateTime);
            }
            
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le("transaction_date", endDateTime);
            }
            
            queryWrapper.orderByDesc("created_at");
            return inventoryTransactionMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询库存流水列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询库存流水列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public InventoryTransaction getTransactionById(Long id) {
        return inventoryTransactionMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createTransaction(InventoryTransaction transaction) {
        try {
            // 保存库存流水
            int result = inventoryTransactionMapper.insert(transaction);
            logger.info("创建库存流水成功: {}", transaction.getTransactionNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建库存流水失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建库存流水失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTransaction(InventoryTransaction transaction) {
        try {
            // 更新库存流水
            int result = inventoryTransactionMapper.updateById(transaction);
            logger.info("更新库存流水成功: {}", transaction.getTransactionNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新库存流水失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新库存流水失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTransaction(Long id) {
        try {
            // 删除库存流水
            int result = inventoryTransactionMapper.deleteById(id);
            logger.info("删除库存流水成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除库存流水失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除库存流水失败: " + e.getMessage());
        }
    }
}