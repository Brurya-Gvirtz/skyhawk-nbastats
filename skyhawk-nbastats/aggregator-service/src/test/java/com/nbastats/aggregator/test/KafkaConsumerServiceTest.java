package com.nbastats.aggregator.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.aggregator.model.PlayerStats;
import com.nbastats.aggregator.service.KafkaConsumerService;
import com.nbastats.aggregator.service.RedisAggregatorService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    private final RedisAggregatorService redisService = mock(RedisAggregatorService.class);
    private final KafkaConsumerService consumerService = new KafkaConsumerService(redisService);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testConsume_validMessage_callsRedisService() throws Exception {
        PlayerStats stats = new PlayerStats("111","p1", "g1", "t1", 10, 20,  10, 20,  10, 20,  10, 20, null);
        String json = objectMapper.writeValueAsString(stats);

        ConsumerRecord<String, String> record = new ConsumerRecord<>("game-stats", 0, 0L, null, json);

        consumerService.consume(record);

        ArgumentCaptor<PlayerStats> captor = ArgumentCaptor.forClass(PlayerStats.class);
        verify(redisService, times(1)).updateStats(captor.capture());

        PlayerStats captured = captor.getValue();
        assert captured.getPlayerId().equals("p1");
        assert captured.getGameId().equals("g1");
    }

    @Test
    void testConsume_invalidJson_doesNotCrash() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("game-stats", 0, 0L, null, "invalid-json");

        consumerService.consume(record);

        verify(redisService, never()).updateStats(any());
    }
}
