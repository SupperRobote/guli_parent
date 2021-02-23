package com.atguigu.msmservice.utils;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;

public class SDKTestSendTemplateSMS {
    /**
     * 发送验证码工具类
     *
     * @param phone 电话号码
     * @return 验证码
     */
    public String sms(String phone) {
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
        restAPI.init("app.cloopen.com", "8883");
        // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
        restAPI.setAccount("8aaf070875da65fe0175db42e7e60065", "e10e480cd0c149d2be76a00906ef2b39");
        // 请使用管理控制台中已创建应用的APPID。
        restAPI.setAppId("8aaf070875da65fe0175db42eb47006c");
        String code = RandomStringUtils.randomNumeric(6);
        //设置需要发送的手机号和发送的验证码及过期时间
        result = restAPI.sendTemplateSMS(phone, "1", new String[]{code, "5"});
        if ("000000".equals(result.get("statusCode"))) {
            return code;
        } else {
            //异常返回输出错误码和错误信息
            String s = "错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg");
            return s;
        }
    }

    /*public static void main(String[] args) {
        String sms = sms("15273016791");
        System.out.println(sms);
    }*/
}
