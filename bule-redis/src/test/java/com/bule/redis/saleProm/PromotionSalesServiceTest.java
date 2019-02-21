package com.bule.redis.saleProm;

import com.bule.redis.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lijianzhen1 on 2019/2/15.
 */
public class PromotionSalesServiceTest extends BaseTest {


    @Autowired
    private PromotionSalesService promotionSalesService;

    @Test
    public void testpromot(){
        //第一种实现方式
        //promotionSalesService.promot(100);
        //第二种实现方式
        promotionSalesService.promot1(100);
    }
}
