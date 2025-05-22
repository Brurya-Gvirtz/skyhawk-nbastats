package com.nbastats.flusher.redis;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Map;

public class RedisClient {

    private final Jedis jedis;

    public RedisClient() {
        String redisHost = System.getProperty("redis.host", "localhost");
        int redisPort = Integer.parseInt(System.getProperty("redis.port", "6379"));
        this.jedis = new Jedis(redisHost, redisPort);
    }

    /**
     * Get aggregated stats for the gameId from Redis.
     * Assumes a hash with key format "game:{gameId}:stats"
     */
    public Map<String, String> getAggregatedStats(String gameId) {
        String key = "game:" + gameId + ":stats";
        Map<String, String> result = jedis.hgetAll(key);
        if (result == null) {
            return Collections.emptyMap();
        }
        return result;
    }

    /**
     * Delete stats from Redis after flushing.
     */
    public void deleteGameStats(String gameId) {
        String key = "game:" + gameId + ":stats";
        jedis.del(key);
    }

    public void close() {
        jedis.close();
    }
}

