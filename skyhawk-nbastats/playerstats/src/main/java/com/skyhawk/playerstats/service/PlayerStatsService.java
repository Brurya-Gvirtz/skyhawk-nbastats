package com.skyhawk.playerstats.service;

import org.springframework.stereotype.Service;

import com.skyhawk.playerstats.dto.PlayerStatRequest;
import com.skyhawk.playerstats.model.PlayerGameStats;
import com.skyhawk.playerstats.respository.PlayerStatsDao;


@Service
public class PlayerStatsService {

    private final PlayerStatsDao playerStatsDao;

    public PlayerStatsService(PlayerStatsDao playerStatsDao) {
        this.playerStatsDao = playerStatsDao;
    }

    public void addPlayerStats(PlayerStatRequest request) {
        PlayerGameStats stats = new PlayerGameStats();
        stats.setPlayerId(request.getPlayerId());
        stats.setGameId(request.getGameId());
        stats.setTeamId(request.getTeamId());
        stats.setPoints(request.getPoints());
        stats.setAssists(request.getAssists());
        stats.setRebounds(request.getRebounds());
        stats.setSteals(request.getSteals());
        stats.setBlocks(request.getBlocks());
        stats.setFouls(request.getFouls());
        stats.setTurnovers(request.getTurnovers());
        stats.setMinutesPlayed(request.getMinutesPlayed());

        playerStatsDao.insertStats(stats);
    }


}
