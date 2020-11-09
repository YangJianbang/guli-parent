package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.controller.TOrderController;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonFactory;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {
    @Autowired
    TOrderService orderService;

    @Override
    public Map createNative(String orderNo) {
        try {
//        1.根据订单号获取订单信息
            TOrder order = orderService.getOrderByOrderNo(orderNo);
//        2.调用微信支付接口生成二维码
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");//支付ID
            m.put("mch_id", "1558950191");//商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//随机数
            m.put("body", order.getCourseTitle());//课程名称
            m.put("out_trade_no", orderNo);//订单号
            //订单金额
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");//客户端地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");//二维码类型

//            调用微信生成二维码接口，设置XML类型参数
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //使用https协议
            httpClient.setHttps(true);
            //将MAP集合变为XML格式，进行加密处理
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            //设置POST提交
            httpClient.post();
//            得到返回的数据为XML形式
            String xml = httpClient.getContent();
//            System.out.println("xml = " + xml);
//            把xml转换为map形式
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
//            System.out.println("resultMap = " + resultMap);
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());

            map.put("result_code", resultMap.get("result_code"));//二维码生成状态
            map.put("code_url", resultMap.get("code_url"));//二维码地址
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取支付二维码失败");
        }
    }

    //        调用微信接口查询支付结果
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
//        设置查询需要的参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //使用httpclient调用微信接口传递参数，转换成xml进行加密
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.post();
            //得到微信接口返回的数据
            String xml = httpClient.getContent();
            System.out.println("xml = " + xml);
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            System.out.println("map = " + map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取订单支付信息失败");
        }
    }

    //        更新订单状态，添加支付记录
    @Override
    public void updateOrderStates(Map<String, String> map) {
        String orderNo = map.get("out_trade_no");
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOrderByOrderNo(orderNo);
        if (order.getStatus().intValue() == 1) {
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);

        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(orderNo);//订单号
        payLog.setPayTime(new Date());//支付时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//支付价额
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));//订单流水号
        payLog.setAttr(JSONObject.toJSONString(map));

        baseMapper.insert(payLog);
    }
}
