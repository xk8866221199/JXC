package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.SupplierInfo;
import com.jxc.repository.SupplierInfoMapper;
import com.jxc.service.SupplierInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商信息服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements SupplierInfoService {
    
    private static final Logger logger = LoggerFactory.getLogger(SupplierInfoServiceImpl.class);
    
    @Autowired
    private SupplierInfoMapper supplierInfoMapper;
    
    @Override
    public IPage<SupplierInfo> getSuppliersWithPagination(Page<SupplierInfo> page, String keyword, Integer status) {
        try {
            QueryWrapper<SupplierInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("supplier_name", keyword)
                    .or()
                    .like("supplier_code", keyword)
                    .or()
                    .like("contact_person", keyword));
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return supplierInfoMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询供应商信息列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询供应商信息列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public SupplierInfo getSupplierById(Long id) {
        return supplierInfoMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createSupplier(SupplierInfo supplier) {
        try {
            // 检查供应商编码是否已存在
            if (checkSupplierCodeExists(supplier.getSupplierCode(), null)) {
                throw new RuntimeException("供应商编码已存在");
            }
            
            // 设置默认状态
            if (supplier.getStatus() == null) {
                supplier.setStatus(1); // 默认启用
            }
            
            // 设置默认信用等级
            if (supplier.getCreditLevel() == null) {
                supplier.setCreditLevel(1);
            }
            
            // 保存供应商
            int result = supplierInfoMapper.insert(supplier);
            logger.info("创建供应商信息成功: {}", supplier.getSupplierName());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建供应商信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建供应商信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSupplier(SupplierInfo supplier) {
        try {
            // 检查供应商编码是否已存在
            if (checkSupplierCodeExists(supplier.getSupplierCode(), supplier.getId())) {
                throw new RuntimeException("供应商编码已存在");
            }
            
            // 更新供应商
            int result = supplierInfoMapper.updateById(supplier);
            logger.info("更新供应商信息成功: {}", supplier.getSupplierName());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新供应商信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新供应商信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSupplier(Long id) {
        try {
            // 逻辑删除供应商
            SupplierInfo supplier = new SupplierInfo();
            supplier.setId(id);
            supplier.setDeleted(1);
            int result = supplierInfoMapper.updateById(supplier);
            logger.info("删除供应商信息成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除供应商信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除供应商信息失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSupplierStatus(Long id, Integer status) {
        try {
            SupplierInfo supplier = new SupplierInfo();
            supplier.setId(id);
            supplier.setStatus(status);
            int result = supplierInfoMapper.updateById(supplier);
            logger.info("更新供应商信息状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新供应商信息状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新供应商信息状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkSupplierCodeExists(String supplierCode, Long excludeId) {
        QueryWrapper<SupplierInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier_code", supplierCode);
        queryWrapper.eq("deleted", 0);
        
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        return supplierInfoMapper.selectCount(queryWrapper) > 0;
    }
}