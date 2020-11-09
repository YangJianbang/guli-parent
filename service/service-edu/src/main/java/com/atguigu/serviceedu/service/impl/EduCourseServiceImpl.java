package com.atguigu.serviceedu.service.impl;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduCourseDescription;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.vo.CourseIndexInfoVo;
import com.atguigu.serviceedu.entity.vo.CourseInfoVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQuery;
import com.atguigu.serviceedu.mapper.EduCourseMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseDescriptionService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    EduVideoService videoService;
    @Autowired
    EduChapterService chapterService;

    @Override
    public String addCourseInfo(CourseInfoVo courseInfoVo) {
        //1.添加课程信息到课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0) {
            throw new GuliException(20001, "添加课程信息失败");
        }
        //2.添加描述信息到描述表
        //获取课程表中课程ID
        String eduCourseId = eduCourse.getId();

        EduCourseDescription eduDescription = new EduCourseDescription();
        String description = courseInfoVo.getDescription();
        eduDescription.setDescription(description);

        //将课程表中的课程ID赋值给描述表中的课程描述ID 保证两个表是一对一
        eduDescription.setId(eduCourseId);

        eduCourseDescriptionService.save(eduDescription);
        return eduCourseId;
    }

    @Override
    public CourseInfoVo getCourseInfo(String id) {
//        先查询课程表
        CourseInfoVo courseInfoVo = new CourseInfoVo();

        EduCourse eduCourse = baseMapper.selectById(id);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        EduCourseDescription description = eduCourseDescriptionService.getById(id);
        BeanUtils.copyProperties(description, courseInfoVo);
        return courseInfoVo;
    }

    @Override
    public void updateCourse(CourseInfoVo courseInfoVo) {
        //修改课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int flag = baseMapper.updateById(eduCourse);
        //如果修改失败抛出异常
        if (flag <= 0) {
            throw new GuliException(20001, "课程信息修改异常");
        }
        //修改课程简介
        EduCourseDescription description = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, description);
        eduCourseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo getConfirmCourseInfo(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublicInfo(id);
        return coursePublishVo;
    }

    @Override
    public void getCoursetoPage(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");

        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, wrapper);
        }
        String status = courseQuery.getStatus();

        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        String title = courseQuery.getTitle();
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void deleteCourse(String id) {
//        先删除小节->章节->课程简介->课程信息
        videoService.deleteVideoByCourseId(id);

        chapterService.deleteChapterByCourseId(id);

        eduCourseDescriptionService.removeById(id);

        int flag = baseMapper.deleteById(id);

        if (flag<=0){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    @Cacheable(value = "course",key = "'hostCourse'")
    @Override
    public List<EduCourse> getHostCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("buy_count");
        wrapper.last("limit 4");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public CourseIndexInfoVo getCourseIndexInfoVo(String id) {
        return baseMapper.getCourseIndexInfoVo(id);
    }

}
