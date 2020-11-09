package com.atguigu.serviceedu.mapper;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseIndexInfoVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getCoursePublicInfo(String id);

    CourseIndexInfoVo getCourseIndexInfoVo(String id);
}
