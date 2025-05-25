package com.nbastats.query.model;

public class PlayerStats {
	private String id;
    private String playerId;
    private String playerName;
	private String teamId;
	private String teamName;
    private String gameId;
    private int points;
    private int rebound;
    private int assist;
    private int steals;
    private int blocks;
    private int founds;
    private int turnovers;
    private int minutesPlayed;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerid) {
		this.playerId = playerid;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamid) {
		this.teamId = teamid;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameid) {
		this.gameId = gameid;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getRebound() {
		return rebound;
	}
	public void setRebound(int rebound) {
		this.rebound = rebound;
	}
	public int getAssist() {
		return assist;
	}
	public void setAssist(int assist) {
		this.assist = assist;
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
	public int getFounds() {
		return founds;
	}
	public void setFounds(int founds) {
		this.founds = founds;
	}
	public int getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(int turnovers) {
		this.turnovers = turnovers;
	}
	public int getMinutesPlayed() {
		return minutesPlayed;
	}
	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
    
    
}
