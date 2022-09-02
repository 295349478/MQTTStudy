package com.zhang.config.baes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @ClassName MqttOutConfig
 * @Description mqtt出站消息处理配置
 * @Author ZPH
 * @Date 2022/9/2 10:56
 * @Version 1.0
 */

@Configuration
public class MqttOutConfig {

    @Value("${mqtt.out-client-id}")
    private String outClientId;

    /**
     * 默认订阅主题 （可以由配置文件配置）
     */
    @Value("${mqtt.default-topic}")
    private String defaultTopic;

    /**
     * 设置出站ID
     * @param outClientId
     */
    public void setOutClientId(String outClientId) {
        this.outClientId = outClientId;
    }

    /**
     * 设置默认主题
     * @param defaultTopic
     */
    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    /**
     * 解码器 解耦合
     */
    @Autowired(required = false)
    private MqttMessageConverter messageConverter;

    public void setMessageConverter(MqttMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }


    /**
     * 出站通道
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }


    /**
     * 出站处理器
     * @param
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory){
        MqttPahoMessageHandler handler =
                new MqttPahoMessageHandler(outClientId,factory);
        handler.setAsync(true);
        handler.setConverter(new DefaultPahoMessageConverter());
        handler.setDefaultTopic(defaultTopic);
        return handler;
    }

}
