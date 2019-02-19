package com.bule.simple;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://github.com/527515025/JavaTest/blob/master/src/main/java/com/us/redis/JedisTest.java
 * Created by lijianzhen1 on 2019/2/19.
 */
public class UseMap {


    public static void main(String[] args) {
        testMap();
    }


    /**
     * 操作map
     * hash 是一个 string 类型的 field 和 value 的映射表。添加,删除操作都是 O(1)(平均)。
     * hash 特别适合用于存储对象。相对于将对象的每个字段存成单个 string 类型。将一个对象存储在 hash 类型中会占用更少的内存,并且可以更方便的存取整个对象。
     */
    private static void testMap() {
        Jedis jedis = JedisPoolTest.getJedis();
        Map<String, String> map = new HashMap<>();
        map.put("address", "上海");
        map.put("name", "abel");
        map.put("age", "23");
        //
        jedis.hmset("user", map);

        // 从map 中取出 value
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变
        List<String> getmap = jedis.hmget("user", "address");
        System.out.println(getmap);
        List<String> getmap2 = jedis.hmget("user", "address", "age");
        System.out.println(getmap2);

        //删除map中的某个键值
        jedis.hdel("user", "age");

        System.out.println(jedis.hlen("user")); //返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true
        System.out.println("all keys ： " + jedis.hkeys("user"));//返回map对象中的所有key
        System.out.println("all values ： " + jedis.hvals("user"));//返回map对象中的所有value

        //获取 user 中的所有key
        Set<String> keys = jedis.hkeys("user");
        keys.stream().forEach(x -> System.out.println("key: " + x));

    }
}

