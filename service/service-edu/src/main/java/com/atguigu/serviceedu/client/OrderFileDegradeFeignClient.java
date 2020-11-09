package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class OrderFileDegradeFeignClient implements OrderClient{

    @Override
    public boolean isBuyCourse(String memberId, String courseId) {
        return false;
    }
}
