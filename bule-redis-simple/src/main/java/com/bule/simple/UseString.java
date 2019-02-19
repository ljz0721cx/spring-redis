package com.bule.simple;

import redis.clients.jedis.Jedis;

/**
 * Created by lijianzhen1 on 2019/2/19.
 */
public class UseString {


    public static void main(String[] args) {

        testString();

    }


    /**
     * 操作string类型的key
     * string 是最基本的类型,而且 string 类型是二进制安全的。意思是 redis 的 string 可以 包含任何数据。
     * 比如 jpg 图片或者序列化的对象。从内部实现来看其实 string 可以看作 byte数组,最大上限是 1G 字节
     */
    public static void testString() {
        Jedis jedis = JedisPoolTest.getJedis();
        jedis.set("name", "abel");
        // set 多个 key: value
        jedis.mset("name", "janle",
                "jetty", "jekey",
                "laha", "lua");
        //age  + 1
        jedis.incr("age");
        System.out.println(jedis.get("name"));
        // delete key
        jedis.del("name");

        JedisPoolTest.close(jedis);
    }
}
