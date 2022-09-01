package com.zhang.config;

public interface MqttService {

    public void addTopic(String topic);

    public void removeTopic(String topic);
}
