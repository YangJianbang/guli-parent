package com.atguigu.vod.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.AccessKeyUtil;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.vod.utils.VideoUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/eduvod/vod")
public class VodController {


    //根据视频ID获取播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) {
        try {
            System.out.println("id = " + id);
            DefaultAcsClient client = VideoUtils.initVodClient(AccessKeyUtil.ACCESSKEY_ID, AccessKeyUtil.ACCESSKEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(id);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth", playAuth);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取视频凭证失败");
        }

    }

    //    删除多个视频
    @DeleteMapping("deleteBatch")
    public R deleteBatch(String videoList) {
//        System.out.println("VOD:videoList = " + videoList);
        try {
            //1 创建初始化对象
            DefaultAcsClient client =
                    VideoUtils.initVodClient(AccessKeyUtil.ACCESSKEY_ID, AccessKeyUtil.ACCESSKEY_SECRET);
            //2 创建删除视频request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3 向request设置视频id
            request.setVideoIds(videoList);
            //4 调用初始化对象的方法删除
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }

    //    根据视频ID删除视频
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id) {
        try {
            //1 创建初始化对象
            DefaultAcsClient client =
                    VideoUtils.initVodClient(AccessKeyUtil.ACCESSKEY_ID, AccessKeyUtil.ACCESSKEY_SECRET);
            //2 创建删除视频request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3 向request设置视频id
            request.setVideoIds(id);
            //4 调用初始化对象的方法删除
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }

    //    上传视频
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename(); //文件实际名称
            // abcd.qw.cc.mp4
            //一般经常传递文件名称不带后缀名
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(AccessKeyUtil.ACCESSKEY_ID, AccessKeyUtil.ACCESSKEY_SECRET,
                    title, fileName, inputStream);

            // request.setEcsRegionId("cn-shanghai");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = "";
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return R.ok().data("videoId", videoId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "上传视频失败");
        }
    }
}
