package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseOrder;
import com.atguigu.commonutils.vo.MemberOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UsersClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/orderservice/t-order")
@CrossOrigin
public class TOrderController {

    @Autowired
    TOrderService tOrderService;
    @Autowired
    EduClient eduClient;
    @Autowired
    UsersClient usersClient;

    //根据课程ID 用户ID 支付状态查询订单信息
    @GetMapping("isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable String memberId,
                               @PathVariable String courseId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status",1);
        int count = tOrderService.count(wrapper);
        return count>0;
    }

    //根据订单号获取订单信息
    @GetMapping("getOrderByOrderNo/{orderNo}")
    public R getOrderByOrderNo(@PathVariable String orderNo) {
        TOrder order = tOrderService.getOrderByOrderNo(orderNo);
        return R.ok().data("order", order);
    }

    //根据课程id和用户id创建订单，返回订单所需数据
    @PostMapping("createOrder/{id}")
    public R createOrder(@PathVariable String id, HttpServletRequest request) {
        CourseOrder courseOrder = eduClient.geCourseOrdertCourseInfoOrder(id);

        String uid = JwtUtils.getMemberIdByJwtToken(request);
        if (uid == null || uid == "") {
            throw new GuliException(20001, "请登录账号");
        }

        MemberOrder memberOrder = usersClient.getInfoUsers(uid);

        TOrder tOrder = new TOrder();
        //设置订单号
        String orderNo = OrderNoUtil.getOrderNo();
        tOrder.setOrderNo(orderNo);
        //设置订单中的课程信息
        tOrder.setCourseId(id);
        tOrder.setCourseTitle(courseOrder.getTitle());
        tOrder.setCourseCover(courseOrder.getCover());
        tOrder.setTeacherName(courseOrder.getTeacherName());
        tOrder.setTotalFee(courseOrder.getPrice());
        //设置订单中的用户信息
        tOrder.setMemberId(uid);
        tOrder.setNickname(memberOrder.getNickname());
        tOrder.setMobile(memberOrder.getMobile());
        tOrder.setPayType(1);
        tOrder.setStatus(0);
        tOrderService.save(tOrder);
        System.out.println("tOrder = " + tOrder);
        return R.ok().data("orderId", orderNo);
    }
}

