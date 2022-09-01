package com.zhang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

import java.util.Arrays;

/**
 * @author: ZPH
 * @date 2022/9/1 21:42
 * @description：主题重写
 * @version: 1.0
 */
public class MqttServiceImpl implements MqttService {

    MqttPahoMessageDrivenChannelAdapter adapter;

    @Autowired
    public MqttServiceImpl(MqttPahoMessageDrivenChannelAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void addTopic(String topic) {
        String[] topics = adapter.getTopic();
        if(!Arrays.asList(topics).contains(topic)){
            adapter.addTopic(topic,0);
        }
    }

    @Override
    public void removeTopic(String topic) {
        adapter.removeTopic(topic);
    }
}
