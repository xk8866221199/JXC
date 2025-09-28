package com.jxc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.common.Result;
import com.jxc.entity.SysUser;
import com.jxc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表，支持搜索和状态筛选")
    @GetMapping
    public Result<IPage<SysUser>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        try {
            Page<SysUser> pageRequest = new Page<>(page, size);
            IPage<SysUser> result = userService.getUsersWithRoles(pageRequest, keyword, status);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            logger.error("查询用户列表失败: {}", e.getMessage(), e);
            return Result.error("查询用户列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        try {
            SysUser user = userService.getUserById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            // 清除密码信息
            user.setPassword(null);
            return Result.success(user, "查询成功");
        } catch (Exception e) {
            logger.error("查询用户详情失败: {}", e.getMessage(), e);
            return Result.error("查询用户详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建用户
     */
    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    public Result<String> createUser(@RequestBody SysUser user) {
        try {
            boolean success = userService.createUser(user, user.getRoleIds());
            if (success) {
                return Result.success("创建成功");
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            logger.error("创建用户失败: {}", e.getMessage(), e);
            return Result.error("创建用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户
     */
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        try {
            user.setId(id);
            boolean success = userService.updateUser(user, user.getRoleIds());
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            logger.error("更新用户失败: {}", e.getMessage(), e);
            return Result.error("更新用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "逻辑删除用户")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户失败: {}", e.getMessage(), e);
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 重置用户密码
     */
    @Operation(summary = "重置用户密码", description = "重置用户登录密码")
    @PutMapping("/{id}/password")
    public Result<String> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
        try {
            boolean success = userService.resetPassword(id, request.getNewPassword());
            if (success) {
                return Result.success("密码重置成功");
            } else {
                return Result.error("密码重置失败");
            }
        } catch (Exception e) {
            logger.error("重置用户密码失败: {}", e.getMessage(), e);
            return Result.error("重置用户密码失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户状态
     */
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        try {
            boolean success = userService.updateUserStatus(id, request.getStatus());
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            logger.error("更新用户状态失败: {}", e.getMessage(), e);
            return Result.error("更新用户状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查用户名是否已存在
     */
    @Operation(summary = "检查用户名", description = "检查用户名是否已存在")
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username, @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = userService.checkUsernameExists(username, excludeId);
            return Result.success(exists, "查询成功");
        } catch (Exception e) {
            logger.error("检查用户名失败: {}", e.getMessage(), e);
            return Result.error("检查用户名失败：" + e.getMessage());
        }
    }
    
    /**
     * 重置密码请求DTO
     */
    public static class ResetPasswordRequest {
        private String newPassword;
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
    /**
     * 更新状态请求DTO
     */
    public static class UpdateStatusRequest {
        private Integer status;
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}