package com.zhang.config.strategy.context;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @ClassName AckStrategyTestImpl
 * @Description TODO
 * @Author ZPH
 * @Date 2022/9/2 15:34
 * @Version 1.0
 */
@Component("AckStrategyTestImpl1")
public class AckStrategyTestImpl implements AckStrategy{
    @Override
    public void dealSuc(JSONObject object) {
        System.out.println("接收成功信息");
    }

    @Override
    public void dealErr(JSONObject object) {
        System.out.println("接收失败信息");
    }
}
