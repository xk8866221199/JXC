package com.jxc.service;

import com.jxc.entity.InventoryTransaction;

import java.util.List;

/**
 * 库存盘点服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface InventoryCheckService {
    
    /**
     * 开始盘点
     * 
     * @param checkTransactions 盘点流水列表
     * @return 是否成功
     */
    boolean startCheck(List<InventoryTransaction> checkTransactions);
    
    /**
     * 完成盘点
     * 
     * @param checkTransactions 盘点流水列表
     * @return 是否成功
     */
    boolean completeCheck(List<InventoryTransaction> checkTransactions);
    
    /**
     * 取消盘点
     * 
     * @param checkId 盘点ID
     * @return 是否成功
     */
    boolean cancelCheck(Long checkId);
}