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
        promotionSalesService.promot(100);
    }
}
