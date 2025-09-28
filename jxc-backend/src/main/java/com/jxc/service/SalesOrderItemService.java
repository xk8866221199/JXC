package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.SalesOrderItem;

import java.util.List;

/**
 * 销售订单明细服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SalesOrderItemService extends IService<SalesOrderItem> {
    
    /**
     * 分页查询销售订单明细列表
     * 
     * @param page 分页对象
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 销售订单明细分页结果
     */
    IPage<SalesOrderItem> getItemsWithPagination(Page<SalesOrderItem> page, Long orderId, Long productId);
    
    /**
     * 根据订单ID获取所有销售订单明细
     * 
     * @param orderId 订单ID
     * @return 销售订单明细列表
     */
    List<SalesOrderItem> getItemsByOrderId(Long orderId);
    
    /**
     * 根据ID获取销售订单明细详情
     * 
     * @param id 明细ID
     * @return 销售订单明细信息
     */
    SalesOrderItem getItemById(Long id);
    
    /**
     * 创建销售订单明细
     * 
     * @param item 销售订单明细信息
     * @return 是否成功
     */
    boolean createItem(SalesOrderItem item);
    
    /**
     * 批量创建销售订单明细
     * 
     * @param items 销售订单明细信息列表
     * @return 是否成功
     */
    boolean createItems(List<SalesOrderItem> items);
    
    /**
     * 更新销售订单明细
     * 
     * @param item 销售订单明细信息
     * @return 是否成功
     */
    boolean updateItem(SalesOrderItem item);
    
    /**
     * 删除销售订单明细
     * 
     * @param id 明细ID
     * @return 是否成功
     */
    boolean deleteItem(Long id);
    
    /**
     * 根据订单ID删除所有销售订单明细
     * 
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean deleteItemsByOrderId(Long orderId);
}