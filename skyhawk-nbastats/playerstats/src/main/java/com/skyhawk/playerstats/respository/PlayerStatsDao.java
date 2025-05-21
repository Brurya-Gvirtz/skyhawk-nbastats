package com.skyhawk.playerstats.respository;

import java.sql.*;

import com.skyhawk.playerstats.model.PlayerGameStats;


public class PlayerStatsDao {

    public void insertStats(PlayerGameStats stats) {
        String sql = """
                INSERT INTO player_game_stats (
                    player_id, game_id, team_id, points, rebounds, assists,
                    steals, blocks, fouls, turnovers, minutes_played, last_updated
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, stats.getPlayerId());
            stmt.setLong(2, stats.getGameId());
            stmt.setLong(3, stats.getTeamId());
            stmt.setInt(4, stats.getPoints());
            stmt.setInt(5, stats.getRebounds());
            stmt.setInt(6, stats.getAssists());
            stmt.setInt(7, stats.getSteals());
            stmt.setInt(8, stats.getBlocks());
            stmt.setInt(9, stats.getFouls());
            stmt.setInt(10, stats.getTurnovers());
            stmt.setDouble(11, stats.getMinutesPlayed());
            stmt.setTimestamp(12, Timestamp.valueOf(stats.getLastUpdated()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

