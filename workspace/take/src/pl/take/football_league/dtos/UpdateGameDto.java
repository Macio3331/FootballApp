package pl.take.football_league.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UpdateGameDto {
	Date date;
    String location;
    Boolean played;
    List<Long> players = new ArrayList<Long>();
    
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Boolean isPlayed() {
		return played;
	}
	
	public void setPlayed(Boolean played) {
		this.played = played;
	}
	
	public List<Long> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Long> players) {
		this.players = players;
	}
}
