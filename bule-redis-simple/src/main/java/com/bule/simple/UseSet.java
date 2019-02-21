package com.bule.simple;

import redis.clients.jedis.Jedis;

/**
 * Created by lijianzhen1 on 2019/2/20.
 */
public class UseSet {
    public static void main(String[] args) {

        //testSet();



    }


    /**
     * set 是无序集合,最大可以包含(2 的 32 次方-1)40多亿个元素。
     * set 的是通过 hash table 实现的, 所以添加,删除,查找的复杂度都是 O(1)。hash table 会随着添加或者删除自动的调整大小。
     */
    public static void testSet() {
        Jedis jedis = JedisPoolTest.getJedis();
        jedis.sadd("person", "abel1");
        jedis.sadd("person", "abel2");
        jedis.sadd("person", "abel3");
        jedis.sadd("person", "abel4");
        jedis.sadd("person", "abel4");
        //获取所有加入的value
        System.out.println(jedis.smembers("person"));
        // 从 person 中 移除 abel4
        jedis.srem("person", "abel4");
        //获取所有加入的value
        System.out.println("values: " + jedis.smembers("person"));

        //判断 abels 是否是 person 集合的元素
        System.out.println(jedis.sismember("person", "abels"));

        //返回集合中的一个随机元素
        System.out.println(jedis.srandmember("person"));
        //返回集合的元素个数
        System.out.println(jedis.scard("person"));

    }
}
