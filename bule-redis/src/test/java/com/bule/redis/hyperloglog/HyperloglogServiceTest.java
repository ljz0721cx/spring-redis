package com.bule.redis.hyperloglog;

import com.bule.redis.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现记录网站每天访问的独立IP数量这样的一个功能
 * Created by lijianzhen1 on 2019/2/27.
 */
public class HyperloglogServiceTest extends BaseTest {

    @Autowired
    private HyperloglogService hyperloglogService;

    @Test
    public void TestrecodIp() {

        hyperloglogService.recodIp();
    }
}
