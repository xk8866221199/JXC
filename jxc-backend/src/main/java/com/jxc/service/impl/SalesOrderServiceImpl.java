package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.SalesOrder;
import com.jxc.repository.SalesOrderMapper;
import com.jxc.service.SalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 销售订单服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderServiceImpl.class);
    
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    
    @Override
    public IPage<SalesOrder> getOrdersWithPagination(Page<SalesOrder> page, String orderNo, Long customerId, Integer orderStatus, String startDate, String endDate) {
        try {
            QueryWrapper<SalesOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (orderNo != null && !orderNo.isEmpty()) {
                queryWrapper.like("order_no", orderNo);
            }
            
            if (customerId != null) {
                queryWrapper.eq("customer_id", customerId);
            }
            
            if (orderStatus != null) {
                queryWrapper.eq("order_status", orderStatus);
            }
            
            if (startDate != null && !startDate.isEmpty()) {
                LocalDate startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                queryWrapper.ge("order_date", startLocalDate);
            }
            
            if (endDate != null && !endDate.isEmpty()) {
                LocalDate endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                queryWrapper.le("order_date", endLocalDate);
            }
            
            queryWrapper.orderByDesc("created_at");
            return salesOrderMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询销售订单列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询销售订单列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public SalesOrder getOrderById(Long id) {
        return salesOrderMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrder(SalesOrder order) {
        try {
            // 设置默认状态
            if (order.getOrderStatus() == null) {
                order.setOrderStatus(1); // 默认待审核
            }
            
            if (order.getPaymentStatus() == null) {
                order.setPaymentStatus(1); // 默认未收款
            }
            
            // 保存销售订单
            int result = salesOrderMapper.insert(order);
            logger.info("创建销售订单成功: {}", order.getOrderNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建销售订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建销售订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrder(SalesOrder order) {
        try {
            // 更新销售订单
            int result = salesOrderMapper.updateById(order);
            logger.info("更新销售订单成功: {}", order.getOrderNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新销售订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新销售订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteOrder(Long id) {
        try {
            // 逻辑删除销售订单
            SalesOrder order = new SalesOrder();
            order.setId(id);
            order.setDeleted(1);
            int result = salesOrderMapper.updateById(order);
            logger.info("删除销售订单成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除销售订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除销售订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrderStatus(Long id, Integer orderStatus) {
        try {
            SalesOrder order = new SalesOrder();
            order.setId(id);
            order.setOrderStatus(orderStatus);
            int result = salesOrderMapper.updateById(order);
            logger.info("更新销售订单状态成功: ID={}, Status={}", id, orderStatus);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新销售订单状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新销售订单状态失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePaymentStatus(Long id, Integer paymentStatus) {
        try {
            SalesOrder order = new SalesOrder();
            order.setId(id);
            order.setPaymentStatus(paymentStatus);
            int result = salesOrderMapper.updateById(order);
            logger.info("更新销售订单收款状态成功: ID={}, Status={}", id, paymentStatus);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新销售订单收款状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新销售订单收款状态失败: " + e.getMessage());
        }
    }
}