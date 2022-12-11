package com.supbuilder.wx.wechat.service;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.supbuilder.common.vo.Result;

public interface WechatService {

    Result<Object> pay(String openId, int price, String ip) throws WxPayException;
}
