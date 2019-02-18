package com.bule.redis.sharding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@ContextConfiguration("classpath:spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ShardedServiceTest {
    @Autowired
    private ShardedService shardedService;

    @Test
    public void testSharded() {
        shardedService.test();
    }
}
