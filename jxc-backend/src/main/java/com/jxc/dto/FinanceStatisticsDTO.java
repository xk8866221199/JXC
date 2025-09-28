package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 财务统计DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "财务统计DTO")
public class FinanceStatisticsDTO {
    
    @Schema(description = "统计日期")
    private LocalDate statDate;
    
    @Schema(description = "总应收金额")
    private BigDecimal totalReceivableAmount;
    
    @Schema(description = "已收金额")
    private BigDecimal receivedAmount;
    
    @Schema(description = "未收金额")
    private BigDecimal unreceivedAmount;
    
    @Schema(description = "总应付金额")
    private BigDecimal totalPayableAmount;
    
    @Schema(description = "已付金额")
    private BigDecimal paidAmount;
    
    @Schema(description = "未付金额")
    private BigDecimal unpaidAmount;
    
    @Schema(description = "净利润")
    private BigDecimal netProfit;
    
    @Schema(description = "毛利率")
    private Double grossProfitMargin;
    
    @Schema(description = "应收账款周转率")
    private Double receivableTurnoverRate;
    
    @Schema(description = "应付账款周转率")
    private Double payableTurnoverRate;
    
    // Getters and Setters
    public LocalDate getStatDate() {
        return statDate;
    }
    
    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
    }
    
    public BigDecimal getTotalReceivableAmount() {
        return totalReceivableAmount;
    }
    
    public void setTotalReceivableAmount(BigDecimal totalReceivableAmount) {
        this.totalReceivableAmount = totalReceivableAmount;
    }
    
    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }
    
    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
    
    public BigDecimal getUnreceivedAmount() {
        return unreceivedAmount;
    }
    
    public void setUnreceivedAmount(BigDecimal unreceivedAmount) {
        this.unreceivedAmount = unreceivedAmount;
    }
    
    public BigDecimal getTotalPayableAmount() {
        return totalPayableAmount;
    }
    
    public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }
    
    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }
    
    public BigDecimal getNetProfit() {
        return netProfit;
    }
    
    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }
    
    public Double getGrossProfitMargin() {
        return grossProfitMargin;
    }
    
    public void setGrossProfitMargin(Double grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }
    
    public Double getReceivableTurnoverRate() {
        return receivableTurnoverRate;
    }
    
    public void setReceivableTurnoverRate(Double receivableTurnoverRate) {
        this.receivableTurnoverRate = receivableTurnoverRate;
    }
    
    public Double getPayableTurnoverRate() {
        return payableTurnoverRate;
    }
    
    public void setPayableTurnoverRate(Double payableTurnoverRate) {
        this.payableTurnoverRate = payableTurnoverRate;
    }
}