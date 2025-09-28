package com.jxc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxc.entity.GoodsCategory;
import com.jxc.repository.GoodsCategoryMapper;
import com.jxc.service.GoodsCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类服务实现类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoodsCategoryServiceImpl.class);
    
    @Autowired
    private GoodsCategoryMapper categoryMapper;
    
    @Autowired
    private com.jxc.repository.GoodsInfoMapper goodsInfoMapper;
    
    @Override
    public IPage<GoodsCategory> getCategoriesWithPagination(Page<GoodsCategory> page, String keyword, Integer status) {
        try {
            QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("category_name", keyword)
                    .or()
                    .like("category_code", keyword));
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("created_at");
            return categoryMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询商品分类列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询商品分类列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public GoodsCategory getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createCategory(GoodsCategory category) {
        try {
            // 检查分类编码是否已存在
            if (checkCategoryCodeExists(category.getCategoryCode(), null)) {
                throw new RuntimeException("分类编码已存在");
            }
            
            // 设置默认状态
            if (category.getStatus() == null) {
                category.setStatus(1); // 默认启用
            }
            
            // 设置默认排序
            if (category.getSortOrder() == null) {
                category.setSortOrder(1);
            }
            
            // 保存分类
            int result = categoryMapper.insert(category);
            logger.info("创建商品分类成功: {}", category.getCategoryName());
            return result > 0;
        } catch (Exception e) {
            logger.error("创建商品分类失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建商品分类失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCategory(GoodsCategory category) {
        try {
            // 检查分类编码是否已存在
            if (checkCategoryCodeExists(category.getCategoryCode(), category.getId())) {
                throw new RuntimeException("分类编码已存在");
            }
            
            // 更新分类
            int result = categoryMapper.updateById(category);
            logger.info("更新商品分类成功: {}", category.getCategoryName());
            return result > 0;
        } catch (Exception e) {
            logger.error("更新商品分类失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新商品分类失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCategory(Long id) {
        try {
            logger.info("开始删除商品分类，ID: {}", id);
            
            // 检查分类是否存在
            GoodsCategory category = categoryMapper.selectById(id);
            if (category == null) {
                logger.warn("尝试删除不存在的分类，ID: {}", id);
                throw new RuntimeException("分类不存在");
            }
            logger.info("找到要删除的分类: ID={}, Name={}", category.getId(), category.getCategoryName());
            
            // 检查是否有商品属于该分类
            int goodsCount = goodsInfoMapper.selectCountByCategoryId(id);
            logger.info("分类 {} 下的商品数量: {}", id, goodsCount);
            if (goodsCount > 0) {
                throw new RuntimeException("该分类下存在商品，无法删除");
            }
            
            // 将该分类下的子分类的parentId设置为0（顶级分类）
            QueryWrapper<GoodsCategory> subCategoryQuery = new QueryWrapper<>();
            subCategoryQuery.eq("parent_id", id);
            subCategoryQuery.eq("deleted", 0);
            List<GoodsCategory> subCategories = categoryMapper.selectList(subCategoryQuery);
            logger.info("分类 {} 下的子分类数量: {}", id, subCategories.size());
            
            for (GoodsCategory subCategory : subCategories) {
                subCategory.setParentId(0L);
                categoryMapper.updateById(subCategory);
                logger.info("更新子分类 {} 的父分类为顶级分类", subCategory.getId());
            }
            
            // 物理删除分类
            logger.info("开始物理删除分类 ID: {}", id);
            int result = categoryMapper.deleteById(id);
            logger.info("删除商品分类完成，ID: {}, 删除结果: {}", id, result);
            
            // 如果没有删除任何记录，可能是因为记录不存在
            if (result == 0) {
                logger.warn("未删除任何记录，分类可能已被删除或不存在，ID: {}", id);
                return false;
            }
            
            return true;
        } catch (RuntimeException e) {
            logger.error("删除商品分类失败: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("删除商品分类失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除商品分类失败: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCategoryStatus(Long id, Integer status) {
        try {
            GoodsCategory category = new GoodsCategory();
            category.setId(id);
            category.setStatus(status);
            int result = categoryMapper.updateById(category);
            logger.info("更新商品分类状态成功: ID={}, Status={}", id, status);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新商品分类状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新商品分类状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkCategoryCodeExists(String categoryCode, Long excludeId) {
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_code", categoryCode);
        queryWrapper.eq("deleted", 0);
        
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        return categoryMapper.selectCount(queryWrapper) > 0;
    }
}