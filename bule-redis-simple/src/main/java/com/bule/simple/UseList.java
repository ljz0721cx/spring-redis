package com.bule.simple;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * Created by lijianzhen1 on 2019/2/20.
 */
public class UseList {



    public static void main(String[] args) {

        testList();

    }


    /**
     * 操作string类型的key
     * string 是最基本的类型,而且 string 类型是二进制安全的。意思是 redis 的 string 可以 包含任何数据。
     * 比如 jpg 图片或者序列化的对象。从内部实现来看其实 string 可以看作 byte数组,最大上限是 1G 字节
     */
    public static void testList() {
        Jedis jedis = JedisPoolTest.getJedis();
        //移除 lists 中所有的内容
        jedis.del("lists");

        // 向key lists 链表头部添加字符串元素
        jedis.lpush("lists", "abel1");
        jedis.lpush("lists", "abel2");
        jedis.lpush("lists", "abel3");
        // 向key lists 链表尾部添加字符串元素
        jedis.rpush("lists", "abel4");
        jedis.rpush("lists", "abel5");

        //获取lists 的长度
        System.out.println(jedis.llen("lists"));
        //按顺序输出链表中所有元素
        System.out.println(jedis.lrange("lists", 0, -1));
        //在abel4 前插入 abelLinsert
        jedis.linsert("lists", BinaryClient.LIST_POSITION.BEFORE, "abel4", "abelLinsert");
        System.out.println(jedis.lrange("lists", 0, -1));

        //在下标为2的位置插入 abelLset 前插入abel0
        jedis.lset("lists", 2, "abelLset");
        System.out.println(jedis.lrange("lists", 0, -1));


        //插入两个 abel2 为了后面的删除
        jedis.lpush("lists", "abel2");
        jedis.lpush("lists", "abel2");

        System.out.println(jedis.lrange("lists", 0, -1));
        // 从 lists 中删除 3 个 value = abel2 的元素 , 可以不连续
        // 当删除 count = 0 个 时则删除全部 value = abel2 的元素
        // 设置数字如果是大于0，则删除对应数字的元素，如果是0删除所有的匹配元素
        jedis.lrem("lists", 0, "abel2");

        System.out.println(jedis.lrange("lists", 0, -1));

    }
}
