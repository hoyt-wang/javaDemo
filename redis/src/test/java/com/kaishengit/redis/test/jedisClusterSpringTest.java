package com.kaishengit.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

/**
 * Created by hoyt on 2017/12/6.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class jedisClusterSpringTest {

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void setValue() {
        jedisCluster.set("name","spring");
    }
}
