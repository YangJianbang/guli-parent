package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Api(description = "小节分类")
@RestController
@RequestMapping("/serviceedu/video")
public class EduVideoController {
    @Autowired
    EduVideoService videoService;
    @Autowired
    VodClient vodClient;

    //    修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }

    //根据小节ID获得小节
    @GetMapping("getVideo/{id}")
    public R getVideo(@PathVariable String id) {
        EduVideo video = videoService.getById(id);
        return R.ok().data("video", video);
    }

    //根据小节ID删除小节
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id) {
        System.out.println("11111");
        EduVideo video = videoService.getById(id);
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            R r = vodClient.deleteVideo(videoSourceId);
            if (r.getCode()!=20000){
                throw new GuliException(20001,"timeout");
            }
        }
        videoService.removeById(id);
        return R.ok();
    }

    //    添加小节
    @PostMapping("saveVideo")
    public R saveVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }
}

