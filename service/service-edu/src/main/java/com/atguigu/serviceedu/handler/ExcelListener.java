package com.atguigu.serviceedu.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExcelListener extends AnalysisEventListener<SubjectReadVo> {
    @Autowired
    EduSubjectService subjectService;

    @Override
    public void invoke(SubjectReadVo subjectReadVo, AnalysisContext analysisContext) {
//        System.out.println("subjectReadVo = " + subjectReadVo);
//        添加一级分类 取出一级分类名称
        String oneLevelSubject = subjectReadVo.getOneLevelSubject();
//        判断一级分类是否重复
        EduSubject oneSubject = this.existOneSubject(oneLevelSubject);
        if (oneSubject == null) {
            //        调用service方法添加一级分类到数据中
            oneSubject = new EduSubject();
            oneSubject.setTitle(oneLevelSubject);
            oneSubject.setParentId("0");//一级分类 父ID=0
            oneSubject.setSort(0);
            subjectService.save(oneSubject);
        }
        //获取一级分类的ID
        String pid = oneSubject.getId();
        //--------------------------------------------------------------
//        添加二级分类
        String twoLevelSubject = subjectReadVo.getTwoLevelSubject();
        EduSubject twoSubject = this.existTwoSubject(twoLevelSubject, pid);
        if (twoSubject == null) {
            twoSubject = new EduSubject();
            twoSubject.setTitle(twoLevelSubject);
            twoSubject.setParentId(pid);
            twoSubject.setSort(0);
            subjectService.save(twoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    //一级分类是否重复
    private EduSubject existOneSubject(String oneName) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", oneName);
        queryWrapper.eq("parent_id", "0");
        EduSubject subject = subjectService.getOne(queryWrapper);
        return subject;
    }

    //二级分类是否重复
    private EduSubject existTwoSubject(String twoName, String pid) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", twoName);
        queryWrapper.eq("parent_id", pid);
        EduSubject subject = subjectService.getOne(queryWrapper);
        return subject;
    }
}
