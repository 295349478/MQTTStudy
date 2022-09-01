package com.zhang.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author: ZPH
 * @date 2022/9/1 21:07
 * @description：mqtt的配置类
 * @version: 1.0
 */
@Configuration
@ConfigurationProperties("mqtt")
public class MqttConfig {
    /**
     *  以下属性将在配置文件中读取
     **/
    //mqtt Broker 地址
    private String[] uris;

    //连接用户名
    private String username;

    //连接密码
    private String password;

    //入站Client ID
    private String inClientId;

    //出站Client ID
    private String outClientId;

    //默认订阅主题
    private String defaultTopic;

    //编码器
    private final AlMingConverter alMingConverter;



    /**
     * 自定义编码器
     * @param alMingConverter
     */
    @Autowired
    public MqttConfig(AlMingConverter alMingConverter) {
        this.alMingConverter = alMingConverter;
    }

/*-----------------------------------------------------------------------------------------*/
    /**
     * 出站处理器
     * @param factory
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


    /**
     * 消息生产者创建
     * @param adapter
     * @return
     */
    @Bean
    public MessageProducer mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter){
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        //入站投递给入站管道
        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setErrorChannel(errorChannel());
        adapter.setQos(0);
        return adapter;
    }

/*-----------------------------------------------------------------------------------------*/

    /**
     * 创建适配器，需要用到工厂，下面有创建工厂
     * @param factory
     * @return
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory factory){
        return new MqttPahoMessageDrivenChannelAdapter(inClientId,factory,defaultTopic);
    }

    /**
     * 客户端工厂
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        //创建播送客户端工厂
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(uris);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }


    /**
     * 创建三个管道分别处理：入站消息，出站消息，错误消息
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }
    @Bean
    public MessageChannel mqttInboundChannel(){
        return new DirectChannel();
    }
    @Bean
    public MessageChannel errorChannel(){
        return new DirectChannel();
    }


/*-----------------------------------------------------------------------------------------*/

    public void setUris(String[] uris) {
        this.uris = uris;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInClientId(String inClientId) {
        this.inClientId = inClientId;
    }

    public void setOutClientId(String outClientId) {
        this.outClientId = outClientId;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }
}
