package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmApiService;
import com.atguigu.msmservice.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/edumsm/msm")
public class MsmApiController {

    @Autowired
    MsmApiService msmApiService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    //    获取手机号，发送验证码
    @GetMapping("/send/{phone}")
    public R sendPhone(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = RandomUtil.getFourBitRandom();
        boolean flag = msmApiService.sendPhone(phone, code);
        if (flag) {
//            设置验证码时长为5分钟
            redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信验证码发送失败");
        }
    }
}
