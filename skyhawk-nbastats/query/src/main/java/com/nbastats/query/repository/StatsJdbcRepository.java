package com.nbastats.query.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nbastats.query.model.PlayerStats;


@Repository
public class StatsJdbcRepository {
    private final JdbcTemplate jdbc;

    public StatsJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<PlayerStats> getStatsForPlayer(String playerId) {
        String sql = "SELECT p.name as playerName, ps.ps.gameId, " +
                     "ps.points, ps.rebound, ps.assist, ps.steals, ps.blocks, ps.founds, ps.turnovers, ps.minutesPlayed " +
                     "FROM playerstats ps " +
                     "JOIN player p ON ps.playerid = p.id " +
                     "JOIN game g ON ps.gameid = g.id " +
                     "WHERE ps.playerid = ? AND g.status = 'FINISHED'";

        return jdbc.query(sql, (rs, rowNum) -> {
            PlayerStats s = new PlayerStats();
            s.setPlayerName(rs.getString("playerName"));
            s.setGameid(rs.getLong("gameId"));
            s.setPoints(rs.getInt("points"));
            s.setRebound(rs.getInt("rebound"));
            s.setAssist(rs.getInt("assist"));
            s.setSteals(rs.getInt("steals"));
            s.setBlocks(rs.getInt("blocks"));
            s.setFounds(rs.getInt("founds"));
            s.setTurnovers(rs.getInt("turnovers"));
            s.setMinutesPlayed(rs.getInt("minutesPlayed"));
            return s;
        }, playerId);
    }

    public List<PlayerStats> getStatsForTeam(String teamId) {
        String sql = "SELECT t.name as teamName, ps.ps.gameid, " +
                     "ps.points, ps.rebound, ps.assist, ps.steals, ps.blocks, ps.founds, ps.turnovers, ps.minutesPlayed " +
                     "FROM playerstats ps " +
                     "JOIN player p ON ps.playerid = p.id " +
                     "JOIN game g ON ps.gameid = g.id " +
                     "WHERE ps.playerid = ? AND g.status = 'FINISHED'";

        return jdbc.query(sql, (rs, rowNum) -> {
            PlayerStats s = new PlayerStats();
            s.setTeamName(rs.getString("teamName"));
            s.setGameid(rs.getLong("gameId"));
            s.setPoints(rs.getInt("points"));
            s.setRebound(rs.getInt("rebound"));
            s.setAssist(rs.getInt("assist"));
            s.setSteals(rs.getInt("steals"));
            s.setBlocks(rs.getInt("blocks"));
            s.setFounds(rs.getInt("founds"));
            s.setTurnovers(rs.getInt("turnovers"));
            s.setMinutesPlayed(rs.getInt("minutesPlayed"));
            return s;
        }, teamId);
    }
}
