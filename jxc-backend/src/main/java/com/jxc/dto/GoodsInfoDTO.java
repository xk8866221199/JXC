package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 商品信息DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "商品信息DTO")
public class GoodsInfoDTO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @NotBlank(message = "商品编码不能为空")
    @Size(max = 64, message = "商品编码长度不能超过64个字符")
    @Schema(description = "商品编码")
    private String productCode;
    
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 128, message = "商品名称长度不能超过128个字符")
    @Schema(description = "商品名称")
    private String productName;
    
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Size(max = 64, message = "品牌长度不能超过64个字符")
    @Schema(description = "品牌")
    private String brand;
    
    @NotBlank(message = "单位不能为空")
    @Size(max = 32, message = "单位长度不能超过32个字符")
    @Schema(description = "单位")
    private String unit;
    
    @Size(max = 256, message = "规格型号长度不能超过256个字符")
    @Schema(description = "规格型号")
    private String specification;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Size(max = 512, message = "商品图片URL长度不能超过512个字符")
    @Schema(description = "商品图片")
    private String imageUrl;
    
    @NotNull(message = "采购价格不能为空")
    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;
    
    @NotNull(message = "销售价格不能为空")
    @Schema(description = "销售价格")
    private BigDecimal salePrice;
    
    @Schema(description = "库存数量")
    private Integer stockQuantity;
    
    @Schema(description = "最小库存")
    private Integer minStock;
    
    @Schema(description = "最大库存")
    private Integer maxStock;
    
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
    
    @Size(max = 512, message = "备注长度不能超过512个字符")
    @Schema(description = "备注")
    private String remark;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getSpecification() {
        return specification;
    }
    
    public void setSpecification(String specification) {
        this.specification = specification;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    
    public BigDecimal getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}