package com.zhang.config.strategy.context;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AckStrategyContext {

    @Autowired
    Map<String, AckStrategy> strategyMap = new HashMap<>();

    public AckStrategyContext(Map<String, AckStrategy> map){
        for (String key : map.keySet()) {
            strategyMap.put(key, map.get(key));
        }
    }

    public void dealSuc(JSONObject object, String key){
        if(strategyMap.get(key) != null){
            strategyMap.get(key).dealSuc(object);
        }
    }

    public void dealErr(JSONObject object, String key){
        if(strategyMap.get(key) != null) {
            strategyMap.get(key).dealErr(object);
        }
    }
}
