package com.nbastats.playerstats.service;

import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.playerstats.model.PlayerStats;

@Service
public class KafkaProducerService {
    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducerService() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    public void send(PlayerStats stat) throws Exception {
        String value = objectMapper.writeValueAsString(stat);
        producer.send(new ProducerRecord<>("game-stats", stat.getPlayerId().toString(), value));
    }
}
