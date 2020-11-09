package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.MemberMapper;
import com.atguigu.ucenterservice.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public String loginUser(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "账号或密码为空");
        }
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(wrapper);
        if (member == null) {
            throw new GuliException(20001, "该账号未注册");
        }
        if (!member.getPassword().equals(MD5.encrypt(password))) {
            throw new GuliException(20001, "账号或密码错误");
        }
        if (member.getIsDisabled()) {
            throw new GuliException(20001, "该账户被禁用");
        }
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return token;
    }

    @Override
    public void registerUser(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        System.out.println("code = " + code);
        if (StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "注册信息为空");
        }

        String codeRedis = redisTemplate.opsForValue().get(mobile);
        System.out.println("codeRedis = " + codeRedis);
        if (!code.equals(codeRedis)) {
            throw new GuliException(20001, "验证码不一致");
        }

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer integer = baseMapper.selectCount(wrapper);
        System.out.println("integer = " + integer);
        if (integer > 0) {
            throw new GuliException(20001, "该手机号已注册");
        }
        Member member = new Member();
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setNickname(nickname);
        member.setIsDisabled(false);
        member.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1604482007304&di=6d2891c1cb3297fe71803e7ee7c04300&imgtype=0&src=http%3A%2F%2F05.imgmini.eastday.com%2Fmobile%2F20180507%2F20180507100239_588253f75a794ea09b82b9d43db00f89_1.jpeg");
        int insert = baseMapper.insert(member);
        System.out.println("insert = " + insert);
    }

    @Override
    public Member getByOpenid(String openid) {
        System.out.println("111111");
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        Member member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer registerCount(String day) {
        System.out.println("=======registerCount");
        Integer count = baseMapper.selectRegisterCount(day);
        System.out.println("count = " + count);
        return count;
    }
}
