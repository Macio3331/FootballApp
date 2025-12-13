package pl.take.football_league;

import pl.take.football_league.dtos.*;
import pl.take.football_league.entities.*;

public class Mapper {

	public ReturnClubDto mapToReturnClubDto(Club club)
	{
		ReturnClubDto clubDto = new ReturnClubDto();
		clubDto.setId(club.getId());
		clubDto.setName(club.getName());
		clubDto.setLocation(club.getLocation());
		clubDto.setDateOfCreation(club.getDateOfCreation());
		clubDto.setPlayers("/clubs/" + club.getId() + "/players");
		clubDto.setHomeMatches("/clubs/" + club.getId() + "/homeMatches");
		clubDto.setAwayMatches("/clubs/" + club.getId() + "/awayMatches");
		return clubDto;
	}
	
	public Club mapToFlatClub(CreateClubDto clubDto)
	{
		Club club = new Club();
		club.setName(clubDto.getName());
		club.setLocation(clubDto.getLocation());
		club.setDateOfCreation(clubDto.getDateOfCreation());
		return club;
	}
	
	public ReturnPlayerDto mapToReturnPlayerDto(Player player)
	{
		ReturnPlayerDto playerDto = new ReturnPlayerDto();
		playerDto.setId(player.getId());
		playerDto.setName(player.getName());
		playerDto.setSurname(player.getSurname());
		playerDto.setNumber(player.getNumber());
		playerDto.setPosition(player.getPosition());
		playerDto.setClub("/players/" + player.getId() + "/club");
		playerDto.setMatches("/players/" + player.getId() + "/matches");
		playerDto.setGoals("/players/" + player.getId() + "/goals");
		playerDto.setAssists("/players/" + player.getId() + "/assists");
		return playerDto;
	}
	
	public Player mapToFlatPlayer(CreatePlayerDto playerDto)
	{
		Player player = new Player();
		player.setName(playerDto.getName());
		player.setSurname(playerDto.getSurname());
		player.setNumber(playerDto.getNumber());
		player.setPosition(playerDto.getPosition());
		return player;
	}
	
	public ReturnGameDto mapToReturnGameDto(Game game)
	{
		ReturnGameDto gameDto = new ReturnGameDto();
		gameDto.setId(game.getId());
		gameDto.setDate(game.getDate());
		gameDto.setLocation(game.getLocation());
		gameDto.setPlayed(game.isPlayed());
		gameDto.setAwayResult(game.getAwayResult());
		gameDto.setHomeResult(game.getHomeResult());
		gameDto.setHomeClub("/matches/" + game.getId() + "/homeClub");
		gameDto.setAwayClub("/matches/" + game.getId() + "/awayClub");
		gameDto.setGoals("/matches/" + game.getId() + "/goals");
		gameDto.setPlayers("/matches/" + game.getId() + "/players");
		return gameDto;
	}
	
	public Game mapToFlatGame(CreateGameDto gameDto)
	{
		Game game = new Game();
		game.setDate(gameDto.getDate());
		game.setLocation(gameDto.getLocation());
		return game;
	}
	
	public ReturnGoalDto mapToReturnGoalDto(Goal goal)
	{
		ReturnGoalDto goalDto = new ReturnGoalDto();
		goalDto.setId(goal.getId());
		goalDto.setMinute(goal.getMinute());
		goalDto.setOwnGoal(goal.isOwnGoal());
		goalDto.setScorer("/goals/" + goal.getId() + "/scorer");
		goalDto.setAssistant("/goals/" + goal.getId() + "/assistant");
		goalDto.setMatch("/goals/" + goal.getId() + "/match");
		return goalDto;
	}
	
	public Goal mapToFlatGoal(CreateGoalDto goalDto)
	{
		Goal goal = new Goal();
		goal.setMinute(goalDto.getMinute());
		goal.setOwnGoal(goalDto.isOwnGoal());
		return goal;
	}
}
