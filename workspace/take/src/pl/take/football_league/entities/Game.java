package pl.take.football_league.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    long id;
    @Temporal(TemporalType.DATE)
    Date date;
    @Column(nullable = true)
    Integer awayResult;
    @Column(nullable = true)
    Integer homeResult;
    String location;
    boolean played;
    @OneToMany(mappedBy = "match",cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true)
    Set<Goal> goals = new HashSet<Goal>();
    @ManyToOne
    Club homeClub;
    @ManyToOne
    Club awayClub;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "gameId"),
            inverseJoinColumns = @JoinColumn(name = "playerId")
    )
    Set<Player> players = new HashSet<Player>();

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

	public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public Club getHomeClub() {
        return homeClub;
    }

    public void setHomeClub(Club homeClub) {
        this.homeClub = homeClub;
    }

    public Club getAwayClub() {
        return awayClub;
    }

    public void setAwayClub(Club awayClub) {
        this.awayClub = awayClub;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
