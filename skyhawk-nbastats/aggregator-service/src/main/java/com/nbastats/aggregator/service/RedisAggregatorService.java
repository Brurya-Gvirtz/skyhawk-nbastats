package com.nbastats.aggregator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.aggregator.model.PlayerStats;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisAggregatorService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOps;
    private final ObjectMapper objectMapper;
    private final Duration ttl = Duration.ofMinutes(10);

    public RedisAggregatorService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.objectMapper = new ObjectMapper();
    }

    public void updateStats(PlayerStats event) {
        String playerKey = "player:" + event.getPlayerId() + ":game:" + event.getGameId();
        String teamKey = "team:" + event.getTeamId() + ":game:" + event.getGameId();

        try {
            // Serialize PlayerStats for player
            String playerJson = objectMapper.writeValueAsString(event);
            valueOps.set(playerKey, playerJson, ttl);

            // Optionally, create a reduced TeamStats object or reuse PlayerStats if appropriate
            String teamJson = objectMapper.writeValueAsString(event); // Replace with actual TeamStats if needed
            valueOps.set(teamKey, teamJson, ttl);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing stats", e);
        }
    }
}
