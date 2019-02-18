package com.bule.redis.saleProm;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by lijianzhen1 on 2019/2/15.
 */
public class ConsumerTask implements Runnable {
    public static final String sale_Num = "saleNum";

    private JedisPool jedisPool;

    private String userName;

    private Semaphore semaphore;

    public ConsumerTask(JedisPool jedisPool, Semaphore semaphore, String userName) {
        this.jedisPool = jedisPool;
        this.semaphore = semaphore;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            startRun(3);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void startRun(int retry) {
        if (retry <= 1) {
            System.out.println(userName + "---再次抢购失败,重试" + (4 - retry) + "次重试");
            return;
        }
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            //商品的key  ， 秒杀有个数量
            //watch 监视一个key，当事务执行之前这个key发生了改变，事务会被打断
            resource.watch(sale_Num);
            int saleNum = Integer.valueOf(resource.get(sale_Num));
            //如果大于0说明才有库存
            if (saleNum > 0) {
                //开启事务
                Transaction tx = resource.multi();
                //使用decr模拟被抢占
                tx.decr(sale_Num);
                //提交事务，如果数量发生了改动 则会返回null
                List<Object> list = tx.exec();
                if (list == null || list.size() == 0) {
                    System.out.println(userName + "抢购失败再次,重试,第" + (4 - retry) + "次重试");
                    startRun(--retry);
                } else {
                    for (Object success : list) {
                        System.out.println(userName + "(" + success.toString() + ")商品抢购成功,当前抢购成功的人数是：" + (1 - (saleNum - 100)));
                    }
                }
            } else {
                System.out.println(userName + "商品已经被抢完了");
            }
            resource.close();
        } catch (JedisConnectionException e) {
            System.out.println(userName + "没有获取到连接或者连接超时，适当调整大maxWaitMillis，正常则忽略" + e);
        } finally {
            if (resource != null)
                resource.close();
        }
    }
}
