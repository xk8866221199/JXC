package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("sys_user_role")
@Schema(description = "用户角色关联")
public class SysUserRole {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;
    
    @TableField("user_id")
    @Schema(description = "用户ID")
    private Long userId;
    
    @TableField("role_id")
    @Schema(description = "角色ID")
    private Long roleId;
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    
    // 构造函数
    public SysUserRole() {}
    
    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    @Override
    public String toString() {
        return "SysUserRole{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", createdTime=" + createdTime +
                '}';
    }
}