package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.GoodsCategory;

/**
 * 商品分类服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {
    
    /**
     * 分页查询商品分类列表
     * 
     * @param page 分页对象
     * @param keyword 搜索关键字
     * @param status 分类状态
     * @return 商品分类分页结果
     */
    IPage<GoodsCategory> getCategoriesWithPagination(Page<GoodsCategory> page, String keyword, Integer status);
    
    /**
     * 根据ID获取商品分类详情
     * 
     * @param id 分类ID
     * @return 商品分类信息
     */
    GoodsCategory getCategoryById(Long id);
    
    /**
     * 创建商品分类
     * 
     * @param category 商品分类信息
     * @return 是否成功
     */
    boolean createCategory(GoodsCategory category);
    
    /**
     * 更新商品分类
     * 
     * @param category 商品分类信息
     * @return 是否成功
     */
    boolean updateCategory(GoodsCategory category);
    
    /**
     * 删除商品分类
     * 
     * @param id 分类ID
     * @return 是否成功
     */
    boolean deleteCategory(Long id);
    
    /**
     * 更新商品分类状态
     * 
     * @param id 分类ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateCategoryStatus(Long id, Integer status);
    
    /**
     * 检查分类编码是否已存在
     * 
     * @param categoryCode 分类编码
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean checkCategoryCodeExists(String categoryCode, Long excludeId);
}