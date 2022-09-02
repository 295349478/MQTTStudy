package com.zhang.config.strategy;

/**
 * 接受时 实现该方法
 */
public interface TopicStrategy {

    void receive(String data);

}
