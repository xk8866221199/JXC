package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.PurchaseOrderItem;

import java.util.List;

/**
 * 采购订单明细服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface PurchaseOrderItemService extends IService<PurchaseOrderItem> {
    
    /**
     * 分页查询采购订单明细列表
     * 
     * @param page 分页对象
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 采购订单明细分页结果
     */
    IPage<PurchaseOrderItem> getItemsWithPagination(Page<PurchaseOrderItem> page, Long orderId, Long productId);
    
    /**
     * 根据订单ID获取所有采购订单明细
     * 
     * @param orderId 订单ID
     * @return 采购订单明细列表
     */
    List<PurchaseOrderItem> getItemsByOrderId(Long orderId);
    
    /**
     * 根据ID获取采购订单明细详情
     * 
     * @param id 明细ID
     * @return 采购订单明细信息
     */
    PurchaseOrderItem getItemById(Long id);
    
    /**
     * 创建采购订单明细
     * 
     * @param item 采购订单明细信息
     * @return 是否成功
     */
    boolean createItem(PurchaseOrderItem item);
    
    /**
     * 批量创建采购订单明细
     * 
     * @param items 采购订单明细信息列表
     * @return 是否成功
     */
    boolean createItems(List<PurchaseOrderItem> items);
    
    /**
     * 更新采购订单明细
     * 
     * @param item 采购订单明细信息
     * @return 是否成功
     */
    boolean updateItem(PurchaseOrderItem item);
    
    /**
     * 删除采购订单明细
     * 
     * @param id 明细ID
     * @return 是否成功
     */
    boolean deleteItem(Long id);
    
    /**
     * 根据订单ID删除所有采购订单明细
     * 
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean deleteItemsByOrderId(Long orderId);
}