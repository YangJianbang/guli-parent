package com.atguigu.serviceedu.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceedu/frontteacher")
public class EduTeacherFrontController {
    @Autowired
    EduTeacherService teacherService;
    @Autowired
    EduCourseService courseService;

    //获取名师
    @GetMapping("getTeacher")
    public R getTeacher() {
        List<EduTeacher> teacherList = teacherService.getTeacher();
        return R.ok().data("teacher", teacherList);
    }

    //    根据ID查询讲师
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id) {
        //先根据讲师ID查询讲师信息
        EduTeacher teacher = teacherService.getById(id);
        //再根据讲师ID查询课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        wrapper.orderByDesc("buy_count");
        wrapper.last("limit 4");
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

    //分页查询所有讲师
    @GetMapping("selectAllTeacher/{page}/{limit}")
    public R selectAllTeacher(@PathVariable Long page,
                              @PathVariable Long limit) {
        Page<EduTeacher> teacherPage = new Page<>(page, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("level");
        teacherService.page(teacherPage, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", teacherPage.getRecords());
        map.put("total", teacherPage.getTotal());
        map.put("current", teacherPage.getCurrent());
        map.put("size", teacherPage.getSize());
        map.put("pages", teacherPage.getPages());
        map.put("hasNext", teacherPage.hasNext());
        map.put("hasPrevious", teacherPage.hasPrevious());
        return R.ok().data(map);
    }
}
