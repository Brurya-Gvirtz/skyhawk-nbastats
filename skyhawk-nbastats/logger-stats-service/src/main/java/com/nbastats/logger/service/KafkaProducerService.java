package com.nbastats.logger.service;

import java.time.LocalDateTime;
import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nbastats.logger.model.PlayerStats;

@Service
public class KafkaProducerService {
    private final Producer<String, String> producer;
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaProducerService(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        if (bootstrapServers == null || bootstrapServers.isEmpty()) {
            throw new IllegalStateException("Kafka bootstrap servers not configured");
        }
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }
    
 // For testing
    public KafkaProducerService(Producer<String, String> producer) {
        this.producer = producer;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void send(PlayerStats stat) throws Exception {
    	stat.setLastUpdated(LocalDateTime.now());
        String value = objectMapper.writeValueAsString(stat);
        producer.send(new ProducerRecord<>("game-stats", stat.getPlayerId().toString(), value));
    }
}
