package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.Member;
import com.atguigu.ucenterservice.service.MemberService;
import com.atguigu.ucenterservice.utils.ConstantPropertiesUtil;
import com.atguigu.ucenterservice.utils.HttpClientUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.HashMap;


@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    MemberService memberService;

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {
        //得到授权临时票据code
        System.out.println("code = " + code);
        System.out.println("state = " + state);
        try {

            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);

            //使用过HttpClientUtils请求这个地址，得到accesstoken
            String accessToken = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken = " + accessToken);

            //解析json字符串
            Gson gson = new Gson();
            HashMap hashMap = gson.fromJson(accessToken, HashMap.class);
            String access_token = (String) hashMap.get("access_token");
            String openid = (String) hashMap.get("openid");

            Member member = memberService.getByOpenid(openid);
            if (member == null) {
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

                String resultUserInfo = HttpClientUtils.get(userInfoUrl);

                HashMap map = gson.fromJson(resultUserInfo, HashMap.class);
                String nickname = (String) map.get("nickname");
                String headimgurl = (String) map.get("headimgurl");
                member = new Member();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token=" + token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取用户信息失败");
        }
    }

    //    生成二维码地址
    @GetMapping("login")
    public String login() {
        String baseUrl  = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String wxOpenRedirectUrl  = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            wxOpenRedirectUrl  = URLEncoder.encode(wxOpenRedirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
        String url  = String.format(
                baseUrl ,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                wxOpenRedirectUrl,
                "aiguigu");
//        System.out.println("qrcodeUrl = " + qrcodeUrl);
        return "redirect:" + url;
    }
}
