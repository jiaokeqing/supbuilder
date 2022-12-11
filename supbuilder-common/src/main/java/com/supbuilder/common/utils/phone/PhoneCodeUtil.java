package com.supbuilder.common.utils.phone;

import com.github.qcloudsms.SmsResultBase;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

/***
 * 腾讯云短信
 * @author jiaokeqing
 *
 */
public class PhoneCodeUtil {

    /**
     * 发送普通短信
     * @param phone
     * @param params
     * @param appId
     * @param appKey
     * @param templateId
     * @param smsSign
     * @throws Exception
     */
    public static void sendMsgByTxPlatform(String phone,String[] params,
                                           int appId,String appKey,
                                           int templateId,String smsSign ) throws Exception {


        SmsSingleSender sSender = new SmsSingleSender(appId, appKey);
        //第一个参数0表示普通短信,1表示营销短信
//        SmsSingleSenderResult result = sSender.send(0, "86",
//                phone,
//                100+ "为您的登录验证码，请于" + 10 + "分钟内填写。如非本人操作，请忽略本短信。", smsSign,"", "");
        SmsResultBase result =sSender.sendWithParam("86", phone, templateId, params, smsSign, "", "");
        if (((SmsSingleSenderResult) result).result != 0) {
            throw new Exception("send phone validateCode is error" + ((SmsSingleSenderResult) result).errMsg);
        }
    }
}
