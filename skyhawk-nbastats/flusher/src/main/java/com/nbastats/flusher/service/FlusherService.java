package com.nbastats.flusher.service;

import java.util.Map;

import com.nbastats.flusher.db.PostgresWriter;
import com.nbastats.flusher.redis.RedisClient;

public class FlusherService {

    private final RedisClient redisClient;
    private final PostgresWriter postgresWriter;
    private final EndDetectionService endDetectionService;

    public FlusherService() {
        this.redisClient = new RedisClient();
        this.postgresWriter = new PostgresWriter();
        this.endDetectionService = new EndDetectionService(this::flushGame);
    }

    // Start the service - listens for end game and inactivity
    public void start() {
        endDetectionService.start();
    }

    // Called by EndDetectionService when a game ends or times out
    public void flushGame(String gameId) {
        System.out.println("Flushing game stats for gameId: " + gameId);

        // Read aggregated stats from Redis
        Map<String, String> stats = redisClient.getAggregatedStats(gameId);
        if (stats.isEmpty()) {
            System.out.println("No stats found for gameId: " + gameId);
            return;
        }

        // Write to PostgreSQL
        boolean success = postgresWriter.writeFinalStats(gameId, stats);

        if (success) {
            // Optionally remove from Redis after flush
            redisClient.deleteGameStats(gameId);
            System.out.println("Flushed and cleaned up stats for gameId: " + gameId);
        } else {
            System.out.println("Failed to flush stats for gameId: " + gameId);
        }
    }
}
