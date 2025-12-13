package pl.take.football_league.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    long id;
    String name;
    String surname;
    int number;
    String position;
    @OneToMany(mappedBy = "scorer",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=false)
    Set<Goal> goals;
    @OneToMany(mappedBy = "assistant",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=false)
    Set<Goal> assists;
    @ManyToOne
    Club club;
    @ManyToMany(mappedBy = "players")
    Set<Game> matches;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public Set<Goal> getAssists() {
        return assists;
    }

    public void setAssists(Set<Goal> assists) {
        this.assists = assists;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Set<Game> getMatches() {
        return matches;
    }

    public void setMatches(Set<Game> matches) {
        this.matches = matches;
    }
}
