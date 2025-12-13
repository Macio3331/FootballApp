package pl.take.football_league.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    long id;
    String name;
    String location;
    @Temporal(TemporalType.DATE)
    Date dateOfCreation;
    @OneToMany(mappedBy = "club",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=false)
    Set<Player> players = new HashSet<Player>();
    @OneToMany(mappedBy = "homeClub",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=false)
    Set<Game> homeMatches = new HashSet<Game>();
    @OneToMany(mappedBy = "awayClub",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=false)
    Set<Game> awayMatches = new HashSet<Game>();

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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Game> getHomeMatches() {
        return homeMatches;
    }

    public void setHomeMatches(Set<Game> homeMatches) {
        this.homeMatches = homeMatches;
    }

    public Set<Game> getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(Set<Game> awayMatches) {
        this.awayMatches = awayMatches;
    }
}
