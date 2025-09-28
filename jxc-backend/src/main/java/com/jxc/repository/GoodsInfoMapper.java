package com.jxc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxc.entity.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 商品信息数据访问层
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Mapper
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {
    
    /**
     * 根据分类ID查询商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(*) FROM product WHERE category_id = #{categoryId} AND deleted = 0")
    int selectCountByCategoryId(Long categoryId);
    
}