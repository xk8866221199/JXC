package com.jxc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 性能优化和监控配置
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Configuration
public class PerformanceConfig {
    
    /**
     * Jackson ObjectMapper配置
     * 优化JSON序列化性能
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .modules(new JavaTimeModule())
                .build();
    }
    
    /**
     * Micrometer监控配置
     * 自定义指标注册表
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags("application", "jxc-backend")
                .meterFilter(MeterFilter.denyNameStartsWith("jvm"));
    }
    
    /**
     * 自定义线程池配置
     * 优化异步任务执行性能
     */
    @Bean
    public java.util.concurrent.ExecutorService taskExecutor() {
        return java.util.concurrent.Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2,
                new java.util.concurrent.ThreadFactory() {
                    private final java.util.concurrent.atomic.AtomicInteger threadNumber = new java.util.concurrent.atomic.AtomicInteger(1);
                    
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "jxc-task-" + threadNumber.getAndIncrement());
                        t.setDaemon(false);
                        return t;
                    }
                }
        );
    }
}