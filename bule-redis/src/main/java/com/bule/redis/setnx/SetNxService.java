package com.bule.redis.setnx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lijianzhen1 on 2019/2/18.
 */
@Service
public class SetNxService {
    //这里的token值可以放在缓存当中
    private static final String token = "token";
    @Autowired
    private JedisPool jedisPool;


    int tokenCount = 100;

    /**
     * FIXME 存在问题
     * <p>
     * 当进程执行出现问题，锁未释放，则其他进程永远处于阻塞状态，出现死锁。
     *
     * @param user
     */
    public void redislock(String user) {
        Jedis redis = jedisPool.getResource();
        //redis.del(token);
        //如果返回0说明没有获得锁，等待
        while (redis.setnx(token, "1") == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //获得锁处理业务,如果处理业务有问题，会将整个业务hold在哪里
        System.out.println(user + "获得token值");
        redis.del(token);
    }


    /**
     * 添加锁定超时时间
     * <p>
     * FIXME 存在的问题
     * 并发时候会有相互覆盖的问题。所以还是有问题
     *
     * @param user
     */
    public void redislock1(String user) {
        Jedis redis = jedisPool.getResource();
        //如果返回0说明没有获得锁，等待
        while (redis.setnx(token, String.valueOf((System.currentTimeMillis() + 1000L))) == 0) {
            try {
                String to = redis.get(token);
                //并发时候多个线程和进程都可以进入这里，同时操作del时候造成相互覆盖的问题
                if (null != to
                        && Long.valueOf(to).compareTo(System.currentTimeMillis()) < 0) {
                    System.out.println("超过等待时间删除token");
                    redis.del(token);
                    redis.setnx(token, String.valueOf((System.currentTimeMillis() + 1000L)));
                    break;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tokenCount = tokenCount - 1;
        if (25 != tokenCount) //处理当token为25时候异常，锁没有释放
        {
            //获得锁处理业务,如果处理业务有问题，会将整个业务hold在哪里，这里可以模拟跑出异常，redis中的token值不会删除，锁不会解锁
            System.out.println(user + "获得token值" + tokenCount);
            redis.del(token);
        }

    }


    /**
     * 使用setnx+getset方式保证
     *
     * 这个就完美的可以控制并发和锁的机制了
     * 具体还有没有更好的方式，希望大家积极提供
     *
     * @param user
     */
    public void redislock2(String user) {
        Jedis redis = jedisPool.getResource();
        //如果返回0说明没有获得锁，等待，这里设置的时间依赖于锁的业务处理时间，必须大于等于，要不会有问题
        while (redis.setnx(token, String.valueOf((System.currentTimeMillis() + 1000L))) == 0) {
            try {
                String to = redis.get(token);
                //并发时候多个线程和进程都可以进入这里，同时操作del时候造成相互覆盖的问题
                if (null != to
                        && Long.valueOf(to).compareTo(System.currentTimeMillis()) < 0
                        && Long.valueOf(redis.getSet(token, String.valueOf((System.currentTimeMillis() + 1000L)))).compareTo(System.currentTimeMillis()) < 0) {
                    System.out.println("超过等待时间删除token");
                    break;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tokenCount = tokenCount - 1;
        if (25 != tokenCount) //处理当token为25时候异常，锁没有释放
        {
            //获得锁处理业务,如果处理业务有问题，会将整个业务hold在哪里，这里可以模拟跑出异常，redis中的token值不会删除，锁不会解锁
            System.out.println(user + "获得token值" + tokenCount);
            redis.del(token);
        }

    }
}
