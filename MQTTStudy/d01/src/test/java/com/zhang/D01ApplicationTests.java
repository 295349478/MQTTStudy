package com.zhang;

import com.zhang.config.baes.MqttSender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class D01ApplicationTests {

    /**
     * 注入发送MQTT的Bean
     */
    @Resource
    private MqttSender mqttSender;

    @Test
    void contextLoads() {

    }

}
