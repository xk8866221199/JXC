package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 库存查询DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "库存查询DTO")
public class InventoryQueryDTO {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品编码")
    private String productCode;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "最小库存数量")
    private Integer minStock;
    
    @Schema(description = "最大库存数量")
    private Integer maxStock;
    
    @Schema(description = "状态：0-下架 1-上架")
    private Integer status;
    
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
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Integer getMinStock() {
        return minStock;
    }
    
    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
    
    public Integer getMaxStock() {
        return maxStock;
    }
    
    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}