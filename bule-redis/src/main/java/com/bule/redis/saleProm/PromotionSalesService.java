package com.bule.redis.saleProm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.Semaphore;

/**
 * 并发计数器
 * <p>
 * 用在验证码错误次数
 * 用在记录拦截错误的次数比如ip限制
 * <p>
 * Created by lijianzhen1 on 2019/2/15.
 */
@Service
public class PromotionSalesService {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 启用多个线程模拟去抢占
     * 使用Transaction会严重影响性能，但是在多个命令时候很有必要
     *
     * @param store 设置促销的商品数量
     */
    public void promot(int store) {
        //先放入对应的商品数量
        Jedis jedis = jedisPool.getResource();
        jedis.set(ConsumerTask.sale_Num, store + "");
        jedis.close();

        //这里在模拟外部设置最大的并发量，这里可以当做单机的最大并发用户
        Semaphore semaphore = new Semaphore(4);
        for (int i = 0; i < 1000; i++) {
            //创建1000个线程抢占消费
            new Thread(new ConsumerTask(jedisPool, semaphore, "userName_" + i)).start();
        }
        try {
            //方便查询
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用多个线程模拟抢占库存实现2
     *
     * @param store
     */
    public void promot1(int store) {
        //先放入对应的商品数量
        Jedis jedis = jedisPool.getResource();
        jedis.flushDB();
        jedis.close();


        for (int i = 0; i < 100; i++) {
            //创建1000个线程抢占消费
            new Thread(new IncrAndDecrTask(jedisPool, "userName_" + i)).start();
        }
        try {
            //方便查询
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
