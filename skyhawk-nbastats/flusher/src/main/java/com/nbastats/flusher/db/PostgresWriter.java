package com.nbastats.flusher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class PostgresWriter {

    private final String url;
    private final String user;
    private final String password;

    public PostgresWriter() {
        url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/nba_stats");
        user = System.getProperty("db.user", "postgres");
        password = System.getProperty("db.password", "postgres");
    }

    /**
     * Write final aggregated stats to PostgreSQL.
     * For simplicity, assume table: game_player_stats(game_id TEXT, stat_key TEXT, stat_value TEXT)
     */
    public boolean writeFinalStats(String gameId, Map<String, String> stats) {
        String insertSQL = "INSERT INTO game_player_stats (game_id, stat_key, stat_value) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                for (Map.Entry<String, String> entry : stats.entrySet()) {
                    stmt.setString(1, gameId);
                    stmt.setString(2, entry.getKey());
                    stmt.setString(3, entry.getValue());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error writing to DB: " + e.getMessage());
            return false;
        }
    }
}

