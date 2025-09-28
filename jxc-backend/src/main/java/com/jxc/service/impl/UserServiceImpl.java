package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.SysUser;
import com.jxc.entity.SysUserRole;
import com.jxc.repository.SysUserMapper;
import com.jxc.repository.SysUserRoleMapper;
import com.jxc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public IPage<SysUser> getUsersWithRoles(Page<SysUser> page, String keyword, Integer status) {
        // TODO: 实现带角色信息的用户查询
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("username", keyword)
                .or()
                .like("real_name", keyword));
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("created_at");
        return userMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public SysUser getUserById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user != null) {
            // 获取用户的角色ID列表
            List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(id);
            if (roleIds != null && !roleIds.isEmpty()) {
                user.setRoleIds(roleIds.toArray(new Long[0]));
            }
        }
        return user;
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createUser(SysUser user, Long[] roleIds) {
        try {
            // 检查用户名是否已存在
            if (checkUsernameExists(user.getUsername(), null)) {
                throw new RuntimeException("用户名已存在");
            }
            
            // 密码加密
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // 设置默认状态
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认启用
            }
            
            // 保存用户
            int result = userMapper.insert(user);
            
            // 保存用户角色关联关系
            if (result > 0 && roleIds != null && roleIds.length > 0) {
                saveUserRoles(user.getId(), roleIds);
            }
            
            logger.info("创建用户成功: {}", user.getUsername());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建用户失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建用户失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(SysUser user, Long[] roleIds) {
        try {
            // 检查用户名是否已存在
            if (checkUsernameExists(user.getUsername(), user.getId())) {
                throw new RuntimeException("用户名已存在");
            }
            
            // 更新用户
            int result = userMapper.updateById(user);
            
            // 更新用户角色关联关系
            if (result > 0) {
                // 先删除原有的角色关联
                userRoleMapper.deleteByUserId(user.getId());
                
                // 保存新的角色关联
                if (roleIds != null && roleIds.length > 0) {
                    saveUserRoles(user.getId(), roleIds);
                }
            }
            
            logger.info("更新用户成功: {}", user.getUsername());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新用户失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新用户失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUser(Long id) {
        try {
            // 物理删除用户
            SysUser user = new SysUser();
            user.setId(id);
            int result = userMapper.deleteUserPhysically(id);
            
            // 同时删除用户角色关联关系
            userRoleMapper.deleteByUserId(id);
            
            logger.info("删除用户成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除用户失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除用户失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resetPassword(Long id, String newPassword) {
        try {
            SysUser user = new SysUser();
            user.setId(id);
            user.setPassword(passwordEncoder.encode(newPassword));
            int result = userMapper.updateById(user);
            
            logger.info("重置用户密码成功: ID={}", id);
            return result > 0;
        } catch (Exception e) {
            logger.error("重置用户密码失败: {}", e.getMessage(), e);
            throw new RuntimeException("重置用户密码失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        try {
            SysUser user = new SysUser();
            user.setId(id);
            user.setStatus(status);
            int result = userMapper.updateById(user);
            
            logger.info("更新用户状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新用户状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新用户状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkUsernameExists(String username, Long excludeId) {
        return userMapper.countByUsername(username, excludeId) > 0;
    }
    
    /**
     * 保存用户角色关联关系
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     */
    private void saveUserRoles(Long userId, Long[] roleIds) {
        for (Long roleId : roleIds) {
            if (roleId != null) {
                SysUserRole userRole = new SysUserRole(userId, roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
}