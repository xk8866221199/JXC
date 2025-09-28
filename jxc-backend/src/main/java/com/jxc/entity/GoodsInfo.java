package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("product")
@Schema(description = "商品信息")
public class GoodsInfo {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Long id;
    
    @TableField("product_code")
    @Schema(description = "商品编码")
    private String productCode;
    
    @TableField("product_name")
    @Schema(description = "商品名称")
    private String productName;
    
    @TableField("category_id")
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @TableField("brand")
    @Schema(description = "品牌")
    private String brand;
    
    @TableField("unit")
    @Schema(description = "单位")
    private String unit;
    
    @TableField("specification")
    @Schema(description = "规格型号")
    private String specification;
    
    @TableField("description")
    @Schema(description = "商品描述")
    private String description;
    
    @TableField("image_url")
    @Schema(description = "商品图片")
    private String imageUrl;
    
    @TableField("purchase_price")
    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;
    
    @TableField("sale_price")
    @Schema(description = "销售价格")
    private BigDecimal salePrice;
    
    @TableField("stock_quantity")
    @Schema(description = "库存数量")
    private Integer stockQuantity;
    
    @TableField("min_stock")
    @Schema(description = "最小库存")
    private Integer minStock;
    
    @TableField("max_stock")
    @Schema(description = "最大库存")
    private Integer maxStock;
    
    @TableField("status")
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
    
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;
    
    @TableField("created_by")
    @Schema(description = "创建人")
    private Long createdBy;
    
    @TableField("updated_by")
    @Schema(description = "更新人")
    private Long updatedBy;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @TableLogic
    @TableField("deleted")
    @Schema(description = "是否删除：0-未删除，1-已删除")
    private Integer deleted;
    
    // 构造函数
    public GoodsInfo() {}
    
    // Getter and Setter methods
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
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    
    @Override
    public String toString() {
        return "GoodsInfo{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", categoryId=" + categoryId +
                ", brand='" + brand + '\'' +
                ", unit='" + unit + '\'' +
                ", specification='" + specification + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", salePrice=" + salePrice +
                ", stockQuantity=" + stockQuantity +
                ", minStock=" + minStock +
                ", maxStock=" + maxStock +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}