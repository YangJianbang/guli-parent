package com.atguigu.serviceedu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api("递归调用")
@RestController
@RequestMapping("/serviceedu/subjectSelect")
public class EduSubjectSelectController {
    @Autowired
    EduSubjectService subjectService;


    //递归调用删除
    @DeleteMapping("removeSubject/{id}")
    public R removeSubject(@PathVariable String id) {
        this.removeSubjectById(id);
        return R.ok();
    }

    private void removeSubjectById(String id) {
        List<String> idList = new ArrayList<>();
        this.deleteChildren(id, idList);
        idList.add(id);
        subjectService.removeByIds(idList);

    }

    private void deleteChildren(String id, List<String> idList) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<EduSubject> childrenList = subjectService.list(wrapper);
        childrenList.stream().forEach(itme -> {
            idList.add(itme.getId());
            this.deleteChildren(itme.getId(), idList);
        });
    }


    //    ------------------------------------------------------
    //递归调用获得课程列表
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        List<EduSubject> allList = subjectService.list(null);
        List<EduSubject> result = this.build(allList);
        return R.ok().data("result", result);
    }

    private List<EduSubject> build(List<EduSubject> allList) {
        List<EduSubject> findAll = new ArrayList<>();

        allList.stream().forEach(node -> {
            if (node.getParentId().equals("0")) {
                node.setLevel(1);
                findAll.add(this.selectChildren(node, allList));
            }
        });
        return findAll;
    }

    private EduSubject selectChildren(EduSubject node, List<EduSubject> allList) {
        allList.stream().forEach(itme -> {
            if (itme.getParentId().equals(node.getId())) {
                itme.setLevel(node.getLevel() + 1);
                node.getChildren().add(this.selectChildren(itme, allList));
            }
        });
        return node;
    }

}
