package com.bule.redis.setnx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@ContextConfiguration({"classpath:spring-config.xml", "classpath:spring-redis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SetNxServiceTest {
    @Autowired
    private SetNxService setNxService;

    @Test
    public void Testlock() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Cuser(setNxService, i)).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Cuser implements Runnable {

    private SetNxService setNxService;
    private int thCount;

    public Cuser(SetNxService setNxService, int thCount) {
        this.setNxService = setNxService;
        this.thCount = thCount;
    }

    @Override
    public void run() {
        //问题代码  setNxService.redislock("user_" + thCount);
        //问题代码  setNxService.redislock1("user_" + thCount);
        //问题代码  setNxService.redislock2("user_" + thCount);
        //正确代码。
        setNxService.redislock3("user_" + thCount);
    }
}