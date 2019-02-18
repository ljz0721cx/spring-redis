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

    private int smsCount=1;
    private int logCount=1;

    @Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列sms频道模拟发布消息
    public void sendSmsMessage(){
        System.out.println("短信"+smsCount+"次通知开始 "+System.currentTimeMillis());
        stringRedisTemplate.convertAndSend("sms","短信"+smsCount+"次收到通知");
        smsCount++;
    }

    @Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列logistic频道模拟发布消息
    public void sendLogisticsMessage(){
        System.out.println("物流"+logCount+"次通知开始 "+System.currentTimeMillis());
        stringRedisTemplate.convertAndSend("logistics","物流信息"+logCount+"次收到通知");
        logCount++;
    }

}
