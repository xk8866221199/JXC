package com.jxc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxc.entity.GoodsInfo;

/**
 * 商品信息服务接口
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface GoodsInfoService extends IService<GoodsInfo> {
    
    /**
     * 分页查询商品信息列表
     * 
     * @param page 分页对象
     * @param keyword 搜索关键字
     * @param categoryId 分类ID
     * @param status 商品状态
     * @return 商品信息分页结果
     */
    IPage<GoodsInfo> getGoodsWithPagination(Page<GoodsInfo> page, String keyword, Long categoryId, Integer status);
    
    /**
     * 根据ID获取商品信息详情
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    GoodsInfo getGoodsById(Long id);
    
    /**
     * 创建商品信息
     * 
     * @param goods 商品信息
     * @return 是否成功
     */
    boolean createGoods(GoodsInfo goods);
    
    /**
     * 更新商品信息
     * 
     * @param goods 商品信息
     * @return 是否成功
     */
    boolean updateGoods(GoodsInfo goods);
    
    /**
     * 删除商品信息
     * 
     * @param id 商品ID
     * @return 是否成功
     */
    boolean deleteGoods(Long id);
    
    /**
     * 更新商品信息状态
     * 
     * @param id 商品ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateGoodsStatus(Long id, Integer status);
    
    /**
     * 检查商品编码是否已存在
     * 
     * @param goodsCode 商品编码
     * @param excludeId 排除的商品ID
     * @return 是否存在
     */
    boolean checkGoodsCodeExists(String goodsCode, Long excludeId);
    
    /**
     * 根据条码获取商品信息
     * 
     * @param barcode 商品条码
     * @return 商品信息
     */
    GoodsInfo getGoodsByBarcode(String barcode);
}