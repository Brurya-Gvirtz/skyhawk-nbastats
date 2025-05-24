package com.nbastats.query.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbastats.query.dto.PlayerStatsSummary;
import com.nbastats.query.dto.TeamStatsSummary;
import com.nbastats.query.service.StatsQueryService;

@RestController
@RequestMapping("/api/stats")
public class StatsQueryController {

    private final StatsQueryService statsService;

    public StatsQueryController(StatsQueryService statsQueryService) {
        this.statsService = statsQueryService;
    }

    @GetMapping("/player/{playerId}")
    public PlayerStatsSummary getStatsByPlayer(@PathVariable String playerId) {
        return statsService.getAveragePointsForPlayer(playerId);
    }

    @GetMapping("/team/{teamId}")
    public TeamStatsSummary getStatsByTeam(@PathVariable String teamId) {
        return statsService.getAveragePointsForTeam(teamId);
    }
}
