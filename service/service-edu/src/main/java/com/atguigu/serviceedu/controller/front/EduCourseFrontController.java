package com.atguigu.serviceedu.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseOrder;
import com.atguigu.serviceedu.client.OrderClient;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduCourseDescription;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.entity.vo.CourseIndexInfoVo;
import com.atguigu.serviceedu.entity.vo.CourseIndexVo;
import com.atguigu.serviceedu.entity.vo.CourseInfoVo;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseDescriptionService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceedu/frontcourse")
public class EduCourseFrontController {
    @Autowired
    EduCourseService courseService;
    @Autowired
    EduChapterService chapterService;
    @Autowired
    CourseOrder courseOrder;
    @Autowired
    OrderClient orderClient;

    //返回订单需要的课程信息
    @GetMapping("getCourseInfoOrders/{id}")
    public CourseOrder geCourseOrdertCourseInfoOrder(@PathVariable String id) {
        CourseIndexInfoVo courseIndexInfoVo = courseService.getCourseIndexInfoVo(id);
        BeanUtils.copyProperties(courseIndexInfoVo, courseOrder);
        return courseOrder;
    }


    //根据课程ID查询课程详细信息
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id, HttpServletRequest request) {
        CourseIndexInfoVo courseIndexInfoVo = courseService.getCourseIndexInfoVo(id);
        List<ChapterVo> allChapterVideo = chapterService.getAllChapterVideo(id);
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == null || memberId == "") {
            return R.ok().
                    data("courseIndexInfoVo", courseIndexInfoVo).
                    data("allChapterVideo", allChapterVideo);
        } else {
            boolean isBuyCourse = orderClient.isBuyCourse(memberId, id);
            return R.ok().
                    data("courseIndexInfoVo", courseIndexInfoVo).
                    data("allChapterVideo", allChapterVideo).
                    data("isBuyCourse", isBuyCourse);
        }
    }


    //    查询热门课程
    @GetMapping("getHostCourse")
    public R getHostCourse() {
        List<EduCourse> courseList = courseService.getHostCourse();
        return R.ok().data("hotCourse", courseList);
    }

    //条件查询全部课程
    @PostMapping("getAllCourse/{page}/{limit}")
    public R getAllCourse(@PathVariable Long page,
                          @PathVariable Long limit,
                          @RequestBody(required = false) CourseIndexVo courseIndexVo) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseIndexVo.getSubjectParentId())) {
            wrapper.eq("subject_parent_id", courseIndexVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseIndexVo.getSubjectId())) {
            wrapper.eq("subject_id", courseIndexVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseIndexVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }
        if (!StringUtils.isEmpty(courseIndexVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseIndexVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }
        courseService.page(pageParam, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageParam.getRecords());
        map.put("total", pageParam.getTotal());
        map.put("current", pageParam.getCurrent());
        map.put("size", pageParam.getSize());
        map.put("pages", pageParam.getPages());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }
}
