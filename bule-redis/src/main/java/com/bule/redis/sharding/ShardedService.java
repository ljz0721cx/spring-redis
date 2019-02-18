package com.bule.redis.sharding;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import java.util.Random;

/**
 * 使用时候要注意，一般不会有碰撞
 * 如果需要可以自己实现hash分片算法，hash一致性算法在抢占中不能使用。
 * Created by lijianzhen1 on 2019/2/18.
 */
@Service
public class ShardedService {

    @Autowired
    private ShardedJedisPool shardedJedisPool;


    /**
     * MurmurHash 的算法测试，
     */
    @Test
    public void TestmurmurHash() {
        Hashing hashing = Hashing.MURMUR_HASH;
        for (int i = 0; i < 1000; i++) {
            System.out.println(hashing.hash("sharded_info_" +i));
        }
    }

    /**
     *
     */
    public void test() {
        ShardedJedis jedis = shardedJedisPool.getResource();
        for (int i = 0; i < 1000; i++) {
            jedis.set("sharded_info_" + i, String.valueOf(new Random().nextInt(1000)));
        }
        for (int i = 0; i < 1000; i++) {
            jedis.del("sharded_info_" + i);
        }
        jedis.close();
    }
}

