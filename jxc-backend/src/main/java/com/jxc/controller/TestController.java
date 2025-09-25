package com.jxc.controller;

import com.jxc.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Tag(name = "测试接口", description = "系统测试相关接口")
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    /**
     * 健康检查
     */
    @Operation(summary = "健康检查", description = "检查系统是否正常运行")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "JXC进销存管理系统");
        data.put("version", "1.0.0");
        
        logger.info("健康检查请求");
        return Result.success(data, "系统运行正常");
    }
    
    /**
     * Echo测试
     */
    @Operation(summary = "Echo测试", description = "回显测试消息")
    @GetMapping("/echo")
    public Result<String> echo(@RequestParam(defaultValue = "Hello JXC!") String message) {
        logger.info("Echo请求: {}", message);
        return Result.success("Echo: " + message, "请求成功");
    }
}