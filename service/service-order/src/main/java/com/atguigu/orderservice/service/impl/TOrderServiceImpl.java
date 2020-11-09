package com.atguigu.orderservice.service.impl;

import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Override
    public TOrder getOrderByOrderNo(String orderNo) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper();
        wrapper.eq("order_no",orderNo);
        TOrder order = baseMapper.selectOne(wrapper);
        return order;
    }
}
