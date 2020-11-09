package com.voddemo;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class test1 {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    public static void getVideoPlayAuth() throws ClientException {
        DefaultAcsClient client = initVodClient("LTAI4G3osvMnDknAciQogBLe", "t3yz9HLvMZky7l5srIY6kdJJw1T4Sd");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        request.setVideoId("dee9107ec15c4408baf65eae034d681d");
        response = client.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        System.out.println("playAuth = " + playAuth);
    }

    public static void getVideoPlayURL() throws ClientException {
        DefaultAcsClient client = initVodClient("LTAI4G3osvMnDknAciQogBLe", "t3yz9HLvMZky7l5srIY6kdJJw1T4Sd");
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        request.setVideoId("dee9107ec15c4408baf65eae034d681d");
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> list = response.getPlayInfoList();
        for (GetPlayInfoResponse.PlayInfo playInfo : list) {
            System.out.println("playInfo = " + playInfo.getPlayURL());
        }

    }

    public static void uoloadVideo() {
        String accessKeyId = "LTAI4G3osvMnDknAciQogBLe";
        String accessKeySecret = "t3yz9HLvMZky7l5srIY6kdJJw1T4Sd";

        String title = "dancing";   //上传之后文件名称
        String fileName = "E:/dancing.mp4";  //本地文件路径和名称
        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.println("1111");
            System.out.print("VideoId=" + response.getVideoId());
        } else {
            System.out.println("2222");
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId());
        }
    }

    public static void main(String[] args) throws Exception {
        getVideoPlayAuth();
//        getVideoPlayURL();
//        uoloadVideo();
    }
}
