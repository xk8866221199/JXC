package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxc.entity.GoodsInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品信息服务测试类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootTest
@SpringJUnitConfig
public class GoodsInfoServiceTest {
    
    @Resource
    private GoodsInfoService goodsInfoService;
    
    @BeforeEach
    void setUp() {
        // 测试前准备
    }
    
    @Test
    void testCreateGoods() {
        // 准备测试数据
        GoodsInfo goods = new GoodsInfo();
        goods.setProductName("测试商品");
        goods.setProductCode("GOODS001");
        goods.setCategoryId(1L);
        goods.setBrand("测试品牌");
        goods.setUnit("个");
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("15.00"));
        goods.setMinStock(10);
        goods.setStatus(1);
        
        // 执行测试
        boolean result = goodsInfoService.createGoods(goods);
        
        // 验证结果
        assertTrue(result);
        assertNotNull(goods.getId());
    }
    
    @Test
    void testGetGoodsWithPagination() {
        // 执行测试
        Page<GoodsInfo> page = new Page<>(1, 10);
        IPage<GoodsInfo> result = goodsInfoService.getGoodsWithPagination(page, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
    
    @Test
    void testUpdateGoods() {
        // 准备测试数据
        GoodsInfo goods = new GoodsInfo();
        goods.setProductName("测试商品");
        goods.setProductCode("GOODS002");
        goods.setCategoryId(1L);
        goods.setBrand("测试品牌");
        goods.setUnit("个");
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("15.00"));
        goods.setMinStock(10);
        goods.setStatus(1);
        goodsInfoService.createGoods(goods);
        
        // 修改数据
        goods.setProductName("更新测试商品");
        boolean result = goodsInfoService.updateGoods(goods);
        
        // 验证结果
        assertTrue(result);
        
        // 验证更新是否成功
        GoodsInfo updatedGoods = goodsInfoService.getGoodsById(goods.getId());
        assertEquals("更新测试商品", updatedGoods.getProductName());
    }
    
    @Test
    void testDeleteGoods() {
        // 准备测试数据
        GoodsInfo goods = new GoodsInfo();
        goods.setProductName("测试商品");
        goods.setProductCode("GOODS003");
        goods.setCategoryId(1L);
        goods.setBrand("测试品牌");
        goods.setUnit("个");
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("15.00"));
        goods.setMinStock(10);
        goods.setStatus(1);
        goodsInfoService.createGoods(goods);
        
        // 执行测试
        boolean result = goodsInfoService.deleteGoods(goods.getId());
        
        // 验证结果
        assertTrue(result);
        
        // 验证删除是否成功
        GoodsInfo deletedGoods = goodsInfoService.getGoodsById(goods.getId());
        assertEquals(1, deletedGoods.getDeleted().intValue());
    }
    
    @Test
    void testCheckGoodsCodeExists() {
        // 准备测试数据
        GoodsInfo goods = new GoodsInfo();
        goods.setProductName("测试商品");
        goods.setProductCode("GOODS004");
        goods.setCategoryId(1L);
        goods.setBrand("测试品牌");
        goods.setUnit("个");
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("15.00"));
        goods.setMinStock(10);
        goods.setStatus(1);
        goodsInfoService.createGoods(goods);
        
        // 执行测试
        boolean exists = goodsInfoService.checkGoodsCodeExists("GOODS004", null);
        
        // 验证结果
        assertTrue(exists);
    }
    
    @Test
    void testGetGoodsByBarcode() {
        // 准备测试数据
        GoodsInfo goods = new GoodsInfo();
        goods.setProductName("测试商品");
        goods.setProductCode("GOODS005");
        goods.setCategoryId(1L);
        goods.setBrand("测试品牌");
        goods.setUnit("个");
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("15.00"));
        goods.setMinStock(10);
        goods.setStatus(1);
        goodsInfoService.createGoods(goods);
        
        // 执行测试
        GoodsInfo result = goodsInfoService.getGoodsByBarcode("1234567890127");
        
        // 验证结果
        assertNotNull(result);
        assertEquals("1234567890127", result.getProductCode());
    }
}