package com.bule.redis.bitmap;

import com.bule.redis.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 利用bitmap做用户的活跃的统计
 * Created by lijianzhen1 on 2019/2/27.
 */
public class BitmapServiceTest extends BaseTest {
    @Autowired
    private BitmapService bitmapService;

    @Test
    public void TestsetBitMap() {
        bitmapService.setBitMap();
    }

    @Test
    public void TestgetBitMap() {
        bitmapService.getBitMap();
    }

    @Test
    public void TestbitCount(){
        bitmapService.bitCount();
    }


    @Test
    public void TestdaysCountActive(){
        bitmapService.daysCountActive();
    }

}
