package com.atguigu.servicesta.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter")
public interface UcenterClient {
    @GetMapping("/ucenter/member/registerCount/{day}")
    public R registerCount(@PathVariable("day") String day);
}
