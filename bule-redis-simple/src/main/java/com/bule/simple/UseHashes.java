package com.bule.simple;

import redis.clients.jedis.Jedis;

/**
 * Created by lijianzhen1 on 2019/2/21.
 */
public class UseHashes {


    public static void main(String[] args) {

        testHash();

    }


    /**
     * 用于用户对于一个对象绑定多个信息的场景，比如围绕用户下的所有信息数据
     */
    public static void testHash() {
        Jedis jedis = JedisPoolTest.getJedis();
        jedis.flushDB();
        String ID_INFO = "id_info"; //用户信息
        String INTERST = "interest";//兴趣爱好
        String VIP_CODE = "vip_code";//用户等级

        jedis.hset("userId_1", ID_INFO, "userinfo:{}");
        jedis.hset("userId_1", INTERST, "interst:{}");
        jedis.hset("userId_1", VIP_CODE, "vipinfo:{}");

        System.out.println(jedis.hget("userId_1",ID_INFO) +"  "+ jedis.hget("userId_1",INTERST));

        //获得用户的所有信息数据
        System.out.println(jedis.hgetAll("userId_1"));
    }
}
