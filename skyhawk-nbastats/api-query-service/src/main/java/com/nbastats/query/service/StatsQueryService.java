package com.nbastats.query.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * Get average stats for a player.
     * - Checks Redis for "history:playerid:<playerId>"
     * - If history key exists, fetch only live Redis stats.
     * - Otherwise, fetch DB stats + live Redis stats, then set the history key in Redis.
     */
    public PlayerStatsSummary getAveragePointsForPlayer(String playerId) {
        String historyKey = "history:playerid:" + playerId;

        PlayerStatsSummary historySummary = null;
        List<PlayerStats> dbStats = new ArrayList<>();

        if (redisService.exists(historyKey)) {
            // Deserialize the summary directly from Redis
            String summaryJson = redisService.getRaw(historyKey);
            try {
                historySummary = new ObjectMapper().readValue(summaryJson, PlayerStatsSummary.class);
            } catch (Exception e) {
                e.printStackTrace(); // fallback to DB if deserialization fails
            }
        } else {
            // No Redis summary -> load full history from DB
            dbStats = statsJdbcRepository.getStatsForPlayer(playerId);
        }

        // Fetch live stats
        List<PlayerStats> liveStats = redisService.getLiveStatsForPlayer(playerId);

        List<PlayerStats> filteredLiveStats = new ArrayList<>();
        Set<Long> historyGameIds;
        if (historySummary != null) {
            historyGameIds = Collections.emptySet(); // placeholder, not used
            filteredLiveStats = liveStats;
        } else {
            historyGameIds = dbStats.stream()
                    .map(PlayerStats::getGameid)
                    .collect(Collectors.toSet());

            filteredLiveStats = liveStats.stream()
                    .filter(stat -> !historyGameIds.contains(stat.getGameid()))
                    .collect(Collectors.toList());
        }

        // Merge both
        int totalGames = 0;
        int totalPoints = 0;
        int totalRebounds = 0;
        int totalAssists = 0;
        int totalSteals = 0;
        int totalBlocks = 0;
        int totalFouls = 0;
        int totalTurnovers = 0;
        String playerName = null;

        // From Redis summary or DB
        if (historySummary != null) {
            int histGames = historySummary.getGames();
            totalGames += histGames;
            totalPoints += historySummary.getAvgPoints() * histGames;
            totalRebounds += historySummary.getAvgRebounds() * histGames;
            totalAssists += historySummary.getAvgAssists() * histGames;
            totalSteals += historySummary.getAvgSteals() * histGames;
            totalBlocks += historySummary.getAvgBlocks() * histGames;
            totalFouls += historySummary.getAvgFouls() * histGames;
            totalTurnovers += historySummary.getAvgTurnovers() * histGames;
            playerName = historySummary.getPlayerName();
        } else {
            for (PlayerStats stat : dbStats) {
                if (playerName == null) playerName = stat.getPlayerName();
                totalGames++;
                totalPoints += stat.getPoints();
                totalRebounds += stat.getRebound();
                totalAssists += stat.getAssist();
                totalSteals += stat.getSteals();
                totalBlocks += stat.getBlocks();
                totalFouls += stat.getFounds();
                totalTurnovers += stat.getTurnovers();
            }
        }

        // Add live stats
        for (PlayerStats stat : filteredLiveStats) {
            if (playerName == null) playerName = stat.getPlayerName();
            totalGames++;
            totalPoints += stat.getPoints();
            totalRebounds += stat.getRebound();
            totalAssists += stat.getAssist();
            totalSteals += stat.getSteals();
            totalBlocks += stat.getBlocks();
            totalFouls += stat.getFounds();
            totalTurnovers += stat.getTurnovers();
        }

        if (totalGames == 0) return new PlayerStatsSummary();

        PlayerStatsSummary avg = new PlayerStatsSummary();
        avg.setPlayerName(playerName);
        avg.setGames(totalGames);
        avg.setAvgPoints((double) totalPoints / totalGames);
        avg.setAvgRebounds((double) totalRebounds / totalGames);
        avg.setAvgAssists((double) totalAssists / totalGames);
        avg.setAvgSteals((double) totalSteals / totalGames);
        avg.setAvgBlocks((double) totalBlocks / totalGames);
        avg.setAvgFouls((double) totalFouls / totalGames);
        avg.setAvgTurnovers((double) totalTurnovers / totalGames);

        // Save only DB-based (not including live) history summary to Redis
        if (historySummary == null) {
            int dbGameCount = dbStats.size();
            if (dbGameCount > 0) {
                PlayerStatsSummary dbOnlySummary = new PlayerStatsSummary();
                dbOnlySummary.setPlayerName(playerName);
                dbOnlySummary.setGames(dbGameCount);
                dbOnlySummary.setAvgPoints((double) dbStats.stream().mapToInt(PlayerStats::getPoints).sum() / dbGameCount);
                dbOnlySummary.setAvgRebounds((double) dbStats.stream().mapToInt(PlayerStats::getRebound).sum() / dbGameCount);
                dbOnlySummary.setAvgAssists((double) dbStats.stream().mapToInt(PlayerStats::getAssist).sum() / dbGameCount);
                dbOnlySummary.setAvgSteals((double) dbStats.stream().mapToInt(PlayerStats::getSteals).sum() / dbGameCount);
                dbOnlySummary.setAvgBlocks((double) dbStats.stream().mapToInt(PlayerStats::getBlocks).sum() / dbGameCount);
                dbOnlySummary.setAvgFouls((double) dbStats.stream().mapToInt(PlayerStats::getFounds).sum() / dbGameCount);
                dbOnlySummary.setAvgTurnovers((double) dbStats.stream().mapToInt(PlayerStats::getTurnovers).sum() / dbGameCount);

                redisService.set(historyKey, dbOnlySummary);
            }
        }

        return avg;
    }


    /**
     * Get stats summary for a team.
     * - Checks Redis for "history:teamid:<teamId>"
     * - If history key exists, fetch only live Redis stats.
     * - Otherwise, fetch DB stats + live Redis stats, then set the history key in Redis.
     */
    public TeamStatsSummary getAveragePointsForTeam(String teamId) {
        String historyKey = "history:teamid:" + teamId;
        boolean hasHistory = redisService.exists(historyKey);

        TeamStatsSummary teamSummary = null;
        List<PlayerStats> dbStats = new ArrayList<>();

        if (hasHistory) {
            String json = redisService.getRaw(historyKey); // add a `get(String key)` method in RedisService
            try {
            	teamSummary = new ObjectMapper().readValue(json, TeamStatsSummary.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dbStats = statsJdbcRepository.getStatsForTeam(teamId);
        }

        List<PlayerStats> liveStats = redisService.getLiveStatsForTeam(teamId);

        List<PlayerStats> filteredLiveStats;
        Set<Long> historyGameIds;

        if (teamSummary != null) {
            filteredLiveStats = liveStats; // no filtering needed
            historyGameIds = Collections.emptySet(); // unused
        } else {
            historyGameIds = dbStats.stream()
                    .map(PlayerStats::getGameid)
                    .collect(Collectors.toSet());

            filteredLiveStats = liveStats.stream()
                    .filter(stat -> !historyGameIds.contains(stat.getGameid()))
                    .collect(Collectors.toList());
        }

        // Merge data and compute
        int totalGames = 0;
        int totalPoints = 0;
        int totalRebounds = 0;
        int totalAssists = 0;
        int totalSteals = 0;
        int totalBlocks = 0;
        int totalFouls = 0;
        int totalTurnovers = 0;

        if (teamSummary != null) {
            totalGames += teamSummary.getGames();
            totalPoints += teamSummary.getAvgPoints() * teamSummary.getGames();
            totalRebounds += teamSummary.getAvgRebounds() * teamSummary.getGames();
            totalAssists += teamSummary.getAvgAssists() * teamSummary.getGames();
            totalSteals += teamSummary.getAvgSteals() * teamSummary.getGames();
            totalBlocks += teamSummary.getAvgBlocks() * teamSummary.getGames();
            totalFouls += teamSummary.getAvgFouls() * teamSummary.getGames();
            totalTurnovers += teamSummary.getAvgTurnovers() * teamSummary.getGames();
        } else {
            for (PlayerStats stat : dbStats) {
                totalPoints += stat.getPoints();
                totalRebounds += stat.getRebound();
                totalAssists += stat.getAssist();
                totalSteals += stat.getSteals();
                totalBlocks += stat.getBlocks();
                totalFouls += stat.getFounds();
                totalTurnovers += stat.getTurnovers();
                totalGames++;
            }
        }

        for (PlayerStats stat : filteredLiveStats) {
            totalPoints += stat.getPoints();
            totalRebounds += stat.getRebound();
            totalAssists += stat.getAssist();
            totalSteals += stat.getSteals();
            totalBlocks += stat.getBlocks();
            totalFouls += stat.getFounds();
            totalTurnovers += stat.getTurnovers();
            totalGames++;
        }

        if (totalGames == 0) return new TeamStatsSummary();

        TeamStatsSummary avg = new TeamStatsSummary();
        avg.setGames(totalGames);
        avg.setAvgPoints((double) totalPoints / totalGames);
        avg.setAvgRebounds((double) totalRebounds / totalGames);
        avg.setAvgAssists((double) totalAssists / totalGames);
        avg.setAvgSteals((double) totalSteals / totalGames);
        avg.setAvgBlocks((double) totalBlocks / totalGames);
        avg.setAvgFouls((double) totalFouls / totalGames);
        avg.setAvgTurnovers((double) totalTurnovers / totalGames);

        if (!hasHistory) {
            try {
                redisService.set(historyKey, new ObjectMapper().writeValueAsString(avg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return avg;
    }

}
