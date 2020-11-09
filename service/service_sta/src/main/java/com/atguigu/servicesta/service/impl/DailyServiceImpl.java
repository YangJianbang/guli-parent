package com.atguigu.servicesta.service.impl;

import com.atguigu.servicesta.client.UcenterClient;
import com.atguigu.servicesta.entity.Daily;
import com.atguigu.servicesta.entity.vo.DailyVo;
import com.atguigu.servicesta.mapper.DailyMapper;
import com.atguigu.servicesta.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
    @Autowired
    UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDate(String day) {
        System.out.println("ServiceImpl:day = " + day);
        QueryWrapper<Daily> wrapper = new QueryWrapper();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        Integer registerNum = (Integer) ucenterClient.registerCount(day).getData().get("countRegister");//注册数
        System.out.println("registerNum = " + registerNum);
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO 登录数
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO 播放数
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO 新增课程数

        Daily daily = new Daily();
        daily.setCourseNum(courseNum);
        daily.setLoginNum(loginNum);
        daily.setRegisterNum(registerNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setDateCalculated(day);
//        daily.setGmtCreate(new Date());
//        daily.setGmtModified(new Date());
        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> showChart(DailyVo dailyVo) {
        String begin = dailyVo.getBegin();
        String end = dailyVo.getEnd();
        String type = dailyVo.getType();

        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("date_calculated");
        wrapper.select(type, "date_calculated");
        wrapper.between("date_calculated", begin, end);
        List<Daily> dailyList = baseMapper.selectList(wrapper);

        Map<String, Object> map = new HashMap<>();

        List<String> dateList = new ArrayList<>();//统计日期
        List<Integer> dataList = new ArrayList<>();//type对应的数据

        for (int i = 0; i < dailyList.size(); i++) {
            Daily daily = dailyList.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        map.put("date_calculatedList", dateList);
        map.put("dataList", dataList);
        return map;
    }
}
