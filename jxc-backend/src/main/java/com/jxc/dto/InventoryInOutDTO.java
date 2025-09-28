package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 出入库管理DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "出入库管理DTO")
public class InventoryInOutDTO {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品编码")
    private String productCode;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "流水类型：1-入库 2-出库")
    private Integer transactionType;
    
    @Schema(description = "来源类型：1-采购入库 2-销售出库 3-调拨 4-盘点 5-其他")
    private Integer sourceType;
    
    @Schema(description = "来源单据ID")
    private Long sourceId;
    
    @Schema(description = "数量")
    private Integer quantity;
    
    @Schema(description = "单位成本")
    private BigDecimal unitCost;
    
    @Schema(description = "总成本")
    private BigDecimal totalCost;
    
    @Schema(description = "备注")
    private String remark;
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public Integer getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
    
    public Integer getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
    
    public Long getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitCost() {
        return unitCost;
    }
    
    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}