package com.supbuilder.common.utils.wx;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceJoddHttpImpl;
import com.supbuilder.common.utils.random.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付工具类
 * @author jiaokeqing
 */
public class WxPayUtil {

    /**
     * 微信支付
     * @param appid
     * @param mchId
     * @param mchKey
     * @param openId
     * @param ip
     * @param price
     * @return
     * @throws WxPayException
     */
    public static Map wxPrepay(String appid,String mchId,String mchKey,String openId,String ip,int price) throws WxPayException {
        WxPayConfig wxPayConfig=new WxPayConfig();
        WxPayService wxPayService=new WxPayServiceJoddHttpImpl();
        wxPayConfig.setAppId(appid);
        wxPayConfig.setMchId(mchId);
        wxPayConfig.setMchKey(mchKey);
        wxPayService.setConfig(wxPayConfig);
        WxPayUnifiedOrderRequest request=new WxPayUnifiedOrderRequest();
        String outTradeNo= RandomUtil.generateString(12);
        String randomStr=RandomUtil.generateString(32);

        request.setOutTradeNo(outTradeNo);                                     //随机字订单号
        request.setSpbillCreateIp(ip);                                          //用户ip
        request.setTradeType("JSAPI");                                           //公众号支付
        request.setNonceStr(randomStr);                 //随机字符串
        request.setNotifyUrl("https://www.zxduck.com/zxduck/wx/pay/callback");       //回调通知支付结果地址(必须外网能访问的地址)
        request.setDeviceInfo("WEB");                                                               //客户终端类型

        request.setBody("付款");
        request.setOpenid(openId);
        request.setTotalFee(price*100);
        SortedMap<Object,Object> map=new TreeMap<>();
        map.put("appid",appid);
        map.put("body","付款");
        map.put("device_info","WEB");
        map.put("mch_id",mchId);
        map.put("notify_url","https://www.zxduck.com/zxduck/wx/pay/callback");
        map.put("nonce_str",randomStr);
        map.put("out_trade_no",outTradeNo);
        map.put("total_fee",price*100);
        map.put("spbill_create_ip",ip);
        map.put("trade_type","JSAPI");
        map.put("openid",openId);
        String sign= PayCommonUtil.createSign("utf8",map,mchKey);
        request.setSign(sign);
            WxPayMpOrderResult result1=wxPayService.createOrder(request);
            SortedMap<Object,Object> map2=new TreeMap<>();
            map2.put("appId",result1.getAppId());
            map2.put("timeStamp",result1.getTimeStamp());
            map2.put("nonceStr",result1.getNonceStr());
            map2.put("package",result1.getPackageValue());
            map2.put("signType",result1.getSignType());
            String sign2=PayCommonUtil.createSign("utf8",map2,mchKey);

            map2.put("paySign",sign2);
            result1.getTimeStamp();
            return map2;

    }


}
