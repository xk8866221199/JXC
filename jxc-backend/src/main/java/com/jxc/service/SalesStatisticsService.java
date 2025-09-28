package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.dto.SalesStatisticsDTO;

import java.time.LocalDate;

/**
 * 销售统计服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SalesStatisticsService {
    
    /**
     * 获取销售统计列表
     * 
     * @param page 分页对象
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售统计分页结果
     */
    IPage<SalesStatisticsDTO> getSalesStatistics(Page<SalesStatisticsDTO> page, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取销售趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售统计列表
     */
    java.util.List<SalesStatisticsDTO> getSalesTrend(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取商品销售排行
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 数量限制
     * @return 销售统计列表
     */
    java.util.List<SalesStatisticsDTO> getProductSalesRanking(LocalDate startDate, LocalDate endDate, Integer limit);
}