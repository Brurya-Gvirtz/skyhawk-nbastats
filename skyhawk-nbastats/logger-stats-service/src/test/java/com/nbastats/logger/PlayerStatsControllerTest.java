package com.nbastats.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbastats.logger.controller.PlayerStatsController;
import com.nbastats.logger.model.PlayerStats;
import com.nbastats.logger.service.KafkaProducerService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerStatsController.class)
class PlayerStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogStatsSuccess() throws Exception {
        PlayerStats stat = new PlayerStats();
        stat.setPlayerId("23");
        stat.setPoints(30);
        stat.setRebounds(10);
        stat.setAssists(5);
        stat.setSteals(2);
        stat.setBlocks(1);
        stat.setFouls(2);
        stat.setTurnovers(3);
        stat.setMinutesPlayed(36.5f);

        mockMvc.perform(post("/log-stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stat)))
            .andExpect(status().isOk());
    }

    @Test
    void testLogStatsKafkaError() throws Exception {
        PlayerStats stat = new PlayerStats();
        stat.setPlayerId("23");
        stat.setPoints(30);
        stat.setRebounds(10);
        stat.setAssists(5);
        stat.setSteals(2);
        stat.setBlocks(1);
        stat.setFouls(2);
        stat.setTurnovers(3);
        stat.setMinutesPlayed(36.5f);

        doThrow(new RuntimeException("Kafka error")).when(kafkaProducerService).send(Mockito.any());

        mockMvc.perform(post("/log-stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stat)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Kafka error"));
    }

    @Test
    void testInvalidInput() throws Exception {
        PlayerStats stat = new PlayerStats();
        stat.setPlayerId(null); // Invalid
        stat.setFouls(10);    // Invalid (max 6)

        mockMvc.perform(post("/log-stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stat)))
            .andExpect(status().isBadRequest());
    }
}
