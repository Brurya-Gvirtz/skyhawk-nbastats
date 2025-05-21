package com.skyhawk.statsaggregation.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.skyhawk.statsaggregation.dto.PlayerAggregateResponse;
import com.skyhawk.statsaggregation.dto.TeamAggregateResponse;
import com.skyhawk.statsaggregation.model.PlayerGameStats;

@Service
public class AggregationService {

	private final RestTemplate restTemplate = new RestTemplate();
    private final String statsServiceBaseUrl = "http://player-stats-service/api/stats";

    public PlayerAggregateResponse getPlayerAggregate(long playerId) {
        String url = statsServiceBaseUrl + "/player/" + playerId;
        PlayerGameStats[] stats = restTemplate.getForObject(url, PlayerGameStats[].class);
        return calculatePlayerAggregate(playerId, Arrays.asList(stats));
    }

    public TeamAggregateResponse getTeamAggregate(long teamId) {
        String url = statsServiceBaseUrl + "/team/" + teamId;
        PlayerGameStats[] stats = restTemplate.getForObject(url, PlayerGameStats[].class);
        return calculateTeamAggregate(teamId, Arrays.asList(stats));
    }

    private PlayerAggregateResponse calculatePlayerAggregate(long playerId, List<PlayerGameStats> stats) {
        PlayerAggregateResponse res = new PlayerAggregateResponse();
        res.playerId = playerId;
        int count = stats.size();
        stats.forEach(s -> {
            res.avgPoints += s.getPoints();
            res.avgRebounds += s.getRebounds();
            res.avgAssists += s.getAssists();
            res.avgSteals += s.getSteals();
            res.avgBlocks += s.getBlocks();
            res.avgFouls += s.getFouls();
            res.avgMinutes += s.getMinutesPlayed();
        });
        if (count > 0) {
            res.avgPoints /= count;
            res.avgRebounds /= count;
            res.avgAssists /= count;
            res.avgSteals /= count;
            res.avgBlocks /= count;
            res.avgFouls /= count;
            res.avgTurnovers /= count;
            res.avgMinutes /= count;
        }
        return res;
    }

    private TeamAggregateResponse calculateTeamAggregate(Long teamId, List<PlayerGameStats> stats) {
        TeamAggregateResponse res = new TeamAggregateResponse();
        res.teamId = teamId;
        int count = stats.size();
        stats.forEach(s -> {
            res.avgPoints += s.getPoints();
            res.avgRebounds += s.getRebounds();
            res.avgAssists += s.getAssists();
            res.avgSteals += s.getSteals();
            res.avgBlocks += s.getBlocks();
            res.avgFouls += s.getFouls();
            res.avgMinutes += s.getMinutesPlayed();
        });
        if (count > 0) {
            res.avgPoints /= count;
            res.avgRebounds /= count;
            res.avgAssists /= count;
            res.avgSteals /= count;
            res.avgBlocks /= count;
            res.avgFouls /= count;
            res.avgTurnovers /= count;
            res.avgMinutes /= count;
        }
        return res;
    }
    

//  private final PlayerStatsDao playerStatsDao;
//  private final TeamDao teamDao;
//
//  public AggregationService(PlayerStatsDao playerStatsDao, TeamDao teamDao) {
//      this.playerStatsDao = playerStatsDao;
//      this.teamDao = teamDao;
//  }
//
//  public PlayerAggregateResponse getPlayerAggregates(Long playerId) {
//      return playerStatsDao.getAggregatedStats(playerId);
//  }
//
//  public TeamAggregateResponse getTeamAggregates(Long teamId) {
//      return teamDao.getTotalPointsForTeam(teamId);
//  }
}
