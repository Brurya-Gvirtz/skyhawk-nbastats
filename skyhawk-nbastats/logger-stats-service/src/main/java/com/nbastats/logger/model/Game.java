package com.nbastats.logger.model;
import java.time.LocalDateTime;

public class Game {
    private String id;
    private LocalDateTime gameDate;
    private String season;
    private String gameStatus; // "SCHEDULED", "IN_PROGRESS", "COMPLETED"

    public Game() {
    }

    public Game(String id, Long homeTeamId, Long awayTeamId, LocalDateTime gameDate, String season, String gameStatus) {
        this.id = id;
        this.gameDate = gameDate;
        this.season = season;
        this.gameStatus = gameStatus;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
}
