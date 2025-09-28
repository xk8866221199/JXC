package com.jxc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxc.entity.SalesOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单数据访问层
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {
    
}