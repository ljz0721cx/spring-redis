package com.bule.redis.hyperloglog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lijianzhen1 on 2019/2/27.
 */
@Service
public class HyperloglogService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 记录用户的访问IP
     */
    public void recodIp() {
        Jedis jedis = jedisPool.getResource();

        jedis.pfadd("unique::ip::counter","192.189.199.3");
        jedis.pfadd("unique::ip::counter","127.0.0.1");
        jedis.pfadd("unique::ip::counter","127.0.0.1");
        jedis.pfadd("unique::ip::counter","255.255.255.255");
        System.out.println(jedis.pfcount("unique::ip::counter"));
        System.out.println(jedis.pfmerge("unique::ip::counterall","unique::ip::counter"));
    }
}
