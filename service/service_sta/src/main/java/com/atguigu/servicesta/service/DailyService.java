package com.atguigu.servicesta.service;

import com.atguigu.servicesta.entity.Daily;
import com.atguigu.servicesta.entity.vo.DailyVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
public interface DailyService extends IService<Daily> {

    void createStatisticsByDate(String day);

    Map<String, Object> showChart(DailyVo dailyVo);
}
