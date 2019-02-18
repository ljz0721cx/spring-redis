package com.bule.redis.pubSub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@Component
@EnableScheduling
public class PbSendService {
    @Autowired
    @Resource(name = "redisTemplate")
    private StringRedisTemplate stringRedisTemplate;


    @Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列sms频道模拟发布消息
    public void sendSmsMessage(){
        stringRedisTemplate.convertAndSend("sms","短信发送");
    }

    @Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列logistic频道模拟发布消息
    public void sendLogisticsMessage(){
        stringRedisTemplate.convertAndSend("logistics","物流信息通知");
    }

}
