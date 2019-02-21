package com.bule.redis.saleProm;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 设置商品的总数量为100
 * Created by lijianzhen1 on 2019/2/21.
 */
public class IncrAndDecrTask implements Runnable {

    private JedisPool jedisPool;

    private String userName;

    public IncrAndDecrTask(JedisPool jedisPool, String userName) {
        this.jedisPool = jedisPool;
        this.userName = userName;
    }

    @Override
    public void run() {
        Jedis jedis = jedisPool.getResource();
        try {
            if (null != jedis.get(ConsumerTask.sale_Num)
                    && Long.valueOf(jedis.get(ConsumerTask.sale_Num)) >= 100) {
                System.out.println("库存已经没有了");
                return;
            }
            long sales = jedis.incr(ConsumerTask.sale_Num);

            if (sales > 100) {
                long returnNum = jedis.decr(ConsumerTask.sale_Num);
                System.out.println(userName + "库存被抢占超过最大限制返回" + returnNum);
            } else {
                System.out.println(userName + "消费库存，总库存还剩余" + (100 - sales));
            }
        } finally {
            jedis.close();
        }
    }
}
