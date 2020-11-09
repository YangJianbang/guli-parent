package com.atguigu.serviceedu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Component
@FeignClient(name = "service-order",fallback = OrderFileDegradeFeignClient.class)
public interface OrderClient {
    @GetMapping("/orderservice/t-order/isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable("memberId") String memberId, @PathVariable("courseId") String courseId);
}
