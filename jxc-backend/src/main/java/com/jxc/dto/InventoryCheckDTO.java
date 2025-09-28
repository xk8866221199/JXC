package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 库存盘点DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "库存盘点DTO")
public class InventoryCheckDTO {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品编码")
    private String productCode;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "系统库存数量")
    private Integer systemQuantity;
    
    @Schema(description = "实际库存数量")
    private Integer actualQuantity;
    
    @Schema(description = "差异数量")
    private Integer differenceQuantity;
    
    @Schema(description = "单位成本")
    private BigDecimal unitCost;
    
    @Schema(description = "差异金额")
    private BigDecimal differenceAmount;
    
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
    
    public Integer getSystemQuantity() {
        return systemQuantity;
    }
    
    public void setSystemQuantity(Integer systemQuantity) {
        this.systemQuantity = systemQuantity;
    }
    
    public Integer getActualQuantity() {
        return actualQuantity;
    }
    
    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }
    
    public Integer getDifferenceQuantity() {
        return differenceQuantity;
    }
    
    public void setDifferenceQuantity(Integer differenceQuantity) {
        this.differenceQuantity = differenceQuantity;
    }
    
    public BigDecimal getUnitCost() {
        return unitCost;
    }
    
    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }
    
    public BigDecimal getDifferenceAmount() {
        return differenceAmount;
    }
    
    public void setDifferenceAmount(BigDecimal differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}