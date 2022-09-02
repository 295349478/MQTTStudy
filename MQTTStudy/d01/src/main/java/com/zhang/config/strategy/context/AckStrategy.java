package com.zhang.config.strategy.context;

import com.alibaba.fastjson.JSONObject;

public interface AckStrategy {
    void dealSuc(JSONObject object);
    void dealErr(JSONObject object);
}
