package com.zhang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * @author: ZPH
 * @date 2022/9/1 21:34
 * @description：消息接收器
 * @version: 1.0
 */
@Component
public class Receiver {
    @Bean
    //使用ServiceActivator 指定接收消息的管道为 mqttInboundChannel，投递到mqttInboundChannel管道中的消息会被该方法接收并执行
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handleMessage() {
        return message -> {
//            User user = (User)message.getPayload();
//            System.out.println(user.getUsername()+":"+user.getPassword());
            System.out.println(message.getPayload());
        };
    }
}
