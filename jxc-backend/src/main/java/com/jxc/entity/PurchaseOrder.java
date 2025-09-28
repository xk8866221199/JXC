package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购订单实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("purchase_order")
@Schema(description = "采购订单")
public class PurchaseOrder {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "采购订单ID")
    private Long id;
    
    @TableField("order_no")
    @Schema(description = "订单编号")
    private String orderNo;
    
    @TableField("supplier_id")
    @Schema(description = "供应商ID")
    private Long supplierId;
    
    @TableField("order_date")
    @Schema(description = "订单日期")
    private LocalDate orderDate;
    
    @TableField("delivery_date")
    @Schema(description = "交货日期")
    private LocalDate deliveryDate;
    
    @TableField("total_amount")
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;
    
    @TableField("discount_amount")
    @Schema(description = "折扣金额")
    private BigDecimal discountAmount;
    
    @TableField("tax_amount")
    @Schema(description = "税额")
    private BigDecimal taxAmount;
    
    @TableField("final_amount")
    @Schema(description = "最终金额")
    private BigDecimal finalAmount;
    
    @TableField("order_status")
    @Schema(description = "订单状态：1-待审核 2-已审核 3-采购中 4-部分到货 5-全部到货 6-已取消")
    private Integer orderStatus;
    
    @TableField("payment_status")
    @Schema(description = "付款状态：1-未付款 2-部分付款 3-已付款")
    private Integer paymentStatus;
    
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
    public PurchaseOrder() {}
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public Long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }
    
    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }
    
    public Integer getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public Integer getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
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
        return "PurchaseOrder{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", supplierId=" + supplierId +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", totalAmount=" + totalAmount +
                ", discountAmount=" + discountAmount +
                ", taxAmount=" + taxAmount +
                ", finalAmount=" + finalAmount +
                ", orderStatus=" + orderStatus +
                ", paymentStatus=" + paymentStatus +
                ", remark='" + remark + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}