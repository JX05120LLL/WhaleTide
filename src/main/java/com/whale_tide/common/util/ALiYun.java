package com.whale_tide.common.util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ALiYun {
    public static void sendToPhone(String phoneNumber, String code) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-qingdao",
                "LTAI5tQpSbZhEFoPK398aP82", //AccessIdKey
                "W9kuvsnmsD2eY8XdKHwdqoxUMhAHo1"); //AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        //下面这3个不要改动
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //接收短信的手机号码
        request.putQueryParameter("PhoneNumbers","19391629120");//此处写电话号码
        //短信签名名称
        request.putQueryParameter("SignName","阿里云短信测试");
        //短信模板ID
        request.putQueryParameter("TemplateCode","SMS_154950909");
        //短信模板变量对应的实际值 ${code} 中的值
        Map<String,String> param = new HashMap<>(2);
        param.put("code", String.valueOf(new Random().nextInt(80000)+10000));

        request.putQueryParameter("TemplateParam",  new ObjectMapper().writeValueAsString(param));


        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());
    }

}
