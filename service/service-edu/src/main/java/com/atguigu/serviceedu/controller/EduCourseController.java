package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQuery;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Api(description = "课程信息管理")
@RestController
@RequestMapping("/serviceedu/course")
public class EduCourseController {
    @Autowired
    EduCourseService eduCourseService;

    @DeleteMapping("deleteCourse/{id}")
    public R deleteCourse(@PathVariable String id){
        eduCourseService.deleteCourse(id);
        return R.ok();
    }


    @GetMapping("getCoursetoPage/{page}/{limit}")
    public R getCoursetoPage(@PathVariable Long page,
                             @PathVariable Long limit,
                             CourseQuery courseQuery) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        eduCourseService.getCoursetoPage(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    //    根据课程ID发布课程
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus("Normal");
        eduCourseService.updateById(course);
        return R.ok();
    }

    //    根据课程Id查询信息
    @GetMapping("getConfirmCourseInfo/{id}")
    public R getConfirmCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.getConfirmCourseInfo(id);
        return R.ok().data("coursePublishVo", coursePublishVo);
    }

    //修改课程信息
    @PostMapping("updateCourse")
    public R updateCourse(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourse(courseInfoVo);
        return R.ok();
    }

    //根据Id查询课程信息
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable("id") String id) {
        CourseInfoVo courseInfo = eduCourseService.getCourseInfo(id);
        return R.ok().data("courseInfo", courseInfo);
    }

    //添加课程的基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String courseId = eduCourseService.addCourseInfo(courseInfoVo);
        return R.ok().data("courseId", courseId);
    }
}

