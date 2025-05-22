package com.nbastats.query.dto;

public class PlayerStatsSummary {
    private String playerName;
    private int games;
    private double avgPoints;
    private double avgRebounds;
    private double avgAssists;
    private double avgSteals;
    private double avgBlocks;
    private double avgFouls;
    private double avgTurnovers;
    
    public PlayerStatsSummary() {}
    
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getGames() {
		return games;
	}
	public void setGames(int games) {
		this.games = games;
	}
	public double getAvgPoints() {
		return avgPoints;
	}
	public void setAvgPoints(double avgPoints) {
		this.avgPoints = avgPoints;
	}
	public double getAvgRebounds() {
		return avgRebounds;
	}
	public void setAvgRebounds(double avgRebounds) {
		this.avgRebounds = avgRebounds;
	}
	public double getAvgAssists() {
		return avgAssists;
	}
	public void setAvgAssists(double avgAssists) {
		this.avgAssists = avgAssists;
	}
	public double getAvgSteals() {
		return avgSteals;
	}
	public void setAvgSteals(double avgSteals) {
		this.avgSteals = avgSteals;
	}
	public double getAvgBlocks() {
		return avgBlocks;
	}
	public void setAvgBlocks(double avgBlocks) {
		this.avgBlocks = avgBlocks;
	}
	public double getAvgFouls() {
		return avgFouls;
	}
	public void setAvgFouls(double avgFouls) {
		this.avgFouls = avgFouls;
	}
	public double getAvgTurnovers() {
		return avgTurnovers;
	}
	public void setAvgTurnovers(double avgTurnovers) {
		this.avgTurnovers = avgTurnovers;
	}

    
}
