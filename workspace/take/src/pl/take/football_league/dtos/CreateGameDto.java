package pl.take.football_league.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CreateGameDto {
    Date date;
    String location;
    Long homeClubId;
    Long awayClubId;
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

	public Long getHomeClubId() {
		return homeClubId;
	}

	public void setHomeClubId(Long homeClubId) {
		this.homeClubId = homeClubId;
	}

	public Long getAwayClubId() {
		return awayClubId;
	}

	public void setAwayClubId(Long awayClubId) {
		this.awayClubId = awayClubId;
	}

	public List<Long> getPlayers() {
		return players;
	}

	public void setPlayers(List<Long> players) {
		this.players = players;
	}
}
