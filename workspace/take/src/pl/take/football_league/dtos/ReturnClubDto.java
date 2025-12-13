package pl.take.football_league.dtos;

import java.util.Date;

public class ReturnClubDto {

    long id;
    String name;
    String location;
    Date dateOfCreation;
    String players;
    String homeMatches;
    String awayMatches;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getHomeMatches() {
        return homeMatches;
    }

    public void setHomeMatches(String homeMatches) {
        this.homeMatches = homeMatches;
    }

    public String getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(String awayMatches) {
        this.awayMatches = awayMatches;
    }
}
