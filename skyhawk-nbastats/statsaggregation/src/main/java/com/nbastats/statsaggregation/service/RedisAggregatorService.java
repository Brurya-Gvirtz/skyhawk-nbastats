package com.nbastats.statsaggregation.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nbastats.statsaggregation.model.PlayerStats;

import java.time.Duration;

@Service
public class RedisAggregatorService {

	 private final HashOperations<String, String, String> hashOps;
	    private final RedisTemplate<String, String> redisTemplate;
	    private final Duration ttl = Duration.ofMinutes(10);

	    public RedisAggregatorService(RedisTemplate<String, String> redisTemplate) {
	        this.redisTemplate = redisTemplate;
	        this.hashOps = redisTemplate.opsForHash();
	    }

	    public void updateStats(PlayerStats event) {
	        String playerKey = "player:" + event.getPlayerId() + ":game:" + event.getGameId();
	        String teamKey = "team:" + event.getTeamId() + ":game:" + event.getTeamId();
	        //player
	        aggregateStat(playerKey, "points", event.getPoints());
	        aggregateStat(playerKey, "rebounds", event.getRebounds());
	        aggregateStat(playerKey, "assists", event.getAssists());
	        aggregateStat(playerKey, "steals", event.getSteals());
	        aggregateStat(playerKey, "blocks", event.getBlocks());
	        aggregateStat(playerKey, "fouls", event.getFouls());
	        aggregateStat(playerKey, "turnovers", event.getTurnovers());

	        hashOps.put(playerKey, "minutesPlayed", String.valueOf(event.getMinutesPlayed()));
	        
	        //team
	        aggregateStat(teamKey, "points", event.getPoints());
	        aggregateStat(teamKey, "rebounds", event.getRebounds());
	        aggregateStat(teamKey, "assists", event.getAssists());
	        aggregateStat(teamKey, "steals", event.getSteals());
	        aggregateStat(teamKey, "blocks", event.getBlocks());
	        aggregateStat(teamKey, "fouls", event.getFouls());
	        aggregateStat(teamKey, "turnovers", event.getTurnovers());

	        hashOps.put(teamKey, "minutesPlayed", String.valueOf(event.getMinutesPlayed()));

	        // Set TTL
	        redisTemplate.expire(playerKey, ttl);
	        redisTemplate.expire(teamKey, ttl);
	    }

	    private void aggregateStat(String key, String field, float value) {
	        hashOps.increment(key, field, value);
	    }
}



