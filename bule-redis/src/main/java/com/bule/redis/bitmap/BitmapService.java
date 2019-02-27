package com.bule.redis.bitmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis允许使用二进制数据的Key(binary keys) 和二进制数据的Value(binary values)。Bitmap就是二进制数据的value。Redis的 setbit(key, offset, value)操作对指定的key的value的指定偏移(offset)的位置1或0，时间复杂度是O(1)。
 * 一个简单的例子：日活跃用户
 * 为了统计今日登录的用户数，我们建立了一个bitmap,每一位标识一个用户ID。当某个用户访问我们的网页或执行了某个操作，就在bitmap中把标识此用户的位置为1。在Redis中获取此bitmap的key值是通过用户执行操作的类型和时间戳获得的。
 *
 * 统计一亿人每天的登录情况，用一亿bit，约1200WByte，约10M的字符就能表示（因为bitop命令的返回值是保存到 time中的字符串的长度（以字节byte为单位），和输入 key 中最长的字符串长度相等。即1亿除以8bit=1250万Byte）；
 * <p>
 * Created by lijianzhen1 on 2019/2/26.
 */
@Service
public class BitmapService {


    @Autowired
    private JedisPool jedisPool;


    private static final String login_data_0227 = "login:20190227";
    private static final String login_data_0226 = "login:20190226";
    private static final String login_data_0225 = "login:20190225";
    private static final String login_data_0224 = "login:20190224";


    private static final String login_activing_data_0224_27 = "login:activing:data_0224_27";
    private static final String login_activied_data_0224_27 = "login:activied:data_0224_27";

    /**
     * 这里对应的bit位的设置值,最大的位数为2^32次的大小
     * <p>
     * <p>
     * 默认的bit位的值为   0 0 0 0 0 0 0 0 0 0 ... 0
     * 设置对应用户的登录  1 1 1 1 1 1 1 1 1 1 ... 0   直到899位设置为20190227时间登录用户的情况
     */
    public void setBitMap() {
        Jedis jedis = jedisPool.getResource();
        /**
         * 第一个参数：某天登录的次数
         * 第二个参数：登录用户的序列号，也算是在bitmap中的偏移位置
         * 第三个参数：设置的bit位置的值
         */
        for (int i = 0; i < 900; i++) {
            jedis.setbit(login_data_0227, i, "1");
        }


        /**
         * 这里从后往前设置登录活跃
         */
        for (int i = 1000; i > 100; i--) {
            jedis.setbit(login_data_0226, i, "1");
            jedis.setbit(login_data_0225, i, "1");
            jedis.setbit(login_data_0224, i, "1");
        }
    }


    /**
     * 获得用户的登录情况，是否有登录过
     * 在900之前的用户登录过，在900之后的用户没有活跃过
     */
    public void getBitMap() {
        Jedis jedis = jedisPool.getResource();
        for (int i = 0; i < 1000; i++) {
            System.out.println(jedis.getbit(login_data_0227, i));
        }
    }


    /**
     * 统计某天的登录数，也是用户活跃数，统计用户活跃数为
     * 900个用户活跃
     */
    public void bitCount() {
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.bitcount(login_data_0227));
    }


    /**
     * 统计所有某段时间内的活跃用户
     */
    public void daysCountActive() {
        Jedis jedis = jedisPool.getResource();

        List<String> datas = new ArrayList<>();
        datas.add(login_data_0224);
        datas.add(login_data_0225);
        datas.add(login_data_0226);
        datas.add(login_data_0227);


        //统计最近几天连续都活跃过得用户统计放到login_activing_data_0224_27中
        jedis.bitop(BitOP.AND, login_activing_data_0224_27, login_data_0225, login_data_0224, login_data_0226, login_data_0227);
        //统计最近几天连续都活跃过得用户统计放到login_activied_data_0224_27中
        jedis.bitop(BitOP.OR, login_activied_data_0224_27, login_data_0225, login_data_0224, login_data_0226, login_data_0227);

        System.out.println("连续活跃过的用户数：" + jedis.bitcount(login_activing_data_0224_27) +
                "\n统计最近活跃过的用户数：" + jedis.bitcount(login_activied_data_0224_27));
    }
}

