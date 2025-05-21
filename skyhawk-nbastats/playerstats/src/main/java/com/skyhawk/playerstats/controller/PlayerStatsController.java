package com.skyhawk.playerstats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skyhawk.playerstats.dto.PlayerStatRequest;
import com.skyhawk.playerstats.service.PlayerStatsService;


@RestController
@RequestMapping("/api/players")
public class PlayerStatsController {

    private final PlayerStatsService playerStatsService;

    public PlayerStatsController(PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
    }

    @PostMapping("/stats")
    public ResponseEntity<String> logStats(@RequestBody PlayerStatRequest request) {
        playerStatsService.addPlayerStats(request);
        return ResponseEntity.ok("Player stats saved successfully");
    }
}

