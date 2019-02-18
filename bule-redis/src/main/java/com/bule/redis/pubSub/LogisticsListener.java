package com.bule.redis.pubSub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@Service("logisticsListener")
public class LogisticsListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("物流消息 " + message.toString());
    }
}
