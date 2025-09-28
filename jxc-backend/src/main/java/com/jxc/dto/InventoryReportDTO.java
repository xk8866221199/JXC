package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 库存报表DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "库存报表DTO")
public class InventoryReportDTO {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品编码")
    private String productCode;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "当前库存数量")
    private Integer currentQuantity;
    
    @Schema(description = "库存成本")
    private BigDecimal inventoryCost;
    
    @Schema(description = "库存预警状态：1-正常 2-预警 3-缺货")
    private Integer warningStatus;
    
    @Schema(description = "库存周转率")
    private Double turnoverRate;
    
    @Schema(description = "销售数量")
    private Integer salesQuantity;
    
    @Schema(description = "采购数量")
    private Integer purchaseQuantity;
    
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
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public Integer getCurrentQuantity() {
        return currentQuantity;
    }
    
    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
    
    public BigDecimal getInventoryCost() {
        return inventoryCost;
    }
    
    public void setInventoryCost(BigDecimal inventoryCost) {
        this.inventoryCost = inventoryCost;
    }
    
    public Integer getWarningStatus() {
        return warningStatus;
    }
    
    public void setWarningStatus(Integer warningStatus) {
        this.warningStatus = warningStatus;
    }
    
    public Double getTurnoverRate() {
        return turnoverRate;
    }
    
    public void setTurnoverRate(Double turnoverRate) {
        this.turnoverRate = turnoverRate;
    }
    
    public Integer getSalesQuantity() {
        return salesQuantity;
    }
    
    public void setSalesQuantity(Integer salesQuantity) {
        this.salesQuantity = salesQuantity;
    }
    
    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }
    
    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }
}