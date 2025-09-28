package com.jxc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@TableName("product_category")
@Schema(description = "商品分类")
public class GoodsCategory {
    
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "分类ID")
    private Long id;
    
    @TableField("parent_id")
    @Schema(description = "父分类ID")
    private Long parentId;
    
    @TableField("category_name")
    @Schema(description = "分类名称")
    private String categoryName;
    
    @TableField("category_code")
    @Schema(description = "分类编码")
    private String categoryCode;
    
    @TableField("description")
    @Schema(description = "分类描述")
    private String description;
    
    @TableField("icon")
    @Schema(description = "分类图标")
    private String icon;
    
    @TableField("sort_order")
    @Schema(description = "排序")
    private Integer sortOrder;
    
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
    public GoodsCategory() {}
    
    // Getter and Setter methods
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
        return "GoodsCategory{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", sortOrder=" + sortOrder +
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