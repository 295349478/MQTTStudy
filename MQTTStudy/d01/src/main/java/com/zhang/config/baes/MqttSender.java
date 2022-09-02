package com.zhang.config.baes;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MessagingGateway 网关定义了从哪个出站信息发送消息，而入站的信道配置在mqtt入站消息处理工具中
 */

@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttSender {
    /**
     * 用默认主题发送信息
     * @param text 发送的消息
     */
    void sendToMqtt(String text);

    /**
     * 指定主题发送信息
     * @param topic 主题
     * @param text  消息主体
     */
    void sendWithTopic(@Header(MqttHeaders.TOPIC) String topic, String text);

    /**
     * 指定主题、qos发送信息
     * @param topic 主题
     * @param Qos   对消息处理的几种机制。
     *              0 表示的是订阅者没收到消息不会再次发送，消息会丢失。
     *              1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。
     *              2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param text  消息主体
     */
    void sendWithTopicAndQos(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer Qos, String text);


//    void sendToMqtt(User user);
//
//    void sendWithTopic(@Header(MqttHeaders.TOPIC) String topic, User user);
//
//    void sendWithTopicAndQos(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer Qos, User user);
}
