package com.atguigu.serviceedu.entity.chaptervo;

import lombok.Data;


//小节
@Data
public class VideoVo {
    private String id;
    private String title;
    private Boolean isFree;
    private String videoSourceId;
}
