package com.bule.simple;

import redis.clients.jedis.Jedis;

/**
 * Created by lijianzhen1 on 2019/2/20.
 */
public class UseSet {
    public static void main(String[] args) {

        //testSet();

        testSortSet();

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

    /**
     * sorted set 是有序集合,它在 set 的基础上增加了一个顺序属性,这一属性在添加修改元素的时候可以指定,每次指定后,会自动重新按新的值调整顺序。
     * 可以理解了有两列的 mysql 表,一列存 value,一列存顺序。
     * <p>
     * sort set和set类型一样，也是string类型元素的集合，也没有重复的元素，
     * <p>
     * 不同的是sort set每个元素都会关联一个权,通过权值可以有序的获取集合中的元素添加，删除，查找的复杂度都是O(1)
     */
    public static void testSortSet() {
        Jedis jedis = JedisPoolTest.getJedis();
        final String sortKey = "sortKey";
        System.out.println(jedis.flushDB());
        jedis.zadd(sortKey, 300, "hadoop");
        jedis.zadd(sortKey, 20, "mysql");
        jedis.zadd(sortKey, 40, "redis");

        // 按权值从小到大排序
        System.out.println(jedis.zrange(sortKey, 0, -1));

        // 按权值从大到小排序
        System.out.println(jedis.zrevrange(sortKey, 0, -1));


        // 元素个数
        System.out.println("元素个数：" + jedis.zcard(sortKey));
        // 元素abel 的 下标
        System.out.println("元素hadoop 的 下标：" + jedis.zscore(sortKey, "hadoop"));

        // 删除元素 hadoop
        //jedis.zrem("sortKey", "hadoop");
        //权值 0-100的总数
        System.out.println("0-100 的总数： " + jedis.zcount(sortKey, 0, 100));
        //给元素 redis 的 权值 + 50
        System.out.println("给元素的 权值  + 50： " + jedis.zincrby(sortKey, 50, "redis"));
        //权值在0-100的值
        System.out.println("权值在0-100的值： " + jedis.zrangeByScore(sortKey, 0, 100));
        //返回 redis 的权值的排名，从0开始计数
        System.out.println(jedis.zrank(sortKey, "redis"));
        // 输出整个集合值
        System.out.println("输出整个集合值： " + jedis.zrange(sortKey, 0, -1));
    }
}
