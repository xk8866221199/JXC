package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.PurchaseOrder;
import com.jxc.repository.PurchaseOrderMapper;
import com.jxc.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 采购订单服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);
    
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    
    @Override
    public IPage<PurchaseOrder> getOrdersWithPagination(Page<PurchaseOrder> page, String orderNo, Long supplierId, Integer orderStatus, String startDate, String endDate) {
        try {
            QueryWrapper<PurchaseOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (orderNo != null && !orderNo.isEmpty()) {
                queryWrapper.like("order_no", orderNo);
            }
            
            if (supplierId != null) {
                queryWrapper.eq("supplier_id", supplierId);
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
            return purchaseOrderMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询采购订单列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询采购订单列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public PurchaseOrder getOrderById(Long id) {
        return purchaseOrderMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrder(PurchaseOrder order) {
        try {
            // 设置默认状态
            if (order.getOrderStatus() == null) {
                order.setOrderStatus(1); // 默认待审核
            }
            
            if (order.getPaymentStatus() == null) {
                order.setPaymentStatus(1); // 默认未付款
            }
            
            // 保存采购订单
            int result = purchaseOrderMapper.insert(order);
            logger.info("创建采购订单成功: {}", order.getOrderNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建采购订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建采购订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrder(PurchaseOrder order) {
        try {
            // 更新采购订单
            int result = purchaseOrderMapper.updateById(order);
            logger.info("更新采购订单成功: {}", order.getOrderNo());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新采购订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新采购订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteOrder(Long id) {
        try {
            // 逻辑删除采购订单
            PurchaseOrder order = new PurchaseOrder();
            order.setId(id);
            order.setDeleted(1);
            int result = purchaseOrderMapper.updateById(order);
            logger.info("删除采购订单成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除采购订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除采购订单失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrderStatus(Long id, Integer orderStatus) {
        try {
            PurchaseOrder order = new PurchaseOrder();
            order.setId(id);
            order.setOrderStatus(orderStatus);
            int result = purchaseOrderMapper.updateById(order);
            logger.info("更新采购订单状态成功: ID={}, Status={}", id, orderStatus);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新采购订单状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新采购订单状态失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePaymentStatus(Long id, Integer paymentStatus) {
        try {
            PurchaseOrder order = new PurchaseOrder();
            order.setId(id);
            order.setPaymentStatus(paymentStatus);
            int result = purchaseOrderMapper.updateById(order);
            logger.info("更新采购订单付款状态成功: ID={}, Status={}", id, paymentStatus);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新采购订单付款状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新采购订单付款状态失败: " + e.getMessage());
        }
    }
}