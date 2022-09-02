package com.zhang.config.baes;

import com.zhang.config.strategy.TopicStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @ClassName MqttMessageReceiver
 * @Description mqtt订阅入站消息处理
 * @Author ZPH
 * @Date 2022/9/2 9:54
 * @Version 1.0
 */

@Slf4j
@Component
public class MqttMessageReceiver implements MessageHandler {

    /**
     * 开启消息处理的yaml配置
     */
    @Value("${mqtt.receiveEnabled}")
    private boolean receiveEnabled;

    @Autowired
    private TopicStrategyContext topicStrategyContext;

    /**
     * 通过解耦合
     * @return
     */
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
        if (receiveEnabled) {
            String payload = String.valueOf(message.getPayload());
            topicStrategyContext.receive(payload, topic);
        }
    }


}
