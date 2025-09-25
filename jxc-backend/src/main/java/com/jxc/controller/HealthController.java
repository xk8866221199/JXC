package com.jxc.controller;

import com.jxc.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Value("${app.name:JXC进销存管理系统}")
    private String appName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    /**
     * 健康检查接口
     */
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", appName);
        health.put("version", appVersion);
        health.put("timestamp", LocalDateTime.now());
        health.put("message", "系统运行正常");
        
        return Result.success(health, "系统健康检查通过");
    }
    
    /**
     * 系统信息接口
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", appName);
        info.put("version", appVersion);
        info.put("description", "专业的超市进销存管理解决方案");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("startTime", LocalDateTime.now());
        
        return Result.success(info, "系统信息获取成功");
    }
}