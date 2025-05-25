package com.nbastats.flusher.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbastats.flusher.db.PostgresWriter;
import com.nbastats.flusher.model.PlayerStats;
import com.nbastats.flusher.redis.RedisClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class FlusherService {

    private final RedisClient redisClient;
    private final PostgresWriter playerStatsRepository; // Your Spring Data JPA repository

    public FlusherService(RedisClient redisClient, PostgresWriter playerStatsRepository) {
        this.redisClient = redisClient;
        this.playerStatsRepository = playerStatsRepository;
    }

    @Scheduled(fixedRate = 60_000) // every minute
    @Transactional
    public void flushPlayerStats() {
        Set<String> keys = redisClient.getAllKeys();

        for (String key : keys) {
            PlayerStats stats = redisClient.getPlayerStats(key);
            if (stats == null) continue;
            
            LocalDateTime now = LocalDateTime.now();
            Duration elapsedSinceLastUpdate = Duration.between(stats.getLastUpdated(), now);

            double totalPlayedMinutes = stats.getMinutesPlayed() + elapsedSinceLastUpdate.toMinutes();

            if (totalPlayedMinutes >= 48) {
                // Game ended - save to DB and remove Redis keys related to the same gameId

                // Save player stats to DB
                playerStatsRepository.writePlayerStats(stats);

                // Remove all keys for this gameId (both player and team)
                removeGameKeys(stats.getGameId());
            }
        }
    }

    public void removeGameKeys(String gameId) {
        Set<String> keys = redisClient.getAllKeys();
        for (String key : keys) {
            if (key.endsWith(":" + gameId)) {
                redisClient.deleteKey(key);
            }
        }
    }
}
