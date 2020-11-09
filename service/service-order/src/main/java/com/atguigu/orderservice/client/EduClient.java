package com.atguigu.orderservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-edu")
@Component
public interface EduClient {
    @GetMapping("/serviceedu/frontcourse/getCourseInfoOrders/{id}")
    public CourseOrder geCourseOrdertCourseInfoOrder(@PathVariable("id") String id);
}
