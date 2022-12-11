package com.supbuilder.wx.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.binarywang.wxpay.exception.WxPayException;

import com.supbuilder.common.utils.http.HttpUtils;
import com.supbuilder.common.utils.wx.WechatDecryptDataUtil;
import com.supbuilder.common.utils.xml.XMLUtil;
import com.supbuilder.common.vo.Result;
import com.supbuilder.wx.wechat.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关接口
 * @author jiaokeqing
 */
@Slf4j
@RestController
@RequestMapping("/wx")
@Api(value = "WX-微信相关接口",tags = "WX-微信相关接口")
public class WechatController {


    @Autowired
    private WechatService wechatService;

    /**
     * 通过code获取openid
     * @param code 微信端获取的code
     * @param session 会话
     */
    @ApiOperation(value = "通过code获取openid",notes = "通过code获取openid")
    @PostMapping("/getOpenid")
    public Result<Object> getUser(@RequestParam("code") String code, HttpSession session){
        try {
//            redisUtil.hset("WX-SESSION","8888",new SecurityWxUserDo("12","2",System.currentTimeMillis()),1800);
//            redisUtil.hset("8888","openId","abcd",1800);
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wxd19df1da7c9f264e&secret=30fe580d662bac7ceba4540f19880e38&grant_type=authorization_code" +
                    "&js_code=" + code;
            String result = HttpUtils.get(url);
            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode == null || errcode == 0) {
                String openId = jsonObject.getString("openid");
                String sessionKey=jsonObject.getString("session_key");
                HashMap<String,String > resultHashMap=new HashMap<>();
                resultHashMap.put("openid",openId);
                resultHashMap.put("sessionKey",sessionKey);
                return Result.ok(resultHashMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取openid失败");
        }
        return Result.error("获取openid失败");
    }

    /**
     * 通过前端传过的数据解析手机号
     * @param encryptedData
     * @param iv
     * @param sessionkey
     * @param uid
     * @return
     */
    @ApiOperation(value = "解析手机号",notes = "解析手机号")
    @PostMapping("/wechat/decodePhone")
    public Result<Object> decodePhone(@RequestParam("encryptedData")String encryptedData,@RequestParam("iv")String iv,@RequestParam("sessionkey")String sessionkey,@RequestParam("uid")String uid){

        String result= WechatDecryptDataUtil.decryptData(encryptedData,sessionkey,iv);
        JsonFactory factory = new JsonFactory();
        try {
            JsonParser jsonParser=factory.createParser(result);
            // 只要还没结束"}"，就一直读
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("phoneNumber".equals(fieldname)) {
                    jsonParser.nextToken();
                    HashMap<String,String> resultHashMap=new HashMap<>();
                    resultHashMap.put("phoneNumber",jsonParser.getText());
                    Result.ok(resultHashMap);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("解析手机号错误");
        }
        return Result.error("解析手机号错误");
    }

    /**
     * 微信支付
     * @param price 支付金额
     * @param request
     * @return
     */
    @ApiOperation(value = "调用微信进行支付",notes = "调用微信进行支付")
    @PostMapping("/pay")
    public Result wxPayy(@RequestParam("price")int price,HttpServletRequest request){
        String sessionKey= (String) request.getSession().getAttribute("sessionKey");
        if (sessionKey!=null){
            String ip=request.getRemoteAddr();
            String openId= "";
            if (openId!=null&&!openId.equals("")){
                try {
                    Result result=wechatService.pay(openId,price,ip);
                    return result;
                } catch (WxPayException e) {
                    e.printStackTrace();
                    return Result.error("支付系统异常");
                }
            }
            return Result.error("登录状态失效，请重新登录");
        }
        return Result.error("登录状态失效，请重新登录");
    }

    /**
     * TODO 订单处理逻辑
     * 微信支付回调接口-处理订单
     * @param request
     */
    @GetMapping("/pay/callback")
    public void wxPayCallBack(HttpServletRequest request, HttpServletResponse response){
        System.out.println("call back");
        Map<String,String> map= XMLUtil.reqParamsToMap(request);
        System.out.println(map);
        if(map.get("return_code").equals("SUCCESS")) {
            System.out.println(map);
            if ( map.get("result_code").equals("SUCCESS")) {
                System.out.println("call3");
                String ordersSn = map.get("openid");//用户
                System.out.println();
                String outTradeNo=map.get("out_trade_no");
               //进行订单处理
                try {
                    response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
