package com.bule.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@ContextConfiguration({"classpath:spring-config.xml","classpath:spring-redis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPubSubRedis {


    @Test
    public void TestSchedule() throws InterruptedException {
        Thread.sleep(10000
        );
    }

}
