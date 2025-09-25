package com.jxc.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

/**
 * 验证控制器 - 直接JDBC连接
 */
@RestController
@RequestMapping("/verify")
@CrossOrigin(origins = "*")
public class VerifyController {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jxc_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";
    
    /**
     * 验证数据库连接
     */
    @GetMapping("/db")
    public Map<String, Object> verifyDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // 查询用户数据
            String sql = "SELECT id, username, real_name, status FROM sys_user";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            List<Map<String, Object>> users = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getLong("id"));
                user.put("username", rs.getString("username"));
                user.put("realName", rs.getString("real_name"));
                user.put("status", rs.getInt("status"));
                users.add(user);
            }
            
            result.put("code", 200);
            result.put("message", "数据库连接成功，数据来自MySQL数据库表sys_user");
            result.put("data", users);
            result.put("timestamp", System.currentTimeMillis());
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "数据库连接失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * 模拟登录验证
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        String username = request.get("username");
        String password = request.get("password");
        
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // 查询用户
            String sql = "SELECT id, username, real_name, password, status FROM sys_user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // 简单验证（这里应该用BCrypt，但为了验证先简化）
                String dbPassword = rs.getString("password");
                int status = rs.getInt("status");
                
                if (status != 1) {
                    result.put("code", 400);
                    result.put("message", "用户已被禁用");
                } else if (password.equals("123456")) { // 简化验证
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", rs.getLong("id"));
                    userInfo.put("username", rs.getString("username"));
                    userInfo.put("realName", rs.getString("real_name"));
                    userInfo.put("token", "mock-jwt-token-" + System.currentTimeMillis());
                    
                    result.put("code", 200);
                    result.put("message", "登录成功 - 用户数据来自MySQL数据库");
                    result.put("data", userInfo);
                } else {
                    result.put("code", 400);
                    result.put("message", "密码错误（测试密码：123456）");
                }
            } else {
                result.put("code", 400);
                result.put("message", "用户不存在");
            }
            
            result.put("timestamp", System.currentTimeMillis());
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "后端服务正常运行");
        result.put("timestamp", System.currentTimeMillis());
        result.put("server", "Spring Boot 3.2.1");
        return result;
    }
}