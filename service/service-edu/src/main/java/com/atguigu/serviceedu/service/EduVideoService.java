package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
public interface EduVideoService extends IService<EduVideo> {

    void deleteVideoByCourseId(String id);
}
