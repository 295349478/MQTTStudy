package com.zhang.config.baes;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName MqttBaseConfig
 * @Description 信息从yaml配置文件中收集，只有开启才能排至
 * @Author ZPH
 * @Date 2022/9/2 9:44
 * @Version 1.0
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttBaseConfig {
    /**
     *  以下属性将在配置文件中读取
     **/
    //mqtt Broker 地址
    @Value("${mqtt.uris}")
    private String[] uris;

    //连接用户名
    @Value("${mqtt.username}")
    private String username;

    //连接密码
    @Value("${mqtt.password}")
    private String password;


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

    public void setUris(String[] uris) {
        this.uris = uris;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
