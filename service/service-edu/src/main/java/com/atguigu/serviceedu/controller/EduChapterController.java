package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.service.EduChapterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Api(description = "章节管理")
@RestController
@RequestMapping("/serviceedu/chapter")
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id) {
        EduChapter chapter = chapterService.getById(id);
        return R.ok().data("chapter",chapter);
    }

    @PostMapping("saveChapter")
    public R saveChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return R.ok();
    }



    @DeleteMapping("deleteChapter/{id}")
    public R deleteChapter(@PathVariable String id) {
        chapterService.deleteChapterById(id);
        return R.ok();
    }

    //查询课程中的章节和小结
    @GetMapping("getChapterVideoByCourse/{id}")
    public R getChapterVideoByCourse(@PathVariable String id) {
        List<ChapterVo> chapterVos = chapterService.getAllChapterVideo(id);
        return R.ok().data("chapterVideo", chapterVos);
    }
}

