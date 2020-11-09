package com.atguigu.servicecms.service.impl;

import com.atguigu.servicecms.entity.CrmBanner;
import com.atguigu.servicecms.mapper.CrmBannerMapper;
import com.atguigu.servicecms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    RedisTemplate redisTemplate;

    //    @Override
//    public List<CrmBanner> selectBanner() {
//
//        //先在缓存中查询，没有数据再去查询数据库
////        redis 的key:banner
//        List<CrmBanner> crmBannerList = (List<CrmBanner>) redisTemplate.opsForValue().get("banner");
//        if (crmBannerList == null) {
//            QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
//            wrapper.orderByDesc("gmt_create");
//            wrapper.last("limit 3");
//            crmBannerList = baseMapper.selectList(wrapper);
//            redisTemplate.opsForValue().set("banner", crmBannerList);
//        }
//        return crmBannerList;
//    }

    @Cacheable(value = "banner",key = "'selectBanner'")
    @Override
    public List<CrmBanner> selectBanner() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        wrapper.last("limit 3");
        List<CrmBanner> crmBannerList = baseMapper.selectList(wrapper);
        return crmBannerList;
    }


}
