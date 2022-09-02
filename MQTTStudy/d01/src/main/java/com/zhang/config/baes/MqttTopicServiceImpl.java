package com.zhang.config.baes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author: ZPH
 * @date 2022/9/1 21:42
 * @description：动态添加主题
 * @version: 1.0
 */
@Service
public class MqttTopicServiceImpl implements MqttTopicService {

    MqttPahoMessageDrivenChannelAdapter adapter;

    @Autowired
    public MqttTopicServiceImpl(MqttPahoMessageDrivenChannelAdapter adapter) {
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
