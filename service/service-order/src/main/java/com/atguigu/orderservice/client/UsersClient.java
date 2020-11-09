package com.atguigu.orderservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.MemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UsersClient {

    @GetMapping("/ucenter/member/getInfoUc/{id}")
    public MemberOrder getInfoUsers(@PathVariable("id") String id);
}
