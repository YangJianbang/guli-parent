package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.Offset;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-20
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/serviceedu/teacher")
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("updateById")
    public R updateById(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getById/{id}")
    public R getById(@PathVariable String id) {

        EduTeacher eduTeacher = eduTeacherService.getById(id);
//        System.out.println("eduTeacher = " + eduTeacher);

        if (eduTeacher != null) {
            return R.ok().data("item", eduTeacher);
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public R save(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.save(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @ApiOperation(value = "条件查询，讲师分页")
    @PostMapping("pageQuery/{page}/{limit}")
    public R pageQuery(
                       @PathVariable Long page,
                       @PathVariable Long limit,
                       @RequestBody(required = false)
                               TeacherQuery teacherQuery) {
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        eduTeacherService.pageQuery(pageParam, teacherQuery);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageList/{page}/{limit}")
    public R pageList(@ApiParam(name = "page", value = "当前页码", required = true)
                      @PathVariable Long page,
                      @ApiParam(name = "limit", value = "每页记录数", required = true)
                      @PathVariable Long limit) {
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        eduTeacherService.page(pageParam, null);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("rows", records).data("total", total);
    }


    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public R list() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id) {
//        boolean flag = eduTeacherService.removeById(id);
        //优化
        boolean flag = eduTeacherService.deleteTeacherById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

