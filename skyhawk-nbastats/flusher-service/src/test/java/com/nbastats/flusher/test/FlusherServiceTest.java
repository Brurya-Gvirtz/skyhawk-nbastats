package com.nbastats.flusher.test;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nbastats.flusher.db.PostgresWriter;
import com.nbastats.flusher.model.PlayerStats;
import com.nbastats.flusher.redis.RedisClient;
import com.nbastats.flusher.service.FlusherService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlusherServiceTest {

    @Mock RedisClient redisClient;
    @Mock PostgresWriter playerStatsRepository;

    @InjectMocks FlusherService flusherService;

    @Test
    void flushPlayerStats_savesAndDeletes_whenGameOver() {
        String key = "player:1:game:100";
        PlayerStats stats = new PlayerStats();
        stats.setGameId("100");
        stats.setPlayerId("100");
        stats.setMinutesPlayed(47); // 47 minutes already played
        stats.setLastUpdated(LocalDateTime.now().minusMinutes(2)); // now 49

        when(redisClient.getAllKeys()).thenReturn(Set.of(key));
        when(redisClient.getPlayerStats(key)).thenReturn(stats);

        flusherService.flushPlayerStats();

        verify(playerStatsRepository).writePlayerStats(stats);
        verify(redisClient).deleteKey(key); // would be called by removeGameKeys
    }

    @Test
    void flushPlayerStats_doesNothing_whenStatsNull() {
        when(redisClient.getAllKeys()).thenReturn(Set.of("key1"));
        when(redisClient.getPlayerStats("key1")).thenReturn(null);

        flusherService.flushPlayerStats();

        verify(playerStatsRepository, never()).writePlayerStats(any());
        verify(redisClient, never()).deleteKey(any());
    }

    @Test
    void flushPlayerStats_doesNothing_whenGameNotOver() {
        String key = "player:1:game:101";
        PlayerStats stats = new PlayerStats();
        stats.setGameId("gameId");
        stats.setPlayerId("playerId");
        stats.setMinutesPlayed(20);
        stats.setLastUpdated(LocalDateTime.now());

        when(redisClient.getAllKeys()).thenReturn(Set.of(key));
        when(redisClient.getPlayerStats(key)).thenReturn(stats);

        flusherService.flushPlayerStats();

        verify(playerStatsRepository, never()).writePlayerStats(any());
        verify(redisClient, never()).deleteKey(any());
    }

    @Test
    void removeGameKeys_deletesMatchingKeysOnly() {
        when(redisClient.getAllKeys()).thenReturn(Set.of(
            "player:1:game:999", "team:1:game:999", "other:random"
        ));

        flusherService.removeGameKeys("999");

        verify(redisClient).deleteKey("player:1:game:999");
        verify(redisClient).deleteKey("team:1:game:999");
        verify(redisClient, never()).deleteKey("other:random");
    }
}

