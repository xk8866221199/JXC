package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 客户信息实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("customer")
@Schema(description = "客户信息")
public class CustomerInfo {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "客户ID")
    private Long id;
    
    @TableField("customer_code")
    @Schema(description = "客户编码")
    private String customerCode;
    
    @TableField("customer_name")
    @Schema(description = "客户名称")
    private String customerName;
    
    @TableField("contact_person")
    @Schema(description = "联系人")
    private String contactPerson;
    
    @TableField("contact_phone")
    @Schema(description = "联系电话")
    private String contactPhone;
    
    @TableField("contact_email")
    @Schema(description = "联系邮箱")
    private String contactEmail;
    
    @TableField("address")
    @Schema(description = "地址")
    private String address;
    
    @TableField("credit_level")
    @Schema(description = "信用等级：1-A级 2-B级 3-C级")
    private Integer creditLevel;
    
    @TableField("customer_type")
    @Schema(description = "客户类型：1-个人 2-企业")
    private Integer customerType;
    
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
    public CustomerInfo() {}
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCustomerCode() {
        return customerCode;
    }
    
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Integer getCreditLevel() {
        return creditLevel;
    }
    
    public void setCreditLevel(Integer creditLevel) {
        this.creditLevel = creditLevel;
    }
    
    public Integer getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
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
        return "CustomerInfo{" +
                "id=" + id +
                ", customerCode='" + customerCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", address='" + address + '\'' +
                ", creditLevel=" + creditLevel +
                ", customerType=" + customerType +
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