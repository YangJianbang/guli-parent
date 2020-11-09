package com.atguigu.serviceedu.controller;


import com.alibaba.excel.EasyExcel;
import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.subjectvo.OneSubjectVo;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.handler.ExcelListener;
import com.atguigu.serviceedu.service.EduSubjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-26
 */
@Api(description = "课程分类")
@RestController
@RequestMapping("/serviceedu/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    ExcelListener excelListener;
    @Autowired
    EduSubjectService eduSubjectService;

    //课程列表
    @GetMapping("getAllSubject")
    public R getAllSubject() {
       List<OneSubjectVo> subjects = eduSubjectService.getAllSubject();
       return R.ok().data("subjects",subjects);
    }

    //添加课程分类
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectReadVo.class, excelListener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok();
    }

}

