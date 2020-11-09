package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
//当edu调用vod超时后会执行VodFileDegradeFeignClient类中的方法
public interface VodClient {
    @DeleteMapping(value = "/eduvod/vod/deleteVideo/{id}")
    public R deleteVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/vod/deleteBatch")
    public R deleteBatch(@RequestParam("videoList") String videoList);
}
