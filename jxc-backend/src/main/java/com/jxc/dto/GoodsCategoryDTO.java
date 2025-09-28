package com.jxc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商品分类DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Schema(description = "商品分类DTO")
public class GoodsCategoryDTO {
    
    @Schema(description = "分类ID")
    private Long id;
    
    @NotNull(message = "父分类ID不能为空")
    @Schema(description = "父分类ID")
    private Long parentId;
    
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 64, message = "分类名称长度不能超过64个字符")
    @Schema(description = "分类名称")
    private String categoryName;
    
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 64, message = "分类编码长度不能超过64个字符")
    @Schema(description = "分类编码")
    private String categoryCode;
    
    @Size(max = 512, message = "分类描述长度不能超过512个字符")
    @Schema(description = "分类描述")
    private String description;
    
    @Size(max = 256, message = "分类图标长度不能超过256个字符")
    @Schema(description = "分类图标")
    private String icon;
    
    @Schema(description = "排序")
    private Integer sortOrder;
    
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
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryCode() {
        return categoryCode;
    }
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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