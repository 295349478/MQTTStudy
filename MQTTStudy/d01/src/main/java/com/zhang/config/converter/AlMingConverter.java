package com.zhang.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.config.User;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.mqtt.support.MqttMessageConverter;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author: ZPH
 * @date 2022/9/1 21:11
 * @description：编解码器
 * @version: 1.0
 */
//@Component
public class AlMingConverter implements MqttMessageConverter {
    private final  static Logger log = LoggerFactory.getLogger(AlMingConverter.class);

    private int defaultQos = 0;
    private boolean defaultRetain = false;
    ObjectMapper om = new ObjectMapper();
    //入站消息解码
    @Override
    public Message<User> toMessage(String topic, MqttMessage mqttMessage) {

        User protocol = null;
        try {
            protocol = om.readValue(mqttMessage.getPayload(), User.class);
        } catch (IOException e) {
            if (e instanceof JsonProcessingException) {
                System.out.println();
                log.error("Converter only support json string");
            }
        }
        assert protocol != null;
        MessageBuilder<User> messageBuilder = MessageBuilder
                .withPayload(protocol);
        //使用withPayload初始化的消息缺少头信息，将原消息头信息填充进去
        messageBuilder.setHeader(MqttHeaders.ID, mqttMessage.getId())
                .setHeader(MqttHeaders.RECEIVED_QOS, mqttMessage.getQos())
                .setHeader(MqttHeaders.DUPLICATE, mqttMessage.isDuplicate())
                .setHeader(MqttHeaders.RECEIVED_RETAINED, mqttMessage.isRetained());
        if (topic != null) {
            messageBuilder.setHeader(MqttHeaders.TOPIC, topic);
        }
        return messageBuilder.build();
    }

    @Override
    public AbstractIntegrationMessageBuilder<?> toMessageBuilder(String topic, MqttMessage mqttMessage) {
        return null;
    }

    //出站消息编码
    @Override
    public Object fromMessage(Message<?> message, Class<?> targetClass) {
        MqttMessage mqttMessage = new MqttMessage();
        String msg = null;
        try {
            msg = om.writeValueAsString(message.getPayload());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert msg != null;
        mqttMessage.setPayload(msg.getBytes(StandardCharsets.UTF_8));
        //这里的 mqtt_qos ,和 mqtt_retained 由 MqttHeaders 此类得出不可以随便取，如需其他属性自行查找
        Integer qos = (Integer) message.getHeaders().get("mqtt_qos");
        mqttMessage.setQos(qos == null ? defaultQos : qos);
        Boolean retained = (Boolean) message.getHeaders().get("mqtt_retained");
        mqttMessage.setRetained(retained == null ? defaultRetain : retained);
        return mqttMessage;
    }
    //此方法直接拿默认编码器的来用的，照抄即可
    @Override
    public Message<?> toMessage(Object payload, MessageHeaders headers) {
        Assert.isInstanceOf(MqttMessage.class, payload,
                () -> "This converter can only convert an 'MqttMessage'; received: " + payload.getClass().getName());
        return this.toMessage(null, (MqttMessage) payload);
    }

    public void setDefaultQos(int defaultQos) {
        this.defaultQos = defaultQos;
    }

    public void setDefaultRetain(boolean defaultRetain) {
        this.defaultRetain = defaultRetain;
    }
}
