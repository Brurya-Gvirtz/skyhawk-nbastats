package com.nbastats.logger;

import static org.mockito.Mockito.*;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nbastats.logger.model.PlayerStats;
import com.nbastats.logger.service.KafkaProducerService;

public class KafkaProducerServiceTest {

    private Producer<String, String> mockProducer;
    private KafkaProducerService kafkaProducerService;

    @SuppressWarnings("unchecked")
	@BeforeEach
    void setup() {
    	mockProducer = (Producer<String, String>) mock(Producer.class);
        kafkaProducerService = new KafkaProducerService(mockProducer);
    }

    @SuppressWarnings("unchecked")
	@Test
    void testSendPlayerStats() throws Exception {
        // Given
        PlayerStats stats = new PlayerStats();
        stats.setPlayerId("23");
        stats.setPoints(10);
        stats.setRebounds(5);
        stats.setAssists(7);
        stats.setMinutesPlayed(36.5f);

        // When
        kafkaProducerService.send(stats);

        // Then
        verify(mockProducer, times(1)).send(any(ProducerRecord.class));
    }
}

