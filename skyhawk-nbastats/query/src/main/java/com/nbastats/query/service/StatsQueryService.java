package com.nbastats.query.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nbastats.query.dto.PlayerStatsSummary;
import com.nbastats.query.dto.TeamStatsSummary;
import com.nbastats.query.model.PlayerStats;
import com.nbastats.query.repository.StatsJdbcRepository;

@Service
public class StatsQueryService {
	
	private final RedisService redisService;
    private final StatsJdbcRepository statsJdbcRepository;

    public StatsQueryService(RedisService redisService, StatsJdbcRepository statsJdbcRepository) {
        this.redisService = redisService;
        this.statsJdbcRepository = statsJdbcRepository;
    }

    public PlayerStatsSummary getAveragePointsForPlayer(String playerId) {
        // 1. DB stats (finished games)
        List<PlayerStats> dbStats = statsJdbcRepository.getStatsForPlayer(playerId);
        Set<Long> dbGameIds = dbStats.stream()
                                     .map(PlayerStats::getGameid)
                                     .collect(Collectors.toSet());

        // 2. Redis stats (live games)
        List<PlayerStats> redisStats = redisService.getLiveStatsForPlayer(playerId);
        List<PlayerStats> newRedisStats = redisStats.stream()
                                                    .filter(stat -> !dbGameIds.contains(stat.getGameid()))
                                                    .collect(Collectors.toList());

        // 3. Combine stats (only one Redis game per gameId)
        List<PlayerStats> allStats = new ArrayList<>(dbStats);
        allStats.addAll(newRedisStats);

        // 4. Calculate average
        int gameCount = allStats.size();
        if (gameCount == 0) return new PlayerStatsSummary();

        int totalPoints = 0;
        int totalRebounds = 0;
        int totalAssists = 0;
        int totalSteals = 0;
        int totalBlocks = 0;
        int totalFouls = 0;
        int totalTurnovers = 0;

        String playerName = null;

        for (PlayerStats stat : allStats) {
            if (playerName == null) playerName = stat.getPlayerName();
            totalPoints += stat.getPoints();
            totalRebounds += stat.getRebound();
            totalAssists += stat.getAssist();
            totalSteals += stat.getSteals();
            totalBlocks += stat.getBlocks();
            totalFouls += stat.getFounds();
            totalTurnovers += stat.getTurnovers();
        }

        PlayerStatsSummary avg = new PlayerStatsSummary();
        avg.setPlayerName(playerName);
        avg.setGames(gameCount);
        avg.setAvgPoints((double) totalPoints / gameCount);
        avg.setAvgRebounds((double) totalRebounds / gameCount);
        avg.setAvgAssists((double) totalAssists / gameCount);
        avg.setAvgSteals((double) totalSteals / gameCount);
        avg.setAvgBlocks((double) totalBlocks / gameCount);
        avg.setAvgFouls((double) totalFouls / gameCount);
        avg.setAvgTurnovers((double) totalTurnovers / gameCount);

        return avg;
    }

	public TeamStatsSummary getStatsForTeam(String teamId) {
		 // 1. DB stats (finished games)
        List<PlayerStats> dbStats = statsJdbcRepository.getStatsForPlayer(teamId);
        Set<Long> dbGameIds = dbStats.stream()
                                     .map(PlayerStats::getGameid)
                                     .collect(Collectors.toSet());

        // 2. Redis stats (live games)
        List<PlayerStats> redisStats = redisService.getLiveStatsForPlayer(teamId);
        List<PlayerStats> newRedisStats = redisStats.stream()
                                                    .filter(stat -> !dbGameIds.contains(stat.getGameid()))
                                                    .collect(Collectors.toList());

        // 3. Combine stats (only one Redis game per gameId)
        List<PlayerStats> allStats = new ArrayList<>(dbStats);
        allStats.addAll(newRedisStats);

        // 4. Calculate average
        int gameCount = allStats.size();
        if (gameCount == 0) return new TeamStatsSummary();

        int totalPoints = 0;
        int totalRebounds = 0;
        int totalAssists = 0;
        int totalSteals = 0;
        int totalBlocks = 0;
        int totalFouls = 0;
        int totalTurnovers = 0;

        String teamName = null;

        for (PlayerStats stat : allStats) {
            if (teamName == null) teamName = stat.getTeamName();
            totalPoints += stat.getPoints();
            totalRebounds += stat.getRebound();
            totalAssists += stat.getAssist();
            totalSteals += stat.getSteals();
            totalBlocks += stat.getBlocks();
            totalFouls += stat.getFounds();
            totalTurnovers += stat.getTurnovers();
        }

        TeamStatsSummary avg = new TeamStatsSummary();
        avg.setTeamName(teamName);
        avg.setGames(gameCount);
        avg.setAvgPoints((double) totalPoints / gameCount);
        avg.setAvgRebounds((double) totalRebounds / gameCount);
        avg.setAvgAssists((double) totalAssists / gameCount);
        avg.setAvgSteals((double) totalSteals / gameCount);
        avg.setAvgBlocks((double) totalBlocks / gameCount);
        avg.setAvgFouls((double) totalFouls / gameCount);
        avg.setAvgTurnovers((double) totalTurnovers / gameCount);

        return avg;
	}
}
