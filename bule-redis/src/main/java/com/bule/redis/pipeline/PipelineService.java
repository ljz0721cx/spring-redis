package com.bule.redis.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lijianzhen1 on 2019/2/27.
 */
@Service
public class PipelineService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 管道添加数据
     */
    public void pipeline() {
        Jedis jedis = jedisPool.getResource();

        Pipeline pipe = jedis.pipelined();// 先创建一个pipeline的链接对象
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pipe.set("popeline:"+String.valueOf(i), String.valueOf(i));
        }
        pipe.sync();// 获取所有的response
        long end = System.currentTimeMillis();
        System.out.println("the pipeline total time is:" + (end - start));




        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pipe.set("normal:"+String.valueOf(i), String.valueOf(i));
        }
        end = System.currentTimeMillis();
        System.out.println("the normal total time is:" + (end - start));



        BlockingQueue<String> logQueue = new LinkedBlockingQueue<String>();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            try {
                logQueue.put("i=" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end = System.currentTimeMillis();
        System.out.println("the BlockingQueue total time is:" + (end - start));

    }


}
