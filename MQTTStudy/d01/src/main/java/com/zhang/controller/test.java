package com.zhang.controller;

import com.zhang.config.MqttSender;
import com.zhang.config.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author: ZPH
 * @date 2022/9/1 22:35
 * @description：
 * @version: 1.0
 */
@Controller
public class test {

    @Resource
    private MqttSender mqttSender;

    @ResponseBody
    @GetMapping(value = "/mqtt")
    public ResponseEntity<String> sendMqtt(@RequestParam(value = "msg") String message) {
//        User user = new User();
//        user.setUsername(message);
//        user.setPassword("123");
        mqttSender.sendToMqtt(message);
        // iMqttSender.sen
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }



}
