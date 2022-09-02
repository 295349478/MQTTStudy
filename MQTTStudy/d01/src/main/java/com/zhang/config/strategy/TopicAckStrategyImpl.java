package com.zhang.config.strategy;

import com.alibaba.fastjson.JSONObject;
import com.zhang.config.strategy.context.AckStrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName TopicAckStrategyImpl
 * @Description 通过主题寻找到初始的ack分发器
 * @Author ZPH
 * @Date 2022/9/2 15:22
 * @Version 1.0
 */
@Component("test1")
public class TopicAckStrategyImpl implements TopicStrategy {

    @Autowired
    private AckStrategyContext ackStrategyContext;

    @Override
    public void receive(String data) {
        try {
            //直接调用AckStrategyContext来选择实现方法
            //这个方法只是为了归类是ack还是别的信息
            JSONObject jsonObject = new JSONObject();
            // 这里选择下面的Impl就是ack下一层的处理器
            ackStrategyContext.dealSuc(jsonObject, "AckStrategyTestImpl1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
