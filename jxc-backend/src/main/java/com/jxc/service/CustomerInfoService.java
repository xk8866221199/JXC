package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.CustomerInfo;

/**
 * 客户信息服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface CustomerInfoService extends IService<CustomerInfo> {
    
    /**
     * 分页查询客户信息列表
     * 
     * @param page 分页对象
     * @param customerCode 客户编码
     * @param customerName 客户名称
     * @param customerType 客户类型
     * @param status 客户状态
     * @return 客户信息分页结果
     */
    IPage<CustomerInfo> getCustomersWithPagination(Page<CustomerInfo> page, String customerCode, String customerName, Integer customerType, Integer status);
    
    /**
     * 根据ID获取客户信息详情
     * 
     * @param id 客户ID
     * @return 客户信息
     */
    CustomerInfo getCustomerById(Long id);
    
    /**
     * 创建客户信息
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    boolean createCustomer(CustomerInfo customer);
    
    /**
     * 更新客户信息
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    boolean updateCustomer(CustomerInfo customer);
    
    /**
     * 删除客户信息
     * 
     * @param id 客户ID
     * @return 是否成功
     */
    boolean deleteCustomer(Long id);
    
    /**
     * 更新客户信息状态
     * 
     * @param id 客户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateCustomerStatus(Long id, Integer status);
    
    /**
     * 检查客户编码是否已存在
     * 
     * @param customerCode 客户编码
     * @param excludeId 排除的客户ID
     * @return 是否存在
     */
    boolean checkCustomerCodeExists(String customerCode, Long excludeId);
}