package com.jxc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JXC进销存管理系统主启动类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.jxc.repository")
public class JxcBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JxcBackendApplication.class, args);
        System.out.println("""
            
            ========================================
            🚀 JXC进销存管理系统启动成功！
            📝 项目文档: README.md
            🌐 接口文档: http://localhost:8080/api/doc.html
            📊 监控中心: http://localhost:8080/api/actuator
            ========================================
            """);
    }
}