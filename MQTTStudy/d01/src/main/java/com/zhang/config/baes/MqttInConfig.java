package com.zhang.config.baes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @ClassName MqttInConfig
 * @Description 入站配置
 * @Author ZPH
 * @Date 2022/9/2 9:52
 * @Version 1.0
 */
@Configuration
public class MqttInConfig {

    /**
     * 入站Client ID （可以由配置文件配置）
     */
    @Value("${mqtt.in-client-id}")
    private String inClientId;

    /**
     * 默认订阅主题 （可以由配置文件配置）
     */
    @Value("${mqtt.default-topic}")
    private String defaultTopic;

    public void setInClientId(String inClientId) {
        this.inClientId = inClientId;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    /**
     * 将入站消息处理进行解耦合
     */
    private final MqttMessageReceiver mqttMessageReceiver;

    public MqttInConfig(MqttMessageReceiver mqttMessageReceiver) {
        this.mqttMessageReceiver = mqttMessageReceiver;
    }


    /**
     * 解码器 解耦合
     */
    @Autowired(required = false)
    private MqttMessageConverter messageConverter;

    public void setMessageConverter(MqttMessageConverter messageConverter) {
        if (messageConverter == null){
            messageConverter = new DefaultPahoMessageConverter();
        }
        this.messageConverter = messageConverter;
    }

    /**
     * 入站信道
     * @return
     */
    @Bean
    public MessageChannel mqttInboundChannel(){
        return new DirectChannel();
    }

    /**
     * 错误信道
     * @return
     */
    @Bean
    public MessageChannel errorChannel(){
        return new DirectChannel();
    }

    /**
     * 创建适配器，需要用到工厂
     * @param
     * @return
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory factory){
        return new MqttPahoMessageDrivenChannelAdapter(inClientId,factory,defaultTopic);
    }


    /**
     * 入站处理
     * @param errorChannel        错误消息信道
     * @param mqttInboundChannel  入站消息信道
     * @param adapter             适配器
     * @return
     */
    @Bean
    public MessageProducer mqttInbound(MessageChannel errorChannel,
                                       MessageChannel mqttInboundChannel,
                                       MqttPahoMessageDrivenChannelAdapter adapter){
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        //入站投递给入站管道
        adapter.setOutputChannel(mqttInboundChannel);
        adapter.setErrorChannel(errorChannel);
        adapter.setQos(0);
        return adapter;
    }

    /**
     *
     * 使用ServiceActivator 指定接收消息的管道为 mqttInboundChannel，投递到mqttInboundChannel管道中的消息会被该方法接收并执行
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handleMessage() {
        //直接返回写好地入站消息处理器，进行入站配置和消息处理解耦合
        return this.mqttMessageReceiver;
    }




    //    @Bean
    //使用ServiceActivator 指定接收消息的管道为 mqttInboundChannel，投递到mqttInboundChannel管道中的消息会被该方法接收并执行
//    @ServiceActivator(inputChannel = "mqttInboundChannel")
//    public MessageHandler handleMessage() {
//        return message -> {
//            User user = (User)message.getPayload();
//            System.out.println(user.getUsername()+":"+user.getPassword());
//            System.out.println(message.getPayload());
//        };
//    }

}
