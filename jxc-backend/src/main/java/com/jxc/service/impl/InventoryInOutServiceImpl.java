package com.jxc.service.impl;

import com.jxc.entity.GoodsInfo;
import com.jxc.entity.InventoryTransaction;
import com.jxc.repository.GoodsInfoMapper;
import com.jxc.repository.InventoryTransactionMapper;
import com.jxc.service.InventoryInOutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出入库管理服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class InventoryInOutServiceImpl implements InventoryInOutService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryInOutServiceImpl.class);
    
    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;
    
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean stockIn(InventoryTransaction transaction) {
        try {
            // 生成流水单号
            if (transaction.getTransactionNo() == null || transaction.getTransactionNo().isEmpty()) {
                transaction.setTransactionNo("IN" + System.currentTimeMillis());
            }
            
            // 设置交易时间
            if (transaction.getTransactionDate() == null) {
                transaction.setTransactionDate(LocalDateTime.now());
            }
            
            // 设置流水类型为入库
            transaction.setTransactionType(1);
            
            // 获取商品当前库存
            GoodsInfo product = goodsInfoMapper.selectById(transaction.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            
            // 记录变动前库存
            transaction.setBeforeQuantity(product.getStockQuantity());
            
            // 计算变动后库存
            int afterQuantity = product.getStockQuantity() + transaction.getQuantity();
            transaction.setAfterQuantity(afterQuantity);
            
            // 保存库存流水
            inventoryTransactionMapper.insert(transaction);
            
            // 更新商品库存
            product.setStockQuantity(afterQuantity);
            goodsInfoMapper.updateById(product);
            
            logger.info("入库操作成功: 商品ID={}, 数量={}", transaction.getProductId(), transaction.getQuantity());
            return true;
        } catch (Exception e) {
            logger.error("入库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("入库操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean stockOut(InventoryTransaction transaction) {
        try {
            // 生成流水单号
            if (transaction.getTransactionNo() == null || transaction.getTransactionNo().isEmpty()) {
                transaction.setTransactionNo("OUT" + System.currentTimeMillis());
            }
            
            // 设置交易时间
            if (transaction.getTransactionDate() == null) {
                transaction.setTransactionDate(LocalDateTime.now());
            }
            
            // 设置流水类型为出库
            transaction.setTransactionType(2);
            
            // 获取商品当前库存
            GoodsInfo product = goodsInfoMapper.selectById(transaction.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            
            // 检查库存是否充足
            if (product.getStockQuantity() < transaction.getQuantity()) {
                throw new RuntimeException("库存不足，当前库存：" + product.getStockQuantity());
            }
            
            // 记录变动前库存
            transaction.setBeforeQuantity(product.getStockQuantity());
            
            // 计算变动后库存（出库为负数）
            int afterQuantity = product.getStockQuantity() - transaction.getQuantity();
            transaction.setAfterQuantity(afterQuantity);
            
            // 保存库存流水
            inventoryTransactionMapper.insert(transaction);
            
            // 更新商品库存
            product.setStockQuantity(afterQuantity);
            goodsInfoMapper.updateById(product);
            
            logger.info("出库操作成功: 商品ID={}, 数量={}", transaction.getProductId(), transaction.getQuantity());
            return true;
        } catch (Exception e) {
            logger.error("出库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("出库操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchStockIn(List<InventoryTransaction> transactions) {
        try {
            for (InventoryTransaction transaction : transactions) {
                stockIn(transaction);
            }
            return true;
        } catch (Exception e) {
            logger.error("批量入库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量入库操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchStockOut(List<InventoryTransaction> transactions) {
        try {
            for (InventoryTransaction transaction : transactions) {
                stockOut(transaction);
            }
            return true;
        } catch (Exception e) {
            logger.error("批量出库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量出库操作失败: " + e.getMessage());
        }
    }
}