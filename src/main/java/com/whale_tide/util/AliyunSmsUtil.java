package com.whale_tide.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * 阿里云短信工具类
 */


@Slf4j
@Component
public class AliyunSmsUtil {


    private  final String ACCESS_KEY_ID = "LTAI5tLPVCfhNKLceE8Uqhof";
    private  final String ACCESS_KEY_SECRET = "epEZ0M7BFssmRCOt6i6A4fPRDxXBYp";


    public void sendToPhone(String phone, String msg) {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-qingdao",
                ACCESS_KEY_ID, //AccessIdKey
                ACCESS_KEY_SECRET); //AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        //下面这3个不要改动
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //接收短信的手机号码
        request.putQueryParameter(phone,"电话号码");//此处写电话号码
        //短信签名名称
        request.putQueryParameter("SignName","阿里云短信测试");
        //短信模板ID
        request.putQueryParameter("TemplateCode","SMS_154950909");
        //短信模板变量对应的实际值 ${code} 中的值
        Map<String,String> param = new HashMap<>(2);
        param.put("code", String.valueOf(new Random().nextInt(80000)+10000)); //写入的短信内容
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}