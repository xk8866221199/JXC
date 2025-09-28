package com.jxc.service.impl;

import com.jxc.entity.GoodsInfo;
import com.jxc.entity.InventoryTransaction;
import com.jxc.repository.GoodsInfoMapper;
import com.jxc.repository.InventoryTransactionMapper;
import com.jxc.service.InventoryCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存盘点服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class InventoryCheckServiceImpl implements InventoryCheckService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryCheckServiceImpl.class);
    
    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;
    
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean startCheck(List<InventoryTransaction> checkTransactions) {
        try {
            // 保存盘点流水记录
            for (InventoryTransaction transaction : checkTransactions) {
                // 生成流水单号
                if (transaction.getTransactionNo() == null || transaction.getTransactionNo().isEmpty()) {
                    transaction.setTransactionNo("CHECK" + System.currentTimeMillis());
                }
                
                // 设置交易时间
                if (transaction.getTransactionDate() == null) {
                    transaction.setTransactionDate(LocalDateTime.now());
                }
                
                // 设置流水类型为盘点
                transaction.setTransactionType(4);
                
                // 设置来源类型为盘点
                transaction.setSourceType(4);
                
                // 保存盘点流水
                inventoryTransactionMapper.insert(transaction);
            }
            
            logger.info("开始盘点操作成功，共{}条记录", checkTransactions.size());
            return true;
        } catch (Exception e) {
            logger.error("开始盘点操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("开始盘点操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean completeCheck(List<InventoryTransaction> checkTransactions) {
        try {
            for (InventoryTransaction transaction : checkTransactions) {
                // 生成流水单号
                if (transaction.getTransactionNo() == null || transaction.getTransactionNo().isEmpty()) {
                    transaction.setTransactionNo("CHECK" + System.currentTimeMillis());
                }
                
                // 设置交易时间
                if (transaction.getTransactionDate() == null) {
                    transaction.setTransactionDate(LocalDateTime.now());
                }
                
                // 设置流水类型为盘点
                transaction.setTransactionType(4);
                
                // 设置来源类型为盘点
                transaction.setSourceType(4);
                
                // 获取商品当前库存
                GoodsInfo product = goodsInfoMapper.selectById(transaction.getProductId());
                if (product == null) {
                    throw new RuntimeException("商品不存在: " + transaction.getProductId());
                }
                
                // 记录变动前库存
                transaction.setBeforeQuantity(product.getStockQuantity());
                
                // 计算变动后库存（盘点调整）
                transaction.setAfterQuantity(transaction.getAfterQuantity());
                
                // 保存盘点流水
                inventoryTransactionMapper.insert(transaction);
                
                // 更新商品库存
                product.setStockQuantity(transaction.getAfterQuantity());
                goodsInfoMapper.updateById(product);
            }
            
            logger.info("完成盘点操作成功，共{}条记录", checkTransactions.size());
            return true;
        } catch (Exception e) {
            logger.error("完成盘点操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("完成盘点操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelCheck(Long checkId) {
        try {
            // 删除盘点记录
            inventoryTransactionMapper.deleteById(checkId);
            
            logger.info("取消盘点操作成功: ID={}", checkId);
            return true;
        } catch (Exception e) {
            logger.error("取消盘点操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("取消盘点操作失败: " + e.getMessage());
        }
    }
}