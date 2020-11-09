package com.atguigu.serviceedu.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.mapper.EduVideoMapper;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    VodClient vodClient;

    @Override
    public void deleteVideoByCourseId(String id) {
        //查询课程中所以的视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        List<EduVideo> videos = baseMapper.selectList(wrapper);

        List<String> videoIds = new ArrayList();

        for (EduVideo video : videos) {
            String videoSourceId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }
        if (videoIds.size() > 0) {
            String videoList = org.apache.commons.lang.StringUtils.join(videoIds.toArray(), ",");
            System.out.println("EDU:videoList = " + videoList);
            R r = vodClient.deleteBatch(videoList);
            if (r.getCode()!=20000){
                throw new GuliException(20001,"time out");
            }
        }

        baseMapper.delete(wrapper);
    }
}
