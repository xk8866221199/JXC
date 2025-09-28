package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售统计DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "销售统计DTO")
public class SalesStatisticsDTO {
    
    @Schema(description = "统计日期")
    private LocalDate statDate;
    
    @Schema(description = "总销售额")
    private BigDecimal totalSalesAmount;
    
    @Schema(description = "总订单数")
    private Integer totalOrderCount;
    
    @Schema(description = "总商品销售数量")
    private Integer totalProductCount;
    
    @Schema(description = "平均客单价")
    private BigDecimal averageOrderAmount;
    
    @Schema(description = "销售增长率")
    private Double growthRate;
    
    // Getters and Setters
    public LocalDate getStatDate() {
        return statDate;
    }
    
    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
    }
    
    public BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }
    
    public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }
    
    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }
    
    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }
    
    public Integer getTotalProductCount() {
        return totalProductCount;
    }
    
    public void setTotalProductCount(Integer totalProductCount) {
        this.totalProductCount = totalProductCount;
    }
    
    public BigDecimal getAverageOrderAmount() {
        return averageOrderAmount;
    }
    
    public void setAverageOrderAmount(BigDecimal averageOrderAmount) {
        this.averageOrderAmount = averageOrderAmount;
    }
    
    public Double getGrowthRate() {
        return growthRate;
    }
    
    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }
}