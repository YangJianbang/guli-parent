package com.atguigu.serviceedu.entity.subjectvo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneSubjectVo {
    private String id;
    private String title;
    //    一级分类中有多个二级分类
    private List<TwoSubjectVo> children = new ArrayList<>();

}
