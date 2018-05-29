package com.microdev.automation.restng.util.db;

import com.google.common.base.Preconditions;
import com.microdev.automation.restng.env.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by wuchao on 17/6/28.
 */
public class Redis {

    private static final String PROP_REDIS_IP = "redis.ip";
    private static final String PROP_REDIS_PORT = "redis.port";
    private Jedis jedis;

    public static String set(String ip, int port, String key, String value) {
        Redis redis = getInstance(ip, port);
        String result = redis.jedis.set(key, value);
        redis.closeJedis();
        return result;
    }

    public static Redis getInstance() {
        String host = Property.dbGet(PROP_REDIS_IP);
        Preconditions.checkArgument(StringUtils.isNotBlank(host), "com.microdev.automation.restng.ut.db.properties没有设置redis.ip");
        String port = Property.dbGet(PROP_REDIS_PORT);
        Preconditions.checkArgument(NumberUtils.isDigits(port), "com.microdev.automation.restng.ut.db.properties没有设置redis.port");
        Redis redis = new Redis();
        redis.initJedis(host, Integer.parseInt(port));
        return redis;
    }

    public static Redis getInstance(String ip, int port) {
        Preconditions.checkArgument(StringUtils.isNotBlank(ip));
        Redis redis = new Redis();
        redis.initJedis(ip, port);
        return redis;
    }

    public static String set(String key, String value) {
        Redis redis = getInstance();
        String result = redis.jedis.set(key, value);
        redis.closeJedis();
        return result;
    }

    public static String get(String key) {
        Redis redis = getInstance();
        String result = redis.jedis.get(key);
        redis.closeJedis();
        return result;
    }

    private void initJedis(String ip, int port) {
        jedis = new Jedis(ip, port);
    }

    private void closeJedis() {
        jedis.disconnect();
    }

    public Jedis getJedis() {
        return jedis;
    }

}
