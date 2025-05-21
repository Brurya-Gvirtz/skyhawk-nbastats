package com.skyhawk.statsaggregation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skyhawk.statsaggregation.dto.PlayerAggregateResponse;
import com.skyhawk.statsaggregation.dto.TeamAggregateResponse;
import com.skyhawk.statsaggregation.service.AggregationService;

@RestController
@RequestMapping("/api/aggregate")
public class AggregationController {

    private final AggregationService aggregationService;

    @Autowired
    public AggregationController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping("/player/{playerId}")
    public PlayerAggregateResponse getPlayerAggregate(@PathVariable Long playerId) {
        return aggregationService.getPlayerAggregate(playerId);
    }

    @GetMapping("/team/{teamId}")
    public TeamAggregateResponse getTeamAggregate(@PathVariable Long teamId) {
        return aggregationService.getTeamAggregate(teamId);
    }
}
