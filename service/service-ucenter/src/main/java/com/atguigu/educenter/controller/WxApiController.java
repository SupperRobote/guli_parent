package com.atguigu.educenter.controller;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstansWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.educenter.utils.JwtUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller //注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //1.生成微信二维码
    @GetMapping("login")
    public String getWxCode(){
        //固定地址，后面要拼接参数
        //微信开发平台授权baseUrl   %s相当于?号占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstansWxUtils.WX_OPEN_REDIRECT_URL;
        try{
            redirectUrl=URLEncoder.encode(redirectUrl,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }



        //设置%s中的值
        String url=String.format(
                baseUrl,
                ConstansWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        //重定向到请求微信地址
        return "redirect:"+url;
    }

    //2. 获取扫描人信息,添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try{
            //1. 获取code值,临时票据 类似于验证码

            //2. 拿着code请求微信固定地址 得到两个值 access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数： id 密钥 code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstansWxUtils.WX_OPEN_APP_ID,
                    ConstansWxUtils.WX_OPEN_APP_SECRET,
                    code
            );

            //请求拼接好的地址 得到返回两个值 access_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);

            //从accessTokenInfo获取两个值 access_token 和 openid
            //把accessTokenInfo字符串转换为map集合
            //使用json转化工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");


            //把扫码人信息添加到数据库
            //判断数据库表是否存在相同信息 根据openid
            UcenterMember member=memberService.getOpenIdMember(openid);
            if(member == null){
                //3. 拿着access_token 和 openid 再去请求微信固定的地址 获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );

                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userInfo字符串扫码人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname=(String) userInfoMap.get("nickname"); //昵称
                String headimgurl=(String) userInfoMap.get("headimgurl"); //头像

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后 返回首页 通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+jwtToken;

        }catch (Exception e){
            throw new GuliException(20001,"登录失败");
        }

    }
}
