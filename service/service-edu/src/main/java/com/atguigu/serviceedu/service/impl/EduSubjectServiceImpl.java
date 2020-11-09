package com.atguigu.serviceedu.service.impl;

import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.subjectvo.OneSubjectVo;
import com.atguigu.serviceedu.entity.subjectvo.TwoSubjectVo;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-26
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<OneSubjectVo> getAllSubject() {
        //获取所有一级
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", "0");
        List<EduSubject> oneSubjects = baseMapper.selectList(wrapper1);
        //获取所有二级
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id", "0");
        List<EduSubject> twoSubject = baseMapper.selectList(wrapper2);
        //封装最终数据
        List<OneSubjectVo> oneSubjectVos = new ArrayList<>();
//        --------------------------------------------------
//        遍历一级分类的集合
        for (int i = 0; i < oneSubjects.size(); i++) {
//            获取一级分类集合的每个对象
            EduSubject oneEduSubject = oneSubjects.get(i);

//            把一级分类的对象封装到一级分类业务bean中
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
//            oneSubjectVo.setId(oneEduSubject.getId());
//            oneSubjectVo.setTitle(oneEduSubject.getTitle());
            //工具类BeanUtils的copyProperties可以执行上面的封装功能
            BeanUtils.copyProperties(oneEduSubject, oneSubjectVo);

//            把封装的对象存入一级对象集合
            oneSubjectVos.add(oneSubjectVo);

            //封装一级中的二级分类
//            创建二级分类集合
            List<TwoSubjectVo> twoSubjectVos = new ArrayList<>();
//            遍历二级分类
            for (int j = 0; j < twoSubject.size(); j++) {
//                获取到二级分类的对象
                EduSubject twoEduSubject = twoSubject.get(j);
                //判断二级分类是否为上面一级分类的子分类
                if (oneEduSubject.getId().equals(twoEduSubject.getParentId())) {
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
//                    把二级对象封装进业务bean对象中
                    BeanUtils.copyProperties(twoEduSubject, twoSubjectVo);
//                    把封装后的对象存入二级集合
                    twoSubjectVos.add(twoSubjectVo);
                }
            }
//            将二级对象的集合封装进对应的一级对象集合中
            oneSubjectVo.setChildren(twoSubjectVos);
        }
        return oneSubjectVos;
    }
}
