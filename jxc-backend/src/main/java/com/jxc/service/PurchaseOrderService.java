package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.PurchaseOrder;

/**
 * 采购订单服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    
    /**
     * 分页查询采购订单列表
     * 
     * @param page 分页对象
     * @param orderNo 订单编号
     * @param supplierId 供应商ID
     * @param orderStatus 订单状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 采购订单分页结果
     */
    IPage<PurchaseOrder> getOrdersWithPagination(Page<PurchaseOrder> page, String orderNo, Long supplierId, Integer orderStatus, String startDate, String endDate);
    
    /**
     * 根据ID获取采购订单详情
     * 
     * @param id 订单ID
     * @return 采购订单信息
     */
    PurchaseOrder getOrderById(Long id);
    
    /**
     * 创建采购订单
     * 
     * @param order 采购订单信息
     * @return 是否成功
     */
    boolean createOrder(PurchaseOrder order);
    
    /**
     * 更新采购订单
     * 
     * @param order 采购订单信息
     * @return 是否成功
     */
    boolean updateOrder(PurchaseOrder order);
    
    /**
     * 删除采购订单
     * 
     * @param id 订单ID
     * @return 是否成功
     */
    boolean deleteOrder(Long id);
    
    /**
     * 更新采购订单状态
     * 
     * @param id 订单ID
     * @param orderStatus 订单状态
     * @return 是否成功
     */
    boolean updateOrderStatus(Long id, Integer orderStatus);
    
    /**
     * 更新采购订单付款状态
     * 
     * @param id 订单ID
     * @param paymentStatus 付款状态
     * @return 是否成功
     */
    boolean updatePaymentStatus(Long id, Integer paymentStatus);
}