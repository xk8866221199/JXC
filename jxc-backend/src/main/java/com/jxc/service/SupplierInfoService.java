package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.SupplierInfo;

/**
 * 供应商信息服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SupplierInfoService extends IService<SupplierInfo> {
    
    /**
     * 分页查询供应商信息列表
     * 
     * @param page 分页对象
     * @param keyword 搜索关键字
     * @param status 供应商状态
     * @return 供应商信息分页结果
     */
    IPage<SupplierInfo> getSuppliersWithPagination(Page<SupplierInfo> page, String keyword, Integer status);
    
    /**
     * 根据ID获取供应商信息详情
     * 
     * @param id 供应商ID
     * @return 供应商信息
     */
    SupplierInfo getSupplierById(Long id);
    
    /**
     * 创建供应商信息
     * 
     * @param supplier 供应商信息
     * @return 是否成功
     */
    boolean createSupplier(SupplierInfo supplier);
    
    /**
     * 更新供应商信息
     * 
     * @param supplier 供应商信息
     * @return 是否成功
     */
    boolean updateSupplier(SupplierInfo supplier);
    
    /**
     * 删除供应商信息
     * 
     * @param id 供应商ID
     * @return 是否成功
     */
    boolean deleteSupplier(Long id);
    
    /**
     * 更新供应商信息状态
     * 
     * @param id 供应商ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateSupplierStatus(Long id, Integer status);
    
    /**
     * 检查供应商编码是否已存在
     * 
     * @param supplierCode 供应商编码
     * @param excludeId 排除的供应商ID
     * @return 是否存在
     */
    boolean checkSupplierCodeExists(String supplierCode, Long excludeId);
}