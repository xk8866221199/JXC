package com.jxc.controller;

import com.jxc.common.Result;
import com.jxc.dto.LoginRequest;
import com.jxc.dto.LoginResponse;
import com.jxc.entity.SysUser;
import com.jxc.repository.SysUserMapper;
import com.jxc.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 企业级实现
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "认证管理", description = "用户认证相关接口")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "使用用户名和密码进行登录认证")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("用户登录请求: {}", request.getUsername());
            
            // 查询用户（先检查默认管理员账户）
            if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
                // 默认管理员账户（系统初始化时使用）
                String token = jwtUtils.generateToken(request.getUsername(), 1L);
                
                LoginResponse response = new LoginResponse();
                response.setToken(token);
                response.setUsername(request.getUsername());
                response.setUserId(1L);
                response.setRealName("系统管理员");
                response.setExpiresIn(86400000L); // 24小时
                
                logger.info("管理员登录成功: {}", request.getUsername());
                return Result.success(response, "登录成功");
            }
            
            // 查询数据库中的用户
            SysUser user = userMapper.findByUsername(request.getUsername());
            if (user == null) {
                logger.warn("登录失败: 用户不存在 - {}", request.getUsername());
                return Result.error("用户名或密码错误");
            }
            
            // 验证密码
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.warn("登录失败: 密码错误 - {}", request.getUsername());
                return Result.error("用户名或密码错误");
            }
            
            // 检查用户状态
            if (user.getStatus() != 1) {
                logger.warn("登录失败: 用户已被禁用 - {}", request.getUsername());
                return Result.error("用户已被禁用");
            }
            
            // 生成JWT Token
            String token = jwtUtils.generateToken(user.getUsername(), user.getId());
            
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setUserId(user.getId());
            response.setRealName(user.getRealName());
            response.setExpiresIn(86400000L); // 24小时
            
            logger.info("用户登录成功: {}", request.getUsername());
            return Result.success(response, "登录成功");
            
        } catch (Exception e) {
            logger.error("登录异常: {}", e.getMessage(), e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "根据JWT Token获取当前登录用户的详细信息")
    @GetMapping("/me")
    public Result<SysUser> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return Result.error(401, "未认证");
            }
            
            String token = authorization.substring(7);
            if (!jwtUtils.validateToken(token)) {
                return Result.error(401, "Token无效或已过期");
            }
            
            String username = jwtUtils.getUsernameFromToken(token);
            
            // 如果是默认管理员，返回模拟数据
            if ("admin".equals(username)) {
                SysUser adminUser = new SysUser();
                adminUser.setId(1L);
                adminUser.setUsername("admin");
                adminUser.setRealName("系统管理员");
                adminUser.setStatus(1);
                return Result.success(adminUser, "获取成功");
            }
            
            // 查询数据库中的用户
            SysUser user = userMapper.findByUsername(username);
            if (user == null) {
                return Result.error(401, "用户不存在");
            }
            
            // 清除密码信息
            user.setPassword(null);
            
            logger.info("获取用户信息成功: {}", username);
            return Result.success(user, "获取成功");
            
        } catch (Exception e) {
            logger.error("获取用户信息异常: {}", e.getMessage(), e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "登出当前用户")
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            // TODO: 将token加入黑名单（Redis实现）
            // 简化实现，实际项目中可以将token加入黑名单
            logger.info("用户登出成功");
            return Result.success("登出成功");
        } catch (Exception e) {
            logger.error("登出异常: {}", e.getMessage(), e);
            return Result.error("登出失败：" + e.getMessage());
        }
    }
    
    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "使用有效的Token获取新的Token")
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return Result.error(401, "未认证");
            }
            
            String token = authorization.substring(7);
            if (!jwtUtils.validateToken(token)) {
                return Result.error(401, "Token无效或已过期");
            }
            
            String username = jwtUtils.getUsernameFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 生成新的Token
            String newToken = jwtUtils.generateToken(username, userId);
            
            LoginResponse response = new LoginResponse();
            response.setToken(newToken);
            response.setUsername(username);
            response.setUserId(userId);
            response.setExpiresIn(86400000L); // 24小时
            
            logger.info("Token刷新成功: {}", username);
            return Result.success(response, "Token刷新成功");
            
        } catch (Exception e) {
            logger.error("Token刷新异常: {}", e.getMessage(), e);
            return Result.error("Token刷新失败：" + e.getMessage());
        }
    }
}