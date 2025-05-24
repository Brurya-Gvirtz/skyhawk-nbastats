package com.nbastats.flusher.db;
import com.nbastats.flusher.model.PlayerStats; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class PostgresWriter {

    private final String url;
    private final String user;
    private final String password;

    public PostgresWriter() {
        url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/nba_stats");
        user = System.getProperty("db.user", "postgres");
        password = System.getProperty("db.password", "postgres");
    }

    public boolean writePlayerStats(PlayerStats stats) {
        String insertSQL = "INSERT INTO player_stats " +
            "(id, player_id, game_id, team_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played, last_updated) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "points = EXCLUDED.points, rebounds = EXCLUDED.rebounds, assists = EXCLUDED.assists, " +
            "steals = EXCLUDED.steals, blocks = EXCLUDED.blocks, fouls = EXCLUDED.fouls, turnovers = EXCLUDED.turnovers, " +
            "minutes_played = EXCLUDED.minutes_played, last_updated = EXCLUDED.last_updated";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setLong(1, stats.getId());
                stmt.setLong(2, stats.getPlayerId());
                stmt.setLong(3, stats.getGameId());
                stmt.setLong(4, stats.getTeamId());
                stmt.setInt(5, stats.getPoints());
                stmt.setInt(6, stats.getRebounds());
                stmt.setInt(7, stats.getAssists());
                stmt.setInt(8, stats.getSteals());
                stmt.setInt(9, stats.getBlocks());
                stmt.setInt(10, stats.getFouls());
                stmt.setInt(11, stats.getTurnovers());
                stmt.setDouble(12, stats.getMinutesPlayed());
                stmt.setObject(13, stats.getLastUpdated());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error writing to DB: " + e.getMessage());
            return false;
        }
    }
}