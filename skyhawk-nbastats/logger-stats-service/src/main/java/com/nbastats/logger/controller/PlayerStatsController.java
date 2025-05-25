package com.nbastats.logger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nbastats.logger.model.PlayerStats;
import com.nbastats.logger.service.KafkaProducerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/log-stats")
@Validated
public class PlayerStatsController {

    @Autowired
    private KafkaProducerService producer;

    @PostMapping
    public ResponseEntity<?> logStats(@Valid @RequestBody PlayerStats stat)  throws Exception {
        try {
            producer.send(stat);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Kafka error");
        }
    }
}



