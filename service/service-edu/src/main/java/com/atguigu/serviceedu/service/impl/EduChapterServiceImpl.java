package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.entity.chaptervo.VideoVo;
import com.atguigu.serviceedu.mapper.EduChapterMapper;
import com.atguigu.serviceedu.mapper.EduVideoMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Resource
    EduVideoMapper videoMapper;

    @Override
    public List<ChapterVo> getAllChapterVideo(String id) {
        //根据ID获取章节信息
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        List<EduChapter> chapters = baseMapper.selectList(wrapper);
        //根据ID获取小节信息
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", id);
        List<EduVideo> videos = videoMapper.selectList(wrapper1);

//        将章节信息封装到业务bean中
        List<ChapterVo> chapterVoList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            EduChapter chapter = chapters.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);
//            将小节信息封装到业务bean中
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < videos.size(); j++) {
                EduVideo video = videos.get(j);
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }
        return chapterVoList;
    }

    @Override
    public void deleteChapterById(String id) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("chapter_id", id);
        List<EduVideo> videos = videoMapper.selectList(wrapper);
        if (videos.size() > 0) {
            throw new GuliException(20001, "该章节存在小节，无法删除");
        } else {
            baseMapper.deleteById(id);
        }
    }

    @Override
    public void deleteChapterByCourseId(String id) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        baseMapper.delete(wrapper);
    }

}
