package pl.take.football_league;

import pl.take.football_league.entities.Game;

public class GameScheduleComparator implements java.util.Comparator<Game> {
	@Override
	public int compare(Game a, Game b) {
        return a.getDate().compareTo(b.getDate());
    }
}
