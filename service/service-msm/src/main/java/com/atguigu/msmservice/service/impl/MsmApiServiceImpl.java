package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmApiService;
import com.atguigu.commonutils.AccessKeyUtil;
import com.atguigu.servicebase.exception.GuliException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsmApiServiceImpl implements MsmApiService {
    @Override
    public boolean sendPhone(String phone, String code) {

        try {
            if (StringUtils.isEmpty(phone)) {
                return false;
            }
            DefaultProfile profile =
                    DefaultProfile.getProfile("default", AccessKeyUtil.ACCESSKEY_ID, AccessKeyUtil.ACCESSKEY_SECRET);
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);//请求方式
            request.setDomain("dysmsapi.aliyuncs.com");//使用域名
            request.setVersion("2017-05-25");//版本
            request.setAction("SendSms");//action方法

            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", "我的谷粒学院在线教育网站");
            request.putQueryParameter("TemplateCode", "SMS_205393465");
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));

            CommonResponse response = client.getCommonResponse(request);
            System.out.println("Data = " + response.getData());
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "发送短信验证码失败");
        }

    }
}
