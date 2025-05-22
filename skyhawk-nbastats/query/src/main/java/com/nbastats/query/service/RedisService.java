package com.nbastats.query.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.query.model.PlayerStats;

@Service
public class RedisService {
	@Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PlayerStats> getLiveStatsForPlayer(String playerId) {
        Set<String> keys = redisTemplate.keys("player:" + playerId + ":game:*");
        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .map(this::deserializeStats)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<PlayerStats> getLiveStatsForTeam(String teamId) {
        Set<String> keys = redisTemplate.keys("team:" + teamId + ":game:*");
        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .map(this::deserializeStats)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PlayerStats deserializeStats(String json) {
        try {
            return objectMapper.readValue(json, PlayerStats.class);
        } catch (Exception e) {
            // Log and skip broken entries
            return null;
        }
    }
}
