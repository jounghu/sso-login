package com.skrein.sso.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * 2020/5/7 12:13
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Service
@Slf4j
public class RedisService {

    private static final JedisCluster jedisCluster = initCluster();

    private static JedisCluster initCluster() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(100);
        config.setMaxWaitMillis(60000);
        config.setTestOnBorrow(true);

        // 集群模式
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        Set<HostAndPort> nodes = new HashSet<>();
        // FIXME 需要修改redis地址
        for (int i = 0; i < 7; i++) {
            HostAndPort hostAndPort = new HostAndPort("192.168.220.131", 7000 + i);
            nodes.add(hostAndPort);
        }

        return new JedisCluster(nodes, poolConfig);
    }


    public void setTicket(String ticket) {
        jedisCluster.setex(ticket, 60 * 10, "1");
    }

    public boolean checkTicket(String ticket) {
        String s = jedisCluster.get(ticket);
        log.info("check ticket {}  value {}", ticket, s);
        return s != null;
    }


}
