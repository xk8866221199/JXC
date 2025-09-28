package com.jxc.service.impl;

import com.jxc.entity.GoodsInfo;
import com.jxc.entity.InventoryTransaction;
import com.jxc.entity.PurchaseOrder;
import com.jxc.entity.PurchaseOrderItem;
import com.jxc.repository.GoodsInfoMapper;
import com.jxc.repository.InventoryTransactionMapper;
import com.jxc.repository.PurchaseOrderItemMapper;
import com.jxc.repository.PurchaseOrderMapper;
import com.jxc.service.PurchaseInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购入库服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class PurchaseInServiceImpl implements PurchaseInService {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseInServiceImpl.class);
    
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;
    
    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;
    
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean purchaseIn(Long orderId, List<PurchaseOrderItem> items) {
        try {
            // 获取采购订单
            PurchaseOrder order = purchaseOrderMapper.selectById(orderId);
            if (order == null) {
                throw new RuntimeException("采购订单不存在");
            }
            
            // 检查订单状态是否为已审核或采购中
            if (order.getOrderStatus() != 2 && order.getOrderStatus() != 3) {
                throw new RuntimeException("订单状态不正确，无法进行入库操作");
            }
            
            // 处理每个入库明细
            for (PurchaseOrderItem item : items) {
                // 更新订单明细的已收货数量
                PurchaseOrderItem orderItem = purchaseOrderItemMapper.selectById(item.getId());
                if (orderItem == null) {
                    throw new RuntimeException("订单明细不存在");
                }
                
                // 计算新的已收货数量
                int newReceivedQuantity = orderItem.getReceivedQuantity() + item.getReceivedQuantity();
                orderItem.setReceivedQuantity(newReceivedQuantity);
                purchaseOrderItemMapper.updateById(orderItem);
                
                // 创建入库流水记录
                InventoryTransaction transaction = new InventoryTransaction();
                transaction.setTransactionNo("IN" + System.currentTimeMillis() + item.getId());
                transaction.setProductId(item.getProductId());
                transaction.setTransactionType(1); // 入库
                transaction.setSourceType(1); // 采购入库
                transaction.setSourceId(orderId);
                transaction.setQuantity(item.getReceivedQuantity());
                
                // 获取商品信息获取单位成本
                GoodsInfo product = goodsInfoMapper.selectById(item.getProductId());
                if (product != null) {
                    transaction.setUnitCost(product.getPurchasePrice());
                    BigDecimal totalCost = product.getPurchasePrice().multiply(new BigDecimal(item.getReceivedQuantity()));
                    transaction.setTotalCost(totalCost);
                }
                
                transaction.setBeforeQuantity(product != null ? product.getStockQuantity() : 0);
                int afterQuantity = (product != null ? product.getStockQuantity() : 0) + item.getReceivedQuantity();
                transaction.setAfterQuantity(afterQuantity);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setRemark("采购入库，订单号：" + order.getOrderNo());
                inventoryTransactionMapper.insert(transaction);
                
                // 更新商品库存
                if (product != null) {
                    product.setStockQuantity(afterQuantity);
                    goodsInfoMapper.updateById(product);
                }
            }
            
            // 更新订单状态
            // 检查是否全部到货
            boolean allReceived = true;
            List<PurchaseOrderItem> orderItems = getItemsByOrderId(orderId);
            for (PurchaseOrderItem orderItem : orderItems) {
                if (orderItem.getReceivedQuantity() < orderItem.getQuantity()) {
                    allReceived = false;
                    break;
                }
            }
            
            // 更新订单状态
            PurchaseOrder updateOrder = new PurchaseOrder();
            updateOrder.setId(orderId);
            updateOrder.setOrderStatus(allReceived ? 5 : 4); // 5-全部到货, 4-部分到货
            purchaseOrderMapper.updateById(updateOrder);
            
            logger.info("采购入库操作成功: 订单ID={}", orderId);
            return true;
        } catch (Exception e) {
            logger.error("采购入库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("采购入库操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean partialPurchaseIn(Long orderId, List<PurchaseOrderItem> items) {
        try {
            // 复用采购入库逻辑
            return purchaseIn(orderId, items);
        } catch (Exception e) {
            logger.error("部分采购入库操作失败: {}", e.getMessage(), e);
            throw new RuntimeException("部分采购入库操作失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createInTransaction(InventoryTransaction transaction) {
        try {
            // 设置默认值
            if (transaction.getTransactionNo() == null || transaction.getTransactionNo().isEmpty()) {
                transaction.setTransactionNo("IN" + System.currentTimeMillis());
            }
            
            if (transaction.getTransactionDate() == null) {
                transaction.setTransactionDate(LocalDateTime.now());
            }
            
            // 设置流水类型为入库
            transaction.setTransactionType(1);
            
            // 设置来源类型为采购入库
            transaction.setSourceType(1);
            
            // 保存入库流水记录
            int result = inventoryTransactionMapper.insert(transaction);
            
            // 更新商品库存
            if (transaction.getProductId() != null && transaction.getQuantity() != null) {
                GoodsInfo product = goodsInfoMapper.selectById(transaction.getProductId());
                if (product != null) {
                    int afterQuantity = product.getStockQuantity() + transaction.getQuantity();
                    product.setStockQuantity(afterQuantity);
                    goodsInfoMapper.updateById(product);
                    
                    // 更新流水中的前后库存量
                    transaction.setBeforeQuantity(product.getStockQuantity() - transaction.getQuantity());
                    transaction.setAfterQuantity(afterQuantity);
                    inventoryTransactionMapper.updateById(transaction);
                }
            }
            
            logger.info("创建入库流水记录成功: 流水单号={}", transaction.getTransactionNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建入库流水记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建入库流水记录失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createInTransactions(List<InventoryTransaction> transactions) {
        try {
            for (InventoryTransaction transaction : transactions) {
                createInTransaction(transaction);
            }
            return true;
        } catch (Exception e) {
            logger.error("批量创建入库流水记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量创建入库流水记录失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrderStatus(Long orderId, Integer orderStatus) {
        try {
            PurchaseOrder order = new PurchaseOrder();
            order.setId(orderId);
            order.setOrderStatus(orderStatus);
            int result = purchaseOrderMapper.updateById(order);
            logger.info("更新采购订单状态成功: 订单ID={}, 状态={}", orderId, orderStatus);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新采购订单状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新采购订单状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据订单ID获取所有采购订单明细
     */
    private List<PurchaseOrderItem> getItemsByOrderId(Long orderId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<PurchaseOrderItem> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return purchaseOrderItemMapper.selectList(queryWrapper);
    }
}