package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.SysRole;

/**
 * 角色服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface RoleService extends IService<SysRole> {
    
    /**
     * 分页查询角色列表
     * 
     * @param page 分页对象
     * @param keyword 搜索关键字
     * @param status 角色状态
     * @return 角色分页结果
     */
    IPage<SysRole> getRolesWithPagination(Page<SysRole> page, String keyword, Integer status);
    
    /**
     * 根据ID获取角色详情
     * 
     * @param id 角色ID
     * @return 角色信息
     */
    SysRole getRoleById(Long id);
    
    /**
     * 创建角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(SysRole role);
    
    /**
     * 更新角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(SysRole role);
    
    /**
     * 删除角色
     * 
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long id);
    
    /**
     * 更新角色状态
     * 
     * @param id 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(Long id, Integer status);
    
    /**
     * 检查角色编码是否已存在
     * 
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean checkRoleCodeExists(String roleCode, Long excludeId);
}