package com.atguigu.servicecms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicecms.entity.CrmBanner;
import com.atguigu.servicecms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
@RestController
@RequestMapping("/servicecms/banner")
public class CrmBannerController {
    @Autowired
    CrmBannerService crmBannerService;

    //    查询幻灯片
    @GetMapping("selectBanner")
    public R selectBanner() {
        List<CrmBanner> crmBannerList = crmBannerService.selectBanner();
        return R.ok().data("crmBannerList",crmBannerList);
    }
}

