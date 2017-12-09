package com.kaishengit.redis.test;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by hoyt on 2017/12/4.
 */

public class JedisTest {

    @Test
    public void save() {
        Jedis jedis = new Jedis("192.168.208.30",6379);
        jedis.set("username","alex");
        String name = jedis.get("name");
        jedis.close();
    }

    @Test
    public void poolSave() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(15);
        config.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(config,"192.168.106.28",6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("age","23");
        jedis.close();
        jedisPool.destroy();
    }


}
