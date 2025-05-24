package com.nbastats.query.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.query.model.PlayerStats;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Check if a key exists in Redis.
     */
    public boolean exists(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }

    /**
     * Set a value (e.g., history key) with optional TTL.
     */
    public void set(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, 6, TimeUnit.HOURS); // store JSON for 6 hours
        } catch (Exception e) {
            e.printStackTrace(); // or log error
        }
    }

    /**
     * Get live stats for a player from Redis.
     * Assumes live stats are stored under key like: "live:player:<playerId>"
     */
    public List<PlayerStats> getLiveStatsForPlayer(String key) {
        String json = redisTemplate.opsForValue().get(key);
        return deserializeStats(json);
    }

    /**
     * Get live stats for a team from Redis.
     * Assumes live stats are stored under key like: "live:team:" + teamId
     */
    public List<PlayerStats> getLiveStatsForTeam(String teamId) {
        String redisKey = "live:team:" + teamId;
        String json = redisTemplate.opsForValue().get(redisKey);
        return deserializeStats(json);
    }

    /**
     * Helper: deserialize JSON string to list of PlayerStats.
     */
    private List<PlayerStats> deserializeStats(String json) {
        if (json == null || json.isEmpty()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<PlayerStats>>() {});
        } catch (Exception e) {
            // Handle malformed JSON gracefully
            e.printStackTrace();
            return List.of();
        }
    }

	public String getRaw(String key) {
		return redisTemplate.opsForValue().get(key);
	}
}
