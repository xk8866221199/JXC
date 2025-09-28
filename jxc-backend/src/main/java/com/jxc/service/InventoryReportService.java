package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.dto.InventoryReportDTO;

import java.time.LocalDate;

/**
 * 库存报表服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface InventoryReportService {
    
    /**
     * 获取库存报表列表
     * 
     * @param page 分页对象
     * @param categoryId 分类ID
     * @param keyword 关键字
     * @return 库存报表分页结果
     */
    IPage<InventoryReportDTO> getInventoryReport(Page<InventoryReportDTO> page, Long categoryId, String keyword);
    
    /**
     * 获取库存预警列表
     * 
     * @param page 分页对象
     * @return 库存报表分页结果
     */
    IPage<InventoryReportDTO> getInventoryWarningReport(Page<InventoryReportDTO> page);
    
    /**
     * 获取库存周转率统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 库存报表列表
     */
    java.util.List<InventoryReportDTO> getInventoryTurnoverRate(LocalDate startDate, LocalDate endDate);
}