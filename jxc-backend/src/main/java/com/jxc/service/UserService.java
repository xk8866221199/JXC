package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.SysUser;

/**
 * 用户服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface UserService {
    
    /**
     * 分页查询用户列表
     * 
     * @param page 分页对象
     * @param keyword 搜索关键字
     * @param status 用户状态
     * @return 用户分页结果
     */
    IPage<SysUser> getUsersWithRoles(Page<SysUser> page, String keyword, Integer status);
    
    /**
     * 根据ID获取用户详情
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    SysUser getUserById(Long id);
    
    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean createUser(SysUser user, Long[] roleIds);
    
    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean updateUser(SysUser user, Long[] roleIds);
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);
    
    /**
     * 重置用户密码
     * 
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long id, String newPassword);
    
    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long id, Integer status);
    
    /**
     * 检查用户名是否已存在
     * 
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkUsernameExists(String username, Long excludeId);
}