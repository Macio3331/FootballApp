package pl.take.football_league.dtos;

import java.util.Date;

public class ReturnGameDto {
	long id;
	Date date;
    Integer awayResult;
    Integer homeResult;
    String location;
    boolean played;
    String homeClub;
    String awayClub;
    String players;
    String goals;
    
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Integer getAwayResult() {
		return awayResult;
	}
	
	public void setAwayResult(Integer awayResult) {
		this.awayResult = awayResult;
	}
	
	public Integer getHomeResult() {
		return homeResult;
	}
	
	public void setHomeResult(Integer homeResult) {
		this.homeResult = homeResult;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean isPlayed() {
		return played;
	}
	
	public void setPlayed(boolean played) {
		this.played = played;
	}
	
	public String getHomeClub() {
		return homeClub;
	}
	
	public void setHomeClub(String homeClub) {
		this.homeClub = homeClub;
	}
	
	public String getAwayClub() {
		return awayClub;
	}
	
	public void setAwayClub(String awayClub) {
		this.awayClub = awayClub;
	}
	
	public String getPlayers() {
		return players;
	}
	
	public void setPlayers(String players) {
		this.players = players;
	}
	
	public String getGoals() {
		return goals;
	}
	
	public void setGoals(String goals) {
		this.goals = goals;
	}
}
