package com.nbastats.logger.model;


import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PlayerStats {
    private String id;
    @NotNull(message = "Player ID is required")
    private String playerId;
    @NotNull(message = "Game ID is required")
    private String gameId;
    @NotNull(message = "Team ID is required")
    private String teamId;
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
    @Min(0) @Max(value = 6, message = "Fouls cannot exceed 6")
    private int fouls;
    @Min(0)
    private int turnovers;
    @Min(0) @Max(48)
    private double minutesPlayed;
    private LocalDateTime lastUpdated;

    public PlayerStats() {
    }

    public PlayerStats(String id, String playerId, String gameId, String teamId, int points, int rebounds, int assists, 
                          int steals, int blocks, int fouls, int turnovers, 
                          double minutesPlayed, LocalDateTime lastUpdated) {
        this.id = id;
        this.playerId = playerId;
        this.gameId = gameId;
        this.teamId = teamId;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.fouls = fouls;
        this.turnovers = turnovers;
        this.minutesPlayed = minutesPlayed;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
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
