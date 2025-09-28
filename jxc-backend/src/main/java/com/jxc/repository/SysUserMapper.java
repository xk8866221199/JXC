package com.jxc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxc.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

/**
 * 系统用户数据访问层
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    SysUser findByUsername(@Param("username") String username);
    
    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE username = #{username} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int countByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);
    
    /**
     * 物理删除用户
     *
     * @param id 用户ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int deleteUserPhysically(@Param("id") Long id);
}