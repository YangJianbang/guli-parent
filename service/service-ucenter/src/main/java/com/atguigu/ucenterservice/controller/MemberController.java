package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.MemberOrder;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/ucenter/member")
@CrossOrigin
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberOrder memberOrder;

    //根据用户ID获取用户信息
    @GetMapping("getInfoUc/{id}")
    public MemberOrder getInfoUsers(@PathVariable String id) {
        Member member = memberService.getById(id);
        BeanUtils.copyProperties(member, memberOrder);
        return memberOrder;
    }

    //根据路径中的token 获取用户信息
    @GetMapping("getLoginInfo")
    public R getLoginUser(HttpServletRequest request) {
        //根据请求头中的token获取 用户ID
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        Member member = memberService.getById(memberId);
        return R.ok().data("member", member);
    }

    //注册
    @PostMapping("registerUser")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.registerUser(registerVo);
        return R.ok();
    }

    //登录
    @PostMapping("loginUser")
    public R loginUser(@RequestBody LoginVo loginVo) {
        String token = memberService.loginUser(loginVo);
        return R.ok().data("token", token);
    }
}

