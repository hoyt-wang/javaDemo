package com.kaishengit.redis.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hoyt on 2017/12/6.
 */

public class RedisCluster {

    @Test
    public void save() throws IOException {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMinIdle(5);

        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort("192.168.106.28",6001));
        hostAndPorts.add(new HostAndPort("192.168.106.28",6002));
        hostAndPorts.add(new HostAndPort("192.168.106.28",6003));
        hostAndPorts.add(new HostAndPort("192.168.106.28",6004));
        hostAndPorts.add(new HostAndPort("192.168.106.28",6005));
        hostAndPorts.add(new HostAndPort("192.168.106.28",6006));

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts,config);
        jedisCluster.append("clusterName","cluster");
        jedisCluster.close();
    }
}
