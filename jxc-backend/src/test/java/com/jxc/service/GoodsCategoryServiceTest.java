package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.GoodsCategory;
import com.jxc.service.impl.GoodsCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品分类服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class GoodsCategoryServiceTest {
    
    @Resource
    private GoodsCategoryService goodsCategoryService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testCreateCategory() {
        // 准备测试数据
        GoodsCategory category = new GoodsCategory();
        category.setCategoryName("测试分类");
        category.setCategoryCode("TEST001");
        category.setParentId(0L);
        category.setStatus(1);
        
        // 执行测试
        boolean result = goodsCategoryService.createCategory(category);
        
        // 验证结果
        assertTrue(result);
        assertNotNull(category.getId());
    }
    
    @Test
    void testGetCategoriesWithPagination() {
        // 执行测试
        Page<GoodsCategory> page = new Page<>(1, 10);
        IPage<GoodsCategory> result = goodsCategoryService.getCategoriesWithPagination(page, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
    
    @Test
    void testUpdateCategory() {
        // 准备测试数据
        GoodsCategory category = new GoodsCategory();
        category.setCategoryName("测试分类");
        category.setCategoryCode("TEST002");
        category.setParentId(0L);
        category.setStatus(1);
        goodsCategoryService.createCategory(category);
        
        // 修改数据
        category.setCategoryName("更新测试分类");
        boolean result = goodsCategoryService.updateCategory(category);
        
        // 验证结果
        assertTrue(result);
        
        // 验证更新是否成功
        GoodsCategory updatedCategory = goodsCategoryService.getCategoryById(category.getId());
        assertEquals("更新测试分类", updatedCategory.getCategoryName());
    }
    
    @Test
    void testDeleteCategory() {
        // 准备测试数据
        GoodsCategory category = new GoodsCategory();
        category.setCategoryName("测试分类");
        category.setCategoryCode("TEST003");
        category.setParentId(0L);
        category.setStatus(1);
        goodsCategoryService.createCategory(category);
        
        // 执行测试
        boolean result = goodsCategoryService.deleteCategory(category.getId());
        
        // 验证结果
        assertTrue(result);
        
        // 验证删除是否成功
        GoodsCategory deletedCategory = goodsCategoryService.getCategoryById(category.getId());
        assertEquals(1, deletedCategory.getDeleted().intValue());
    }
    
    @Test
    void testCheckCategoryCodeExists() {
        // 准备测试数据
        GoodsCategory category = new GoodsCategory();
        category.setCategoryName("测试分类");
        category.setCategoryCode("TEST004");
        category.setParentId(0L);
        category.setStatus(1);
        goodsCategoryService.createCategory(category);
        
        // 执行测试
        boolean exists = goodsCategoryService.checkCategoryCodeExists("TEST004", null);
        
        // 验证结果
        assertTrue(exists);
    }
}