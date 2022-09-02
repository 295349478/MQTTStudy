package com.zhang.config.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 分发策略控制器
 * 收集了所有类型的信息处理:ack等信息处理器放到map中,通过主题进行分类
 */
@Service
public class TopicStrategyContext {

    @Autowired
    private Map<String, TopicStrategy> strategyMap = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(TopicStrategyContext.class);

    public TopicStrategyContext(Map<String, TopicStrategy> map) {
        for (String topic : map.keySet()) {
            strategyMap.put(topic, map.get(topic));
        }
    }

    public void receive(String data, String topic) {

        if (strategyMap.get(topic) != null) {
            strategyMap.get(topic).receive(data);
        } else {
            log.error("出错");
        }
    }






}
