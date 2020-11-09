package com.atguigu.serviceedu.entity.chaptervo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//章节
@Data
public class ChapterVo {
    private String id;
    private String title;
    private List<VideoVo> children = new ArrayList<>();
}
