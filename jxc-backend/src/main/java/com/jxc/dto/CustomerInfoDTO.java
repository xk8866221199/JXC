package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 客户信息DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "客户信息DTO")
public class CustomerInfoDTO {
    
    @Schema(description = "客户ID")
    private Long id;
    
    @NotBlank(message = "客户编码不能为空")
    @Size(max = 64, message = "客户编码长度不能超过64个字符")
    @Schema(description = "客户编码")
    private String customerCode;
    
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 128, message = "客户名称长度不能超过128个字符")
    @Schema(description = "客户名称")
    private String customerName;
    
    @Size(max = 64, message = "联系人长度不能超过64个字符")
    @Schema(description = "联系人")
    private String contactPerson;
    
    @Size(max = 32, message = "联系电话长度不能超过32个字符")
    @Schema(description = "联系电话")
    private String contactPhone;
    
    @Size(max = 128, message = "联系邮箱长度不能超过128个字符")
    @Schema(description = "联系邮箱")
    private String contactEmail;
    
    @Size(max = 256, message = "地址长度不能超过256个字符")
    @Schema(description = "地址")
    private String address;
    
    @Schema(description = "信用等级：1-A级 2-B级 3-C级")
    private Integer creditLevel;
    
    @NotNull(message = "客户类型不能为空")
    @Schema(description = "客户类型：1-个人 2-企业")
    private Integer customerType;
    
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
}