package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.SysRole;
import com.jxc.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
public class RoleController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 分页查询角色列表
     */
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表，支持搜索和状态筛选")
    @GetMapping
    public Result<IPage<SysRole>> getRoles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        try {
            Page<SysRole> pageRequest = new Page<>(page, size);
            IPage<SysRole> result = roleService.getRolesWithPagination(pageRequest, keyword, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询角色列表失败: {}", e.getMessage(), e);
            return Result.error("查询角色列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取角色详情
     */
    @Operation(summary = "根据ID获取角色详情", description = "根据ID获取角色详情")
    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(@PathVariable Long id) {
        try {
            SysRole role = roleService.getRoleById(id);
            if (role == null || role.getDeleted() == 1) {
                return Result.error("角色不存在");
            }
            return Result.success(role, "查询成功");
        } catch (Exception e) {
            logger.error("查询角色详情失败: {}", e.getMessage(), e);
            return Result.error("查询角色详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建角色
     */
    @Operation(summary = "创建角色", description = "创建角色")
    @PostMapping
    public Result<Boolean> createRole(@RequestBody SysRole role) {
        try {
            boolean result = roleService.createRole(role);
            return Result.success(result, result ? "创建成功" : "创建失败");
        } catch (Exception e) {
            logger.error("创建角色失败: {}", e.getMessage(), e);
            return Result.error("创建角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新角色
     */
    @Operation(summary = "更新角色", description = "更新角色")
    @PutMapping("/{id}")
    public Result<Boolean> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        try {
            role.setId(id);
            boolean result = roleService.updateRole(role);
            return Result.success(result, result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            logger.error("更新角色失败: {}", e.getMessage(), e);
            return Result.error("更新角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(@PathVariable Long id) {
        try {
            boolean result = roleService.deleteRole(id);
            return Result.success(result, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            logger.error("删除角色失败: {}", e.getMessage(), e);
            return Result.error("删除角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新角色状态
     */
    @Operation(summary = "更新角色状态", description = "更新角色状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateRoleStatus(@PathVariable Long id, @RequestBody SysRole role) {
        try {
            boolean result = roleService.updateRoleStatus(id, role.getStatus());
            return Result.success(result, result ? "状态更新成功" : "状态更新失败");
        } catch (Exception e) {
            logger.error("更新角色状态失败: {}", e.getMessage(), e);
            return Result.error("更新角色状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有可用角色列表（不分页）
     */
    @Operation(summary = "获取所有可用角色列表", description = "获取所有可用角色列表（不分页）")
    @GetMapping("/all")
    public Result<IPage<SysRole>> getAllRoles() {
        try {
            Page<SysRole> page = new Page<>(1, 1000); // 设置较大页数以获取所有数据
            IPage<SysRole> result = roleService.getRolesWithPagination(page, null, 1); // 只查询启用状态的角色
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询所有角色失败: {}", e.getMessage(), e);
            return Result.error("查询所有角色失败：" + e.getMessage());
        }
    }
}