package com.nbastats.statsaggregation.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.statsaggregation.model.PlayerStats;

@Service
public class KafkaConsumerService {

	private final RedisAggregatorService redisService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaConsumerService(RedisAggregatorService redisService) {
        this.redisService = redisService;
    }

    @KafkaListener(topics = "game-stats", groupId = "live-aggregator")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            PlayerStats stat = objectMapper.readValue(record.value(), PlayerStats.class);
            redisService.updateStats(stat);
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
    }
}
