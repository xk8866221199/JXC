package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.dto.FinanceStatisticsDTO;

import java.time.LocalDate;

/**
 * 财务统计服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface FinanceStatisticsService {
    
    /**
     * 获取财务统计列表
     * 
     * @param page 分页对象
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 财务统计分页结果
     */
    IPage<FinanceStatisticsDTO> getFinanceStatistics(Page<FinanceStatisticsDTO> page, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取应收统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 财务统计列表
     */
    java.util.List<FinanceStatisticsDTO> getReceivableStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取应付统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 财务统计列表
     */
    java.util.List<FinanceStatisticsDTO> getPayableStatistics(LocalDate startDate, LocalDate endDate);
}