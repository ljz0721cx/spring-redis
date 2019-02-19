package com.bule.redis.setnx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

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
     * <p>
     * 这个看着就完美的可以控制并发和锁的机制了
     * <p>
     * FIXME 但是在getset的地方也会有并发的问题
     * 1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
     * 2. 当锁过期的时候，如果多个客户端同时执行jedis.getSet()方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖。
     * 3. 锁不具备拥有者标识，即任何客户端都可以解锁。
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


    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    //意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
    private static final String SET_IF_NOT_EXIST = "NX";
    //意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 使用脚本的方式保证
     *
     * @param user
     */
    public void redislock3(String user) {
        Jedis redis = jedisPool.getResource();
        /**
         *
         * 第一个为key，我们使用key来当锁，因为key是唯一的。
         * 第二个为value，我们传的user，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
         * expireTime为过期超时时间，
         */
        //FIXME 如果让不能获得锁的等待，不能单独提出来 String result = redis.set(token, user, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 1000);
        //如果没有获得锁等待
        while (!LOCK_SUCCESS.equals(redis.set(token, user, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 1000))) {
            try {
                //没有获得锁循环等待
                System.out.println(user + "获得token锁失败");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user + "获得token值锁成功，" + "并执行业务逻辑完成");
        //执行完成后释放锁
        //使用两行脚本保证锁
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object release = redis.eval(script, Collections.singletonList(token), Collections.singletonList(user));
        if (RELEASE_SUCCESS.equals(release)) {
            System.out.println(user + "释放token值锁");
        } else {
            System.out.println(user + "释放token锁失败");
        }
    }
}
