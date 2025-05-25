package com.nbastats.query.dto;

public class TeamStatsSummary {
    private String teamName;
    private int games;
    private double avgPoints;
    private double avgRebounds;
    private double avgAssists;
    private double avgSteals;
    private double avgBlocks;
    private double avgFouls;
    private double avgTurnovers;
    
	public TeamStatsSummary(String teamName, int games, double avgPoints, double avgRebounds, double avgAssists,
			double avgSteals, double avgBlocks, double avgFouls, double avgTurnovers) {
		super();
		this.teamName = teamName;
		this.games = games;
		this.avgPoints = avgPoints;
		this.avgRebounds = avgRebounds;
		this.avgAssists = avgAssists;
		this.avgSteals = avgSteals;
		this.avgBlocks = avgBlocks;
		this.avgFouls = avgFouls;
		this.avgTurnovers = avgTurnovers;
	}
	public TeamStatsSummary() {}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
