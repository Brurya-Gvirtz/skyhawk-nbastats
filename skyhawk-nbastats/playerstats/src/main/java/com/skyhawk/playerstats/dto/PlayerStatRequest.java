package com.skyhawk.playerstats.dto;


import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public class PlayerStatRequest {
	@NotNull
	private Long playerId;
	@NotNull
    private Long gameId;
	@NotNull
    private Long teamId;
	@Min(0)
    private int points;
	@Min(0)
    private int rebounds;
	@Min(0)
    private int assists;
	@Min(0)
    private int steals;
	@Min(0)
    private int blocks;
    @NotNull @Min(0) @Max(6)private int fouls;
    private int turnovers;
    @DecimalMin("0.0") @DecimalMax("48.0")private double minutesPlayed;
    private LocalDateTime lastUpdated;

    
    
    // Getters and setters
    public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getRebounds() {
		return rebounds;
	}
	public void setRebounds(int rebounds) {
		this.rebounds = rebounds;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getSteals() {
		return steals;
	}
	public void setSteals(int steals) {
		this.steals = steals;
	}
	public int getBlocks() {
		return blocks;
	}
	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}
	public int getFouls() {
		return fouls;
	}
	public void setFouls(int fouls) {
		this.fouls = fouls;
	}
	public int getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(int turnovers) {
		this.turnovers = turnovers;
	}
	public double getMinutesPlayed() {
		return minutesPlayed;
	}
	public void setMinutesPlayed(double minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}

