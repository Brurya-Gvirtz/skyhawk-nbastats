package com.nbastats.aggregator.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.aggregator.model.PlayerStats;
import com.nbastats.aggregator.service.RedisAggregatorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.mockito.Mockito.*;

class RedisAggregatorServiceTest {

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private RedisAggregatorService redisService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
	@BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisService = new RedisAggregatorService(redisTemplate);
    }

    @Test
    void testUpdateStats_setsPlayerAndTeamKeysInRedis() throws Exception {
        PlayerStats stat = new PlayerStats("111","playerId", "gameId", "teamId", 10, 20,  10, 20,  10, 20,  10, 20, null);

        redisService.updateStats(stat);

        String playerKey = "player:p1:game:g1";
        String teamKey = "team:t1:game:g1";
        String expectedJson = objectMapper.writeValueAsString(stat);

        verify(valueOperations).set(eq(playerKey), eq(expectedJson), eq(Duration.ofMinutes(10)));
        verify(valueOperations).set(eq(teamKey), eq(expectedJson), eq(Duration.ofMinutes(10)));
    }
}

