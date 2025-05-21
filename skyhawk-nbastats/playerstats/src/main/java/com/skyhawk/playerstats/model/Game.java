package com.skyhawk.playerstats.model;
import java.time.LocalDateTime;

public class Game {
    private Long id;
    private Long homeTeamId;
    private Long awayTeamId;
    private LocalDateTime gameDate;
    private String season;
    private String gameStatus; // "SCHEDULED", "IN_PROGRESS", "COMPLETED"

    public Game() {
    }

    public Game(Long id, Long homeTeamId, Long awayTeamId, LocalDateTime gameDate, String season, String gameStatus) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.gameDate = gameDate;
        this.season = season;
        this.gameStatus = gameStatus;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
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
