package com.icekredit.examples.test.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icekredit.examples.test.client.service.TestService;
import com.icekredit.phone.PhoneType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/addPhoneToUser")
    public String addPhoneToUser(@RequestBody String requestParamJsonStr){
        if (StringUtils.isBlank(requestParamJsonStr)){
            throw new RuntimeException("抱歉，POST请求请求体为空！");
        }

        JSONObject requestParamJsonObj = JSON.parseObject(requestParamJsonStr);
        String phoneNumber = requestParamJsonObj.getString("phone_number");

        long startTime = System.nanoTime();

        String result = testService.addPhoneToUser(1, PhoneType.WORK, phoneNumber);

        return String.format("addPhoneToUser-->result:%s,spent time in nanos:%d",result,System.nanoTime() - startTime);
    }
}
