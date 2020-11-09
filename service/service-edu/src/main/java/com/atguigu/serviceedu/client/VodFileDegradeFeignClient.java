package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;
//当edu调用vod超时 会调用该方法
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteVideo(String id) {
        return R.error().message("time out");
    }

    @Override
    public R deleteBatch(String videoList) {
        return R.error().message("time out");
    }
}
