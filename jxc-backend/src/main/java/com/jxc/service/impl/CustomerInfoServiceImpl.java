package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.CustomerInfo;
import com.jxc.repository.CustomerInfoMapper;
import com.jxc.service.CustomerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户信息服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerInfoServiceImpl.class);
    
    @Autowired
    private CustomerInfoMapper customerInfoMapper;
    
    @Override
    public IPage<CustomerInfo> getCustomersWithPagination(Page<CustomerInfo> page, String customerCode, String customerName, Integer customerType, Integer status) {
        try {
            QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (customerCode != null && !customerCode.isEmpty()) {
                queryWrapper.like("customer_code", customerCode);
            }
            
            if (customerName != null && !customerName.isEmpty()) {
                queryWrapper.like("customer_name", customerName);
            }
            
            if (customerType != null) {
                queryWrapper.eq("customer_type", customerType);
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return customerInfoMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询客户信息列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询客户信息列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public CustomerInfo getCustomerById(Long id) {
        return customerInfoMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createCustomer(CustomerInfo customer) {
        try {
            // 检查客户编码是否已存在
            if (checkCustomerCodeExists(customer.getCustomerCode(), null)) {
                throw new RuntimeException("客户编码已存在");
            }
            
            // 设置默认状态
            if (customer.getStatus() == null) {
                customer.setStatus(1); // 默认启用
            }
            
            // 设置默认信用等级
            if (customer.getCreditLevel() == null) {
                customer.setCreditLevel(1); // 默认A级
            }
            
            // 设置默认客户类型
            if (customer.getCustomerType() == null) {
                customer.setCustomerType(1); // 默认个人
            }
            
            // 保存客户信息
            int result = customerInfoMapper.insert(customer);
            logger.info("创建客户信息成功: {}", customer.getCustomerName());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建客户信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建客户信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCustomer(CustomerInfo customer) {
        try {
            // 检查客户编码是否已存在
            if (checkCustomerCodeExists(customer.getCustomerCode(), customer.getId())) {
                throw new RuntimeException("客户编码已存在");
            }
            
            // 更新客户信息
            int result = customerInfoMapper.updateById(customer);
            logger.info("更新客户信息成功: {}", customer.getCustomerName());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新客户信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新客户信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCustomer(Long id) {
        try {
            // 逻辑删除客户信息
            CustomerInfo customer = new CustomerInfo();
            customer.setId(id);
            customer.setDeleted(1);
            int result = customerInfoMapper.updateById(customer);
            logger.info("删除客户信息成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除客户信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除客户信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCustomerStatus(Long id, Integer status) {
        try {
            CustomerInfo customer = new CustomerInfo();
            customer.setId(id);
            customer.setStatus(status);
            int result = customerInfoMapper.updateById(customer);
            logger.info("更新客户信息状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新客户信息状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新客户信息状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkCustomerCodeExists(String customerCode, Long excludeId) {
        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_code", customerCode);
        queryWrapper.eq("deleted", 0);
        
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        return customerInfoMapper.selectCount(queryWrapper) > 0;
    }
}