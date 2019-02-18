package com.bule.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lijianzhen1 on 2019/2/15.
 */
@ContextConfiguration({"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRedis {
    @Autowired
    private JedisPool jedisPool;


    /**
     * 简单测试
     */
    @Test
    public void testJedisPool(){
        //拿到连接资源
        Jedis jedis= jedisPool.getResource();
        jedis.set("janle","janle_mesg");
        jedis.del("janle");
    }



}
