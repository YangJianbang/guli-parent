package com.atguigu.servicecms.service;

import com.atguigu.servicecms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> selectBanner();
}
