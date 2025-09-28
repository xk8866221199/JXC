package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("inventory_transaction")
@Schema(description = "库存流水")
public class InventoryTransaction {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "流水ID")
    private Long id;
    
    @TableField("transaction_no")
    @Schema(description = "流水单号")
    private String transactionNo;
    
    @TableField("product_id")
    @Schema(description = "商品ID")
    private Long productId;
    
    @TableField("transaction_type")
    @Schema(description = "流水类型：1-入库 2-出库 3-调拨 4-盘点")
    private Integer transactionType;
    
    @TableField("source_type")
    @Schema(description = "来源类型：1-采购入库 2-销售出库 3-调拨 4-盘点 5-其他")
    private Integer sourceType;
    
    @TableField("source_id")
    @Schema(description = "来源单据ID")
    private Long sourceId;
    
    @TableField("quantity")
    @Schema(description = "数量(正数为入库,负数为出库)")
    private Integer quantity;
    
    @TableField("unit_cost")
    @Schema(description = "单位成本")
    private BigDecimal unitCost;
    
    @TableField("total_cost")
    @Schema(description = "总成本")
    private BigDecimal totalCost;
    
    @TableField("before_quantity")
    @Schema(description = "变动前库存")
    private Integer beforeQuantity;
    
    @TableField("after_quantity")
    @Schema(description = "变动后库存")
    private Integer afterQuantity;
    
    @TableField("transaction_date")
    @Schema(description = "交易时间")
    private LocalDateTime transactionDate;
    
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;
    
    @TableField("created_by")
    @Schema(description = "创建人")
    private Long createdBy;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    // 构造函数
    public InventoryTransaction() {}
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTransactionNo() {
        return transactionNo;
    }
    
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
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
    
    public Integer getBeforeQuantity() {
        return beforeQuantity;
    }
    
    public void setBeforeQuantity(Integer beforeQuantity) {
        this.beforeQuantity = beforeQuantity;
    }
    
    public Integer getAfterQuantity() {
        return afterQuantity;
    }
    
    public void setAfterQuantity(Integer afterQuantity) {
        this.afterQuantity = afterQuantity;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "InventoryTransaction{" +
                "id=" + id +
                ", transactionNo='" + transactionNo + '\'' +
                ", productId=" + productId +
                ", transactionType=" + transactionType +
                ", sourceType=" + sourceType +
                ", sourceId=" + sourceId +
                ", quantity=" + quantity +
                ", unitCost=" + unitCost +
                ", totalCost=" + totalCost +
                ", beforeQuantity=" + beforeQuantity +
                ", afterQuantity=" + afterQuantity +
                ", transactionDate=" + transactionDate +
                ", remark='" + remark + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}