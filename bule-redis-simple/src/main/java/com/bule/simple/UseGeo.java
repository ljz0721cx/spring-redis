package com.bule.simple;

import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

/**
 * 地图相关的操作  地理位置数据处理
 *
 * Created by lijianzhen1 on 2019/2/21.
 */
public class UseGeo {
    public static void main(String[] args) {
        setGeo();
    }

    private static void setGeo() {
        Jedis jedis = JedisPoolTest.getJedis();
        jedis.geoadd("cities", 116.404269, 39.91582, "beijing");
        jedis.geoadd("cities", 121.478799, 31.235456, "shanghai");
        jedis.geoadd("cities", 120.165036, 30.278973, "hangzhou");

        System.out.println(jedis.zrange("cities", 0, -1));

        //计算两地之间的距离,单位千米
        System.out.println(jedis.geodist("cities", "beijing", "shanghai", GeoUnit.KM) + GeoUnit.KM.name());
        //显示两地的地址坐标
        System.out.println(jedis.geopos("cities", "beijing", "shanghai"));
        //查询距离杭州500km内的城市
        for (GeoRadiusResponse geo :
                jedis.georadius("cities", 120, 30, 500, GeoUnit.KM)) {
            System.out.println("距离120，30坐标 500km以内的城市 " + geo.getMemberByString());
        }
        //查询距离某个地方200km内的地址
        for (GeoRadiusResponse geo : jedis.georadiusByMember("cities", "shanghai", 200, GeoUnit.KM)) {
            System.out.println("距离shanghai 200km以内的城市 " + geo.getMemberByString());
        }
        //删除某一地址
        jedis.zrem("cities", "hangzhou");
        System.out.println("删除hangzhou后剩余的城市 " + jedis.zrange("cities", 0, -1));
    }
}
