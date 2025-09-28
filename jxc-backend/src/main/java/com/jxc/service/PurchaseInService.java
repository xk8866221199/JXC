package com.jxc.service;

import com.jxc.entity.InventoryTransaction;
import com.jxc.entity.PurchaseOrder;
import com.jxc.entity.PurchaseOrderItem;

import java.util.List;

/**
 * 采购入库服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface PurchaseInService {
    
    /**
     * 采购入库
     * 
     * @param orderId 采购订单ID
     * @param items 入库明细列表
     * @return 是否成功
     */
    boolean purchaseIn(Long orderId, List<PurchaseOrderItem> items);
    
    /**
     * 部分采购入库
     * 
     * @param orderId 采购订单ID
     * @param items 入库明细列表
     * @return 是否成功
     */
    boolean partialPurchaseIn(Long orderId, List<PurchaseOrderItem> items);
    
    /**
     * 创建入库流水记录
     * 
     * @param transaction 入库流水信息
     * @return 是否成功
     */
    boolean createInTransaction(InventoryTransaction transaction);
    
    /**
     * 批量创建入库流水记录
     * 
     * @param transactions 入库流水信息列表
     * @return 是否成功
     */
    boolean createInTransactions(List<InventoryTransaction> transactions);
    
    /**
     * 更新采购订单状态
     * 
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @return 是否成功
     */
    boolean updateOrderStatus(Long orderId, Integer orderStatus);
}