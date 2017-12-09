package com.kaishengit.redis.test;

import com.alibaba.fastjson.JSON;
import com.kaishengit.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoyt on 2017/12/4.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void setUser() {
        User user = new User(1011,"李四","南阳");
        Jedis jedis = jedisPool.getResource();
        Map<String,String> map = new HashMap<String, String>();
        map.put("id",user.getId().toString());
        map.put("userName",user.getName());
        map.put("address",user.getAddress());

        jedis.hmset("user:1011",map);

        jedis.close();;

    }

    @Test
    public void getUser() {
        Jedis jedis = jedisPool.getResource();
        Map<String,String> result = jedis.hgetAll("user:1011");
        User user = new User();
        user.setId(Integer.valueOf(result.get("id")));
        user.setName(result.get("name"));
        user.setAddress(result.get("address"));
        System.out.println(user);
        jedis.close();
    }

    @Test
    public void save() {
        Jedis jedis = jedisPool.getResource();
        User user = new User(1012,"zhangsan","beijing");
        jedis.set("user:1012", JSON.toJSONString(user));
        jedis.close();
    }

    @Test
    public void get() {
        Jedis jedis = jedisPool.getResource();
        User user = JSON.parseObject(jedis.get("user:1012"),User.class);
        System.out.println(user);
        jedis.close();
    }
}
