package com.atguigu.servicesta.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicesta.client.UcenterClient;
import com.atguigu.servicesta.entity.Daily;
import com.atguigu.servicesta.entity.vo.DailyVo;
import com.atguigu.servicesta.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/servicesta/daily")
public class DailyController {
    @Autowired
    DailyService dailyService;

    //获取图表显示所需要的数据
    @PostMapping("showChart")
    public R showChart(@RequestBody DailyVo dailyVo) {
        Map<String, Object> map = dailyService.showChart(dailyVo);
        return R.ok().data(map);
    }

    //添加统计某天注册人数的数据
    @PostMapping("createStatisticsByDate/{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        System.out.println("Controller:day = " + day);
        dailyService.createStatisticsByDate(day);
        return R.ok();
    }
}

