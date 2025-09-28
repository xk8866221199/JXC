package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.SalesOrder;

/**
 * 销售订单服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SalesOrderService extends IService<SalesOrder> {
    
    /**
     * 分页查询销售订单列表
     * 
     * @param page 分页对象
     * @param orderNo 订单编号
     * @param customerId 客户ID
     * @param orderStatus 订单状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售订单分页结果
     */
    IPage<SalesOrder> getOrdersWithPagination(Page<SalesOrder> page, String orderNo, Long customerId, Integer orderStatus, String startDate, String endDate);
    
    /**
     * 根据ID获取销售订单详情
     * 
     * @param id 订单ID
     * @return 销售订单信息
     */
    SalesOrder getOrderById(Long id);
    
    /**
     * 创建销售订单
     * 
     * @param order 销售订单信息
     * @return 是否成功
     */
    boolean createOrder(SalesOrder order);
    
    /**
     * 更新销售订单
     * 
     * @param order 销售订单信息
     * @return 是否成功
     */
    boolean updateOrder(SalesOrder order);
    
    /**
     * 删除销售订单
     * 
     * @param id 订单ID
     * @return 是否成功
     */
    boolean deleteOrder(Long id);
    
    /**
     * 更新销售订单状态
     * 
     * @param id 订单ID
     * @param orderStatus 订单状态
     * @return 是否成功
     */
    boolean updateOrderStatus(Long id, Integer orderStatus);
    
    /**
     * 更新销售订单收款状态
     * 
     * @param id 订单ID
     * @param paymentStatus 收款状态
     * @return 是否成功
     */
    boolean updatePaymentStatus(Long id, Integer paymentStatus);
}