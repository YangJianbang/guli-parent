package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getAllChapterVideo(String id);

    void deleteChapterById(String id);


    void deleteChapterByCourseId(String id);
}
