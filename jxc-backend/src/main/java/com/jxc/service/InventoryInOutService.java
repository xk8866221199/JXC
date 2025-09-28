package com.jxc.service;

import com.jxc.entity.InventoryTransaction;

import java.util.List;

/**
 * 出入库管理服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface InventoryInOutService {
    
    /**
     * 入库操作
     * 
     * @param transaction 库存流水信息
     * @return 是否成功
     */
    boolean stockIn(InventoryTransaction transaction);
    
    /**
     * 出库操作
     * 
     * @param transaction 库存流水信息
     * @return 是否成功
     */
    boolean stockOut(InventoryTransaction transaction);
    
    /**
     * 批量入库操作
     * 
     * @param transactions 库存流水信息列表
     * @return 是否成功
     */
    boolean batchStockIn(List<InventoryTransaction> transactions);
    
    /**
     * 批量出库操作
     * 
     * @param transactions 库存流水信息列表
     * @return 是否成功
     */
    boolean batchStockOut(List<InventoryTransaction> transactions);
}