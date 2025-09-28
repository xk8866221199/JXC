package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.SysRole;
import com.jxc.repository.SysRoleMapper;
import com.jxc.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements RoleService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Override
    public IPage<SysRole> getRolesWithPagination(Page<SysRole> page, String keyword, Integer status) {
        try {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("role_name", keyword)
                    .or()
                    .like("role_code", keyword)
                    .or()
                    .like("description", keyword));
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return roleMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询角色列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询角色列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public SysRole getRoleById(Long id) {
        return roleMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createRole(SysRole role) {
        try {
            // 检查角色编码是否已存在
            if (checkRoleCodeExists(role.getRoleCode(), null)) {
                throw new RuntimeException("角色编码已存在");
            }
            
            // 设置默认状态
            if (role.getStatus() == null) {
                role.setStatus(1); // 默认启用
            }
            
            // 保存角色
            int result = roleMapper.insert(role);
            logger.info("创建角色成功: {}", role.getRoleName());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建角色失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(SysRole role) {
        try {
            // 检查角色编码是否已存在
            if (checkRoleCodeExists(role.getRoleCode(), role.getId())) {
                throw new RuntimeException("角色编码已存在");
            }
            
            // 更新角色
            int result = roleMapper.updateById(role);
            logger.info("更新角色成功: {}", role.getRoleName());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新角色失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRole(Long id) {
        try {
            // 逻辑删除角色
            SysRole role = new SysRole();
            role.setId(id);
            role.setDeleted(1);
            int result = roleMapper.updateById(role);
            logger.info("删除角色成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除角色失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleStatus(Long id, Integer status) {
        try {
            SysRole role = new SysRole();
            role.setId(id);
            role.setStatus(status);
            int result = roleMapper.updateById(role);
            logger.info("更新角色状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新角色状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新角色状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkRoleCodeExists(String roleCode, Long excludeId) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", roleCode);
        queryWrapper.eq("deleted", 0);
        
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        return roleMapper.selectCount(queryWrapper) > 0;
    }
}