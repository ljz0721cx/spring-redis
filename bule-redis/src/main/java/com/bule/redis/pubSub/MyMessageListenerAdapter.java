package com.bule.redis.pubSub;

import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

/**
 *
 * 如果是使用监听适配器来适配的话可以参考
 * https://blog.csdn.net/qq_32867467/article/details/82944209
 *
 * Created by lijianzhen1 on 2019/2/18.
 */
@Service("myMessageListenerAdapter")
public class MyMessageListenerAdapter extends MessageListenerAdapter {

    public MyMessageListenerAdapter() {
        super(new LogisticsListener());
    }


}
