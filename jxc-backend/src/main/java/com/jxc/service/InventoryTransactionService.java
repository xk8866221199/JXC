package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.InventoryTransaction;

/**
 * 库存流水服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface InventoryTransactionService extends IService<InventoryTransaction> {
    
    /**
     * 分页查询库存流水列表
     * 
     * @param page 分页对象
     * @param productId 商品ID
     * @param transactionType 流水类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 库存流水分页结果
     */
    IPage<InventoryTransaction> getTransactionsWithPagination(Page<InventoryTransaction> page, Long productId, Integer transactionType, String startDate, String endDate);
    
    /**
     * 根据ID获取库存流水详情
     * 
     * @param id 流水ID
     * @return 库存流水信息
     */
    InventoryTransaction getTransactionById(Long id);
    
    /**
     * 创建库存流水
     * 
     * @param transaction 库存流水信息
     * @return 是否成功
     */
    boolean createTransaction(InventoryTransaction transaction);
    
    /**
     * 更新库存流水
     * 
     * @param transaction 库存流水信息
     * @return 是否成功
     */
    boolean updateTransaction(InventoryTransaction transaction);
    
    /**
     * 删除库存流水
     * 
     * @param id 流水ID
     * @return 是否成功
     */
    boolean deleteTransaction(Long id);
}