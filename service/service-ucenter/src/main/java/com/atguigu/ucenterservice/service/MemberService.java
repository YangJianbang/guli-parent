package com.atguigu.ucenterservice.service;

import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
public interface MemberService extends IService<Member> {

    String loginUser(LoginVo loginVo);

    void registerUser(RegisterVo registerVo);

    Member getByOpenid(String openid);
}
