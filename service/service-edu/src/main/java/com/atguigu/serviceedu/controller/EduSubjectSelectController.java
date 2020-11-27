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

    //递归删除课程列表
    @DeleteMapping("deleteSubject/{id}")
    public R deleteSubject(@PathVariable String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        this.deleteChildren(id, idList);
        subjectService.removeByIds(idList);
        return R.ok();
    }

    private void deleteChildren(String id, List<String> idList) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<EduSubject> children = subjectService.list(wrapper);
        children.stream().forEach(item -> {
            String itemId = item.getId();
            System.out.println("itemId = " + itemId);
            idList.add(itemId);
            this.deleteChildren(item.getId(), idList);
        });
    }

    //--------------------------------------------------------------------------------
    //递归查询课程列表
    @GetMapping("selectAllSubject")
    public R selectAllSubject() {
        List<EduSubject> alltList = subjectService.list(null);
        List<EduSubject> result = this.build(alltList);
        return R.ok().data("result", result);
    }

    private List<EduSubject> build(List<EduSubject> alltList) {
        List<EduSubject> oneList = new ArrayList<>();
        alltList.stream().forEach(node -> {
            if (node.getParentId().equals("0")) {
                node.setLevel(1);
                oneList.add(this.selecyChildren(node, alltList));
            }
        });
        return oneList;
    }

    private EduSubject selecyChildren(EduSubject node, List<EduSubject> alltList) {
        alltList.stream().forEach(item -> {
            if (item.getParentId().equals(node.getId())) {
                item.setLevel(node.getLevel() + 1);
                node.getChildren().add(this.selecyChildren(item, alltList));
            }
        });
        return node;
    }
}
