package com.nbastats.flusher.redis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.flusher.model.PlayerStats;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisClient(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // Save PlayerStats JSON with key
    public void savePlayerStats(PlayerStats stats) {
        try {
            String key = "player:" + stats.getPlayerId() + ":" + stats.getGameId();
            String value = objectMapper.writeValueAsString(stats);
            redisTemplate.opsForValue().set(key, value, 2, TimeUnit.HOURS); // example TTL
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing PlayerStats", e);
        }
    }

    // Save TeamStats JSON with key (similar to PlayerStats)
    public void saveTeamStats(PlayerStats stats) {
        try {
            String key = "team:" + stats.getTeamId() + ":" + stats.getGameId();
            String value = objectMapper.writeValueAsString(stats);
            redisTemplate.opsForValue().set(key, value, 2, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing TeamStats", e);
        }
    }

    // Get all keys related to players and teams for active games
    public Set<String> getAllKeys() {
        // Scan keys like player:* and team:*
        // Note: Using keys("*") can be expensive in production, better to use SCAN or a Redis set for keys management
        Set<String> playerKeys = redisTemplate.keys("player:*");
        Set<String> teamKeys = redisTemplate.keys("team:*");

        playerKeys.addAll(teamKeys);
        return playerKeys;
    }

    // Get PlayerStats by key
    public PlayerStats getPlayerStats(String key) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, PlayerStats.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing PlayerStats", e);
        }
    }

    // Delete key from Redis
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
