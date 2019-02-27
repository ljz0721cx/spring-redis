package com.bule.redis.pipeline;

import com.bule.redis.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lijianzhen1 on 2019/2/27.
 */
public class PipelineServiceTest extends BaseTest{


    @Autowired
    private PipelineService pipelineService;


    @Test
    public void Testpipeline(){
        pipelineService.pipeline();
    }


}
