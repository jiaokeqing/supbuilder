package com.supbuilder.wx.wechat.service.impl;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.supbuilder.common.utils.wx.WxPayUtil;
import com.supbuilder.common.vo.Result;
import com.supbuilder.wx.wechat.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${supbuilder.wx.appid}")
    private String appid;
    @Value("${supbuilder.wx.pay.mchId}")
    private String mchId;
    @Value("${supbuilder.wx.pay.mchKey}")
    private String mchKey;
    /**
     * todo 添加判断支付的业务逻辑
     * @param openId
     * @param price
     * @param ip
     * @return
     */
    @Override
    public Result pay(String openId, int price, String ip) throws WxPayException {
        Map resultMap= WxPayUtil.wxPrepay(appid,mchId,mchKey,openId,ip,price);
        return Result.error("支付失败");
    }
}
