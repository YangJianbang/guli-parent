package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/orderservice/t-pay-log")
@CrossOrigin
public class TPayLogController {
    @Autowired
    TPayLogService payLogService;

    //根据订单号查询支付结果
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
//        调用微信接口查询支付结果
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        if (map == null) {
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {
            //        更新订单状态，添加支付记录
            payLogService.updateOrderStates(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("正在支付");
    }

    //根据订单号 生成支付二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

}

